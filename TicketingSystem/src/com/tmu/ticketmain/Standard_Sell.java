package com.tmu.ticketmain;

import java.io.*;
import java.util.*;

public class Standard_Sell extends User{

    public boolean getUser(String target){
        //Placeholder function. Used to retrieve user data from user list.
        String formatted = String.format("Found user %s", target);
        System.out.println(formatted);
        
        return true;
    }
    
    public Standard_Sell(String usertype, String username, double credit) {
        super(usertype, username, credit);
    }
    
    public void refundRequest(){
        //Currently in preliminary development. Will eventually send request to admin.
        //basic requirements
        String operation;
        String seller = "currentUser"; //Used temporarily. Will eventually save this as the current active user's username information.
        String buyer;
        double sellerCredit = 300.00; //Used temporarily. Will eventually save this as the current active user's credit information.
        double buyerCredit = 150.00; //Used temporarily. Will eventually save this as the buyer's credit information based on getUser().
        double refundCredit = 0;
        //method specific
        boolean refundRequest_active = true;
        Scanner userInput = new Scanner(System.in);
        boolean validInput = false;
        double comparator = 0.0;
        
        while(refundRequest_active){
            System.out.println("Select operation: new refund / cancel");
            do {
                operation = userInput.nextLine().toLowerCase();
                if(!(operation.toLowerCase().equals("new refund") || operation.toLowerCase().equals("cancel"))){
                    System.out.println("Invalid operation. Please resubmit.");
                }
            }while(!(operation.toLowerCase().equals("new refund") || operation.toLowerCase().equals("cancel")));

            switch(operation){
                    case "cancel":
                        userInput.close();
                        refundRequest_active = false;
                        break;
                    case "new refund":
                        //find buyer
                        System.out.println("Enter buyer username");
                        buyer = userInput.nextLine();
                        while(buyer.length() > 15){
                            System.out.println("Invalid entry for username. Please resubmit.");
                            buyer = userInput.nextLine();
                        }
                        getUser(buyer);
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
                        //Formatting request
                        String formattedRequest = String.format("Created refund request for %s for an amount of %.2f", buyer, refundCredit);
                        System.out.println(formattedRequest);
            }
        }
    }
}
