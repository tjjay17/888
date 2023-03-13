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
    //Refund request list
    private static List<RefundRequest> refundRequestList = new ArrayList<RefundRequest>();
    
    private static User activeUser = null;
    
    public static void main(String[] args) throws IOException{
        if(args.length > 0){
            System.out.println("No args needed.");
        }

        readTickets();
        readUsers();
        
        String userInput = "";
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
                }else if(userInput.equals("Create") && activeUser != null && activeUser.getUsertype().equals("AA")){
                    
                    //Sample admin implementation
                    Admin admin = new Admin(activeUser.getUsername(), activeUser.getUsertype(), activeUser.getCredit());
                    userList = admin.createUser(userList);

                }else if(userInput.equals("Delete") && activeUser != null && activeUser.getUsertype().equals("AA")){
                    
                    //Sample admin implementation
                    Admin admin = new Admin(activeUser.getUsername(), activeUser.getUsertype(), activeUser.getCredit());
                    admin.deleteUser(userList);

                }else if(userInput.equals("Refund") && activeUser != null && activeUser.getUsertype().equals("AA")){
                    
                    //Sample admin implementation
                    Admin admin = new Admin(activeUser.getUsername(), activeUser.getUsertype(), activeUser.getCredit());
                    userList = admin.refund(userList, refundRequestList);
                    

                }else if(userInput.equals("Refund") && activeUser != null && (activeUser.getUsertype().equals("SS") || activeUser.getUsertype().equals("FS"))){
                    
                    //Sample user implementation
                    switch (activeUser.getUsertype()){
                        case "SS":
                            Standard_Sell seller = new Standard_Sell("SS", activeUser.getUsername(), activeUser.getCredit());
                            seller.refundRequest(activeUser.getUsername(), activeUser.getCredit(), userList, refundRequestList);
                            break;
                        case "FS":
                            Standard_Full buyer = new Standard_Full("FS", activeUser.getUsername(), activeUser.getCredit());
                            buyer.refundRequest(activeUser.getUsername(), activeUser.getCredit(), userList, refundRequestList);
                            break;
                    }

                }else if(userInput.equals("Buy") && activeUser != null && activeUser.getUsertype() != "SS"){
                    activeUser.Buy();
                }else if(userInput.equals("Sell") && activeUser != null && activeUser.getUsertype() != "BS"){    
                    activeUser.sell();
                }else if(userInput.equals("AddCredit") && activeUser != null && activeUser.getUsertype().equals("AA")){
                    
                    //Sample admin implementation
                    Admin admin = new Admin(activeUser.getUsername(), activeUser.getUsertype(), activeUser.getCredit());
                    admin.addCredit(userList);

                }else if(userInput.equals("AddCredit") && activeUser != null){
                    //call the addCredit method
                    activeUser.addCredit();
                }else{
                    System.out.println("Command not understood");
                }
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
            //Updated to include refund
            System.out.println("Sell, AddCredit, Refund, Logout, Commands");
        }else if(activeUser.getUsertype().equals("FS")){
            //Updated to include refund
            System.out.println("Buy, Sell, AddCredit, Refund, Logout, Commands");
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
    //Changed to correct format
    public static void addCreditTransaction(String userName, double credit, int code, String userType){
        transactionList.add(new DailyTransaction(userName, credit, code, userType));
    }
    //Changed to correct order
    public static void addRefundTransaction(int code , double refund, String buyerUser, String sellerUser){
        transactionList.add(new DailyTransaction(code, refund, buyerUser, sellerUser));
    }

    public static void addSessionEndTransaction(int code, String generalUser, String userType, double credit){
        transactionList.add(new DailyTransaction(code, generalUser, userType, credit ));
    }

    public static void addBuySellTransaction(int code, String eventName, String sellerUser, int ticketQuantity, double price){
        buy_sell_transList.add(new DailyTransaction(code, eventName, sellerUser, ticketQuantity, price));
    }
    //Create Transaction
    public static void addCreateUserTransaction(int code, String userName, double credit, String userType){
        transactionList.add(new DailyTransaction(code, userName, credit, userType));
    }
    //Delete Transaction
    public static void addDeleteUserTransaction(int code, String userName, String userType, double credit){
        transactionList.add(new DailyTransaction(code, userName, credit, userType));
    }
  

    public static String getUserType(String username){
        String type = "";
        try{
            type = "";
            for(int i = 0; i < userList.size(); i++){
                if(userList.get(i).getUsername().equals(username)){
                    type = userList.get(i).getUsertype();
                }
            }
            
            if(type.equals("")){
                throw new Exception("Error: user does not exist");
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return type;
    }

/* uncomment after transactionstream class made
    public TransactionStream getTicketTransaction(){
    
    }
*/
// populate ticketList with tickets from tickets.txt file
    public static void readTickets() throws FileNotFoundException, IOException{

    	// FOR THANOOJ - File inputFile = new File("../tickets.txt");
        File inputFile = new File("tickets.txt");
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

    	//FOR THANOOJ - File inputFile = new File("../users.txt");
        File inputFile = new File("users.txt");
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
                userList.add(new Standard_Buy ("BS", userName, credit));
            }else if (userType.equals("SS")){
                userList.add(new Standard_Sell ("SS", userName, credit));
            }
	    	currentLine = reader.readLine();
    	}

	reader.close();
    } 


        //*******************For furture, all file modify programs can be written to single function */
    //a function that'll modify the users file itself
    //it will copy contents of the users file, empty the file, and rewrite with new user
    public static void writeToUsersFile(String username, String usertype, double credits){
        //the fields for the new user to add
        String formattedUser = Admin.userFormatter(username);
        String formattedType = Admin.typeFormatter(usertype);
        String formattedCredits = Admin.creditFormatter(credits);
        List<String> userContents = new ArrayList<String>();

        String fileLine;
        //FOR THANOOJ ---> File userFile = new File("../users.txt")
        File userFile = new File(".users.txt");

        try{
            BufferedReader rw = new BufferedReader(new FileReader(userFile));
            BufferedWriter fw = new BufferedWriter( new FileWriter(userFile, true));
            PrintWriter pw;

            //copy all current file contents
            while((fileLine = rw.readLine()) != null){
                userContents.add(fileLine);
            }

            //this line deletes contents of current file
            //FOR THANOOJ - pw = new PrintWriter("../users.txt");
            pw = new PrintWriter("users.txt");
            //rewrite every single old line except "END"
            for(int i = 0 ; i < userContents.size() - 1; i++){
                if(i != 0){
                    fw.newLine();  
                }
                fw.append(userContents.get(i));
            }

            //append the new user to the file and the "END" line
            fw.newLine();
            fw.append(formattedUser + " " + formattedType + formattedCredits);
            fw.newLine();
            fw.append("END                         ");

            //close writers & readers
            rw.close();
            pw.close();
            fw.close();
            //fw.
            //fw.append(formattedUser + " " + formattedType + formattedCredits);
            //fw.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void deleteFromUsersFile(String username){
        String shortenedUsername = username.trim();

        List<String> userContents = new ArrayList<String>();
        String fileLine;
        //FOR THANOOJ - File userFile = new File("../users.txt");
        File userFile = new File("users.txt");

        try{
            BufferedReader rw = new BufferedReader(new FileReader(userFile));
            BufferedWriter fw = new BufferedWriter( new FileWriter(userFile, true));
            PrintWriter pw;

            while((fileLine = rw.readLine()) != null){
                if(!fileLine.substring(0, 16).trim().equals(shortenedUsername)){
                    userContents.add(fileLine);
                }
            }

            pw = new PrintWriter(userFile);

            for(int i = 0; i < userContents.size(); i++){
                 if(i != 0){
                     fw.newLine();
                 }
                // System.out.println(userContents.get(i));
                fw.append(userContents.get(i));
            }

            rw.close();
            fw.close();
            pw.close();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void refundFileChange(String buyer, String seller, double amount){
        String shortenedBuyerUser = buyer.trim();
        String shortenedSellerUser = seller.trim();
        List<String> userContents = new ArrayList<String>();
        double oldCredit, newCredit;

        String fileLine;
        //FOR THANOOJ - File userFile = new File("../users.txt");
        File userFile = new File("users.txt");

        try{
            BufferedReader rw = new BufferedReader(new FileReader(userFile));
            BufferedWriter fw = new BufferedWriter( new FileWriter(userFile, true));
            PrintWriter pw;

            while((fileLine = rw.readLine()) != null){
                if(fileLine.substring(0,16).trim().equals(shortenedBuyerUser)){
                    oldCredit = Double.parseDouble(fileLine.substring(19, 28).trim());
                    fileLine = fileLine.substring(0, 19);
                    newCredit = oldCredit + amount;
                    fileLine = fileLine + Admin.creditFormatter(newCredit);
                }

                if(fileLine.substring(0,16).trim().equals(shortenedSellerUser)){
                    oldCredit = Double.parseDouble(fileLine.substring(19, 28).trim());
                    newCredit = oldCredit - amount;
                    fileLine = fileLine.substring(0, 19);
                    fileLine = fileLine + Admin.creditFormatter(newCredit);
                }
                userContents.add(fileLine);
            }

            pw = new PrintWriter(userFile);

            for(int i = 0; i < userContents.size(); i++){
                if(i != 0){
                    fw.newLine();
                }

                fw.append(userContents.get(i));
            }

            pw.close();
            rw.close();
            fw.close();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void addCredFileChange(String username, double amount){
        String user = username.trim();
        double oldCredit, newCredit;
        List<String> userContents = new ArrayList<String>();

        String fileLine;
        //FOR THANOOJ - File userFile = new File("users.txt");
        File userFile = new File("users.txt");

        try{
            BufferedReader rw = new BufferedReader(new FileReader(userFile));
            BufferedWriter fw = new BufferedWriter( new FileWriter(userFile, true));
            PrintWriter pw;

            while((fileLine = rw.readLine()) != null){
                if(fileLine.substring(0,16).trim().equals(user)){
                    oldCredit = Double.parseDouble(fileLine.substring(19, 28).trim());
                    fileLine = fileLine.substring(0, 19);
                    newCredit = oldCredit + amount;
                    fileLine = fileLine + Admin.creditFormatter(newCredit);
                }

                userContents.add(fileLine);
            }

            pw = new PrintWriter(userFile);

            for(int i = 0; i < userContents.size(); i++){
                if(i != 0){
                    fw.newLine();
                }

                fw.append(userContents.get(i));
            }

            pw.close();
            rw.close();
            fw.close();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    
}
    
