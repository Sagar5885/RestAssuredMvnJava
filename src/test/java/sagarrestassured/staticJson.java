package sagarrestassured;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static sagarrestassured.ReusableMethods.rawToJson;
import static io.restassured.RestAssured.given;

public class staticJson {

    @Test
    public void addBookTest() throws IOException {
        RestAssured.baseURI="http://216.10.245.166";
        Response res = given().
                header("Content-Type", "application/json").
                body(GenerateStringFromResources("/Users/sdodia/Projects/RestAssuredProject/src/files/test.json")).
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

    public static String GenerateStringFromResources(String path) throws IOException{
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
