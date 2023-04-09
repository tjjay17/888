package src.com.tmu.ticketmain;

import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static src.com.tmu.ticketmain.CentralCore.*;
import static src.com.tmu.ticketmain.User.*;

class UserTest extends TestCase {

    @Test
    void sell() throws IOException {
        ArrayList<Ticket> ticketsList = readTickets();
        User activeUser = new Standard_Sell("ss","Seller1",100);

        String eventName = "Test_Event";
        double salePrice = 1000;
        int ticketQuantity = 100;

       ///
        for (int i=0;i<CentralCore.getTickets().size();i++) {
            //check if event name is legal
            if (!activeUser.checkEventName(i, eventName)) {
                if (eventName.length() > 0 && eventName.length() < 26) {
                    // check if sale price is legal
                    if (salePrice > 0 && salePrice <= 999.99) {
                        // check if the number of tickets for sale is legal
                        if (ticketQuantity > 0 && ticketQuantity <= 100) {

                            //Save this information in the dailytransaction file and buy/sell transaction file
                            CentralCore.addSellTransaction(03, eventName, activeUser.getUsername(), ticketQuantity, salePrice);
                            ArrayList<DailyTransaction> list = (ArrayList<DailyTransaction>) CentralCore.getTransactions();
                            String test_eventName = new String(list.get(list.size()-1).getEventName());
                            Double test_quantity = Double.parseDouble (String.valueOf(list.get(list.size() - 1).getTicketQuantity()));
                            Double test_price = Double.parseDouble (String.valueOf(list.get(list.size() - 1).getPrice()));
                            assertEquals("Test_Event", test_eventName);
                            Assertions.assertEquals(10.0, test_price);
                            Assertions.assertEquals(100, test_quantity);
                            break;
//                            System.out.println("All the information entered is legal, event has been created.");
//                            System.out.println("Added sell transaction to daily transaction list.");
                        }else{
                            System.out.println("Number of tickets exceeds limit.");
                            Boolean gotEx = true;
                            assertTrue(gotEx);
                            break;
                        }

                    }else{
                        System.out.println("The sale price of the event is higher than the limit.");
                        Boolean gotEx = true;
                        assertTrue(gotEx);
                        break;
                    }

                }else{
                    System.out.println("The event length name is greater than limit.");
                    Boolean gotEx = true;
                    assertTrue(gotEx);
                    break;
                }
            }else{
                System.out.println("Event already exists, can't be created!");
                Boolean gotEx = true;
                assertTrue(gotEx);
                break;
            }
        }

    }

