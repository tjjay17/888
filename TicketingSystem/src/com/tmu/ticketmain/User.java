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

    // variables
    // check if event name exist on database
    public Boolean checkEventName (int index, String eventName) {
        if (CentralCore.getTickets().get(index).getEventName().equals(eventName)) {
            return true;
        } else {
            return false;
        }
    }
    
    // check if seller exist on the database
    public Boolean checkSeller(int index, String sellerUsername) {
        if (CentralCore.getTickets().get(index).getSellerUsername().equals(sellerUsername)) {
            return true;
        } else {
            return false;
        }
    }

    //check if there are tickets in stock
    public Boolean checkTicketsinStock(int index, int ticketQuantity) {
        if (CentralCore.getTickets().get(index).getTicketsinStock() >= ticketQuantity) {
            return true;
        } else {
            return false;
        }
    }

	public boolean checkTicketExists(String eventName, String sellerName){
		List<Ticket> tickets = CentralCore.getTickets();
		for(int i = 0; i < tickets.size(); i++){
			if(tickets.get(i).getEventName().equals(eventName) && tickets.get(i).getSellerUsername().equals(sellerName)){
			     return true;
			}
		}

		return false;
	}
    //modify tickets in stock after buy()
    public void modifyTicketinStock(int index, int ticketQuantity){
        CentralCore.getTickets().get(index).setTicketsinStock(CentralCore.getTickets().get(index).getTicketsinStock() - ticketQuantity);
    }

	public void modifyTicketFileBuy(String eventName, String sellerName, double price, int ticketQuantity){
		try{
			File ticketFile = new File("../tickets.txt");
			PrintWriter pw;
			BufferedWriter fw = new BufferedWriter( new FileWriter(ticketFile, true));
			BufferedReader fr = new BufferedReader( new FileReader(ticketFile));
			List<String> ticketContents = new ArrayList<String>();
			String fileLine = "";

			while((fileLine = fr.readLine()) != null){
			       ticketContents.add(fileLine);
			}

			pw = new PrintWriter(ticketFile);
			for(int i = 0; i < ticketContents.size(); i++){
				String fileEventName = ticketContents.get(i).substring(0, 26).trim();
				String filesellerName = ticketContents.get(i).substring(26, 40).trim();
				String fileTicketQuantity = ticketContents.get(i).substring(41, 44).trim();
				String filePrice = ticketContents.get(i).substring(45, 51);

				if(fileEventName.equals(eventName) && filesellerName.equals(sellerName) && Double.parseDouble(filePrice) == price){
					int newTicketNumber = Integer.parseInt(fileTicketQuantity) - ticketQuantity;
					ticketContents.set(i,Ticket.formatEventName(fileEventName) + Ticket.formatSellerName(filesellerName) + Ticket.formatTicketQuantity(newTicketNumber) + Ticket.formatSellerPrice(Double.parseDouble(filePrice)));
				}
			}

			for(int i = 0; i < ticketContents.size(); i++){
				if(i != 0){
				      fw.newLine();
				}
				fw.append(ticketContents.get(i));
			}

			fw.close();
			fr.close();
			pw.close();


		}catch(Exception e){
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void modifyTicketFileSell(String eventName, String sellerName, int ticketQuantity, double price){
		try{
			File ticketFile = new File("../tickets.txt");
			PrintWriter pw;
			BufferedWriter fw = new BufferedWriter( new FileWriter(ticketFile, true));
			BufferedReader fr = new BufferedReader( new FileReader(ticketFile));
			List<String> ticketContents = new ArrayList<String>();
			String fileLine = "";

			while((fileLine = fr.readLine()) != null){
				ticketContents.add(fileLine);
			}

			pw = new PrintWriter(ticketFile);
			for(int i = 0; i < ticketContents.size() - 1; i++){
				if(i != 0){
					fw.newLine();
				}

				fw.append(ticketContents.get(i));
			}

			fw.newLine();
			fw.append(Ticket.formatEventName(eventName) + Ticket.formatSellerName(sellerName) + Ticket.formatTicketQuantity(ticketQuantity) + Ticket.formatSellerPrice(price));
			fw.newLine();
			fw.append("END                                                ");

			pw.close();
			fw.close();
			fr.close();
		}catch(Exception e){
			System.out.println("Error: " + e.getMessage());
		}
	}

    // Buy method

    public void Buy ()  {
        //local variables
        String eventName;
        String sellerUsername;
        int ticketQuantity;
        boolean flag = true;
		boolean validUserInput = false;

        do {
            // take input from user
            Scanner scan1 = new Scanner(System.in);
            Scanner scan2 = new Scanner(System.in);

            System.out.println("Search for event: ");
            eventName = scan1.nextLine();

            System.out.println("How many tickets would you like to buy?");
            ticketQuantity = scan2.nextInt();

            System.out.println("What is the seller's username?");
            sellerUsername = scan1.nextLine();

            try {

                //check if the number of tickets asked for purchase is legal
                if (ticketQuantity <= 0  || ticketQuantity >=5 ){
                    System.out.println("The number of tickets legal for purchase is between 1 and 4, try again.");
                    continue;
				}else {
                    for (int i = 0; i< CentralCore.getTickets().size();i++) {
                        //check the event name of the ticket exists on the database
						boolean validTicketStock = checkTicketsinStock(i, ticketQuantity);
						boolean validEventName = checkEventName(i, eventName);
						boolean validSellerName = checkSeller(i, sellerUsername);
                        if(validTicketStock && validEventName && validSellerName) {
							validUserInput = true;

                            double Price = CentralCore.getTickets().get(i).getTicketPrice();
                            double totalCost = (CentralCore.getTickets().get(i).getTicketPrice() * ticketQuantity);

                            System.out.println("The price for a single ticket is: " + Price);
                            System.out.println("The total cost for the number of tickets purchased is: " + totalCost);
                            System.out.println("Would you like to proceed with the purchase of the tickets: Yes / No ");
                            String select = scan1.nextLine();

                            switch (select) {

                                case "Yes":

                                    //modify tickets in stock after buy()
                                    for (int j = 0; j < CentralCore.getUsers().size(); j++) {

										//check if seller exists as a user
                                        if (!CentralCore.getUsers().contains(CentralCore.findUser(CentralCore.getTickets().get(j).getSellerUsername()))) {
                                            System.out.println("Seller user not found in the list of users. Transaction cannot be completed.");
                                            continue;
                                        }

										//check if the ticket price is less than the buyer's credit.
                                        else if (CentralCore.getUsers().get(j).getCredit() < CentralCore.getTickets().get(i).getTicketPrice()) {

                                            System.out.println("The user does not have enough credit to complete this transaction.");
                                            continue;
                                        } else{
											//modifyTicketinStock(i, ticketQuantity);
											//modifyTicketFileBuy(eventName, sellerUsername, Price, ticketQuantity);
                                            System.out.println("Transaction processed successfully");
                                            System.out.println("Added buy transaction to daily transaction list.");
					                        // CentralCore.buyFileChange(this.username,Price,ticketQuantity);
                                            //CentralCore.sellFileChange(sellerUsername,Price,ticketQuantity);
                                            flag = false;

                                            //Save this information in the dailytransaction list and buy/sell transaction list
                                            CentralCore.addBuyTransaction(04, eventName, sellerUsername, ticketQuantity, Price);
                                            CentralCore.addBuySellTransaction(04, eventName, sellerUsername, ticketQuantity, Price);
					                        //break out of this loop.
                                            break;
                                        }
                                    }
				     break;
                                case "No":
                                    System.out.println("Transaction cancelled.");
				    flag = false;
                                    break;

                                default:
                                    System.out.println("You entered an invalid option");
				    flag = false;
                                    break;
                            }
			     break;
                        }else if(i == CentralCore.getTickets().size() - 1){
				System.out.println("Wrong seller name, too many tickets requested or wrong event name.");
				flag = false;
				break;
			}
			if(validUserInput){
				break;
			}
                    }
                }
            }

            // catch every possible exception
            catch (Exception e) {
                System.out.println("Exception caught. Try again!");
            }

        }while(flag == true);
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

            System.out.println ("Enter name of event");
            eventName = scan1.nextLine();

            System.out.println("How many tickets would you like to sell? ");
            ticketQuantity = scan2.nextInt();

            System.out.println("How much do you want to sell each ticket for? ");
            salePrice = scan1.nextDouble();

            try {
                try {

                for (int i=0;i<CentralCore.getTickets().size();i++){
                    //check if event name is legal
                    if(!checkEventName(i,eventName)){
                        if (eventName.length() > 0 && eventName.length() <26){
                            // check if sale price is legal
                            if ( salePrice> 0 && salePrice <= 999.99){

                                // check if the number of tickets for sale is legal
                                if(ticketQuantity > 0 && ticketQuantity <= 100){

                                    System.out.println("All the information entered is legal, event has been created.");
                                    System.out.println("Added sell transaction to daily transaction list.");
                                    flag = false;

                                    //Save this information in the dailytransaction file and buy/sell transaction file
                                    CentralCore.addSellTransaction(03, eventName, this.username, ticketQuantity, salePrice);
                                    CentralCore.addBuySellTransaction(03, eventName, this.username, ticketQuantity, salePrice);
                                    //modify the tickets for sale
                                    modifyTicketFileSell(eventName, this.getUsername(), ticketQuantity, salePrice);
                                    flag = false;
                                    break;
                                }else{
                                    System.out.println("The number of tickets for sale is illegal. Try again.");
                                    flag=false;
                                    break;
                                }
                            }else{
                                System.out.println("The price of tickets for sale is illegal. Try again.");
                                flag=false;
                                break;
                            }
                        } else {
                            System.out.println("The event name length must be between 0 and 26. Try again.");
                            flag=false;
                            break;
                        }
                    }else{
                        System.out.println("You are already selling this event. Try again.");
                        flag=false;
                        break;
                    }
                }
            }
            //catch every possible exception
            catch (Exception e) {
                System.out.println("Exception caught,try again!");
            }

        } while(flag == true);
    }
    // addCredit method for standard user
    // for admin needs to be Overriden
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
			CentralCore.addCreditTransaction(this.username, credit_to_add, 6, this.usertype);
			User currUser = CentralCore.findUser(username);
			//CentralCore.addCredFileChange(currUser.getUsername(), credit_to_add);
			currUser.setCredit(currUser.getCredit() + credit_to_add);
			System.out.println("New credit is: " + currUser.getCredit());
            System.out.println("Added add credit transaction to daily transaction list.");
			break;
		    } else {
				System.out.println("The amount entered is not correct!");
		    } 
	    }

	    catch (InputMismatchException ex) {
		System.out.println("Try again!");	
	    } 

	}while(flag == true);

    }

}
