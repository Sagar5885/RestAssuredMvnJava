package ReadTestCommon;

import java.io.IOException;

import org.apache.http.client.methods.HttpRequestBase;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

public class EndPointTesterSupport {

	public static String javaToJsonStr(Object obj) {
        String jsonReq = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(AnnotationIntrospector.pair(new JaxbAnnotationIntrospector(),new JacksonAnnotationIntrospector()));
        try {
            jsonReq = mapper.writeValueAsString(obj);
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Json Parse Exception", e);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            throw new RuntimeException("Json Mapping Exception", e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IO Exception", e);
        }

        return jsonReq;
    }
	
	public static Object mapResponseToPojo(Object obj, String response) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            obj = mapper.readValue(response,
                    obj.getClass());
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Json Parse Exception", e);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            throw new RuntimeException("Json Mapping Exception", e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IO Exception", e);
        }
        return obj;
    }
	
	public static HttpRequestBase getHeaders(HttpRequestBase method) {
        method.addHeader("Content-Type", "application/json");
        method.addHeader("Accept", "application/json");
        method.addHeader("Key1", "Value1");
        return method;
    }
}
