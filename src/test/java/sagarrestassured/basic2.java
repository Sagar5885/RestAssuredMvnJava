package sagarrestassured;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class basic2 {

    @Test
    public void postdata(){
        RestAssured.baseURI="http://dummy.restapiexample.com";
        given().
                body("{\"name\":\"test\",\"salary\":\"123\",\"age\":\"23\"}").
                when().
                post("/api/v1/create").
                then().assertThat().statusCode(200);
                //.and().body("name", equalTo("test"));
    }
}
