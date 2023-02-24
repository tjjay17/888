package com.tmu.ticketmain;

import java.io.*;
import java.util.*;



/**
* Abstract class User
*/ 

public abstract class User {

    // instance variables

    private String usertype;
    private String username;
    private double credit;

    // constructor 

    public User (String usertype, String username, double credit){

        this.usertype = usertype;
	this.username = username;
	this.credit = credit;

    }


    // setters and getters methods for instance variables

    public void setUsername(String username){
        this.username = username; 
    }

    public String getUsername(){
	return username;
    }

    public void setCredit(Double credit){
	this.credit = credit;
    }
    
    public Double getCredit(){
	return credit;
    }

    public void setUsertype(String usertype){
        this.usertype = usertype; 
    }

    public String getUsertype(){
	return usertype;
    }
    
    	
    // Buy method
	
    public void Buy ()  {
     
     //local variables
     String eventName;
     String sellerUsername;
     int ticketQuantity;
     boolean flag = true;
     
     
     
     do {
	    // take input from user
	    Scanner scan1 = new Scanner(System.in);
	    Scanner scan2 = new Scanner(System.in);
		    
	    System.out.println("Search for event:");
	    eventName = scan1.nextLine();
	     
	    System.out.println("How many tickets would you like to buy?");
	    ticketQuantity = scan2.nextInt();
	     
	    System.out.println("What is the seller's username?");
	    sellerUsername = scan1.nextLine();

	    try {
		    
		    //check if the number of tickets asked for purchase is legal
		    if (ticketQuantity > 0  && ticketQuantity <5 ){
		    
		        for (int i = 0; i< CentralCore.ticketList.size();i++){
		            
		            //check the event name of the ticket exists on the database
		            if (CentralCore.ticketList.get(i).getEventName.equals(eventName)){
		                
		                //check if the seller exists on the database
		                if (CentralCore.ticketList.get(i).getSellerUsername.equals(sellerUsername)){
		                    
		                    //check if there are tickets in stock
		                    if (CentralCore.ticketList.get(i).getTicketscinStock() >= ticketQuantity){
		                        
		                        
		                        System.out.println("The price for a single ticket is: " + CentralCore.ticketList.get(i).getTicketPrice());
		                        System.out.println("The total cost for the number of tickets purchased is: " + (CentralCore.ticketList.get(i).getTicketPrice()) * ticketQuantity); 
		                        System.out.println("Would you like to proceed with the purchase of the tickets: Yes / No ");
		                        String select = scan1.nextLine();
		                        
		                        
		                        switch(select){
		                            
		                            case"Yes":
		                                
		                               CentralCore.ticketList.get(i).setTicketsinStock(CentralCore.ticketList.get(i).getTicketsinStock() - ticketQuantity);
		                               for (int j = 0; j< CentralCore.userList.size(); j++){
		                                   
		                                   if (CentralCore.ticketList.get(j).getUsername().equals(this.getUsername())){
		                                       
		                                       if (CentralCore.userList.get(j).getCredit > CentralCore.ticketList.get(i).getTicketPrice()){
		                                           System.out.println("Transaction processed successfully");
		                                           flag = false;
							       
		                                           //Save this information in the dailytransaction file and buy/sell transaction file
		                                           
		                                           CentralCore.transactionList.add(04, eventName, sellerUsername, ticketQuantity, CentralCore.ticketList.get(i).getTicketPrice());
		                                           CentralCore.buy_sell_transactionList.add(04, eventName, sellerUsername, ticketQuantity, CentralCore.ticketList.get(i).getTicketPrice());
		                                           break;
		                                           
		                                           
		                                       }else{
		                                           System.out.println("The user does not have enough credit to complete this transaction.");
		                                           continue;
		                                       }
		                                   }else{
		                                       System.out.println("User not in the list of users. Transaction cannot be completed.");
		                                       continue;
		                                   }
		                               }
		                            
		                            case"No":
		                                
		                                System.out.println("Transaction cancelled.");
		                                break;
		                               
		                            default:
                                        	System.out.println("You entered an invalid option");
                                        	break;      
		                        }
		                        
		                        
		                    }else{
		                        System.out.println("There are not enough tickets in stock to buy.");
		                        break;
		                    }
		                    
		                }else{
		                    System.out.println("The seller does not exit on the database. Try again.");
		                    continue;
		                }
		            
		            }else{
		                System.out.println("The event you are looking for does not exist.");
		                continue;
		            }
		        
		        }
		    
	        }else{
	            System.out.println("The number of tickets legal for purchase is between 1 and 4.");
	            continue;
	        }    
		    
	    }
        
        // catch every possible exception
	    catch (Exception e) {
		System.out.println("Exception caught. Try again!");	

	    } 

	  }while(flag = true); 
    }
	
     // Sell method
	
    public void sell(){
    
    //local variables
    String eventName;
    double salePrice;
    int ticketQuantity;   
    boolean flag = true;
      
    
    do {
	
	//take input from user
        Scanner scan1 = new Scanner(System.in);
	Scanner scan2 = new Scanner(System.in); 
	    
	System.out.println ("Search for Event: ");
	eventName = scan1.nextLine();
	    
        System.out.println("How many tickets would you like to buy? ");
	ticketQuantity = scan2.nextInt();
	    
	System.out.println("How much do you want to sell this ticket?");
	salePrice = scan1.nextDouble();
        
        
	    
        try {
	      
	    //check if event name is legal
	    if (eventName.length() > 0 && eventName.length() <26){
		      
		 // check if sale price is legal
		 if ( salePrice> 0 && salePrice <= 999.99){
		          
		     // check if the number of tickets for sale is legal
		     if(ticketQuantity > 0 && ticketQuantity <= 100){
		              
		         System.out.println("All the information entered is legal.");
		         flag = false;
			     
		         //Save this information in the dailytransaction file and buy/sell transaction file
		              
		         CentralCore.transactionList.add(03, eventName, this.username, ticketQuantity, salePrice);
		         CentralCore.buy_sell_transactionList.add(03, eventName, this.username, ticketQuantity, salePrice);
		         break;
		     }else{
		           System.out.println("The number of tickets for sale is illegal. Try again.");
		           continue;
		          }
		          
		 }else{
		       System.out.println("The price tickets for sale is illegal. Try again.");
		       continue;
		      }
		      
		      
		      
		 
             } else {
	   	    System.out.println("The event you are looking for does not exist.");
	   	    continue;
		      } 
		      
		      
       }
	    
       //catch every possible exception 
       catch (Exception e) {
		System.out.println("Exception caught,try again!");	
	    } 

       } while(flag = true);      
      
    }
    
    // addCredit method for standard user
    // for admin should be overriden						       

    public void addCredit (){
	
	//local variables    
	Double credit_to_add;
	boolean flag = true;

	do {
	
	    Scanner input = new Scanner(System.in);
	    System.out.println ("Enter the amount you want to add as credit: ");

	    try {
		    credit_to_add = input.nextDouble();
		    if (credit_to_add > 0 && credit_to_add <=1000){
			    
		        flag = false;
			//Save this information in the dailytransaction file
			CentralCore.transactionList.add(06, this.username, this.usertype, credit_to_add);    
			break;
		    } else {
				System.out.println("The amount entered is not correct!");
		           } 
	    }

	    catch (InputMismatchException ex) {
		
		System.out.println("Try again!");	

	    } 

	}while(flag = true);

     }

}
