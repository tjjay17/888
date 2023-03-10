package com.tmu.ticketmain;

import java.io.*;
import java.util.*;

public class Admin extends User{

    public Admin(String usertype, String username, double credit) {
        super(usertype, username, credit);
    }
    
    public boolean getUser(String target){
        //Placeholder function. Used to retrieve user data from user list.
        String formatted = String.format("Found user %s", target);
        System.out.println(formatted);

        List<User> userList = CentralCore.getUsers();
        for(int i = 0; i < userList.size(); i++){
            if(userList.get(i).getUsername() == target){
                return true;
            }
        }        
        
        return false;
    }
    
    public void getRefundRequests(){
        //Placeholder function. Used to retreive list of refund requests from sellers
        System.out.println("No refund requests to be retrieved.");
    }
    
    public void createUser(){
        //basic requirements
        String username;
        String type;
        double credits = 0;
        //specific for function
        boolean createUser_active = true;
        Scanner userInput = new Scanner(System.in);
        boolean validInput = false;

        while(createUser_active){
            //filling data
            System.out.println("Enter a new username");
            username = userInput.nextLine();
            while(username.length() > 15 || getUser(username)){
                System.out.println("Invalid entry for username. Please resubmit.");
                username = userInput.nextLine();
            }
            
            System.out.println("Enter the user type");
            do {
                type = userInput.nextLine().toLowerCase();
                if(!(type.toLowerCase().equals("aa") || type.toLowerCase().equals("fs") || type.toLowerCase().equals("sb") || type.toLowerCase().equals("ss"))){
                    System.out.println("Invalid entry for type. Please resubmit.");
                }
            }while(!(type.toLowerCase().equals("aa") || type.toLowerCase().equals("fs") || type.toLowerCase().equals("sb") || type.toLowerCase().equals("ss")));
            validInput = false;
            
            System.out.println("Enter the credit amount");
            do {
                if(userInput.hasNextDouble()){
                    credits = userInput.nextDouble();
                    validInput = true;
                }
                else{
                    System.out.println("Invalid entry for credit. Please resubmit.");
                    validInput = false;
                    userInput.next();
                }
            }while(!(validInput));
            validInput = false;
            
            userInput.close();
            
            //attempt to bundle data
            try{
                String formatted = String.format("Created user %s of type %s with balance %.2f", username, type, credits);
                System.out.println(formatted);
            }
            catch (Exception e) {
		            System.out.println("Exception caught. Reattempt submission.");
	        }
        }
        
    }
    
    public void deleteUser(){
        //Preliminary version. No reading from UserList currently incorporated. Future version will update function to search userList for matching user.
        String username;
        boolean isFound;
        List<User> userList = CentralCore.getUsers();
        
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter user to be deleted");
        username = userInput.nextLine();
        while(username.length() > 15){
            System.out.println("Invalid entry for username. Please resubmit.");
            username = userInput.nextLine();
        }
        isFound = getUser(username);
        if(isFound == true){
            for(int i  = 0; i < userList.size(); i++){
                if(userList.get(i).equals(username)){
                    userList.remove(i);
                }
            }
            String deletionMessageSuccess = String.format("Deleted user with username %s", username);
            System.out.println(deletionMessageSuccess);
        }
        else{
            String deletionMessageFail = String.format("Failed to find user with username %s", username);
            System.out.println(deletionMessageFail);
        }
        
        userInput.close();
    }
    
    public void refund(){
        //Preliminary version. Currently not reading or receiving data from sources. Future version will process requests as well as create new refunds and update user data accordingly.
        //basic requirements
        String operation;
        String seller;
        String buyer;
        double sellerCredit = 0;
        double buyerCredit = 0;
        double refundCredit = 0;
        //method specific
        boolean refundUser_active = true;
        Scanner userInput = new Scanner(System.in);
        boolean validInput = false;
        double comparator = 0.0;
        
        while(refundUser_active){
            System.out.println("Select operation: fetch requests / new refund / cancel");
            do {
                operation = userInput.nextLine().toLowerCase();
                if(!(operation.toLowerCase().equals("fetch requests") || operation.toLowerCase().equals("new refund") || operation.toLowerCase().equals("cancel"))){
                    System.out.println("Invalid operation. Please resubmit.");
                }
            }while(!(operation.toLowerCase().equals("fetch requests") || operation.toLowerCase().equals("new refund") || operation.toLowerCase().equals("cancel")));
            switch(operation){
                case "cancel":
                    userInput.close();
                    refundUser_active = false;
                    break;
                case "fetch requests":
                    getRefundRequests();
                    break;
                case "new refund":
                    //find seller
                    System.out.println("Enter seller username");
                    seller = userInput.nextLine();
                    while(seller.length() > 15){
                        System.out.println("Invalid entry for username. Please resubmit.");
                        seller = userInput.nextLine();
                    }
                    getUser(seller);
                    //find buyer
                    System.out.println("Enter buyer username");
                    buyer = userInput.nextLine();
                    while(buyer.length() > 15){
                        System.out.println("Invalid entry for username. Please resubmit.");
                        buyer = userInput.nextLine();
                    }
                    getUser(buyer);
                    //seller wallet
                    System.out.println("Enter the seller credit amount");
                    do {
                        if(userInput.hasNextDouble()){
                            sellerCredit = userInput.nextDouble();
                            validInput = true;
                        }
                        else{
                            System.out.println("Invalid entry for credit. Please resubmit.");
                            validInput = false;
                            userInput.next();
                        }
                    }while(!(validInput));
                    validInput = false;
                    //buyer wallet
                    System.out.println("Enter the buyer credit amount");
                    do {
                        if(userInput.hasNextDouble()){
                            buyerCredit = userInput.nextDouble();
                            validInput = true;
                            break;
                        }
                        else{
                            System.out.println("Invalid entry for credit. Please resubmit.");
                            validInput = false;
                            userInput.next();
                        }
                    }while(!(validInput));
                    validInput = false;
                    //refund amount
                    System.out.println("Enter the refund credit amount");
                    do {
                        if(userInput.hasNextDouble()){
                            refundCredit = userInput.nextDouble();
                            comparator = Double.compare(refundCredit, sellerCredit);
                            if(comparator > 0){
                                System.out.println("Insufficient seller credit. Please resubmit.");
                                validInput = false;
                                userInput.next();
                            }
                            else{
                                validInput = true;
                                break;
                            }
                        }
                        else{
                            System.out.println("Invalid entry for credit. Please resubmit.");
                            validInput = false;
                            userInput.next();
                        }
                    }while(!(validInput));
                    validInput = false;
                    //adding credit
                    double tempBuyer = buyerCredit + refundCredit;
                    buyerCredit = tempBuyer;
                    //subtracting credit
                    double tempSeller = sellerCredit - refundCredit;
                    sellerCredit = tempSeller;
                    //confirmation message
                    String successfulAdded = String.format("Successfully added %.2f to %s wallet, resulting in %.2f",refundCredit, buyer, buyerCredit);
                    System.out.println(successfulAdded);
                    String successfulSubtracted = String.format("Successfully subtracted %.2f from %s wallet, resulting in %.2f",refundCredit, seller, sellerCredit);
                    System.out.println(successfulSubtracted);
                    
                    break;
            }
        }
    }
}
