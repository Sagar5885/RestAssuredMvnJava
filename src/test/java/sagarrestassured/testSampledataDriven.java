package sagarrestassured;
import java.io.IOException;
import java.util.List;

public class testSampledataDriven {
	
	public static void main(String args[]) throws IOException {
		dataDriven d = new dataDriven();
		List<String> res = d.getData("Purchase", "Sheet1");
		
		System.out.println(res.get(0));
		System.out.println(res.get(1));
		System.out.println(res.get(2));
		System.out.println(res.get(3));
	}

}
