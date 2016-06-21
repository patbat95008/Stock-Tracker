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

	private static void createGUI(StockOption[] market){
		//Create and set up the window
		JFrame frame = new JFrame("Stock-Tracker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container topLevelContainer = new Container();
		
		//Add items
		//JLabel label = new JLabel("     Hello World!");
		//frame.getContentPane().add(label);
		/* Example of adding content
		 * contentPane.setBorder(someBorder);
		 * contentPane.add(someComponent, BorderLayout.CENTER); 
		 * contentPane.add(anotherComponent, BorderLayout.PAGE_END);
		 */
		JPanel contentPane = new JPanel(new BorderLayout());
		JTextField textField = new JTextField(20);
		//textField.addActionListener(this);
		
		String allMarketTxt = buildList(market);
		
		JTextArea textArea = new JTextArea(
				allMarketTxt
				);
		
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		frame.add(textArea);
		
		
		//Display the window
		//frame.add();
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) throws IOException{
		//testStock
		/*StockOption testStock = new StockOption("ASC");
		testStock.update();
		testStock.printData();*/
		StockOption[] market = buildMarket(3);
		
		//Print results
		/*for(int i = 0; i < market.length; i++){
			market[i].printData();
		}*/
		
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
		String allProfiles = " ";
		
		for(int i = 0; i < market.length; i++){
			allProfiles += market[i].giveData();
		}
		
		System.out.printf(allProfiles);
		
		return allProfiles;
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
			
			//download and build market array
			StockOption stock = new StockOption(symb);
			
			System.out.printf("Now downloading: %s%n",symb);
			
			try{
				stock.update();
			} catch(IOException e){
				System.out.printf("%n=====%nData Unreadable!%n=====%n%n");
			}
			//stock.printData();
			
			market[index] = stock;
		}
		
		
		
		return market;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
