import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

//StockOption class - Downloads and stores individual stock options
public class StockOption {
	private String symbol, name;
	private double price, change, percentChange;
	private String marketCap, market; 
	
	public StockOption(String symbol, String name, double price, double change, double percentChange, String marketCap, String market){
		this.symbol = symbol;
		this.name = name;
		this.price = price;
		this.change = change;
		this.percentChange = percentChange;
		this.marketCap = marketCap;
		this.market = market;
	}
	
	public StockOption(String symbol){
		this.symbol = symbol;
	}
	
	//Update: Pulls the stock data from Google Finance using an HTML scraper
	//In: Symbol
	//Out: Nothing - Fills/Overwrites other parameters
	public void update () throws IOException{
		URL link = new URL("https://www.google.com/finance?q=" + symbol);
		
		URLConnection conn = link.openConnection();
		InputStream in = conn.getInputStream();
		
		// Get data
		BufferedReader buff = new BufferedReader(new InputStreamReader(in));
		
		String line = null, page = "";
		
		while ((line = buff.readLine()) != null){
			page += line;
			page += '\n';
		}
		
		int start = page.indexOf("values:[", 1);
		int stop = page.indexOf("]", start);
		
		String data = page.substring(start + 8, stop);
		
		//Debug Data printout
		System.out.println(data);
		
		//Get data into variables
		start = data.indexOf(',');
		stop = data.indexOf(',', start + 1);
		
		name = data.substring(start + 2, stop - 1);
		
		start = stop;
		stop = data.indexOf(',', start + 1);
		
		//price = Double.parseDouble( data.substring(start + 2, stop - 1) );
		
		start = stop;
		stop = data.indexOf(',', start + 1);
		
		//change = Double.parseDouble( data.substring(start + 2, stop - 1) );
		
		start = stop;
		stop = data.indexOf(',', start + 1);
		// skip "chg" data
		start = stop;
		stop = data.indexOf(',', start + 1);
		
		//percentChange = Double.parseDouble( data.substring(start + 2, stop - 1) );
		
		start = stop;
		stop = data.indexOf(',', start + 1);
		// skip ""
		start = stop;
		stop = data.indexOf(',', start + 1);
		
		//marketCap = data.substring(start +2, stop - 1);
		
		start = stop;
		stop = data.indexOf(',', start + 1);
		
		//market = data.substring(start + 2, stop - 1);
		
	}
	
	public void printData(){
		System.out.printf("==========%n"
				+ "%s: %s "
				+ "%n==========%n"
				+ "Price:\t$%.2f%n"
				+ "Change:\t%.2f ( %.2f %% )%n"
				+ "Market:\t%s; Cap: %s%n"
				+ "==========%n",
				symbol, name, price, change, percentChange, market, marketCap);
	}
	
	//Getters//
	public String getSymbol(){ return symbol;}
	
	public String getName(){ return name;}
	
	public double getPrice(){ return price;}
	
	public double getChange(){ return change;}
	
	public double getPercChange(){ return percentChange;}
	
	public String getMrktCap(){ return marketCap;}
	
	public String getMarket(){ return market;}
	
}
