package sagarrestassured;

public class payload {

    public static String getPostData(){
        return "{\"name\":\"test\",\"salary\":\"123\",\"age\":\"23\"}";
    }

    public static String addBook(String isbn, String aisle){
        return "{ \"name\":\"Learn Rest Assured with Java8\", \"isbn\":\""+isbn+"\", \"aisle\":\""+aisle+"\", \"author\":\"Saggy\" }";
    }
}
