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
    private static List<Ticket> ticketList = new ArrayList<Ticket>();
    private static List<User> userList = new ArrayList<User>();
    private static List<DailyTransaction> transactionList = new ArrayList<DailyTransaction>();
    private static List<DailyTransaction> buy_sell_transList = new ArrayList<DailyTransaction>();
    
    private static User activeUser = null;
    
    public static void main(String[] args) throws IOException{
        if(args.length > 0){
            System.out.println("No args needed.");
        }

        readTickets();
        readUsers();
        
        String userInput = "";
        boolean firstRun = true;
        try(BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))){
            //prompts can be changed
            System.out.println("Welcome to the ticket system");

            while(true){
                //readin user inputs and assess them
                System.out.println("Enter a command, or 'Commands' for a list of commands.");
                userInput = stdIn.readLine();
                
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
                }else if(userInput.equals("Commands")){
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

                // if(firstRun){
                //     firstRun = false; 
                //  }else{
                //      System.out.println("Enter a command, or 'c' for a list of commands.");
                //  }
            }        
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    public static boolean login(String userName){
        boolean userFound = false;
        for(int i = 0; i < userList.size(); i++){
            if(userList.get(i).getUsername().equals(userName)){
                userFound = true;
                activeUser = userList.get(i);
            }
        }

        return userFound;
    }
    
    public static void logout(){
        //once the PR for creating a daily trans file is merged, need to call that method here to actually write the daily file
        System.out.println(activeUser.getUsername());
        System.out.println(activeUser.getUsertype());
        System.out.println(activeUser.getCredit());
        addSessionEndTransaction(0, activeUser.getUsername(), activeUser.getUsertype(), activeUser.getCredit());
        activeUser = null;
        Daily_Transaction_File.createOrUpdateDailyFile();

        System.out.println("Logout successful.");
    }
    
    // public void createDailyTransaction(int transCode, String userName, String userType, int userCredit){
    // }
    
    public static void getUserOperations(){
        if(activeUser == null){
            System.out.println("Login, Quit");
        }else if(activeUser.getUsertype().equals("AA")){
            System.out.println("Buy, Sell, Refund, Delete, Create, AddCredit, Logout, Commands");
        }else if(activeUser.getUsertype().equals("BS")){
            System.out.println("Buy, AddCredit, Logout, Commands");
        }else if(activeUser.getUsertype().equals("SS")){
            System.out.println("Sell, AddCredit, Logout, Commands");
        }else if(activeUser.getUsertype().equals("FS")){
            System.out.println("Buy, Sell, AddCredit, Logout, Commands");
        }
    }
    
    public static List<Ticket> getTickets(){
        return ticketList;
    }

    public static User findUser(String userName){
        for(int i = 0; i < userList.size(); i++){
            if(userList.get(i).getUsername().equals(userName)){
                return userList.get(i);
            }
        }

        return null;
    }
    
    public static List<User> getUsers(){
        return userList;
    }
    
    public static List<DailyTransaction> getTransactions(){
        return transactionList;
    }
    
    public static void addSellTransaction(int code, String eventName, String sellerUser, int ticketQuantity, double price){
        transactionList.add(new DailyTransaction(code, eventName, sellerUser, ticketQuantity, price));
    }

    public static void addBuyTransaction(int code, String eventName, String buyerUser, int ticketQuantity, double price){
        transactionList.add(new DailyTransaction(code, eventName, buyerUser, ticketQuantity, price));
    }

    public static void addCreditTransaction(int code, String addCredUser, double credit){
        transactionList.add(new DailyTransaction(code, addCredUser, credit));
    }
    
    public static void addRefundTransaction(int code, double refund, String buyerUser, String sellerUser){
        transactionList.add(new DailyTransaction(code, refund, buyerUser, sellerUser));
    }

    public static void addSessionEndTransaction(int code, String generalUser, String userType, double credit){
        transactionList.add(new DailyTransaction(code, generalUser, userType, credit ));
    }

    public static void addBuySellTransaction(int code, String eventName, String sellerUser, int ticketQuantity, double price){
        buy_sell_transList.add(new DailyTransaction(code, eventName, sellerUser, ticketQuantity, price));
    }
    
    // public static void addTransaction(int code, String userName, String userType, double credit){
    //     //add a transaction stream here for refunds
    // }

/* uncomment after transactionstream class made
    public TransactionStream getTicketTransaction(){
    
    }
*/
// populate ticketList with tickets from tickets.txt file
    public static void readTickets() throws FileNotFoundException, IOException{

    	File inputFile = new File("../tickets.txt");
    	BufferedReader reader = new BufferedReader(new FileReader(inputFile));
    	String currentLine = reader.readLine();

    	while (!currentLine.equals("END")) {
            if(currentLine.trim().equals("END")){
                break;
            }
	    	String eventName = currentLine.substring(0,26).trim();
	    	String sellerUsername = currentLine.substring(26,40).trim();
	    	int ticketsinStock = Integer.valueOf(currentLine.substring(41,44).trim());
	    	double ticketPrice = Double.valueOf(currentLine.substring(45,51).trim());
	    	ticketList.add(new Ticket(eventName, sellerUsername, ticketsinStock, ticketPrice));
	    	currentLine = reader.readLine();
    	}

	reader.close();
    } 
    
    // populate userList with users from  users.txt file 
    
    public static void readUsers() throws FileNotFoundException, IOException{

    	File inputFile = new File("../users.txt");
    	BufferedReader reader = new BufferedReader(new FileReader(inputFile));
    	String currentLine = reader.readLine();

    	while (true) {
            if(currentLine.trim().equals("END")){
                break;
            }
	    	String userName = currentLine.substring(0,16).trim();
	    	String userType = currentLine.substring(16,19).trim();
	    	double credit = Double.valueOf(currentLine.substring(19,28).trim());
            
            if (userType.equals("AA")){
                userList.add(new Admin ("AA", userName, credit));
            }else if (userType.equals("FS")){
                userList.add(new Standard_Full ("FS", userName, credit));
            }else if (userType.equals("BS")){
                userList.add(new Standard_Buy ("FS", userName, credit));
            }else if (userType.equals("SS")){
                userList.add(new Standard_Sell ("SS", userName, credit));
            }
	    	currentLine = reader.readLine();
    	}

	reader.close();
    } 
    
}
    
