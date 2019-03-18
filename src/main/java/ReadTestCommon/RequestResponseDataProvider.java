package ReadTestCommon;

import java.io.IOException;
import java.util.Iterator;

public interface RequestResponseDataProvider<REQUEST, RESPONSE> {
	
	void verifyRequestSerialization(REQUEST request) throws Exception;

    void verifyResponseSerialization(RESPONSE response) throws Exception;

    String serializeRequest(REQUEST request) throws Exception;

    String serializeResponse(RESPONSE response) throws Exception;

    Iterator<Object[]> positiveAndPartialCases() throws IOException;

    Iterator<Object[]> negativeCases() throws IOException;
    
}
