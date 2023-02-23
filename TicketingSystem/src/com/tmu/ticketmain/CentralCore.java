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
    List<Ticket> tickets = new ArrayList();
    //uncomment 2 line under when user class created
    //List<User> userList = new ArrayList();
    //User activeUser;
    public static void main(String[] args) throws IOException{
        if(args.length > 0){
            System.out.println("No args needed.");
        }
        
        String userInput = "";
        try(BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))){
            //prompts can be changed
            System.out.println("Welcome to the ticket system");
            System.out.println("Enter a command to begin");
            while((userInput = stdIn.readLine()) != null){
                //readin user inputs and assess them
                if(userInput.equals("login")){
                    System.out.println("login stuff");
                    //call login
                    break;
                }
            }        
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    public void login(){
    }
    
    public void logout(){
    }
    
    public void createDailyTransaction(int transCode, String userName, String userType, int userCredit){
    }
    
    public void getUserOperations(String activeUser, String userType){
    }
    
    public List<Ticket> getTickets(){
        return null;
    }
    
/* uncomment after transactionstream class made
    public TransactionStream getTicketTransaction(){
    
    }
*/


    
    
}
