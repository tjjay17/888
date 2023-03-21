/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmu.ticketmain;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.Thread;
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
        //This code allows for the creation of a daily transaction file in case a user hard quits.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try{
                    if(activeUser != null){
                        logout();
                    }
                }catch (NullPointerException e){
                    //do nothing, this normal
                }catch (Exception e){
                    System.out.println("Could not gracefully logout " + e);

                }
            }
        });

        CentralCore.processFileChanges();

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
                if(userInput.equals("Login") && activeUser == null){
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
                }else if(userInput.equals("Exit") && activeUser == null){
                    break;
                }else if(userInput.equals("Commands")){
                    getUserOperations();
                }else if(userInput.equals("Logout") && activeUser != null){
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
        System.out.println("Created daily transaction file.");
    }
    
    public static void getUserOperations(){
        if(activeUser == null){
            System.out.println("Login, Exit");
        }else if(activeUser.getUsertype().equals("AA")){
            System.out.println("Buy, Sell, Refund, Delete, Create, AddCredit, Logout, Commands, Exit");
        }else if(activeUser.getUsertype().equals("BS")){
            System.out.println("Buy, AddCredit, Logout, Commands, Exit");
        }else if(activeUser.getUsertype().equals("SS")){
            //Updated to include refund
            System.out.println("Sell, AddCredit, Refund, Logout, Commands, Exit");
        }else if(activeUser.getUsertype().equals("FS")){
            //Updated to include refund
            System.out.println("Buy, Sell, AddCredit, Refund, Logout, Commands, Exit");
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

    	//FOR THANOOJ - File inputFile = new File("../users.txt");
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
                userList.add(new Standard_Buy ("BS", userName, credit));
            }else if (userType.equals("SS")){
                userList.add(new Standard_Sell ("SS", userName, credit));
            }
	    	currentLine = reader.readLine();
    	}

	    reader.close();
    } 

    public static void processFileChanges(){
        Calendar calendarToday = Calendar.getInstance();
        Calendar calendarYesterday = Calendar.getInstance();
        Date date_today = calendarToday.getTime();
        Date date_yesterday = calendarYesterday.getTime();
        
        String today_day = new SimpleDateFormat("dd").format(date_today);
        String today_month = new SimpleDateFormat("MM").format(date_today);
        String today_year = new SimpleDateFormat("yyyy").format(date_today);

        //CHANGE TO YESTERDAY DATE THIS STUFF
        String yesterday_day = new SimpleDateFormat("dd").format(date_today);
        String yesterday_month = new SimpleDateFormat("MM").format(date_today);
        String yesterday_year = new SimpleDateFormat("yyyy").format(date_today);

        //We can check to see if a dtf from the previous day exists. 
        //if it does, then we simply process those transactions, add them to a merged dtf, and then delete that dtf.
        String dtfFile = "dtf_" + yesterday_year + "_" + yesterday_month + "_" + yesterday_day + ".txt";
        File dtfYesterday = new File("../DailyTransactionFiles/" + dtfFile);
        File mergedDtf = new File("../DailyTransactionFiles/dtf_merged.txt");

        if(dtfYesterday.exists()){
            try{
                BufferedReader rw = new BufferedReader(new FileReader("../DailyTransactionFiles/" + dtfFile));
                BufferedWriter fw = new BufferedWriter( new FileWriter(mergedDtf, true));
                String dtf_line = "";

                //write into the merged file
                while((dtf_line = rw.readLine()) != null){
                    fw.append(dtf_line);
                    fw.newLine();
                }
                fw.append("00");
                fw.newLine();

                CentralCore.applyDtfChanges(dtfFile);
                rw.close();
                fw.close();
                dtfYesterday.delete();
            }catch(Exception e){
                System.out.println(e);
            }
            
        }
    }

    public static void applyDtfChanges(String dtfFileParam){
        String dtfFile = "../DailyTransactionFiles/" + dtfFileParam;
        try{
            File userFile = new File("../users.txt");
            File ticketFile = new File("../tickets.txt");
            BufferedReader rw = new BufferedReader(new FileReader(dtfFile));
            BufferedWriter userfw = new BufferedWriter(new FileWriter(userFile, true));
            BufferedWriter ticketfw = new BufferedWriter(new FileWriter(ticketFile, true));

            String dtf_line = "";
            List<String> userLines = CentralCore.userFileLines();
            List<String> ticketLines = CentralCore.ticketFileLines();

            while((dtf_line = rw.readLine()) != null){
                String[] dtfLineSplit = dtf_line.split("_");
                String code = dtfLineSplit[0];

                if(code.equals("01") || code.equals("02") || code.equals("06")){
                    String username = dtfLineSplit[1];
                    String type = dtfLineSplit[2];
                    String credit = dtfLineSplit[3];
                    int userIndex = -1;

                    for(int i = 0; i < userLines.size(); i++){
                        String userNameLine = userLines.get(i).substring(0,16).trim();
                        if(userNameLine.equals(username)){
                            userIndex = i;
                            break;
                        }
                    }

                    if(userIndex == -1 && code.equals(01)){
                        String END = userLines.remove(userLines.size() - 1);
                        String lineToAdd = Admin.userFormatter(username) + " " + Admin.typeFormatter(type) + Admin.creditFormatter(Double.parseDouble(credit));
                        userLines.add(lineToAdd);
                        userLines.add(END);
                        rewriteUsers(userLines);
                    }else if(userIndex != -1 && code.equals("01")){
                        //log a create error
                    }else if(userIndex != -1 && code.equals("02")){
                        userLines.remove(userIndex);
                        rewriteUsers(userLines);
                    }else if(userIndex == -1 && code.equals("02")){
                        //log a delete error
                    }else if(userIndex != -1 && code.equals("06")){
                        double oldCredit = Double.parseDouble(userLines.get(userIndex).substring(19, 28).trim());
                        double newCredit = oldCredit + Double.parseDouble(credit);
                        String newLine = Admin.userFormatter(username) + " " + Admin.typeFormatter(type) + Admin.creditFormatter(newCredit);
                        userLines.set(userIndex, newLine);
                        rewriteUsers(userLines);
                    }else if(userIndex == -1 && code.equals("06")){
                        //log addcred error
                    }
                }else if(code.equals("05")){
                    String buyerName = dtfLineSplit[1];
                    String sellerName = dtfLineSplit[2];
                    String refundAmt = dtfLineSplit[3];
                    int buyerIndex = -1;
                    int sellerIndex = -1;
                    boolean sellerFundsErr = false;
                    String buyerNewLine = "";
                    String sellerNewLine = "";

                    for(int j = 0; j < userLines.size(); j++){
                        String currUser = userLines.get(j).substring(0, 16).trim();
                        if(currUser.equals(buyerName)){
                            buyerIndex = j;
                            double oldCredit = Double.parseDouble(userLines.get(j).substring(19, 28).trim());
                            double newCredit = oldCredit + Double.parseDouble(refundAmt);
                            String type = userLines.get(j).substring(16, 19).trim();
                            buyerNewLine = Admin.userFormatter(buyerName) + " " + Admin.typeFormatter(type) + Admin.creditFormatter(newCredit);
                        }else if(currUser.equals(sellerName)){
                            sellerIndex = j;
                            double oldCredit = Double.parseDouble(userLines.get(j).substring(19, 28).trim());
                            double newCredit = oldCredit + Double.parseDouble(refundAmt);
                            String type = userLines.get(j).substring(16, 19).trim();
                            sellerNewLine = Admin.userFormatter(sellerName) + " " + Admin.typeFormatter(type) + Admin.creditFormatter(newCredit);
                        }
                    }

                    if(buyerIndex == -1 || sellerIndex == -1){
                        if(buyerIndex == -1){
                            //buyer finding error
                        }

                        if(sellerIndex == -1){
                            //seller finding error
                        }
                    }else if(sellerFundsErr){
                        //log the funds error - seller can't have neg funds
                    }else if(sellerIndex != -1 && buyerIndex != -1){
                        userLines.set(sellerIndex, sellerNewLine);
                        userLines.add(buyerIndex ,buyerNewLine);
                        rewriteUsers(userLines);
                    }
                }else if(code.equals("03")){
                    String eventName = dtfLineSplit[1];
                    String sellerName = dtfLineSplit[2];
                    String ticketNum = dtfLineSplit[3];
                    String ticketPrice = dtfLineSplit[4];
                    String newUserLine = "";

                    int sellerIndex = -1;
                    int ticketIndex = -1;

                    for(int i = 0; i < userLines.size(); i++){
                        if(userLines.get(i).substring(0, 16).equals(sellerName)){
                            sellerIndex = i;
                            String sellerNameCurr = userLines.get(i).substring(0, 16).trim();
                            String typeCurr = userLines.get(i).substring(16, 19).trim();
                            String creditCurr = userLines.get(i).substring(19, 28).trim() + Integer.parseInt(ticketNum) * Double.parseDouble(ticketPrice);
                            newUserLine = sellerNameCurr + " " + typeCurr + creditCurr;
                        }
                    }

                    for(int j = 0; j < ticketLines.size(); j++){
                        String currTicketEvent = ticketLines.get(j).substring(0, 26);
                        String currTicketSeller = ticketLines.get(j).substring(26, 40);
                        if(eventName.equals(currTicketEvent) && sellerName.equals(currTicketSeller)){
                            ticketIndex = j;
                        }
                    }

                    if(sellerIndex != -1 && ticketIndex != -1){
                        String fileEventName = ticketLines.get(ticketIndex).substring(0, 26).trim();
                        String fileSellerName = ticketLines.get(ticketIndex).substring(26, 40).trim();
                        int fileTickets = Integer.parseInt(ticketLines.get(ticketIndex).substring(41, 44).trim()) - Integer.parseInt(ticketNum);
                        double filePrice = Double.parseDouble(ticketLines.get(ticketIndex).substring(45, 51).trim());
                        
                        if(fileTickets > 0){
                            String newTicketLine = Ticket.formatEventName(fileEventName) + Ticket.formatSellerName(fileSellerName) + Ticket.formatTicketQuantity(fileTickets) + Ticket.formatSellerPrice(filePrice);
                            userLines.set(sellerIndex, newUserLine);
                            ticketLines.set(ticketIndex, newTicketLine);
                            rewriteTickets(ticketLines);
                        }else{
                            //negative ticket error
                        }
                    }else if(sellerIndex == -1){
                        //seller not found
                    }else if(ticketIndex == -1){
                        //event not found
                    }
                }else if(code.equals("04")){
                    String eventName = dtfLineSplit[1];
                    String sellerName = dtfLineSplit[2];
                    int ticketNum = Integer.parseInt(dtfLineSplit[3]);
                    double ticketPrice = Double.parseDouble(dtfLineSplit[4]);

                    String ticketToAdd = Ticket.formatEventName(eventName) + Ticket.formatSellerName(sellerName) + Ticket.formatTicketQuantity(ticketNum) + Ticket.formatSellerPrice(ticketPrice);
                    ticketLines.add(ticketToAdd);
                    rewriteTickets(ticketLines);
                }
            }

            userfw.close();
            rw.close();
            ticketfw.close();
        }catch (Exception e){
            System.out.println("Dtf apply error" + e + "line number: " + e.getStackTrace()[0].getLineNumber());
        }
    }

    public static List<String> userFileLines(){
        List<String> userLines = new ArrayList<String>();
        String userLine = "";

        File userFile = new File("../users.txt");
        try{
            BufferedReader fr = new BufferedReader(new FileReader(userFile));
            while((userLine = fr.readLine()) != null){
                userLines.add(userLine);
            }
            fr.close();
        }catch(Exception e){
            System.out.println("user file line read err: " + e);
        }

        return userLines;
    }

    public static List<String> ticketFileLines(){
        List<String> ticketLines = new ArrayList<String>();
        String ticketLine = "";

        File ticketFile = new File("../tickets.txt");
        try{
            BufferedReader fr = new BufferedReader(new FileReader(ticketFile));
            while((ticketLine = fr.readLine()) != null){
                ticketLines.add(ticketLine);
            }
            fr.close();
        }catch(Exception e){
            System.out.println("user file line read err: " + e);
        }

        return ticketLines;
    }

    public static void rewriteUsers(List<String> userLines){
        try{
            //erase everything
            PrintWriter pw = new PrintWriter("../users.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter("../users.txt", true));
            for(int i = 0; i < userLines.size(); i++){
                bw.append(userLines.get(i));
                bw.newLine();
            }
            pw.close();
            bw.close();

        }catch(Exception e){
            System.out.println("rewrite users error " + e);
        }
    }

    public static void rewriteTickets(List<String> ticketLines){
        try{
            PrintWriter pw = new PrintWriter("../tickets.txt");
            for(int i = 0; i < ticketLines.size(); i++){
                pw.append(ticketLines.get(i));
            }
            pw.close();

        }catch(Exception e){
            System.out.println("rewrite tickets error " + e);
        }
    }


    /********************************************************************************************************************************* 
    *******************************************************DO NOT DELETE THE CODE BELOW ***************************************************
    *******************************************************THEY ARE OLD FUNCTIONS BUT CAN BE REUSED**********************************************
    ********************************************************************************************************************************** */
    public static void writeToUsersFile(String username, String usertype, double credits){
        //the fields for the new user to add
        String formattedUser = Admin.userFormatter(username);
        String formattedType = Admin.typeFormatter(usertype);
        String formattedCredits = Admin.creditFormatter(credits);
        List<String> userContents = new ArrayList<String>();

        String fileLine;
        //FOR THANOOJ ---> File userFile = new File("../users.txt")
        File userFile = new File("../users.txt");

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
            pw = new PrintWriter("../users.txt");
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
        File userFile = new File("../users.txt");

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
        File userFile = new File("../users.txt");

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
        File userFile = new File("../users.txt");

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
	
	//Buy file change
    public static void buyFileChange(String username, double ticketPrice, int ticketQuantity ){
        String user = username.trim();
        double oldCredit, newCredit;
        List<String> userContents = new ArrayList<String>();

        String fileLine;
        File userFile = new File("../users.txt");

        try {
            BufferedReader rw = new BufferedReader(new FileReader(userFile));
            BufferedWriter fw = new BufferedWriter(new FileWriter(userFile, true));
            PrintWriter pw;

            while ((fileLine = rw.readLine()) != null) {

                if (fileLine.substring(0, 16).trim().equals(user)) {
                    oldCredit = Double.parseDouble(fileLine.substring(19, 28).trim());
                    fileLine = fileLine.substring(0, 19);
                    newCredit = oldCredit - (ticketPrice * ticketQuantity);
                    fileLine = fileLine + Admin.creditFormatter(newCredit);
                }

                userContents.add(fileLine);
            }
            pw = new PrintWriter(userFile);

            for (int i = 0; i < userContents.size(); i++) {
                if (i != 0) {
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

    //Sell file change
    public static void sellFileChange(String username, double ticketPrice, int ticketQuantity ){
        String user = username.trim();
        double oldCredit, newCredit;
        List<String> userContents = new ArrayList<String>();

        String fileLine;
        File userFile = new File("../users.txt");

        try {
            BufferedReader rw = new BufferedReader(new FileReader(userFile));
            BufferedWriter fw = new BufferedWriter(new FileWriter(userFile, true));
            PrintWriter pw;

            while ((fileLine = rw.readLine()) != null) {

                if (fileLine.substring(0, 16).trim().equals(user)) {
                    oldCredit = Double.parseDouble(fileLine.substring(19, 28).trim());
                    fileLine = fileLine.substring(0, 19);
                    newCredit = oldCredit + (ticketPrice * ticketQuantity);
                    fileLine = fileLine + Admin.creditFormatter(newCredit);
                }

                userContents.add(fileLine);
            }
            pw = new PrintWriter(userFile);

            for (int i = 0; i < userContents.size(); i++) {
                if (i != 0) {
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
    
