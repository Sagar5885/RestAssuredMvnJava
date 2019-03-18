package sagarrestassured;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.http.client.methods.HttpGet;
import ReadTestCommon.EndPointTesterSupport;
import ReadTestCommon.HttpMethodType;
import ReadTestCommon.ServiceUtils;
import au.com.bytecode.opencsv.CSVReader;

public class TestUsingServiceUtil {

	private static ServiceUtils serviceUtils = new ServiceUtils();
	private static EndPointTesterSupport endPointTester = new EndPointTesterSupport();
	private static String CSV_FILE_PATH = "src/main/java/exportsr.csv";
	  
	public static void main(String args[]) throws Exception {
		CSV_FILE_PATH = args[0];
		
		Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
	    CSVReader csvReader = new CSVReader(reader);
	    
//	    Writer writer = Files.newBufferedWriter(Paths.get(CSV_FILE_W_PATH));
//	    CSVWriter cvsWriter = new CSVWriter(writer);
	    
	    String[] nextRecord;
	    while ((nextRecord = csvReader.readNext()) != null) {
	    	if(nextRecord[1] != null && nextRecord[0] != null) {
	    		try {
		    		Object resGet =	APIGet(nextRecord[0].toString(), nextRecord[1].toString());
		    		if(resGet.toString() != "OK") { //GetStatus
		    			System.out.println(nextRecord[0]+","+nextRecord[1]+"Get SM - Failed!");
	            	}else {
	            		String resGetStr = "{ \"header\": { \"headerAttributes\": {} }, \"payload\": ["+endPointTester.javaToJsonStr(resGet.toString())+"] }";//GetPayload
	            		//System.out.println(resGetStr);
	            	}
		    		
	    		}catch(Exception ex) {
	    			System.out.println(nextRecord[0]+","+nextRecord[1]+" - "+ex.toString()+" - Failed!");
	    		}
	    	}else {
	    		System.out.println("Null Data! - Fail!");
	    	}	
	    }
	    
	    System.out.println("=============DONE=============");
	}
	    
	private static Object APIGet(String param1, String param2) {
		Object response = new Object();
		String url = "http://api.url.com/"+param1+"/"+param2;
		String getResponse1 = serviceUtils.httpMethodWithHeaders(url, endPointTester.getHeaders(new HttpGet()), HttpMethodType.GET);
	
		Object response1 = new Object();
		response = (Object) endPointTester.mapResponseToPojo(response1, getResponse1);
	  return response;
	}
}
