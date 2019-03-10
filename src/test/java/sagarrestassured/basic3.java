package sagarrestassured;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static sagarrestassured.ReusableMethods.rawToJson;
import static sagarrestassured.payload.getPostData;
import static sagarrestassured.resources.deleteData;
import static sagarrestassured.resources.postData;
import static io.restassured.RestAssured.given;

public class basic3 {
    private static Logger log = LogManager.getLogger(basic3.class.getName()); 
	
    Properties props = new Properties();

    @BeforeTest
    public void getData() throws IOException {

        FileInputStream fis = new FileInputStream("/Users/sdodia/Desktop/RestAssuredMevanProject/reassuredwithmevanjava/src/main/java/sagarrestassured/env.properties");
        ///
        props.load(fis);
        props.get("HOST");
    }

    @Test
    public void AddandDelete(){
        //String b = "{\"name\":\"test\",\"salary\":\"123\",\"age\":\"23\"}";
    	try {
        log.info("log info!"+props.getProperty("HOST"));

        RestAssured.baseURI= props.getProperty("HOST");
        Response res = given().
                body(getPostData()).
                when().
                post(postData()).
                then().assertThat().statusCode(200).
                extract().response();

        //System.out.println(res.asString());
        log.debug("I am debuggin!"+res.asString());

        //JsonPath json = new JsonPath(res.asString());
        JsonPath json = rawToJson(res);
        String id = json.get("id");

        System.out.println(id);

        given().when().delete(deleteData()+id).then().
                assertThat().statusCode(200);
    	}catch(Exception ex) {
    		log.error("log error!"+ex.toString());
            log.fatal("log fatal!");
    	}
    }
}
