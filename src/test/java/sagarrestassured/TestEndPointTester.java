package sagarrestassured;

import static org.mockito.Mockito.mock;
import javax.ws.rs.core.HttpHeaders;
import org.codehaus.jackson.type.TypeReference;
import org.testng.annotations.Test;
import com.google.common.collect.ImmutableMap;
import ReadTestCommon.EndpointTesterBase;

public class TestEndPointTester extends EndpointTesterBase<String, String>{
	
	private ImmutableMap<String, String> requestResourcePaths =
            ImmutableMap.of(REQUEST_DATA, RESPONSE_DATA);

    private static final String REQUEST_DATA = "api-read/request";
    private static final String RESPONSE_DATA = "api-read/response";
    HttpHeaders headers = mock(HttpHeaders.class);
    
    @Override
    public ImmutableMap<String, String> requestResponseResourcePaths() {
        return requestResourcePaths;
    }
    
    @Test
    public void testTrigger() {
        // Just a test trigger for eclipse!
    }

	public TypeReference<String> requestType() {
		// TODO Request Object
		return null;
	}

	public TypeReference<String> responseType() {
		// TODO Response Object
		return null;
	}

	public String invokeServiceCall(String request) {
		// TODO Pass Request and return Response
		return null;
	}

	public String updateWithSkipCacheFlags(String request) {
		// TODO Pass Request and return request
		return null;
	}

}
