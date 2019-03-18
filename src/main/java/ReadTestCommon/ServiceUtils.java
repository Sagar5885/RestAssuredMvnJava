package ReadTestCommon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class ServiceUtils {
	private static final Logger LOG = LoggerFactory
			.getLogger(ServiceUtils.class);
	private HttpGet get;
	private HttpPost post;
	private HttpPut put;

	//public void closeConnection(){
//		//httpClient.getConnectionManager().shutdown();
//		//httpClient.getConnectionManager().closeExpiredConnections();
//		//httpClient.getConnectionManager().releaseConnection(null, 0, null);
//		//httpClient.getConnectionManager().shutdown();
//		try {
//			httpClient.getConnectionManager().wait(1l);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	//}

	// To call the RestPOST JSON Service and API by providing URL / Service name
	// and the input String
	public String restPostJSONServiceCall(String link, String serviceName,
			String inputString) {
		String responseBody = null;
		HttpClient httpClient = new DefaultHttpClient();
		try {
			post = new HttpPost(link + serviceName);
			StringEntity input = new StringEntity(inputString);
			input.setContentType("application/json");
			post.setEntity(input);
			HttpResponse response = httpClient.execute(post);
			// Assert.assertEquals(response.getStatusLine().getStatusCode(),200);
			int responseCode = response.getStatusLine().getStatusCode();
			LOG.info("RESPONSE CODE:" + responseCode);
			switch (responseCode) {
			case 200:
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					responseBody = EntityUtils.toString(entity);
					LOG.info("reponseBody" + responseBody);
				}
				break;
			}
		} catch (MalformedURLException e) {

		} catch (IOException e) {

		}
		return responseBody;
	}

	// To call the sterling service and API by providing the Service name/ input
	// xml / invokeFlow / isFlow / enviornmentPassword / API name / ProgID /
	// userid / SterlignURL
	public String restPostSterlingService(String serviceName,
			String interopApiData, String invokeFlow, String isFlow,
			String environmentPassword, String interopApiName, String progId,
			String userId, String sterlingURL) {
		String output = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			post = new HttpPost(sterlingURL);
			LOG.info("serviceName" + serviceName);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add((NameValuePair) new BasicNameValuePair("ServiceName",
					serviceName));
			params.add(new BasicNameValuePair("InteropApiData", interopApiData));
			params.add(new BasicNameValuePair("InvokeFlow", invokeFlow));
			params.add(new BasicNameValuePair("IsFlow", isFlow));
			params.add(new BasicNameValuePair("YFSEnvironment.password",
					environmentPassword));
			params.add(new BasicNameValuePair("InteropApiName", interopApiName));
			params.add(new BasicNameValuePair("YFSEnvironment.progId", progId));
			// params.add(new BasicNameValuePair("TemplateData", templateData));
			params.add(new BasicNameValuePair("YFSEnvironment.userId", userId));

			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpClient.execute(post);
			output = EntityUtils.toString(response.getEntity());
			LOG.info("Service Output" + output);

		} catch (MalformedURLException e) {
			LOG.info("Exception" + e);
			e.printStackTrace();
		} catch (IOException e) {
			LOG.info("Exception" + e);
			e.printStackTrace();
		}
		return output;
	}

	// To call the sterling service and API With Template Data by providing the
	// Service name/ input xml / invokeFlow / isFlow / enviornmentPassword / API
	// name / ProgID / userid / SterlignURL/templateData
	public String restPostSterlingService_templateData(String serviceName,
			String interopApiData, String invokeFlow, String isFlow,
			String environmentPassword, String interopApiName, String progId,
			String userId, String sterlingURL, String templateData) {
		String output = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			post = new HttpPost(sterlingURL);
			LOG.info("serviceName" + serviceName);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add((NameValuePair) new BasicNameValuePair("ServiceName",
					serviceName));
			params.add(new BasicNameValuePair("InteropApiData", interopApiData));
			params.add(new BasicNameValuePair("InvokeFlow", invokeFlow));
			params.add(new BasicNameValuePair("IsFlow", isFlow));
			params.add(new BasicNameValuePair("YFSEnvironment.password",
					environmentPassword));
			params.add(new BasicNameValuePair("InteropApiName", interopApiName));
			params.add(new BasicNameValuePair("YFSEnvironment.progId", progId));
			params.add(new BasicNameValuePair("TemplateData", templateData));
			params.add(new BasicNameValuePair("YFSEnvironment.userId", userId));

			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpClient.execute(post);
			output = EntityUtils.toString(response.getEntity());
			LOG.info("Service Output" + output);

		} catch (MalformedURLException e) {
			LOG.info("Exception" + e);
			e.printStackTrace();
		} catch (IOException e) {
			LOG.info("Exception" + e);
			e.printStackTrace();
		}
		return output;
	}

	public static String httpGet(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}

		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();
		JSONObject obj = null;
		try {
			obj = new JSONObject(sb.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// HashMap hMap = obj.getString("payload");
		String itemString = null;
		try {
			itemString = obj.getString("payload");
		} catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.info("RESPONSE : " + itemString);
		return itemString;
	}

	public JSONObject getPayloadFromJSONServiceResponse(String response)
			throws org.json.JSONException {

		JSONObject obj = null;
		JSONObject payloadResponse = null;
		String itemString = null;
		obj = new JSONObject(response);
		itemString = obj.getString("payload");
		String outputString = itemString.substring(1, itemString.length() - 1);
		payloadResponse = new JSONObject(outputString);
		LOG.info("response         :" + payloadResponse);
		return payloadResponse;
	}

	/**
	 * Method to extracts the given tag info from the given HttpReposne.
	 * 
	 * @param response input HTTPResponse
	 * @param tag required tag
	 * @return String content of the given tag from the given response
	 */
	public String getTagFromResponse(HttpResponse response, String tag) {
		BufferedReader br = null;
		StringBuilder sb = null;

		try {
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				sb = new StringBuilder();
				br = new BufferedReader(new InputStreamReader(
						entity.getContent()));
				String readLine;
				while (((readLine = br.readLine()) != null)) {
					sb.append(readLine);
				}
			}

		} catch (IllegalStateException e) {
			throw new RuntimeException("Illegal State Exception", e);

		} catch (IOException e) {
			throw new RuntimeException("IO Exception", e);

		}

		return getTagFromResponse(sb.toString(), tag);
	}

	/**
	 * Method to extracts the given tag info from the given HttpReposne String.
	 * 
	 * @param response input response string
	 * @param tag required tag
	 * @return String content of the given tag from the given response
	 */
	public String getTagFromResponse(String response, String tag) {
		String tagInfo = null;

		try {
			JSONObject entityJson = new JSONObject(response);
			tagInfo = entityJson.getString(tag);

		} catch (IllegalStateException e) {
			throw new RuntimeException("Illegal State Exception", e);

		} catch (JSONException e) {
			throw new RuntimeException("JSON Exception", e);

		}

		return tagInfo;
	}

	/**
	 * Method to extracts the given tag info (first element from the list) from the given HttpReposne String.
	 * 
	 * @param response input response string
	 * @param tag required tag
	 * @return String content of the given tag from the given response
	 */
	public String getTagFromResponseArray(String response, String tag) {
		String tagInfo = null;

		try {
			JSONObject entityJson = new JSONObject(response);
			tagInfo = entityJson.getJSONArray(tag).get(0).toString();

		} catch (IllegalStateException e) {
			throw new RuntimeException("Illegal State Exception", e);

		} catch (JSONException e) {
			throw new RuntimeException("JSON Exception", e);

		}

		return tagInfo;
	}

	/**
	 * Method converts HttpReposne to string and returns it.
	 * 
	 * @param response input HTTPResponse
	 * @return String converted string of given HTTPResponse
	 */
	private String returnResponseAsString(HttpResponse response) {
		BufferedReader br = null;
		StringBuilder sb = null;
		String responseString = null;

		try {
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				sb = new StringBuilder();
				br = new BufferedReader(new InputStreamReader(
						entity.getContent()));
				String readLine;
				while (((readLine = br.readLine()) != null)) {
					sb.append(readLine);
				}
			}

			responseString = sb.toString();

		} catch (IllegalStateException e) {
			throw new RuntimeException("Illegal State Exception", e);

		} catch (IOException e) {
			throw new RuntimeException("IO Exception", e);

		}

		return responseString;
	}

	/**
	 * Method to execute the given type of HttpMethod and returns the total
	 * response as string. Response includes status code, errors and payload.
	 * Use method getTagFromResponse(String response, String tag) to extract
	 * specific tag information. Use this method for GET requests.
	 * 
	 * @param endPointURL end Point URL
	 * @param httpMethodType HTTP Method Type
	 * @return String executes given request and returns response as string
	 */
	public String httpMethodWithHeaders(String endPointURL,
			HttpMethodType httpMethodType) {

		return httpMethodWithHeaders(endPointURL, httpMethodType, null);
	}

	/**
	 * @author plankir 
	 * 
	 * Method to make a Web Service call with your own custom
	 *         headers
	 * 
	 * @param endPointURL end Point URL
	 * @param headers HttpRequestBase input headers method
	 * @param httpMethodType HTTP Method Type
	 * @return String executes given request and returns response as string
	 */

	public String httpMethodWithHeaders(String endPointURL, HttpRequestBase headers,
			HttpMethodType httpMethodType) {
		return httpMethodWithHeaders(endPointURL,headers, httpMethodType, null);
	}

	/**
	 * Method to execcute the given type of HttpMethod and returns the total
	 * response as string. Response includes status code, errors and payload.
	 * Use method getTagFromResponse(String response, String tag) to extract
	 * specific tag information.
	 * 
	 * @param endPointURL end Point URL
	 * @param httpMethodType HTTP Method Type
	 * @param inputJsonReqStr input JSON Request
	 * @return String executes given request and returns response as string
	 */
	public String httpMethodWithHeaders(String endPointURL,
			HttpMethodType httpMethodType, String inputJsonReqStr) {
		HttpResponse response = null;

		if (httpMethodType == HttpMethodType.GET) {
			response = getResponseHttpGetWithHeaders(endPointURL);
		} else if (httpMethodType == HttpMethodType.POST) {
			response = getResponseHttpPostWithHeaders(endPointURL,
					inputJsonReqStr);
		} else if (httpMethodType == HttpMethodType.PUT) {
			response = getResponseHttpPutWithHeaders(endPointURL,
					inputJsonReqStr);
		} else if (httpMethodType == HttpMethodType.DELETE) {
			// TODO add implementation for delete
		}

		return returnResponseAsString(response);
	}

	/**
	 * Method to execcute the given type of HttpMethod and returns the total
	 * response as string. Response includes status code, errors and payload.
	 * Use method getTagFromResponse(String response, String tag) to extract
	 * specific tag information.
	 * 
	 * @param endPointURL end Point URL
	 * @param httpMethodType HTTP Method Type
	 * @param inputJsonReqStr input JSON Request
	 * @param headers HttpRequestBase
	 *            object with Headers
	 * @return String executes given request and returns response as string
	 */
	public String httpMethodWithHeaders(String endPointURL,
			HttpRequestBase headers, HttpMethodType httpMethodType,
			String inputJsonReqStr) {
		HttpResponse response = null;

		if (httpMethodType == HttpMethodType.GET) {
			response = getResponseHttpGetWithHeaders(endPointURL, headers);
		} else if (httpMethodType == HttpMethodType.POST) {
			response = getResponseHttpPostWithHeaders(endPointURL,headers,
					inputJsonReqStr);
		} else if (httpMethodType == HttpMethodType.PUT) {
			response = getResponseHttpPutWithHeaders(endPointURL,headers,
					inputJsonReqStr);
		} else if (httpMethodType == HttpMethodType.DELETE) {
			// TODO add implementation for delete
		}

		return returnResponseAsString(response);
	}

	/**
	 * Method to execute HttpGet request for the given endpoint url. Also
	 * extracts Payload from the response and returns it.
	 * 
	 * @param endPointURL end Point URL
	 * @return PayLoad from the response
	 */
	public String httpGetWithHeaders(String endPointURL) {

		return httpGetWithHeaders(endPointURL, HttpStatus.SC_OK);
	}

	/**
	 * Method to execute HttpGet request for the given endpoint url. It
	 * validates the response status code with the given status code. Also
	 * extracts Payload from the response and returns it.
	 * 
	 * @param endPointURL end Point URL
	 * @param statusCode input status code
	 * @return String returns response as string
	 */
	public String httpGetWithHeaders(String endPointURL, int statusCode) {

		HttpResponse response = getResponseHttpGetWithHeaders(endPointURL);
		Assert.assertTrue(response.getStatusLine().getStatusCode() == statusCode);

		return getTagFromResponse(response, "payload");
	}

	public HttpResponse httpGetResponse(String endPointURL) {

		HttpResponse response = getResponseHttpGetWithHeaders(endPointURL);
		return response;
	}

	/**
	 * Method to execute HttpPost request for the given endpoint url and given
	 * input json request string. Also extracts Payload from the response and
	 * returns it.
	 * 
	 * @param endPointURL end Point URL
	 * @param inputJsonReqStr input JSON Request
	 * @return PayLoad from the response
	 */
	public String httpPostWithHeaders(String endPointURL, String inputJsonReqStr) {

		return httpPostWithHeaders(endPointURL, inputJsonReqStr,
				HttpStatus.SC_OK);
	}

	/**
	 * Method to execute HttpPost request for the given endpoint url and given
	 * input json request string. It validates the response status code with the
	 * given status code. Also extracts Payload from the response and returns
	 * it.
	 * 
	 * @param endPointURL end Point URL
	 * @param inputJsonReqStr input JSON Request
	 * @param statusCode input status code
	 * @return String extract payload from given response and returns it as string
	 */
	public String httpPostWithHeaders(String endPointURL,
			String inputJsonReqStr, int statusCode) {

		HttpResponse response = getResponseHttpPostWithHeaders(endPointURL,
				inputJsonReqStr);
		Assert.assertTrue(response.getStatusLine().getStatusCode() == statusCode);

		return getTagFromResponse(response, "payload");
	}

	/**
	 * Method to execute HttpGet request for the given endpoint url. Returns
	 * HttpResponse.
	 * 
	 * @param endPointURL end Point URL
	 * @return HttpResponse after executing HttpGet
	 */
	private HttpResponse getResponseHttpGetWithHeaders(String endPointURL) {
		HttpResponse response = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			get = new HttpGet(endPointURL);
			get = (HttpGet) setHeaders(get);

			response = httpClient.execute(get);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new RuntimeException("Client Protocol Exception", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("IO Exception", e);
		}

		return response;
	}

	/**
	 * @author plankir 
	 * 
	 * Method to execute HttpGet request for the given endpoint
	 *         url. Returns HttpResponse.
	 * 
	 * @param endPointURL end Point URL
	 * @return HttpResponse after executing HttpGet
	 */
	private HttpResponse getResponseHttpGetWithHeaders(String endPointURL,
			HttpRequestBase headers) {
		HttpResponse response = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			get = new HttpGet(endPointURL);
			get.setHeaders(headers.getAllHeaders());
			response = httpClient.execute(get);

		} catch (ClientProtocolException e) {
			throw new RuntimeException("Client Protocol Exception", e);
		} catch (IOException e) {
			throw new RuntimeException("IO Exception", e);
		}

		return response;
	}

	/**
	 * @author plankir
	 * Method to execute HttpPost request for the given endpoint url and given
	 * input json request string. Returns HttpResponse.
	 * 
	 * @param endPointURL end Point URL
	 * @param inputJsonReqStr input JSON Request
	 * @param headers httpRequestBase with Headers set
	 * @return HttpResponse after executing HttpPost
	 * 
	 */
	public HttpResponse getResponseHttpPostWithHeaders(String endPointURL,
			HttpRequestBase headers, String inputJsonReqStr) {
		HttpResponse response = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			post = new HttpPost(endPointURL);
			post.setHeaders(headers.getAllHeaders());
			StringEntity requestEntity = new StringEntity(inputJsonReqStr);
			post.setEntity(requestEntity);

			response = httpClient.execute(post);

		} catch (ClientProtocolException e) {
			throw new RuntimeException("Client Protocol Exception", e);
		} catch (IOException e) {
			throw new RuntimeException("IO Exception", e);
		}

		return response;
	}

	/**
	 * Method to execute HttpPost request for the given endpoint url and given
	 * input json request string. Returns HttpResponse.
	 * 
	 * @param endPointURL end Point URL
	 * @param inputJsonReqStr input JSON Request
	 * @return HttpResponse after executing HttpPost
	 */
	public HttpResponse getResponseHttpPostWithHeaders(String endPointURL,
			String inputJsonReqStr) {
		HttpResponse response = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			post = new HttpPost(endPointURL);
			post = (HttpPost) setHeaders(post);

			StringEntity requestEntity = new StringEntity(inputJsonReqStr); // ContentType.APPLICATION_JSON);
			post.setEntity(requestEntity);

			response = httpClient.execute(post);

		} catch (ClientProtocolException e) {
			throw new RuntimeException("Client Protocol Exception", e);
		} catch (IOException e) {
			throw new RuntimeException("IO Exception", e);
		}

		return response;
	}

	/**
	 * Method to execute HttpPost request for the given endpoint url and given
	 * input json request string. Returns HttpResponse.
	 * 
	 * @param endPointURL end Point URL
	 * @param inputJsonReqStr input JSON Request
	 * @param headers httpRequestBase with Headers set
	 * @return HttpResponse after executing HttpPost
	 * 
	 */
	public HttpResponse getResponseHttpPutWithHeaders(String endPointURL,
			HttpRequestBase headers, String inputJsonReqStr) {
		HttpResponse response = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			put = new HttpPut(endPointURL);
			put.setHeaders(headers.getAllHeaders());
			StringEntity requestEntity = new StringEntity(inputJsonReqStr);
			put.setEntity(requestEntity);

			response = httpClient.execute(put);

		} catch (ClientProtocolException e) {
			throw new RuntimeException("Client Protocol Exception", e);
		} catch (IOException e) {
			throw new RuntimeException("IO Exception", e);
		}

		return response;
	}

	/**
	 * Method to execute HttpPut request for the given endpoint url and given
	 * input json request string. Returns HttpResponse.
	 * 
	 * @param endPointURL end Point URL
	 * @param inputJsonReqStr input JSON Request
	 * @return HttpResponse after executing HttpPut
	 */
	public HttpResponse getResponseHttpPutWithHeaders(String endPointURL,
			String inputJsonReqStr) {
		HttpClient httpClient = new DefaultHttpClient();
		return getPutResponse(endPointURL,inputJsonReqStr);
	}

	private HttpResponse getPutResponse(String endPointURL,String inputJsonReqStr)
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			put = new HttpPut(endPointURL);
			put = (HttpPut) setHeaders(put);

			StringEntity requestEntity = new StringEntity(inputJsonReqStr);			//		ContentType.APPLICATION_JSON);
			put.setEntity(requestEntity);

			response = httpClient.execute(put);

		} catch (ClientProtocolException e) {
			throw new RuntimeException("Client Protocol Exception", e);
		} catch (IOException e) {
			throw new RuntimeException("IO Exception", e);
		}
		return response;
	}

	public HttpResponse getResponseHttpPutWithHeaders(String endPointURL,
			String inputJsonReqStr, boolean poolingCon) {

		PoolingClientConnectionManager conMan = new PoolingClientConnectionManager( SchemeRegistryFactory.createDefault() );
		conMan.setDefaultMaxPerRoute(20);

		//HttpClient httpClient = new DefaultHttpClient(conMan, null);
		return getPutResponse(endPointURL,inputJsonReqStr);
	}

	/**
	 * Method to add the header parameters to given HttpRequestBase method.
	 * 
	 * @param method input HttpRequestBase
	 * @return HttpRequestBase sets headers and returns HttpRequestBase method
	 */
	private HttpRequestBase setHeaders(HttpRequestBase method) {
		method.addHeader("Key1", "Value1");
		method.addHeader("Key2", "Value2");
		method.addHeader("Key3", "Value3");
		return method;
	}
}

