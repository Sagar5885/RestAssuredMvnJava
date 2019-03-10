package sagarrestassured;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static sagarrestassured.ReusableMethods.getSessionKEY;
import static sagarrestassured.ReusableMethods.rawToJson;
import static io.restassured.RestAssured.given;

public class basicJiraAPITest {

    @Test
    public void testJiraApi(){
        RestAssured.baseURI= "http://localhost:8080";
        Response res=given().header("Content-Type", "application/json").
                header("Cookie","JSESSIONID="+getSessionKEY()).
                body("{\n" +
                        "\t\"fields\": {\n" +
                        "\t\t\"project\": {\n" +
                        "\t\t\t\"key\": \"RES\"\n" +
                        "\t\t},\n" +
                        "\t\t\"summary\": \"REST API Create Issue/Bug Test with Automation\",\n" +
                        "\t\t\"description\": \"Creating issue/bug using rest api!\",\n" +
                        "\t\t\"issuetype\": {\n" +
                        "\t\t\t\"name\": \"Bug\"\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "}").when().
                post("/rest/api/2/issue").then().
                //log().all().
                statusCode(201).extract().response();
        JsonPath js= rawToJson(res);
        String id=js.get("id");
        System.out.println(id);
    }
}
