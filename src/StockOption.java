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
	private boolean badStock = false;
	
	public StockOption () throws IOException{
		symbol = "AAPL";
		this.update();
	}
	
	//Debug constructor - DON'T USE
	public StockOption(String symbol, String name, double price, double change, double percentChange, String marketCap, String market){
		this.symbol = symbol;
		this.name = name;
		this.price = price;
		this.change = change;
		this.percentChange = percentChange;
		this.marketCap = marketCap;
		this.market = market;
	}
	
	//Default constructor
	//NOTE: It needs a valid stock market symbol to work!
	public StockOption(String symbol){
		this.symbol = symbol;
		name = "";
		price = 0; change = 0; percentChange = 0;
		marketCap = ""; market = "";
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
		//If it can't find the data where it's supposed to be, mark the stock as "bad"
		if(start == -1){
			badStock = true;
			throw new IOException("Data Unreadable!");
		}
		String data = page.substring(start + 8, stop);
		
		//Debug Data printout
		//System.out.printf("Updating...%nData Downloaded:%n%s%n%n", data);
		
		//Get data into variables
		start = data.indexOf(',');	//Increment
		stop = data.indexOf(',', start + 1);
		name = data.substring(start + 2, stop - 1);
		
		start = stop;	//Increment
		stop = data.indexOf(",\"", start + 1);		
		price = this.getDouble(data, start, stop);
		
		start = stop;	//Increment
		stop = data.indexOf(",\"", start + 1);
		change = this.getDouble(data, start, stop);
		
		start = stop;	//Increment
		stop = data.indexOf(",\"", start + 1);
		// skip line
		start = stop;	//Increment
		stop = data.indexOf(",\"", start + 1);
		percentChange = this.getDouble(data, start, stop);
		
		start = stop;	//Increment
		stop = data.indexOf(',', start + 1);
		// skip line
		start = stop;	//Increment
		stop = data.indexOf(',', start + 1);
		marketCap = data.substring(start +2, stop - 1);
		
		start = stop;	//Increment
		stop = data.indexOf(',', start + 1);
		market = data.substring(start + 2, stop - 1);
		
	}
	
	//try and pull a double out of the data
	private double getDouble(String line, int start, int stop){
		double dub = 0;
				
		//Remove potential commas
		String priceSTR = line.substring(start + 2, stop - 1);
		priceSTR = this.removeComma(priceSTR);
		
		try{
			dub = Double.parseDouble(priceSTR);
		} catch (NumberFormatException e){
			dub = 0;
			badStock = true;
		}
		
		return dub;
	}
	
	//Takes in a string, removes all commas, returns fixed string
	private String removeComma(String in){
		while(in.indexOf(',') != -1){
			int index = in.indexOf(',');
			in = in.substring( 0, index )
					+ in.substring(index + 1, in.length() - 1);
		}
		
		return in;
	}
	
	/* Prints all data - Example:
	 *  ==========
	 *	SYM: Stock Name 
	 *	==========
	 *	Price:	$3627.90
	 *	Change:	167.91 ( 4.85 % )
	 *	Market:	LON; Cap: 3.03B
	 *	==========
	 */
	public void printData(){
		
		System.out.printf(""
				+ "==========%n"
				+ "%s: %s "
				+ "%n==========%n"
				+ "Price:\t$%.2f%n"
				+ "Change:\t%.2f ( %.2f %% )%n"
				+ "Market:\t%s; Cap: %s%n"
				+ "==========%n%n",
				symbol, name, price, change, percentChange, market, marketCap);
	}
	
	//Returns an unedited string for GUI display
	//Prints in same format as above
	public String giveData(){
		String data = "";
		
		data += "==========\n";
		data += "( " + symbol + " )" + ": " + name;
		data += "\n==========\n";
		data += "Price:\t$ " + Double.toString(price) + '\n';
		data += "Change:\t" + Double.toString(change) + " ( " + Double.toString(percentChange) +"%% )\n";
		data += "Market:\t" + market + "; Cap: " + marketCap + "\n"
				+ "==========\n\n";
		
		return data;
	}
	
	//Setter//
	public void setSymbol(String symb) throws IOException{
		symbol = symb;
		this.update();
	}
	
	//Getters//
	public boolean isBadStock(){ return badStock;}
	
	public String getSymbol(){ return symbol;}
	
	public String getName(){ return name;}
	
	public double getPrice(){ return price;}
	
	public double getChange(){ return change;}
	
	public double getPercChange(){ return percentChange;}
	
	public String getMrktCap(){ return marketCap;}
	
	public String getMarket(){ return market;}
	
}
