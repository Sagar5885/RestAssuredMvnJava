package ReadTestCommon;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.unitils.reflectionassert.ReflectionComparatorMode;
import static org.testng.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;
import junit.framework.AssertionFailedError;

public abstract class EndpointTesterBase<REQUEST, RESPONSE> implements EndpointTester<REQUEST, RESPONSE> {
	//public abstract class EndpointTesterBase<REQUEST, RESPONSE> extends AbstractTestNGSpringContextTests implements EndpointTester<REQUEST, RESPONSE> {

    private RequestResponseDataProvider<REQUEST, RESPONSE> requestResponseDataProvider;

    private final ImmutableMap<String, String> defaultRequestResponseResourcePaths =
            ImmutableMap.of
                    (REQUEST_RESOURCE_PREFIX, RESPONSE_RESOURCE_PREFIX);
    private final ImmutableMap<String, Pattern> requestPatternsByType =
            ImmutableMap.of(TestCaseRunContext.POSITIVE, POSITIVE_REQ_FILE_PATTERN,
                    TestCaseRunContext.PARTIAL, PARTIAL_REQ_FILE_PATTERN,
                    TestCaseRunContext.NEGATIVE, NEGATIVE_REQ_FILE_PATTERN);
    private final ImmutableMap<String, Pattern> responsePatternsByType =
            ImmutableMap.of(TestCaseRunContext.POSITIVE, POSITIVE_RESP_FILE_PATTERN,
                TestCaseRunContext.PARTIAL, PARTIAL_RESP_FILE_PATTERN,
                TestCaseRunContext.NEGATIVE, NEGATIVE_RESP_FILE_PATTERN);

    private static final String ASSERTION_MSG_FMT = "Assertion Failed for test case: %s \nRequest file: %s" +
            "\nRequest JSON:\n%s \nResponse File: %s, \nExpected JSON Response: \n%s" +
            "\nBut got JSON:\n%s";
    private static final String CACHE_SKIP_CACHE_ASSERTION_MSG_FMT = "Service call with Cache & skipCache failed for " +
            "test case: [%s] \nRequest file: %s \nRequest JSON:\n%s \nResponse File: %s, " +
            "\nExpected JSON Response: \n%s \nBut got JSON:\n%s";

    private static final String REQUEST_RESOURCE_PREFIX = "tests/request";
    private static final String RESPONSE_RESOURCE_PREFIX = "tests/response";
    private static final String PARTIAL_REQ_FILE_REGEX = ".*request-(partial.*-testcase-[0-9]+).json";
    private static final String PARTIAL_RESP_FILE_REGEX = ".*response-(partial.*-testcase-[0-9]+).json";
    private static final String POSITIVE_REQ_FILE_REGEX = ".*request-(positive.*-testcase-[0-9]+).json";
    private static final String POSITIVE_RESP_FILE_REGEX = ".*response-(positive.*-testcase-[0-9]+).json";
    private static final String NEGATIVE_REQ_FILE_REGEX = ".*request-(negative.*-testcase-[0-9]+).json";
    private static final String NEGATIVE_RESP_FILE_REGEX = ".*response-(negative.*-testcase-[0-9]+).json";

    private static final Pattern PARTIAL_REQ_FILE_PATTERN = Pattern.compile(PARTIAL_REQ_FILE_REGEX);
    private static final Pattern PARTIAL_RESP_FILE_PATTERN = Pattern.compile(PARTIAL_RESP_FILE_REGEX);
    private static final Pattern POSITIVE_REQ_FILE_PATTERN = Pattern.compile(POSITIVE_REQ_FILE_REGEX);
    private static final Pattern POSITIVE_RESP_FILE_PATTERN = Pattern.compile(POSITIVE_RESP_FILE_REGEX);
    private static final Pattern NEGATIVE_REQ_FILE_PATTERN = Pattern.compile(NEGATIVE_REQ_FILE_REGEX);
    private static final Pattern NEGATIVE_RESP_FILE_PATTERN = Pattern.compile(NEGATIVE_RESP_FILE_REGEX);

    @BeforeClass
    public void setup() throws IOException {
        requestResponseDataProvider =
                JSONRequestResponseDataProvider.<REQUEST, RESPONSE>builder()
                        .includeTestCases(includeTestCases())
                        .excludeTestCases(excludeTestCases())
                        .requestFilePatterns(requestPatternsByType)
                        .responseFilePatterns(responsePatternsByType)
                        .requestResponseResourcePaths(requestResponseResourcePaths())
                        .requestType(requestType()).responseType(responseType()).build();
    }

