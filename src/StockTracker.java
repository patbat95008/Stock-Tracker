import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

//Using StockQuote.java as an example

public class StockTracker {

	
	public static void main(String[] args) throws IOException{
		//testStock
		StockOption testStock = new StockOption("ASC");
		testStock.update();
		testStock.printData();
	}
	
}
