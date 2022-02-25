// Written by Tunahan DANIŞ - B1905.010027

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FinalProject {

	private static ArrayList <String> shoppingCart = new ArrayList<String>(); // These are the global variables I had to use to make the program work
	private static ArrayList <Product> productList = new ArrayList<Product>();
	private static ArrayList <Customer> customerList = new ArrayList<Customer>();
	private static String currentID;

	
	public static void main(String[] args) {
		
		File file = new File("C:\\Users\\xD\\Desktop\\products.txt"); // This is for taking data from products.txt file and write it on an arraylist for use
		try {
			Scanner sc = new Scanner(file);
			while(sc.hasNextLine()) {
				String[] tempArr;
				tempArr = sc.nextLine().split("-");
				if(tempArr[3].equals("Phone")) { // Writing is done polymorphically using upcasting
					Product pro = new Phone(tempArr[0],tempArr[1],tempArr[2],tempArr[3],tempArr[4], tempArr[5]);
					productList.add(pro);
				} else if(tempArr[3].equals("TV")) {
					Product pro = new TV(tempArr[0],tempArr[1],tempArr[2],tempArr[3],tempArr[4], tempArr[5]);
					productList.add(pro);
				} else {
					Product pro = new DrawingTablet(tempArr[0],tempArr[1],tempArr[2],tempArr[3],tempArr[4], tempArr[5]);
					productList.add(pro);
				}
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		login(); // Initiates the program
		
		
	}
	
	public static void login() { // Login interface
		System.out.print("Welcome to X Shopping.\nPlease enter your ID: ");
		Scanner scanner = new Scanner(System.in); // Takes customer id and password for login check
		currentID = scanner.nextLine();
		System.out.print("Enter your password: ");
		String password = scanner.nextLine();
		
		
		try {
			File file = new File("C:\\Users\\xD\\Desktop\\customers.txt"); // Takes data from the customers.txt file to login check
			Scanner scn = new Scanner(file);
			while(scn.hasNextLine()) {
				String[] tempArr;
				tempArr = scn.nextLine().split("-");
				Customer cst = new Customer(tempArr[0], tempArr[1], tempArr[2]); // Adds to arraylist
				customerList.add(cst);
		}
			
			boolean contains = false; 
			for(Customer cst: customerList) { // Check is done here
				if(cst.id.equals(currentID) && cst.password.equals(password)) {
					contains = true;
					System.out.println("Welcome " + cst.name + ".");
					seeMenu(); // Initiates menu
					break;
				}
			}
			if(!contains) {
				System.out.println("Wrong ID or password. Try again.\n");
				login(); // If password or id is incorrect, it recurses
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}
	
	public static void seeMenu() {
		
		System.out.print("Please enter:\n1 if you wish to see the products\n2 if you wish to see your previous purchases\n3 to exit");
		Scanner sc = new Scanner(System.in);
		int answer = sc.nextInt();
		if(answer == 1) { // Goes to interface to examine products
			seeProducts();
		} else if(answer == 2) seePrevious(); // Opens purchases.txt and examines the previous purchases
		else {
			System.exit(0); // Just terminates the program
		}
	}
	
	public static void seeProducts() {
		
		System.out.print("Please enter what type of product you wish to see(TV,Phone,Drawing Tablet): "); // For searching by type
		File file = new File("C:\\Users\\xD\\Desktop\\products.txt");
		
		ArrayList<String[]> aList = new ArrayList<String[]>();

		try {
			
			Scanner scn = new Scanner(file); // Stores product information on an arraylist
while(scn.hasNextLine()) {
				
				aList.add(scn.nextLine().split("-"));
				
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		Scanner sc = new Scanner(System.in);
		String answer = sc.nextLine(); // Takes the answer
		switch(answer) { // Checks the products.txt file according to search by type
		 case "TV":
		 for(Product prod: productList) {
		 	if(prod.type.equals("TV")) {
		 		System.out.println(prod.toString()); // Polymorphically prints the information on products
		        	}
		        }
		        break;
		 case "Phone":
			for(Product prod: productList) {
			 if(prod.type.equals("Phone")) {
			 		System.out.println(prod.toString());
			        }
			}
		        break;
		    case "Drawing Tablet":
			for(Product prod: productList) {
			 if(prod.type.equals("Drawing Tablet")) {
			 		System.out.println(prod.toString());
			 }
			}
			break;
		 default:
 System.out.println("Incorrect command");
			 seeProducts(); // Recursion if incorrect command
		}
		System.out.println("If you wish to add any products in your shopping cart, enter its ID.\nIf you wish to see the menu, type 0");
		String ans = sc.nextLine();
		if(ans.equals("0")) {
			seeMenu();
		} else {
			shoppingCart.add(ans); // Adds product id to shopping cart
			System.out.println("You added the item with the id " + ans + " to your shopping cart, please enter: \n1 to clear the cart and go to main menu\n2 to finalize your purchase and go back to main menu\n3 to just go back to main menu ");
			String ans2 = sc.nextLine();
			switch(ans2) {
			case "1": // Clears the arraylist and goes back to menu
				shoppingCart.clear();
				seeMenu();
				break;
			case "2": // For purchasing the items on shopping cart
				purchase();
				break;
			case "3":
				seeMenu();
				break;
			}
		}
	}
	
	public static void purchase() {
		try {
			File file = new File("C:\\Users\\xD\\Desktop\\purchases.txt");
			FileWriter fw = new FileWriter(file); // For writing purchases
			fw.write(currentID); // Starts by writing id at the beginning of the line
			for(String str: shoppingCart) {
				for(int i = 0; i < productList.size(); i++) { // Checks if the item stock is above 0 in order to purchase
					if(productList.get(i).productID.equals(str) && Integer.parseInt(productList.get(i).stock) > 0) {
						fw.write("-" + str);
						productList.get(i).setStock(Integer.parseInt(productList.get(i).stock)); // Decreases stock of the product by 1
					}
				}
			}
			fw.write("\r\n"); // This was for line breaking so that different purchases would be on different lines but it doesn't work, I don't know why
			fw.close();
			seeMenu();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void seePrevious() { // Opens the purchases.txt and searches by currentID to print previous purchases
		File file = new File("C:\\Users\\xD\\Desktop\\purchases.txt");
		try {
			Scanner sc = new Scanner(file);
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				if(line.split("-")[0].equals(currentID)) {
					for(int i = 1; i < line.split("-").length; i++) {
						System.out.println(", " + line.split("-")[i]);
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

class Product {
	
	String productName, productID, type, stock, price;
	
	Product() {
		
	}
	
	Product(String productName, String productID, String type, String stock, String price) {
		
		this.productName = productName;
		this.productID = productID;
		this.type = type;
		this.stock = stock;
		this.price = price;
		
	}
	
	public String toString() { // This will be overridden
		return productID + " " + stock;
	}
	
	public void setStock(int stock) { // This is for decreasing the stock after purchase
		this.stock = String.valueOf(stock - 1);
	}
	
}

class TV extends Product {

	String screensize;
	
	TV() {
		
	}
	
	TV(String productName, String productID, String stock,String type, String price, String screensize) {
		super(productName, productID, type, stock, price);
		this.screensize = screensize;
	}
	@Override
	public String toString() { // Overrides the .toString() method that prints information on seeProducts() 
		
		return "Product Name: " + productName + "\nID: " + productID + "\nIn stock: " + stock + "\nPrice in USD: " + price + "\nScreen size: " + screensize + "\n\n";

	}
	
}

class Phone extends Product {

	String resolution;
	
	Phone() {
		
	}
	
	Phone(String productName, String productID, String stock,String type, String price, String resolution) {
		super(productName, productID, type, stock, price);
		this.resolution = resolution;
	}
	@Override
 public String toString() { // Overrides the .toString() method that prints information on seeProducts() 
		
		return "Product Name: " + productName + "\nID: " + productID + "\nIn stock: " + stock + "\nPrice in USD: " + price + "\nResolution: " + resolution + "\n\n";

	}
}

class DrawingTablet extends Product {
	
	String battery;
	
	DrawingTablet() {
		
	}
	
	DrawingTablet(String productName, String productID, String stock, String type, String price, String battery) {
		super(productName, productID, type, stock, price);
		this.battery = battery;
	}
	@Override
 public String toString() { // Overrides the .toString() method that prints information on seeProducts() 
		
		return "Product Name: " + productName + "\nID: " + productID + "\nIn stock: " + stock + "\nPrice in USD: " + price + "\nPen has battery?: " + battery + "\n\n";

	}
	
}

class Customer {
	
	String id,password,name;
	
	Customer() {
		
	}
	
	Customer(String id, String password, String name) {
		this.id = id;
		this.password = password;
		this.name = name;
	}
	
}