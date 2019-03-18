//package ReadTestCommon;
//
//import com.google.common.base.Splitter;
//import com.google.common.collect.ImmutableMap;
//import com.google.common.collect.ImmutableSet;
//import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//import static com.google.common.base.Preconditions.checkArgument;
//import static com.google.common.base.Preconditions.checkNotNull;
//import static com.google.common.base.Strings.isNullOrEmpty;
//import static org.testng.Assert.assertEquals;
//import static org.testng.Assert.assertFalse;
//import static org.testng.Assert.assertNotNull;
//import static org.testng.Assert.assertTrue;
//import static org.testng.Assert.fail;
//import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
//import java.io.IOException;
//import java.util.Iterator;
//import java.util.List;
//import java.util.regex.Pattern;
//
//public abstract class ResourceCRUDTesterBase<RESOURCE extends ServiceEntity, RESULT extends ServiceEntity> extends
//        AbstractTestNGSpringContextTests implements ResourceCRUDTester<RESOURCE, RESULT> {
//
//    private RequestResponseDataProvider<ServiceRequest<List<RESOURCE>>, ServiceResponse<List<RESULT>>>
//            requestResponseDataProvider;
//
//    private final ImmutableMap<String, String> defaultRequestResponseResourcePaths = ImmutableMap.of
//            (REQUEST_RESOURCE_PREFIX, RESPONSE_RESOURCE_PREFIX);
//
//    private final ImmutableMap<String, Pattern> requestPatternsByType = ImmutableMap.of(TestCaseRunContext.POSITIVE,
//            POSITIVE_REQ_FILE_PATTERN, TestCaseRunContext.PARTIAL, PARTIAL_REQ_FILE_PATTERN, TestCaseRunContext.NEGATIVE,
//            NEGATIVE_REQ_FILE_PATTERN);
//    private final ImmutableMap<String, Pattern> responsePatternsByType = ImmutableMap.of(TestCaseRunContext.POSITIVE,
//            POSITIVE_RESP_FILE_PATTERN, TestCaseRunContext.PARTIAL, PARTIAL_RESP_FILE_PATTERN, TestCaseRunContext.NEGATIVE,
//            NEGATIVE_RESP_FILE_PATTERN);
//
//    private static final String REQUEST_RESOURCE_PREFIX = "tests/request";
//    private static final String RESPONSE_RESOURCE_PREFIX = "tests/response";
//    private static final String PARTIAL_REQ_FILE_REGEX = ".*request-(partial.*-testcase-[0-9]+).json";
//    private static final String PARTIAL_RESP_FILE_REGEX = ".*response-(partial.*-testcase-[0-9]+).json";
//    private static final String POSITIVE_REQ_FILE_REGEX = ".*request-(positive.*-testcase-[0-9]+).json";
//    private static final String POSITIVE_RESP_FILE_REGEX = ".*response-(positive.*-testcase-[0-9]+).json";
//    private static final String NEGATIVE_REQ_FILE_REGEX = ".*request-(negative.*-testcase-[0-9]+).json";
//    private static final String NEGATIVE_RESP_FILE_REGEX = ".*response-(negative.*-testcase-[0-9]+).json";
//
//    private static final Pattern PARTIAL_REQ_FILE_PATTERN = Pattern.compile(PARTIAL_REQ_FILE_REGEX);
//    private static final Pattern PARTIAL_RESP_FILE_PATTERN = Pattern.compile(PARTIAL_RESP_FILE_REGEX);
//    private static final Pattern POSITIVE_REQ_FILE_PATTERN = Pattern.compile(POSITIVE_REQ_FILE_REGEX);
//    private static final Pattern POSITIVE_RESP_FILE_PATTERN = Pattern.compile(POSITIVE_RESP_FILE_REGEX);
//    private static final Pattern NEGATIVE_REQ_FILE_PATTERN = Pattern.compile(NEGATIVE_REQ_FILE_REGEX);
//    private static final Pattern NEGATIVE_RESP_FILE_PATTERN = Pattern.compile(NEGATIVE_RESP_FILE_REGEX);
//
//    public ImmutableMap<String, String> requestResponseResourcePaths() {
//        return defaultRequestResponseResourcePaths;
//    }
//
//    public ImmutableSet<String> includeTestCases() {
//        return ImmutableSet.of();
//    }
//
//    public ImmutableSet<String> excludeTestCases() {
//        return ImmutableSet.of();
//    }
//
//    @BeforeClass
//    public void setup() throws IOException {
//        requestResponseDataProvider =
//                JSONRequestResponseDataProvider.<ServiceRequest<List<RESOURCE>>, ServiceResponse<List<RESULT>>>builder()
//                        .includeTestCases(includeTestCases())
//                        .excludeTestCases(excludeTestCases())
//                        .requestFilePatterns(requestPatternsByType)
//                        .responseFilePatterns(responsePatternsByType)
//                        .requestResponseResourcePaths(requestResponseResourcePaths())
//                        .requestType(resourcesType()).responseType(resultsType()).build();
//    }
//
//    @DataProvider(name = "requestResponseData", parallel = true)
//    protected Iterator<Object[]> testRequestResponseData() throws IOException {
//        return requestResponseDataProvider.positiveAndPartialCases();
//    }
//
//    @Test(dataProvider = "requestResponseData")
//    public void
//    testCRUDCompleteAndPartialScenarios(
//            final TestCaseRunContext<ServiceRequest<List<RESOURCE>>, ServiceResponse<List<RESULT>>> testCaseRunContext)
//            throws Exception {
//
//        // Check the request is valid
//        checkTestCaseValidity(testCaseRunContext);
//
//        // GET Invoke GET first, since we are creating resources, we should not find anything.
//        assertResultsNotFoundForGet(testCaseRunContext.getRequest());
//
//        // CREATE
//        final ServiceResponse<List<RESULT>> createResponse = executeCreateOrUpdate(testCaseRunContext.getRequest());
//        assertRequestResponse(testCaseRunContext, createResponse);
//
//        // GET
//        final ServiceResponse<List<RESOURCE>> getAfterCreate = executeGet(testCaseRunContext.getRequest());
//        assertResponseParity(getAfterCreate, createResponse);
//
//        // UPDATE
//        // get the updated resources and then update
//        final ServiceRequest<List<RESOURCE>> updatedResources = updateOriginalRequest(testCaseRunContext.getRequest());
//        final ServiceResponse<List<RESULT>> updateResponse = executeCreateOrUpdate(updatedResources);
//        assertRequestResponse(testCaseRunContext, updateResponse);
//
//        // GET
//        final ServiceResponse<List<RESOURCE>> getAfterUpdate = executeGet(updatedResources);
//        assertResponseParity(getAfterUpdate, updateResponse);
//
//        // DELETE
//        final List<ServiceResponse<Void>> deleteResponse = executeDelete(testCaseRunContext.getRequest());
//        assertDeleteResponseStatus(deleteResponse, getAfterUpdate);
//
//        // GET
//        assertResultsNotFoundForGet(testCaseRunContext.getRequest());
//    }
//
//    private void
//    checkTestCaseValidity(final TestCaseRunContext<ServiceRequest<List<RESOURCE>>, ServiceResponse<List<RESULT>>> testCaseRunContext) {
//        checkNotNull(testCaseRunContext);
//        checkArgument(!isNullOrEmpty(testCaseRunContext.getTestCaseNo()));
//        checkArgument(!isNullOrEmpty(testCaseRunContext.getTestCaseDescription()));
//
//        final ServiceRequest<List<RESOURCE>> request = testCaseRunContext.getRequest();
//        checkNotNull(request);
//        checkNotNull(request.getPayload());
//        checkArgument(!request.getPayload().isEmpty());
//
//        final ServiceResponse<List<RESULT>> expectedResults = testCaseRunContext.getExpectedResponse();
//        checkNotNull(expectedResults);
//        checkNotNull(expectedResults.getPayload());
//        checkArgument(!expectedResults.getPayload().isEmpty());
//
//        checkArgument(request.getPayload().size() == expectedResults.getPayload().size());
//    }
//
//    private void assertResultsNotFoundForGet(final ServiceRequest<List<RESOURCE>> request) {
//        try {
//            invokeGet(request);
//            fail(String.format("Expected %s exception, since no results should be obtained for GET",
//                    ServiceException.class));
//        } catch (final ServiceException se) {
//            // check detailed errors here. inspect the errors etc
//            assertNotNull(se);
//        }
//    }
//
//    private ServiceResponse<List<RESULT>> executeCreateOrUpdate(final ServiceRequest<List<RESOURCE>> request)
//            throws ServiceException {
//        try {
//            return invokeCreateOrUpdate(request);
//        } catch (final ServiceException se) {
//            fail("Expected successful CREATE/UPDATE response, however failed with exception", se);
//            throw se;
//        }
//
//    }
//
//    private ServiceResponse<List<RESOURCE>> executeGet(final ServiceRequest<List<RESOURCE>> request)
//            throws ServiceException {
//        try {
//            return invokeGet(request);
//        } catch (final ServiceException se) {
//            fail("Expected successful GET response, however failed with exception", se);
//            throw se;
//        }
//    }
//
//    private List<ServiceResponse<Void>> executeDelete(final ServiceRequest<List<RESOURCE>> request)
//            throws ServiceException {
//        try {
//            return invokeDelete(request);
//        } catch (final ServiceException se) {
//            fail("Expected successful DELETE responses, however failed with exception", se);
//            throw se;
//        }
//    }
//
//    private void
//    assertRequestResponse(final TestCaseRunContext<ServiceRequest<List<RESOURCE>>, ServiceResponse<List<RESULT>>> testCaseRunContext,
//            final ServiceResponse<List<RESULT>> actualResponse) throws Exception {
//        assertNotNull(actualResponse);
//
//        final List<RESULT> actualResponsePayload = actualResponse.getPayload();
//        assertNotNull(actualResponsePayload);
//
//        final List<RESULT> expectedResponsePayload = testCaseRunContext.getExpectedResponse().getPayload();
//        assertTrue(actualResponsePayload.size() == expectedResponsePayload.size(),
//                String.format("Expected response size [%s != %s] actual response size",
//                        expectedResponsePayload.size(),
//                        actualResponsePayload.size())
//        );
//
//        assertReflectionEquals(String.format("For Request:\n%s \nExpected:\n%s, \nbut got:\n%s",
//                        requestResponseDataProvider.serializeRequest(testCaseRunContext.getRequest()),
//                        requestResponseDataProvider.serializeResponse(testCaseRunContext.getExpectedResponse()),
//                        requestResponseDataProvider.serializeResponse(actualResponse)),
//                testCaseRunContext.getExpectedResponse(),
//                actualResponse
//        );
//    }
//
//    private void assertResponseParity(final ServiceResponse<List<RESOURCE>> getResponse,
//            final ServiceResponse<List<RESULT>> createResponse) {
//        // Ensure the Get responses match with create results
//        assertNotNull(getResponse);
//        assertNotNull(getResponse.getStatus());
//        assertNotNull(getResponse.getPayload());
//        assertEquals(getResponse.getPayload().size(), createResponse.getPayload().size());
//
//        // Ensure status is same
//        assertTrue(getResponse.getStatus() == createResponse.getStatus());
//
//        // Ensure error code parity for each resource in the list
//        final Iterator<RESOURCE> getIterator = getResponse.getPayload().iterator();
//        final Iterator<RESULT> createIterator = createResponse.getPayload().iterator();
//        for (; getIterator.hasNext() && createIterator.hasNext(); ) {
//            final RESOURCE resource = getIterator.next();
//            final RESULT result = createIterator.next();
//            assertOnRolledupErrors(resource, result);
//        }
//    }
//
//    private void assertDeleteResponseStatus(final List<ServiceResponse<Void>> deleteResponse,
//            final ServiceResponse<List<RESOURCE>> getResponse) {
//        assertNotNull(deleteResponse);
//        assertFalse(deleteResponse.isEmpty());
//        assertNotNull(getResponse);
//        assertNotNull(getResponse.getPayload());
//        assertFalse(getResponse.getPayload().isEmpty());
//        assertEquals(deleteResponse.size(), getResponse.getPayload().size());
//
//        // For delete at the moment, would always return a 200 because it does not know if the item existed in the
//        // internal store and got deleted or item did not exist in the first place.
//        for (final ServiceResponse<Void> response : deleteResponse) {
//            assertTrue(response.getStatus() == Status.OK);
//        }
//
//    }
//
//    private void assertOnRolledupErrors(final RESOURCE resource, final RESULT result) {
//        assertEquals(resource.hasErrors(), result.hasErrors());
//        if (resource.hasErrors() && result.hasErrors()) {
//            final int resourceHttpStatus = rolledUpError(resource);
//            final int resultHttpStatus = rolledUpError(result);
//            // If the create/update succeeded then resource should not any errors.
//            // If the create/update was 500, we should expect 404 for GET
//            // If the create/update was 400, we should expect either 400 or 404.
//            if (resultHttpStatus == 400) {
//                assertTrue((resourceHttpStatus == 400) || (resourceHttpStatus == 404));
//            } else if (resultHttpStatus == 500) {
//                assertTrue(resourceHttpStatus == 404);
//            }
//        }
//    }
//
//    private int rolledUpError(final ServiceEntity entity) {
//        int resultStatus = 400;
//        for (final Error error : entity.getErrors()) {
//            final Iterator<String> codeParts = Splitter.on('.').split(error.getCode()).iterator();
//            assertTrue(codeParts.hasNext());
//            final int httpStatus = Integer.parseInt(codeParts.next());
//            resultStatus = Math.max(resultStatus, httpStatus);
//        }
//        return resultStatus;
//    }
//
//}
//
