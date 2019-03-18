package ReadTestCommon;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ResourceInfo;

import static com.google.common.base.Preconditions.*;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.testng.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONRequestResponseDataProvider<REQUEST, RESPONSE> implements
RequestResponseDataProvider<REQUEST, RESPONSE> {

	private final ImmutableSet<String> includeTestCases;
    private final ImmutableSet<String> excludeTestCases;
    private final TypeReference<REQUEST> requestType;
    private final TypeReference<RESPONSE> responseType;

    // { <test-type>-<testcase>-<number>, Pattern }
    private final ImmutableMap<String, Pattern> requestPatterns;
    // { <test-type>-<testcase>-<number>, Pattern }
    private final ImmutableMap<String, Pattern> responsePatterns;
    // { requestResourcePath, responseResourcePath }
    private final ImmutableMap<String, String> requestResponseResourcePaths;

    // loaded data
    private ImmutableMap<String, Properties> descriptionsByRequestPath;
    // Populated Iterables
    private Iterable<Object[]> positiveCasesIterable;
    private Iterable<Object[]> negativeCasesIterable;

    private static final ObjectMapper OBJECT_MAPPER = initialize();
    private static final String TEST_CASE_DESCRIPTIONS_FILE = "%s/test-cases.properties";

    private static final Logger LOG = LoggerFactory.getLogger(JSONRequestResponseDataProvider.class);

    private JSONRequestResponseDataProvider(final Builder<REQUEST, RESPONSE> builder) {
        this.includeTestCases = builder.includeTestCases;
        this.excludeTestCases = builder.excludeTestCases;
        this.requestType = builder.requestType;
        this.responseType = builder.responseType;
        this.requestPatterns = builder.requestPatterns;
        this.responsePatterns = builder.responsePatterns;
        this.requestResponseResourcePaths = builder.requestResponseResourcePaths;
    }

    private void constructAndLoad() throws IOException {
        constructIterables();
    }

    public void verifyRequestSerialization(REQUEST request) throws Exception {
        assertSerialization(request, requestType);
    }

    public void verifyResponseSerialization(RESPONSE response) throws Exception {
        assertSerialization(response, responseType);
    }

    public String serializeRequest(REQUEST request) throws Exception {
        return serialize(request);
    }

    public String serializeResponse(RESPONSE response) throws Exception {
        return serialize(response);
    }

    public <T> String serialize(final T response) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        } catch (final Exception e) {
            return "Could not convert Response Object to JSON";
        }
    }

    public <T> void assertSerialization(final T original, final TypeReference<T> typeRef) throws Exception {
        assertNotNull(original);
        final String actualStr = OBJECT_MAPPER.writeValueAsString(original);
        final T actual = OBJECT_MAPPER.readValue(actualStr, typeRef);
        assertReflectionEquals(original, actual);
    }

    public Iterator<Object[]> positiveAndPartialCases() throws IOException {
        return positiveCasesIterable.iterator();
    }

    public Iterator<Object[]> negativeCases() throws IOException {
        return negativeCasesIterable.iterator();
    }

    private void loadDescriptions(ClassLoader classLoader) throws IOException {
        ImmutableMap.Builder<String, Properties> descriptionsByReq = ImmutableMap.builder();
        for (String requestPath : requestResponseResourcePaths.keySet()) {
            String descriptionsResource = descriptionsFileResource(requestPath);
            descriptionsByReq.put(requestPath, loadProperties(classLoader, descriptionsResource));
        }
        descriptionsByRequestPath = descriptionsByReq.build();
    }

    private String descriptionsFileResource(final String requestFilesResourcePath) {
        return String.format(TEST_CASE_DESCRIPTIONS_FILE, requestFilesResourcePath);
    }

    private Properties loadProperties(ClassLoader classLoader, final String descriptionsResource) throws IOException {
        LOG.info("Loading the test case numbers and descriptions");
        final InputStream is = classLoader.getResourceAsStream(descriptionsResource);
        checkState(is != null, "Could not find test case descriptions file=[%s]",
                descriptionsResource);
        final Properties testCaseDescriptions = new Properties();
        testCaseDescriptions.load(is);
        return testCaseDescriptions;
    }

    private void constructIterables() throws IOException {

        LOG.info("Scanning all classpath resources for test cases files");

        final ClassLoader classLoader = JSONRequestResponseDataProvider.class.getClassLoader();
        final ClassPath classPath = ClassPath.from(classLoader);
        final ImmutableSet<ResourceInfo> allResources = classPath.getResources();

        // Load the test case descriptions
        loadDescriptions(classLoader);

        // Go through all the resources, for each of the different request/response folders add them to the table by
        // TestCaseRunContext type.
        ImmutableMap.Builder<String, ResourceInfo> requestsBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<String, ResourceInfo> responsesBuilder = ImmutableMap.builder();

        for (ResourceInfo resourceInfo : allResources) {
            if (isRequestResource(resourceInfo)) {
                addIfMatchesPattern(requestPatterns.values(), resourceInfo, requestsBuilder);
            } else if (isResponseResource(resourceInfo)) {
                addIfMatchesPattern(responsePatterns.values(), resourceInfo, responsesBuilder);
            }
        }

        ImmutableMap<String, ResourceInfo> requestsByTestName = requestsBuilder.build();
        ImmutableMap<String, ResourceInfo> responsesByTestName = responsesBuilder.build();

        // Create 2 different iterators, one for positive + partial and other for negative
        Predicate<Entry<String, ResourceInfo>> negativeTestPredicate = negativeTestPredicate();
        Predicate<Entry<String, ResourceInfo>> positivePredicate =
                Predicates.and(includePredicate(), excludePredicate(), Predicates.not(negativeTestPredicate));
        positiveCasesIterable = new TestCasesIterable(classLoader, requestsByTestName, responsesByTestName,
                positivePredicate);

        Predicate<Entry<String, ResourceInfo>> negativePredicate = Predicates.and(includePredicate(), excludePredicate(),
                negativeTestPredicate);
        negativeCasesIterable = new TestCasesIterable(classLoader, requestsByTestName, responsesByTestName,
                negativePredicate);
    }

    private boolean isRequestResource(ResourceInfo resourceInfo) {
        for (String requestPrefix : requestResponseResourcePaths.keySet()) {
            if (resourceInfo.getResourceName().startsWith(requestPrefix)) {
                return true;
            }
        }
        return false;
    }

    private boolean isResponseResource(ResourceInfo resourceInfo) {
        for (String responsePrefix : requestResponseResourcePaths.values()) {
            if (resourceInfo.getResourceName().startsWith(responsePrefix)) {
                return true;
            }
        }
        return false;
    }

    private void addIfMatchesPattern(ImmutableCollection<Pattern> patterns, ResourceInfo resourceInfo,
            ImmutableMap.Builder<String, ResourceInfo> resultBuilder) {
        final Optional<Matcher> matcher = findMatcher(patterns, resourceInfo);
        if (matcher.isPresent()) {
            resultBuilder.put(matcher.get().group(1), resourceInfo);
        }
    }

    private Optional<Matcher> findMatcher(ImmutableCollection<Pattern> patterns, ResourceInfo resourceInfo) {
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(resourceInfo.getResourceName());
            if (matcher.matches()) {
                return Optional.of(matcher);
            }
        }
        return Optional.absent();
    }

    protected static ObjectMapper initialize() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(AnnotationIntrospector.pair(new JaxbAnnotationIntrospector(),
                new JacksonAnnotationIntrospector()));
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }

    private Predicate<Entry<String, ResourceInfo>> negativeTestPredicate() {
        return new Predicate<Entry<String, ResourceInfo>>() {
            public boolean apply(Entry<String, ResourceInfo> input) {
                final Pattern pattern = requestPatterns.get(TestCaseRunContext.NEGATIVE);
                Matcher matcher = pattern.matcher(input.getValue().getResourceName());
                return matcher.matches();
            }
        };
    }

    private Predicate<Entry<String, ResourceInfo>> excludePredicate() {
        return new Predicate<Entry<String, ResourceInfo>>() {
            public boolean apply(final Entry<String, ResourceInfo> input) {
                return isEmpty(excludeTestCases) || !excludeTestCases.contains(input.getKey());
            }
        };
    }

    private Predicate<Entry<String, ResourceInfo>> includePredicate() {
        return new Predicate<Entry<String, ResourceInfo>>() {
            public boolean apply(final Entry<String, ResourceInfo> input) {
                return isEmpty(includeTestCases) || includeTestCases.contains(input.getKey());
            }
        };
    }

    private class TestCasesIterable implements Iterable<Object[]> {

        private final ClassLoader classLoader;
        private final ImmutableMap<String, ResourceInfo> responsesByTestName;
        private final PeekingIterator<Entry<String, ResourceInfo>> requestIterator;

        TestCasesIterable(final ClassLoader classLoader, final ImmutableMap<String, ResourceInfo> requestsByTestName,
                final ImmutableMap<String, ResourceInfo> responsesByTestName,
                final Predicate<Entry<String, ResourceInfo>> predicate) {
            this.classLoader = classLoader;
            this.responsesByTestName = responsesByTestName;
            this.requestIterator = filteredPeekingIterator(requestsByTestName, predicate);
        }

        private PeekingIterator<Entry<String, ResourceInfo>> filteredPeekingIterator(ImmutableMap<String,
                ResourceInfo> requestsByTestName, final Predicate<Entry<String, ResourceInfo>> predicate) {
            Iterable<Entry<String, ResourceInfo>> iterable = requestsByTestName.entrySet();
            Iterable<Entry<String, ResourceInfo>> filtered = Iterables.filter(iterable, predicate);
            return Iterators.peekingIterator(filtered.iterator());
        }

        public Iterator<Object[]> iterator() {
            return new Iterator<Object[]>() {
                public boolean hasNext() {
                    if (requestIterator.hasNext()) {
                        Entry<String, ResourceInfo> next = requestIterator.peek();
                        return responsesByTestName.containsKey(next.getKey());
                    }
                    return false;
                }

                public Object[] next() {
                    final Entry<String, ResourceInfo> requestEntry = requestIterator.next();
                    final String testCaseNo = requestEntry.getKey();
                    // This should be not happen if hasNext is called correctly.
                    checkState(responsesByTestName.containsKey(testCaseNo),
                            "Unexpected state; Could not find response resource for [testCaseNo=%s]",
                            testCaseNo);

                    try {
                        return new Object[] {testCase(requestEntry)};
                    } catch (final Exception e) {
                        throw new RuntimeException(String.format("Could not load resources for [testCaseNo=%s]",
                                testCaseNo), e);
                    }
                }

                public void remove() { // Not allowed!
                    throw new UnsupportedOperationException();
                }

                private TestCaseRunContext<REQUEST, RESPONSE> testCase(Entry<String, ResourceInfo> requestEntry) throws
                        IOException {
                    final String testCaseNo = requestEntry.getKey();
                    final ResourceInfo requestResource = requestEntry.getValue();
                    final ResourceInfo responseResource = responsesByTestName.get(testCaseNo);

                    final REQUEST request = readRequestJSON(classLoader, resource(requestResource));
                    final RESPONSE expectedResponse = readResponseJSON(classLoader, resource(responseResource));

                    // Get the descriptions
                    Properties descriptions = findDescriptions(requestResource);

                    return TestCaseRunContext.<REQUEST, RESPONSE>builder().testCaseNo(testCaseNo)
                            .testCaseDescription((String) descriptions.get(testCaseNo))
                            .requestResource(requestResource).responseResource(responseResource)
                            .request(request)
                            .expectedResponse(expectedResponse).build();
                }

                private Properties findDescriptions(ResourceInfo requestResource) {
                    for (String requestPath : descriptionsByRequestPath.keySet()) {
                        final String resourceName = requestResource.getResourceName();
                        if (resourceName.contains(requestPath)) {
                            return descriptionsByRequestPath.get(requestPath);
                        }
                    }
                    throw new IllegalStateException(String.format("Did not find associated descriptions file for " +
                            "[resource=%s]", requestResource));
                }

                private RESPONSE readResponseJSON(final ClassLoader classLoader, final String filePath)
                        throws IOException {
                    final InputStream stream = classLoader.getResourceAsStream(filePath);
                    return OBJECT_MAPPER.readValue(stream, responseType);
                }

                private REQUEST readRequestJSON(final ClassLoader classLoader, final String filePath)
                        throws IOException {
                    final InputStream stream = classLoader.getResourceAsStream(filePath);
                    return OBJECT_MAPPER.readValue(stream, requestType);
                }

                private String resource(final ResourceInfo resourceInfo) {
                    return resourceInfo.getResourceName();
                }
            };
        }
    }

    private static <T> boolean isEmpty(final Collection<T> collection) {
        return (collection == null) || collection.isEmpty();
    }

    public static <REQUEST, RESPONSE> Builder<REQUEST, RESPONSE> builder() {
        return new Builder<REQUEST, RESPONSE>();
    }

    public static class Builder<REQUEST, RESPONSE> {
        private ImmutableSet<String> includeTestCases;
        private ImmutableSet<String> excludeTestCases;
        private TypeReference<REQUEST> requestType;
        private TypeReference<RESPONSE> responseType;
        private ImmutableMap<String, Pattern> requestPatterns;
        private ImmutableMap<String, Pattern> responsePatterns;
        private ImmutableMap<String, String> requestResponseResourcePaths;

        public Builder<REQUEST, RESPONSE> includeTestCases(final ImmutableSet<String> includeTestCases) {
            this.includeTestCases = includeTestCases;
            return this;
        }

        public Builder<REQUEST, RESPONSE> excludeTestCases(final ImmutableSet<String> excludeTestCases) {
            this.excludeTestCases = excludeTestCases;
            return this;
        }

        public Builder<REQUEST, RESPONSE> requestFilePatterns(final ImmutableMap<String, Pattern> requestPatterns) {
            this.requestPatterns = requestPatterns;
            return this;
        }

        public Builder<REQUEST, RESPONSE> responseFilePatterns(final ImmutableMap<String, Pattern> responsePatterns) {
            this.responsePatterns = responsePatterns;
            return this;
        }

        public Builder<REQUEST, RESPONSE> requestType(final TypeReference<REQUEST> requestType) {
            this.requestType = requestType;
            return this;
        }

        public Builder<REQUEST, RESPONSE> responseType(final TypeReference<RESPONSE> responseType) {
            this.responseType = responseType;
            return this;
        }

        public Builder<REQUEST, RESPONSE> requestResponseResourcePaths(
                final ImmutableMap<String, String> requestResponseResourcePaths) {
            this.requestResponseResourcePaths = requestResponseResourcePaths;
            return this;
        }

        public JSONRequestResponseDataProvider<REQUEST, RESPONSE> build() throws IOException {
            JSONRequestResponseDataProvider provider = new JSONRequestResponseDataProvider<REQUEST, RESPONSE>(this);
            provider.constructAndLoad();
            return provider;
        }
    }

}
