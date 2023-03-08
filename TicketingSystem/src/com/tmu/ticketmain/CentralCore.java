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
    
    private static User activeUser;
    
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
                    if(login(loginUser)){
                        System.out.println("Login successful!");
                    }else{
                        System.out.println("Cannot find a user with that username");
                    }
                }else if(userInput.equals("Quit")){
                    break;
                }else if(userInput.equals("c")){
                    getUserOperations();
                }else if(userInput.equals("Logout")){
                    logout();
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
        activeUser = null;
        System.out.println("Logout successful.");
    }
    
    public void createDailyTransaction(int transCode, String userName, String userType, int userCredit){
    }
    
    public static void getUserOperations(){
        if(activeUser == null){
            System.out.println("Login, Quit");
        }else if(activeUser != null){
            System.out.println("Buy, Sell, Logout, Quit, Create, Delete, Addcredit, Refund");
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


    
    
}
