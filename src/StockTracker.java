import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

//Using StockQuote.java as an example

public class StockTracker {

	
	public static void main(String[] args) throws IOException{
		//testStock
		/*StockOption testStock = new StockOption("ASC");
		testStock.update();
		testStock.printData();*/
		StockOption[] market = buildMarket(10);
	}
	
	public static StockOption[] buildMarket(int marketSize) throws IOException{
		StockOption market[] = new StockOption[marketSize];
		int start, stop;
		
		//Get latest market data
		URL link = new URL("ftp://ftp.nasdaqtrader.com/SymbolDirectory/nasdaqlisted.txt");
		URLConnection conn =link.openConnection();
		InputStream in = conn.getInputStream();
		
		BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
		String line = null, symb = "";
		
		//download and file each stock
		buffer.readLine(); //Skip first line

		for(int index = 0; index < marketSize; index++){
			//Get symbols from data
			line = buffer.readLine();
			start = 0;
			stop = line.indexOf('|', start);
			
			symb = line.substring(start, stop);
			
			//download and build market object
			StockOption stock = new StockOption(symb);
			System.out.printf("%nNow downloading: %s%n",symb);
			stock.update();
			stock.printData();
		}
		
		
		
		return market;
	}
	
}
