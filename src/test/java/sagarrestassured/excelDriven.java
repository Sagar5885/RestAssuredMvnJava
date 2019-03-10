package sagarrestassured;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static sagarrestassured.ReusableMethods.rawToJson;
import static sagarrestassured.payload.addBook;
import static io.restassured.RestAssured.given;

public class excelDriven {

    @DataProvider(name="booksData")
    public Object[][] setupData(){
        return new Object[][] {{"abcd", "1238"}, {"abcd", "1239"}, {"abcd", "12340"}};
    }

    @Test(dataProvider = "booksData")
    public void addBookTest(String isbn, String aisle){
        RestAssured.baseURI="http://216.10.245.166";
        Response res = given().
                header("Content-Type", "application/json").
                body(addBook(isbn, aisle)).
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
