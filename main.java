import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.lang.Object;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

class CodeLab extends JFrame{

	public static String currentUser;
	public static double Amount;
	public static String purchaseInfo;
	public static ArrayList<String> processedData = new ArrayList<String>();
	public static Calendar cal = Calendar.getInstance();
	public static SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
	public static Date date = cal.getTime();
	public static String currentDate = formatter.format(date);
	public static String totalRow;
	
	static void alert(String title, String message, int mode) {
		JFrame alertWin = new JFrame(title);
		JOptionPane.showMessageDialog(alertWin, message, title, mode);
		alertWin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	static void dashboard()
    {
		JFrame dashWin = new JFrame("CodeLab Dashboard");
        //headers for the table
        String[] columns = new String[] {
            "Service", "Quantity", "Price", "Total"
        };
         
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        //create widgets 
        JTextField serviceName = new JTextField("Service/Product");
        JTextField quantity = new JTextField("Quantity");
        JTextField price = new JTextField("Price");
        JButton addContent = new JButton("ADD");
        JButton clearContent = new JButton("CLEAR");
        JButton Checkout = new JButton("CONFIRM");
        JButton save = new JButton("SAVE");
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane();
        File logFile = new File("save.txt");
        try {
        	 if (logFile.createNewFile()) {
        	        System.out.println("File created: " + logFile.getName());
        	      } else {
        	        System.out.println("File Ready: " + logFile.getName());
        	      }
        }catch(IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          } 
        
        ActionListener addData = new ActionListener() {
        	public void actionPerformed(ActionEvent actionEvent){
        		try {
	        		String sn = serviceName.getText();
	        		String qt = quantity.getText();
	        		String prc = price.getText();
		            double subtotal = Double.parseDouble(qt) * Double.parseDouble(prc);
		            String subTotal =  Double.toString(subtotal);
	        		tableModel.addRow(new Object[]{sn, qt, prc, subTotal});
	        		double sum = 0;
	        		String name = " ";
	        		String quanti = " ";
	        		String smackaroonies = " ";
	        		String sub= " ";
	        		String info = " ";
	        		for(int i = 0; i < tableModel.getRowCount(); i++) {
	        			name = tableModel.getValueAt(i, 0).toString();
	        			quanti = tableModel.getValueAt(i, 1).toString();
	        			smackaroonies = tableModel.getValueAt(i, 2).toString();
	        			sub = tableModel.getValueAt(i, 3).toString();
	        			sum = sum + Double.parseDouble(tableModel.getValueAt(i, 3).toString());
	        		}
	        		
	        		Vector allData = tableModel.getDataVector();
        			info = String.format("%2s %10s %10s %10s", name, quanti, smackaroonies, sub) + "\n";
        			processedData.add(info);
	        		
	        		Amount = sum;
	        		purchaseInfo = String.join(",", processedData).replaceAll(",", "");
	        		
	        		int rows = tableModel.getRowCount();
	        		totalRow = Integer.toString(rows);
	        		
        		}catch(NumberFormatException ex) {
        			ex.printStackTrace();
        		}
        	}
        };
        
        ActionListener checkout = new ActionListener() {
        	public void actionPerformed(ActionEvent actionEvent) {
        		checkout();
        	}
        };
         
        ActionListener clearData = new ActionListener() {
        	public void actionPerformed(ActionEvent actionEvent) {
        		tableModel.setRowCount(0);
        		processedData.clear();
        		Amount = 0;
        		purchaseInfo = " ";
        	}
        };
        
        ActionListener saveData = new ActionListener() {
        	public void actionPerformed(ActionEvent actionEvent) {
        		 try {
        		      FileWriter myWriter = new FileWriter(logFile);
        		      String currentLog = "Name of In-Charge: " + currentUser + "\nTotal Amount: " + Amount + "\nCustomer Order:\n" + purchaseInfo;
        		      myWriter.write(currentLog);
        		      myWriter.close();
        		      System.out.println("Successfully wrote to the file.");
        		      alert("Process Notice", "Data Saved Successfully!", JOptionPane.INFORMATION_MESSAGE);
        		    } catch (IOException e) {
        		      System.out.println("An error occurred.");
        		      e.printStackTrace();
        		    }
        	}
        };
        
        //add the widgets to the frame
        dashWin.setVisible(true);
        dashWin.setLayout(null);
        dashWin.setResizable(false);
        serviceName.setBounds(5, 20, 215, 30);
        quantity.setBounds(5, 60, 215, 30);
        price.setBounds(5, 100, 215, 30);
        scrollPane.setBounds(230, 20, 420, 191);
        addContent.setBounds(5, 140, 100, 30);
        clearContent.setBounds(120, 140, 100, 30);
        Checkout.setBounds(5, 180, 100, 30);
        save.setBounds(120, 180, 100, 30);
        
        
        dashWin.add(scrollPane);
        dashWin.add(serviceName);
        dashWin.add(quantity);
        dashWin.add(price);
        dashWin.add(addContent);
        dashWin.add(clearContent);
        dashWin.add(Checkout);
        dashWin.add(save);
        addContent.addActionListener(addData);
        Checkout.addActionListener(checkout);
        clearContent.addActionListener(clearData);
        save.addActionListener(saveData);
        scrollPane.setViewportView(table);
        dashWin.setSize(660, 250);
        dashWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
       
    }
	 
	static void checkout() {
		JFrame checkoutWin = new JFrame("Checkout");
		JTextField customer = new JTextField("Customer Name");
		JTextField date = new JTextField("Date of Validity");
		JTextArea description = new JTextArea("Description/Services");
		JTextField refNum = new JTextField("Reference Number");
		JTextField amountPaid = new JTextField("Received Payment");
		JTextField totalPrice = new JTextField("Total Price");
		JButton generateButton = new JButton("Generate");
		JButton printReceipt = new JButton("Print Receipt");
		String totalAmount = Double.toString(Amount);
		String uniqueID = UUID.randomUUID().toString();
		cal.add(Calendar.YEAR, 1);
		Date nextYear = cal.getTime();
		String validity = formatter.format(nextYear);
		ActionListener generate = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					String customerName = customer.getText();
					String operationDate = date.getText();
					String referenceNum = refNum.getText();
					String services = description.getText();
					String tP = totalPrice.getText();
					String Info = customerName + "***" + tP  + "***" + operationDate + "***" + referenceNum + "***" + services + "***" + currentUser;
					String receiptContent = "---------------------------------";
					File parser = new File("parser.txt");
					FileWriter parsedInfo = new FileWriter(parser);
					parsedInfo.write(Info);
					parsedInfo.close();
					alert("Process Notice", "Data Stored Successfully!", JOptionPane.INFORMATION_MESSAGE);
				}catch (IOException e) {
					alert("Error Notice", "An Error Occured!", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
				
			}
		};
		
		ActionListener pntReceipt = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				JFrame receiptMaker = new JFrame("Saving Receipt");
				JLabel instruct = new JLabel("Enter your filename here:");
				JTextField fileName = new JTextField("Filename");
				JButton saveFileButton = new JButton("Save");	 
					
				ActionListener saveReceipt = new ActionListener() {
					public void actionPerformed(ActionEvent actionEvent) {
						try {
							String businessLoc = "\nPurok 1 Sta Rosa Sur\nJose Panganiban Camarines Norte\n";
							double TotalAmount = Double.parseDouble(amountPaid.getText()) - Double.parseDouble(totalPrice.getText());
							String receiptName = fileName.getText() + ".txt";
							File receiptDoc = new File(receiptName);
							if(receiptDoc.exists()) {
								alert("Creation Warning", "This file already exists!\nPlease rename your file.", JOptionPane.WARNING_MESSAGE);
								receiptMaker.dispose();
							}else {
								FileWriter receiptWriter = new FileWriter(receiptDoc);
								receiptWriter.write("\t\tCodeLab\t\n\tPrinting Services\nProcessed by: " + currentUser + businessLoc
										+ "REF No.: " + refNum.getText() + "\n\n\n" + "Service/Product Availed: " + totalRow + "\n\n" + description.getText() + "\n\n" + "TOTAL: " +
										totalPrice.getText() + "\nCash: " + amountPaid.getText() + "\nCHANGE: " + Double.toString(TotalAmount) + "\n\nReceived by: " + 
										customer.getText() + "\nReceived Date: " + date.getText() + "\nValid Until: " + validity.toString() + 
										"\n\n\n\"This serves as your sales invoice\"\n\tTHANK YOU, COME AGAIN!");
								receiptWriter.close();
								alert("Process Notice", "File Created Successfully", JOptionPane.INFORMATION_MESSAGE);
								receiptMaker.dispose();
							}
							
						}catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				
				
				instruct.setBounds(5, 5, 200, 50);
				fileName.setBounds(5, 45, 180, 30);
				saveFileButton.setBounds(5, 85, 180, 30);
				saveFileButton.addActionListener(saveReceipt);
				
				receiptMaker.add(saveFileButton);
				receiptMaker.add(fileName);
				receiptMaker.add(instruct);
				receiptMaker.setSize(200,160);
				receiptMaker.setResizable(false);
				receiptMaker.setLayout(null);
				receiptMaker.setVisible(true);
				receiptMaker.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		};
		
		customer.setBounds(5, 20, 380, 30);
		date.setBounds(5, 60, 380, 30);
		refNum.setBounds(5, 100, 380, 30);
		amountPaid.setBounds(5, 140, 380, 30);
		totalPrice.setBounds(5, 180, 380, 30);
		description.setBounds(5, 220, 380, 300);
		generateButton.setBounds(5, 530, 380, 30);
		generateButton.addActionListener(generate);
		printReceipt.setBounds(5, 570, 380, 30);
		totalPrice.setText(totalAmount);
		description.setText("Services\tQuantity\tPrice\tTotal" + "\n" + purchaseInfo);
		printReceipt.addActionListener(pntReceipt);
		description.disable();
		description.setDisabledTextColor(Color.black);
		refNum.setText(uniqueID);
		refNum.disable();
		refNum.setDisabledTextColor(Color.black);
		totalPrice.disable();
		totalPrice.setDisabledTextColor(Color.black);
		date.setText(currentDate);
		date.disable();
		date.setDisabledTextColor(Color.black);
		
		checkoutWin.setSize(400,640);
		checkoutWin.setResizable(false);
		checkoutWin.setLayout(null);
		checkoutWin.setVisible(true);
		checkoutWin.add(customer);
		checkoutWin.add(date);
		checkoutWin.add(description);
		checkoutWin.add(refNum);
		checkoutWin.add(amountPaid);
		checkoutWin.add(totalPrice);
		checkoutWin.add(generateButton);
		checkoutWin.add(printReceipt);
		
		checkoutWin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
	public static void main(String[] args) throws Exception {
		JFrame signWin = new JFrame("Sign In to CodeLab");
		JTextField name = new JTextField("Name");
		JTextField controlPass = new JTextField("Control Number");
		JButton verify = new JButton("Verify");
		ActionListener verifyInput = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					String user = name.getText();
					String controlKey = controlPass.getText();
					File database = new File("database.txt");
				    Scanner dataReader = new Scanner(database);
					while(dataReader.hasNextLine()) {
						String data = dataReader.nextLine();
						String[] dataArr = data.split("~");
						List dataList = Arrays.asList(dataArr);
						if(dataList.contains(user) && dataList.contains(controlKey)) {
							currentUser = user;
							dashboard();
							signWin.dispose();
						}
						else {
							System.out.println("Not Found");
						}
					}
				}catch(FileNotFoundException ex){
					System.out.println("Database not Found");
				}
			}
		};
		name.setBounds(5, 20, 380, 30);
		controlPass.setBounds(5, 60, 380, 30);
		signWin.add(name);
		signWin.add(controlPass);
		signWin.setSize(400,180);
		signWin.setResizable(false);
		signWin.setLayout(null);
		signWin.setVisible(true);
		verify.setBounds(150, 100, 100, 30);
		verify.addActionListener(verifyInput);
		signWin.add(verify);
		signWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
		
}



