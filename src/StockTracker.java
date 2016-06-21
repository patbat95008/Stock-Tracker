import java.awt.*;
import java.awt.event.*;
import java.awt.GridBagConstraints;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.*;
import java.util.*;

//Using StockQuote.java as an example

public class StockTracker extends JPanel implements ActionListener {

	//Create a GUI using market data
	private static void createGUI(StockOption[] market){
		//Create and set up the window
		JFrame frame = new JFrame("Stock-Tracker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container topLevelContainer = new Container();
		
		//Add items
		/* Example of adding content
		 * contentPane.setBorder(someBorder);
		 * contentPane.add(someComponent, BorderLayout.CENTER); 
		 * contentPane.add(anotherComponent, BorderLayout.PAGE_END);
		 */
		JPanel contentPane = new JPanel(new BorderLayout());
		JTextField textField = new JTextField(20);
		//textField.addActionListener(this);
		
		String allMarketTxt = buildList(market);
		JTextArea textArea = new JTextArea(allMarketTxt);
		textArea.setEditable(false);
		//JScrollPane scrollPane = new JScrollPane(textArea);
		
		//Add everything
		frame.add(textArea);
		
		//Display the window
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) throws IOException{
		StockOption[] market = buildRandomList(5);
		
		//Debug: Print portfolio
		/*for(int i = 0; i < market.length; i++){
			market[i].printData();
		}*/
		//System.out.print(buildList(market));
		//Final market to display in text
		final StockOption[] fMarket = market;
		//Create and show the GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createGUI(fMarket);
			}
		});
	}
	
	//Creates a single string with printed results
	public static String buildList(StockOption[] market){
		String allProfiles = "";
		
		for(int i = 0; i < market.length; i++){
			allProfiles += market[i].giveData();
		}

		//Debug Printer
		//System.out.printf(allProfiles);
		
		return allProfiles;
	}
	
	public static StockOption[] buildRandomList(int marketSize) throws IOException{
		StockOption market[] = new StockOption[marketSize];
		int start, stop;
		
		//Get latest market data
		URL link = new URL("ftp://ftp.nasdaqtrader.com/SymbolDirectory/nasdaqlisted.txt");
		URLConnection conn =link.openConnection();
		InputStream in = conn.getInputStream();
		
		BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
		String line = null, symb = "", page = "";
		
		//download and file each stock
		buffer.readLine(); //Skip first line
		
		for(int index = 0; index < marketSize; index++){
			//Get symbols from data
			int j = (int)Math.random() * 100;
			
			while (j >= 0 && line != null){
				line = buffer.readLine();
				j--;
			}
			
			if(line == null){
				buffer = new BufferedReader(new InputStreamReader(in));
				line = buffer.readLine();
			}
			
			start = 0;
			stop = line.indexOf('|', start);
			
			symb = line.substring(start, stop);
			//download and build market array
			StockOption stock = new StockOption(symb);
			
			//Debug download statement
			System.out.printf("Now downloading: %s%n",symb);
			try{
				stock.update();
			} catch(IOException e){
				System.out.printf("%n=====%n"
						+ "Data Unreadable!"
						+ "%n=====%n%n");
			}
			//Only add stocks that work
			if(!stock.isBadStock())	market[index] = stock;
			else index--; //Skip the stock and read a new line
		}
		
		System.out.printf(page);
		return market;
	}
	
	//Builds a test array of stock options using an alphabetical list of market data
	//Throws out non-valid entries (It's pretty choosy)
	//IN: an integer of the number of listings to download
	//OUT: An array of the first [marketSize] of stocks
	public static StockOption[] buildTestList(int marketSize) throws IOException{
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
			//download and build market array
			StockOption stock = new StockOption(symb);
			
			//Debug download statement
			//System.out.printf("Now downloading: %s%n",symb);
			try{
				stock.update();
			} catch(IOException e){
				System.out.printf("%n=====%nData Unreadable!%n=====%n%n");
			}
			//Only add stocks that work
			if(!stock.isBadStock())	market[index] = stock;
			else index--; //Skip the stock and read a new line
		}
		return market;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
