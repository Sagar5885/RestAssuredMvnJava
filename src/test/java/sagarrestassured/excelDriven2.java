package sagarrestassured;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static sagarrestassured.ReusableMethods.rawToJson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;

public class excelDriven2 {

    @Test
    public void addBookTest() throws IOException{
    	
    	dataDriven d = new dataDriven();
		List<String> datalist = d.getData("RestTest", "RestAssured");
    	
    	HashMap<String, Object> jsonAsMap = new HashMap<String, Object>();
    	jsonAsMap.put("name", datalist.get(0));
    	jsonAsMap.put("isbn", datalist.get(1));
    	jsonAsMap.put("aisle", datalist.get(2));
    	jsonAsMap.put("author", datalist.get(3));
    	
    	//For nested json - create a another hashmap and pass to parent hashmap as object
    	
        RestAssured.baseURI="http://216.10.245.166";
        Response res = given().
                header("Content-Type", "application/json").
                body(jsonAsMap).
                when().
                post("/Library/Addbook.php").
                then().assertThat().statusCode(200).
                extract().response();

        JsonPath json = rawToJson(res);
        String id = json.get("ID");
        System.out.println(id);
        
      //Delete Data
        Response resDelete = given().
                body("{\"ID\" : \""+id+"\"}").
                when().
                post("/Library/DeleteBook.php").
                then().assertThat().statusCode(200).
                extract().response();
    }
}
