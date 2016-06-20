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
		
		int start = page.indexOf("values:[", 1); //find start of data
		int stop = page.indexOf("]", start); //Find end of data
		
		String data = page.substring(start + 8, stop);
		
		//Debug Data printout
		//System.out.printf("Updating...%nData Downloaded:%n%s%n", data);
		
		
		//Get data into variables
		start = data.indexOf(',');	//Increment
		stop = data.indexOf(',', start + 1);
		
		name = data.substring(start + 2, stop - 1);
		
		start = stop;	//Increment
		stop = data.indexOf(",\"", start + 1);
		
		//Remove potential commas
		String priceSTR = data.substring(start + 2, stop - 1);
		priceSTR = this.removeComma(priceSTR);
		
		price = Double.parseDouble(priceSTR);
		
		start = stop;	//Increment
		stop = data.indexOf(",\"", start + 1);
		
		change = Double.parseDouble( data.substring(start + 2, stop - 1) );
		
		start = stop;	//Increment
		stop = data.indexOf(",\"", start + 1);
		// skip "chg" data
		start = stop;	//Increment
		stop = data.indexOf(",\"", start + 1);
		
		percentChange = Double.parseDouble( data.substring(start + 2, stop - 1) );
		
		start = stop;	//Increment
		stop = data.indexOf(',', start + 1);
		// skip ""
		start = stop;	//Increment
		stop = data.indexOf(',', start + 1);
		
		marketCap = data.substring(start +2, stop - 1);
		
		start = stop;	//Increment
		stop = data.indexOf(',', start + 1);
		
		market = data.substring(start + 2, stop - 1);
		
	}
	
	private String removeComma(String in){
		while(in.indexOf(',') != -1){
			int index = in.indexOf(',');
			in = in.substring( 0, index )
					+ in.substring(index + 1, in.length() - 1);
		}
		
		return in;
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