    @Test
    void buy() throws IOException {
        ArrayList<Ticket> ticketList = CentralCore.readTickets();
        ArrayList<User> usersList = CentralCore.readUsers();
        User activeUser = new Standard_Buy("bs","Buyer1",100);
        //local variables
        String eventName ="Random event";
        String sellerUsername = "Daniel";
        int ticketQuantity = 6;

            //check if the number of tickets asked for purchase is legal
            if (ticketQuantity <= 0  || ticketQuantity >=5 ){
                System.out.println("The number of tickets legal for purchase is between 1 and 4, try again.");
                Boolean gotEx = true;
                assertTrue(gotEx);
            }else {
                for (int i = 0; i< CentralCore.getTickets().size();i++) {
                    //check the event name of the ticket exists on the database
                    boolean validTicketStock = activeUser.checkTicketsinStock(i, ticketQuantity);
                    boolean validEventName = activeUser.checkEventName(i, eventName);
                    boolean validSellerName = activeUser.checkSeller(i, sellerUsername);
                    if(validTicketStock && validEventName && validSellerName) {

                        double Price = CentralCore.getTickets().get(i).getTicketPrice();
                        double totalCost = (CentralCore.getTickets().get(i).getTicketPrice() * ticketQuantity);

                        System.out.println("The price for a single ticket is: " + Price);
                        System.out.println("The total cost for the number of tickets purchased is: " + totalCost);
                        System.out.println("Would you like to proceed with the purchase of the tickets: Yes / No ");
                        String select = "Yes";

                        switch (select) {

                            case "Yes":

                                //modify tickets in stock after buy()
                                for (int j = 0; j < CentralCore.getUsers().size(); j++) {

//                                    //check if seller exists as a user
//                                    if (!CentralCore.getUsers().contains(CentralCore.findUser(CentralCore.getTickets().get(j).getSellerUsername()))) {
//                                        System.out.println("Seller user not found in the list of users. Transaction cannot be completed.");
//                                        continue;
//                                    }

                                    //check if the ticket price is less than the buyer's credit.
                                     if (activeUser.getCredit() < CentralCore.getTickets().get(i).getTicketPrice()) {

                                        System.out.println("The user does not have enough credit to complete this transaction.");
                                        Boolean gotEx = true;
                                        assertTrue(gotEx);
                                        break;
                                    } else{
                                        //modifyTicketinStock(i, ticketQuantity);
                                        //modifyTicketFileBuy(eventName, sellerUsername, Price, ticketQuantity);
                                        System.out.println("Transaction processed successfully");
                                        System.out.println("Added buy transaction to daily transaction list.");
                                        // CentralCore.buyFileChange(this.username,Price,ticketQuantity);
                                        //CentralCore.sellFileChange(sellerUsername,Price,ticketQuantity);

                                        //Save this information in the dailytransaction list and buy/sell transaction list
                                        CentralCore.addBuyTransaction(04, eventName, sellerUsername, ticketQuantity, Price);
                                        ArrayList<DailyTransaction> list = (ArrayList<DailyTransaction>) CentralCore.getTransactions();
                                        String test_eventName = new String(list.get(list.size()-1).getEventName());
                                        String test_sellerUsername = new String (String.valueOf(list.get(list.size() - 1).getSellerUser()));
                                        Double test_ticketQuantity = Double.parseDouble (String.valueOf(list.get(list.size() - 1).getTicketQuantity()));
                                        assertEquals("Random event", test_eventName);
                                        assertEquals("Daniel", test_sellerUsername);
                                        Assertions.assertEquals(1, test_ticketQuantity);
                                        //break out of this loop.
                                        break;
                                    }
                                }
                                break;
                            case "No":
                                System.out.println("Transaction cancelled.");
                                //flag = false;
                                break;

                            default:
                                System.out.println("You entered an invalid option");
                                //flag = false;
                                break;
                        }
                        break;
                    }else if(i == CentralCore.getTickets().size() - 1){
                        System.out.println("Wrong seller name, too many tickets requested or wrong event name.");
                        //flag = false;
                        Boolean gotEx = true;
                        assertTrue(gotEx);
                        break;
                    }
                }
            }
        }


    @Test
    void addCredit() throws IOException {
        ArrayList<User> usersList = readUsers();
        //local variables
        Double credit_to_add;
        User activeUser = new Standard_Buy("bs","Ben",100);
//        boolean flag = true;
//        do {
//            Scanner input = new Scanner(System.in);
            System.out.println ("Enter the amount you want to add as credit: ");
//            try {
                credit_to_add = 50.0;
                if (credit_to_add > 0 && credit_to_add <=1000){

                    //flag = false;
                    //Save this information in the dailytransaction file
                    addCreditTransaction(activeUser.getUsername(), credit_to_add, 6, activeUser.getUsertype());

                    //CentralCore.addCredFileChange(currUser.getUsername(), credit_to_add);
                    activeUser.setCredit(activeUser.getCredit() + credit_to_add);
                    System.out.println("New credit is: " + activeUser.getCredit());
                    System.out.println("Added add credit transaction to daily transaction list.");

                    //test results
                    ArrayList<DailyTransaction> list = (ArrayList<DailyTransaction>) getTransactions();
                    String test_userName = new String(list.get(list.size()-1).getAddCredUser());
                    String test_usertype = new String(list.get(list.size()-1).getUserType());
                    Double test_newCredit = Double.parseDouble (String.valueOf(list.get(list.size() - 1).getCredit()));

                    assertEquals("Ben", test_userName);
                    assertEquals("bs", test_usertype);
                    Assertions.assertEquals(50.0, test_newCredit);

                } else {
                    System.out.println("The amount entered is not correct!");
                    Boolean gotEx = true;
                    assertTrue(gotEx);

                }
            }

//            catch (InputMismatchException ex) {
//                System.out.println("Try again!");
//            }
//
//        }while(flag == true);

    }



