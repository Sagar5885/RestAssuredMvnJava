package sagarrestassured;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static sagarrestassured.ReusableMethods.rawToJson;
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.RestAssured.given;

public class basictest {

    @Test
    public void main(){
        RestAssured.baseURI="https://maps.googleapis.com";
        Response res = given().
                param("location", "-33.8670522,151.1957362").
                param("radius", "500").
                param("key", "AIzaSyBw0GzsF4oxmw2-05Evt4OI1WSEWu7pJ_A").
                when().
                get("/maps/api/place/nearbysearch/json").
                then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and()
                .body("error_message", equalTo("You have exceeded your daily request quota for this API. If you did not set a custom daily request quota, verify your project has an active billing account: http://g.co/dev/maps-no-account")).
                and().header("server", "scaffolding on HTTPServer2").extract().response();

        JsonPath json = rawToJson(res);
        int count = json.get("results.size()");
        System.out.println(count);
                /* given also contains:
                headers,
                body,
                cookies,
                 */
    }

    @Test
    public void main1(){
        RestAssured.baseURI="http://dummy.restapiexample.com";
        Response res = given().
                when().
                get("/api/v1/employee/419").
                then().
                log().all().
                assertThat().statusCode(200).
                log().body().
                extract().response();

        //System.out.println(rawToJson(res).toString());

    }
}
