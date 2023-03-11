/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmu.ticketmain;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Tj
 */
public class CentralCore {
    private static List<Ticket> ticketList = new ArrayList();
    private static List<User> userList = new ArrayList();
    private static List<TransactionStream> transactionList = new ArrayList();
    private static List<TransactionStream> buy_sell_transList = new ArrayList();
    
    private static User activeUser = null;
    
    public static void main(String[] args) throws IOException{
        if(args.length > 0){
            System.out.println("No args needed.");
        }
        
        String userInput = "";
        boolean firstRun = true;
        try(BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))){
            //prompts can be changed
            System.out.println("Welcome to the ticket system");
            System.out.println("Enter a command, or 'c' for a list of commands.");

            while((userInput = stdIn.readLine()) != null){
                //readin user inputs and assess them
                
                if(userInput.equals("Login")){
                    System.out.println("Please enter your username.");
                    String loginUser = stdIn.readLine();
                   
                    while(!login(loginUser)){
                        System.out.println("No such user can be found, try again or enter 'quit' to stop");
                        loginUser = stdIn.readLine();
                        if(loginUser.equals("quit")){
                            System.out.println("Login attempt ended");
                            break;
                        }
                    }

                    if(activeUser != null){
                        System.out.println("Login successful");
                    }
                }else if(userInput.equals("Quit") && activeUser == null){
                    break;
                }else if(userInput.equals("c")){
                    getUserOperations();
                }else if(userInput.equals("Logout")){
                    logout();
                }else if(userInput.equals("Create") && activeUser != null && activeUser.getUsertype().equals("aa")){
                    //call admin create
                }else if(userInput.equals("Delete") && activeUser != null && activeUser.getUsertype().equals("aa")){
                    //call admin delete
                }else if(userInput.equals("Buy") && activeUser != null && activeUser.getUsertype() != "ss"){
                    //call the sell method
                }else if(userInput.equals("Sell") && activeUser != null && activeUser.getUsertype() != "bs"){    
                    //call the buy method
                }else if(userInput.equals("AddCredit") && activeUser != null){
                    //call addcredit
                }else{
                    System.out.println("Command not understood");
                }

                if(firstRun){
                    firstRun = false; 
                 }else{
                     System.out.println("Enter a command, or 'c' for a list of commands.");
                 }
            }        
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    public static boolean login(String userName){
        boolean userFound = false;
        for(int i = 0; i < userList.size(); i++){
            if(userList.get(i).getUsername().equals("userName")){
                userFound = true;
                activeUser = userList.get(i);
            }
        }

        return userFound;
    }
    
    public static void logout(){
        //once the PR for creating a daily trans file is merged, need to call that method here to actually write the daily file
        activeUser = null;
        System.out.println("Logout successful.");
    }
    
    public void createDailyTransaction(int transCode, String userName, String userType, int userCredit){
    }
    
    public static void getUserOperations(){
        if(activeUser == null){
            System.out.println("Login, Quit");
        }else if(activeUser.getUsertype().equals("aa")){
            System.out.println("Buy, Sell, Refund, Delete, Create, AddCredit, Logout, c");
        }else if(activeUser.getUsertype().equals("bs")){
            System.out.println("Buy, AddCredit, Logout, c");
        }else if(activeUser.getUsertype().equals("ss")){
            System.out.println("Sell, AddCredit, Logout, c");
        }else if(activeUser.getUsertype().equals("fs")){
            System.out.println("Buy, Sell, AddCredit, Logout, c");
        }
    }
    
    public static List<Ticket> getTickets(){
        return ticketList;
    }
    
    public static List<User> getUsers(){
        return userList;
    }
    
    public static List<TransactionStream> getTransactions(){
        return transactionList;
    }
    
    public static void addTransaction(int code, String eventName, String sellerUser, int ticketQuantity, double price){
        transactionList.add(new TransactionStream(code, eventName, sellerUser, ticketQuantity, price));
    }
    
    public static void addBuySellTransaction(int code, String eventName, String sellerUser, int ticketQuantity, double price){
        buy_sell_transList.add(new TransactionStream(code, eventName, sellerUser, ticketQuantity, price));
    }
    
    public static void addTransaction(int code, String userName, String userType, double credit){
        //add a transaction stream here for refunds
    }
/* uncomment after transactionstream class made
    public TransactionStream getTicketTransaction(){
    
    }
*/
// populate ticketList with tickets from tickets.txt file
    public static void readTickets() throws FileNotFoundException, IOException{

    	File inputFile = new File("tickets.txt");
    	BufferedReader reader;

    	reader = new BufferedReader(new FileReader(inputFile));
    	String currentLine = reader.readLine();

    	while (currentLine.equals("END")) {
	    
	    	String eventName = currentLine.substring(0,19);
	    	String sellerUsername = currentLine.substring(20,33);
	    	int ticketsinStock = Integer.valueOf(currentLine.substring(34,37));
	    	double ticketPrice = Double.valueOf(currentLine.substring(38,44));
	    	ticketList.add(new Ticket(eventName, sellerUsername, ticketsinStock, ticketPrice));
	    	currentLine = reader.readLine();
    	}

	reader.close();
    } 
    
    // populate userList with users from  users.txt file 
    
    public static void readUsers() throws FileNotFoundException, IOException{

    	File inputFile = new File("users.txt");
    	BufferedReader reader;

    	reader = new BufferedReader(new FileReader(inputFile));
    	String currentLine = reader.readLine();

    	while (currentLine.equals("END")) {
	    
	    	String userName = currentLine.substring(0,15);
	    	String userType = currentLine.substring(16,18);
	    	double credit = Double.valueOf(currentLine.substring(19,29));
            
            if (userType.equals("AA")){
                userList.add(new Admin ("AA", userName, credit));
            }else if (userType.equals("FS")){
                userList.add(new Standard_Full ("FS", userName, credit));
            }else if (userType.equals("BS")){
                userList.add(new Standard_Buy ("BS", userName, credit));
            }
	    	currentLine = reader.readLine();
    	}

	reader.close();
    } 
    
}
    