    public ImmutableMap<String, String> requestResponseResourcePaths() {
        return defaultRequestResponseResourcePaths;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * By default this base implementation includes all the tests it can read.
     */
    public ImmutableSet<String> includeTestCases() {
        return ImmutableSet.of();
    }

    /**
     * {@inheritDoc}
     * <p/>
     * By default no test cases are excluded.
     */
    public ImmutableSet<String> excludeTestCases() {
        return ImmutableSet.of();
    }

    public void executeBeforeCall(final TestCaseRunContext<REQUEST, RESPONSE> testCaseRunContext)
            throws Exception {
        requestResponseDataProvider.verifyRequestSerialization(testCaseRunContext.getRequest());
        requestResponseDataProvider.verifyResponseSerialization(testCaseRunContext.getExpectedResponse());
    }

    @Test(dataProvider = "positive-requestResponseData")
    public void testSuccessAndPartialScenarios(final TestCaseRunContext<REQUEST, RESPONSE> testCaseRunContext)
            throws Exception {
        // Temporarily include lenient ordering until the code is fixed to support correct order.
        // In success scenarios the order should match.
        info("Executing POSITIVE/PARTIAL test: [%s] - %s", testCaseRunContext.getTestCaseNo(),
                testCaseRunContext.getTestCaseDescription());
        executeCallAndVerify(testCaseRunContext, ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test(dataProvider = "negative-requestResponseData")
    public void testNegativeScenarios(final TestCaseRunContext<REQUEST, RESPONSE> testCaseRunContext)
            throws Exception {
        info("Executing NEGATIVE test: [%s] - %s", testCaseRunContext.getTestCaseNo(),
                testCaseRunContext.getTestCaseDescription());
        executeCallAndVerify(testCaseRunContext, ReflectionComparatorMode.LENIENT_ORDER);
    }

    private void executeCallAndVerify(final TestCaseRunContext<REQUEST, RESPONSE> testCaseRunContext,
            final ReflectionComparatorMode... modes) throws Exception {

        executeBeforeCall(testCaseRunContext);

        // Call Service with Cache enabled
        final REQUEST request = testCaseRunContext.getRequest();
        final RESPONSE cachedResponse = invokeServiceCall(request);
        assertNotNull(cachedResponse);
        final RESPONSE expectedResponse = testCaseRunContext.getExpectedResponse();
        assertExpectedAndActual(testCaseRunContext, request, expectedResponse, cachedResponse, ASSERTION_MSG_FMT,
                modes);

        // Get the updated request to skip cache
        final REQUEST skipCacheRequest = updateWithSkipCacheFlags(request);
        final RESPONSE skipCacheResponse = invokeServiceCall(skipCacheRequest);
        assertNotNull(skipCacheResponse);
        assertExpectedAndActual(testCaseRunContext, request, expectedResponse, skipCacheResponse,
                CACHE_SKIP_CACHE_ASSERTION_MSG_FMT, modes);
    }

    private void assertExpectedAndActual(final TestCaseRunContext<REQUEST, RESPONSE> testCaseRunContext,
            final REQUEST request,
            final RESPONSE expectedResponse,
            final RESPONSE actualResponse,
            final String assertionMsgFormat,
            final ReflectionComparatorMode... modes) throws Exception {
        // The reason we call assertReflectionEquals without the message is to avoid the expense of creating the
        // test context specific message during the assertion check. The context specific message is constructed only
        // if there is an assertion failure.
        try {
            assertReflectionEquals(expectedResponse, actualResponse, modes);
        } catch (final AssertionFailedError ae) {
            // Add more detailed information now.
            final String testContext =
                    testContextMessage(testCaseRunContext, actualResponse, request, expectedResponse,
                            assertionMsgFormat);
            final String diffDetail = ae.getMessage();
            final String completeMessage = String.format("%s \n%s", testContext, diffDetail);
            throw new AssertionFailedError(completeMessage);
        }

    }

    private String testContextMessage(final TestCaseRunContext<REQUEST, RESPONSE> testCaseRunContext,
            final RESPONSE actualResponse,
            final REQUEST request,
            final RESPONSE expectedResponse,
            final String assertionMsgFormat) throws Exception {
        return String.format(assertionMsgFormat,
                testCaseRunContext.getTestCaseNo(),
                testCaseRunContext.getRequestResource(),
                requestResponseDataProvider.serializeRequest(request),
                testCaseRunContext.getResponseResource(),
                requestResponseDataProvider.serializeResponse(expectedResponse),
                requestResponseDataProvider.serializeResponse(actualResponse));
    }

    @DataProvider(name = "positive-requestResponseData", parallel = true)
    protected Iterator<Object[]> positiveRequestResponseData() throws IOException {
        return requestResponseDataProvider.positiveAndPartialCases();
    }

    @DataProvider(name = "negative-requestResponseData", parallel = true)
    protected Iterator<Object[]> negativeRequestResponseData() throws IOException {
        return requestResponseDataProvider.negativeCases();
    }

    private static void info(final String template, final Object... args) {
        // TODO: Use a Logger here
        System.out.println(String.format(template, args));
    }
}
