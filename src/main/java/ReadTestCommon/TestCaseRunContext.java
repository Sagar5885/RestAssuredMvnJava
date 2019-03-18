package ReadTestCommon;

import com.google.common.base.Objects;
import com.google.common.reflect.ClassPath.ResourceInfo;

public class TestCaseRunContext<REQUEST, RESPONSE> {

	private final String testCaseNo;
    private final String testCaseDescription;
    private final ResourceInfo requestResource;
    private final ResourceInfo responseResource;
    private final REQUEST request;
    private final RESPONSE expectedResponse;

    public static final String POSITIVE = "positive";
    public static final String PARTIAL = "partial";
    public static final String NEGATIVE = "negative";

    private TestCaseRunContext(final Builder<REQUEST, RESPONSE> builder) {
        this.testCaseNo = builder.testCaseNo;
        this.testCaseDescription = builder.testCaseDescription;
        this.requestResource = builder.requestResource;
        this.responseResource = builder.responseResource;
        this.request = builder.request;
        this.expectedResponse = builder.expectedResponse;
    }

    /**
     * Gets the testCaseNo.
     *
     * @return the testCaseNo
     */
    public String getTestCaseNo() {
        return testCaseNo;
    }


    /**
     * Gets the testCaseDescription.
     *
     * @return the testCaseDescription
     */
    public String getTestCaseDescription() {
        return testCaseDescription;
    }

    public ResourceInfo getRequestResource() {
        return requestResource;
    }

    public ResourceInfo getResponseResource() {
        return responseResource;
    }

    /**
     * Gets the request.
     *
     * @return the request
     */
    public REQUEST getRequest() {
        return request;
    }

    /**
     * Gets the expectedResponse.
     *
     * @return the expectedResponse
     */
    public RESPONSE getExpectedResponse() {
        return expectedResponse;
    }

//    @Override
//    public String toString() {
//        return Objects.toStringHelper(this).add("testCase", testCaseNo)
//                      .add("description", testCaseDescription).toString();
//    }
    
    @Override
	public String toString() {
		return "TestCaseRunContext [testCaseNo=" + testCaseNo + ", testCaseDescription=" + testCaseDescription
				+ ", requestResource=" + requestResource + ", responseResource=" + responseResource + ", request="
				+ request + ", expectedResponse=" + expectedResponse + "]";
	}

    public static <REQUEST, RESPONSE> Builder<REQUEST, RESPONSE> builder() {
        return new Builder<REQUEST, RESPONSE>();
    }

	public static class Builder<REQUEST, RESPONSE> {
        private String testCaseNo;
        private String testCaseDescription;
        private ResourceInfo requestResource;
        private ResourceInfo responseResource;
        private REQUEST request;
        private RESPONSE expectedResponse;

        public Builder<REQUEST, RESPONSE> testCaseNo(final String testCaseNo) {
            this.testCaseNo = testCaseNo;
            return this;
        }

        public Builder<REQUEST, RESPONSE> requestResource(final ResourceInfo requestResource) {
            this.requestResource = requestResource;
            return this;
        }

        public Builder<REQUEST, RESPONSE> responseResource(final ResourceInfo responseResource) {
            this.responseResource = responseResource;
            return this;
        }

        public Builder<REQUEST, RESPONSE> testCaseDescription(final String testCaseDescription) {
            this.testCaseDescription = testCaseDescription;
            return this;
        }

        public Builder<REQUEST, RESPONSE> request(final REQUEST request) {
            this.request = request;
            return this;
        }

        public Builder<REQUEST, RESPONSE> expectedResponse(final RESPONSE expectedResponse) {
            this.expectedResponse = expectedResponse;
            return this;
        }

        public TestCaseRunContext<REQUEST, RESPONSE> build() {
            return new TestCaseRunContext<REQUEST, RESPONSE>(this);
        }
    }
    
}
