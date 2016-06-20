import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class StockTracker {

	public static void main(String[] args) throws IOException{
		// Connect to stock data repository
		URL link = new URL("ftp://ftp.nasdaqtrader.com/SymbolDirectory/nasdaqlisted.txt");
		
		URLConnection conn = link.openConnection();
		InputStream in = conn.getInputStream();
		
		// Get data
		BufferedReader buff = new BufferedReader(new InputStreamReader(in));
		
		String line = null;
		
		//TMP - Print results - TMP
		while ((line = buff.readLine()) != null){
			System.out.println(line);
		}
		// Filter information to get relevant stocks
		// Analyze stock data
		

	}

}
