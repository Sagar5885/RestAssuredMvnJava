package sagarrestassured;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static sagarrestassured.ReusableMethods.rawToJson;
import static io.restassured.RestAssured.given;

public class basicTwitterAPITest1 {

    String consumerKey = "EL7rhbG7oOGY8CssJ6nsZ3bFh";
    String consumerSecret = "BPw4h4D9XOgifcnH5SUSL1PKJNe1TZPUXoQNrfoGr59SLiHYzw";
    String token = "800845638170603521-LxtWdjFFdkZfGXBTA4IQ4qvobPHRRb2";
    String tokenSecret = "l0KMMavJUzAl1uRofwAeVMNGDj0KnpWTYJi7lW505mhsV";
    String id;

    @Test
    public void testCreateTweet(){
        RestAssured.baseURI= "https://api.twitter.com";
        Response res = given().
                auth().oauth(consumerKey, consumerSecret, token, tokenSecret).
                queryParam("status", "I am tweeting from Automation Rest Api New - 2!").
                when().post("/1.1/statuses/update.json").then().extract().response();

        JsonPath js= rawToJson(res);
        id = js.get("id").toString();
    }

    @Test(dependsOnMethods = "testCreateTweet")
    public void testGetLatestTweet(){
        RestAssured.baseURI= "https://api.twitter.com";
        Response res = given().
                auth().oauth(consumerKey, consumerSecret, token, tokenSecret).
                queryParam("count", "1").
                when().get("/1.1/statuses/home_timeline.json").then().extract().response();

        JsonPath js= rawToJson(res);
        System.out.println(js.get("text").toString());
    }

    @Test(dependsOnMethods = "testGetLatestTweet")
    public void testDeleteTweet(){
        RestAssured.baseURI= "https://api.twitter.com";
        Response res = given().
                auth().oauth(consumerKey, consumerSecret, token, tokenSecret).
                when().post("/1.1/statuses/destroy/"+id+".json").then().extract().response();

        JsonPath js= rawToJson(res);
        System.out.println("Deleted Tweet using automation!");
        System.out.println(js.get("text").toString());
        System.out.println(js.get("truncated").toString());
    }
}
