package com.tmu.ticketmain;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CentralCoreTest extends TestCase{

    @Test
    void applyDtfChanges() {
        try{

            //system files
            File userFile = new File("../users.txt");
            File ticketFile = new File("../tickets.txt");
            File errorFile = new File("../error.txt");

            //filereaders
            BufferedReader rw = new BufferedReader(new FileReader("../DailyTransactionFiles/dtf_merged.txt"));
            BufferedReader userReader = new BufferedReader(new FileReader(userFile));
            BufferedReader ticketReader = new BufferedReader(new FileReader(ticketFile));
            BufferedReader errorReader = new BufferedReader(new FileReader(errorFile));
         
            BufferedWriter userfw = new BufferedWriter(new FileWriter(userFile, true));
            BufferedWriter ticketfw = new BufferedWriter(new FileWriter(ticketFile, true));
            BufferedWriter errorfw = new BufferedWriter(new FileWriter(errorFile, true));

            String dtf_line = "";
            List<String> userLines = CentralCore.userFileLines();
            List<String> ticketLines = CentralCore.ticketFileLines();

            while((dtf_line = rw.readLine()) != null){
                String[] dtfLineSplit = dtf_line.split("_");
                String code = dtfLineSplit[0];

                if(code.equals("01") || code.equals("02") || code.equals("06")){

                    String username = dtfLineSplit[1].trim();
                    String type = dtfLineSplit[2].trim();
                    String credit = dtfLineSplit[3].trim();
                   
                    int userIndex = -1;

                    if(username.trim().length() > 15){
                        errorfw.append("ERROR: username longer than 15 characters " + "DailyTransactionFiles/dtf_merged.txt");
                        errorfw.newLine();
                        break;
                    }

                    if(!type.equals("AA") && !type.equals("BS") && !type.equals("SS") && !type.equals("FS")){
                        errorfw.append("ERROR: Invalid type " + "DailyTransactionFiles/dtf_merged.txt");
                        errorfw.newLine();
                        break;
                    }

                    if(Double.parseDouble(credit) < 0){
                        errorfw.append("ERROR: Negative Credit " + "DailyTransactionFiles/dtf_merged.txt");
                        errorfw.newLine();
                        break;
                    }

                    //we
                    // for(int i = 0; i < userLines.size(); i++){
                    //     String userNameLine = userLines.get(i).substring(0,16).trim();
                    //     if(userNameLine.equals(username)){
                    //         userIndex = i;
                    //         break;
                    //     }
                    // }

                    if(code.equals("01")){
                        List<String> lines = CentralCore.userFileLines();
                        int index = -1;
                        boolean res;
                        for(int i = 0; i < lines.size(); i++){
                            String currLine = lines.get(i);
                            String currUser = lines.get(i).substring(0, 16).trim();
                            String currType = lines.get(i).substring(16, 19).trim();
                            String currAmt = lines.get(i).substring(19, 28).trim();

                            res = currUser.equals(username);
                            if(res){
                                index = i;
                                break;
                            }
                        }

                        if(!res){
                            //test fails if no user found
                            assertEquals(true, false);
                        }else{
                            //test that user file actually has the values as being equal.
                            assertEquals(lines.get(index).substring(0, 16).trim(), username);
                            assertEquals(lines.get(index).substring(16, 19).trim(), type);
                            assertEquals(lines.get(index).substring(19, 28).trim(), credit);
                        }

                        //Test the creation
                        
                    }else if(userIndex != -1 && code.equals("01")){
                        //log a create error
                        errorfw.append("ERROR: Create transaction, user not found " + dtf_line);
                        errorfw.newLine();
                    }else if(userIndex != -1 && code.equals("02")){
                        userLines.remove(userIndex);
                        CentralCore.rewriteUsers(userLines);
                    }else if(userIndex == -1 && code.equals("02")){
                        //log a delete error
                        errorfw.append("ERROR: Delete transaction, user not found " + dtf_line);
                        errorfw.newLine();
                    }else if(userIndex != -1 && code.equals("06")){
                        double oldCredit = Double.parseDouble(userLines.get(userIndex).substring(19, 28).trim());
                        double newCredit = oldCredit;// + Double.parseDouble(credit);
                        //Assertions.assertEquals(150.0, newCredit);

                        String newLine = Admin.userFormatter(username) + " " + Admin.typeFormatter(type) + Admin.creditFormatter(newCredit);
                        userLines.set(userIndex, newLine);
                        CentralCore.rewriteUsers(userLines);
                    }else if(userIndex == -1 && code.equals("06")){
                        //log addcred error
                        errorfw.append("ERROR: Add credit transaction, user not found " + dtf_line);
                        errorfw.newLine();
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

                    if(buyerName.trim().length() > 15){
                        errorfw.append("ERROR: Buyer name longer than 15 characters " + "DailyTransactionFiles/dtf_merged.txt");
                        errorfw.newLine();
                        break;
                    }

                    if(sellerName.trim().length() > 15){
                        errorfw.append("ERROR: Seller name longer than 15 characters " + "DailyTransactionFiles/dtf_merged.txt");
                        errorfw.newLine();
                        break;
                    }

                    if(Double.parseDouble(refundAmt) < 0){
                        errorfw.append("ERROR: Negative refund amount" + "DailyTransactionFiles/dtf_merged.txt");
                        errorfw.newLine();
                        break;
                    }

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
                            errorfw.append("ERROR: Refund transaction, buyer user not found " + dtf_line);
                            errorfw.newLine();
                        }

                        if(sellerIndex == -1){
                            //seller finding error
                            errorfw.append("ERROR: Refund transaction, seller user not found " + dtf_line);
                            errorfw.newLine();
                        }
                    }else if(sellerFundsErr){
                        //log the funds error - seller can't have neg funds
                        errorfw.append("ERROR: Refund transaction, seller cannot have negative funds " + dtf_line);
                        errorfw.newLine();
                    }else if(sellerIndex != -1 && buyerIndex != -1){
                        userLines.set(sellerIndex, sellerNewLine);
                        userLines.add(buyerIndex ,buyerNewLine);
                        CentralCore.rewriteUsers(userLines);
                    }
                }else if(code.equals("04")){
                    String eventName = dtfLineSplit[1].trim();
                    String sellerName = dtfLineSplit[2].trim();
                    String ticketNum = dtfLineSplit[3];
                    String ticketPrice = dtfLineSplit[4];
                    String newUserLine = "";

                    int sellerIndex = -1;
                    int ticketIndex = -1;

                    if(sellerName.trim().length() > 15){
                        errorfw.append("ERROR: Seller name longer than 15 characters " + dtf_line);
                        errorfw.newLine();
                        break;
                    }

                    for(int i = 0; i < userLines.size(); i++){
                        System.out.println(userLines.get(i).substring(0, 16).trim());
                        System.out.println(sellerName);
                        if(userLines.get(i).substring(0, 16).trim().equals(sellerName)){
                            sellerIndex = i;
                            String sellerNameCurr = userLines.get(i).substring(0, 16).trim();
                            String typeCurr = userLines.get(i).substring(16, 19).trim();
                            double creditCurr = Double.parseDouble(userLines.get(i).substring(19, 28).trim()) + Integer.parseInt(ticketNum) * Double.parseDouble(ticketPrice);
                            newUserLine = Admin.userFormatter(sellerNameCurr) + " " + Admin.typeFormatter(typeCurr) + Admin.creditFormatter(creditCurr);
                        }
                    }

                    for(int j = 0; j < ticketLines.size(); j++){
                        String currTicketEvent = ticketLines.get(j).substring(0, 26).trim();
                        String currTicketSeller = ticketLines.get(j).substring(26, 40).trim();
                        if(eventName.equals(currTicketEvent) && sellerName.equals(currTicketSeller)){
                            ticketIndex = j;
                            break;
                        }
                    }

                    if(sellerIndex != -1 && ticketIndex != -1){
                        String fileEventName = ticketLines.get(ticketIndex).substring(0, 26).trim();
                        String fileSellerName = ticketLines.get(ticketIndex).substring(26, 40).trim();
                        int fileTickets = Integer.parseInt(ticketLines.get(ticketIndex).substring(41, 44).trim()) - Integer.parseInt(ticketNum);
                        double filePrice = Double.parseDouble(ticketLines.get(ticketIndex).substring(45, 51).trim());

                        if(fileTickets >= 0){
                            String newTicketLine = Ticket.formatEventName(fileEventName) + Ticket.formatSellerName(fileSellerName) + Ticket.formatTicketQuantity(fileTickets) + Ticket.formatSellerPrice(filePrice);
                            userLines.set(sellerIndex, newUserLine);
                            ticketLines.set(ticketIndex, newTicketLine);
                            CentralCore.rewriteTickets(ticketLines);
                        }else{
                            errorfw.append("ERROR: Buy transaction, cannot have negative number of tickets for sale " + dtf_line);
                            errorfw.newLine();
                        }
                    }else if(sellerIndex == -1){
                        //seller not found
                        errorfw.append("ERROR: Buy transaction, seller user not found " + dtf_line);
                        errorfw.newLine();
                    }else if(ticketIndex == -1){
                        //event not found
                        errorfw.append("ERROR: Buy transaction, event not found " + dtf_line);
                        errorfw.newLine();
                    }
                }else if(code.equals("03")){
                    String eventName = dtfLineSplit[1];
                    String sellerName = dtfLineSplit[2];
                    int ticketNum = Integer.parseInt(dtfLineSplit[3]);
                    double ticketPrice = Double.parseDouble(dtfLineSplit[4]);

                    String ticketToAdd = Ticket.formatEventName(eventName) + Ticket.formatSellerName(sellerName) + Ticket.formatTicketQuantity(ticketNum) + Ticket.formatSellerPrice(ticketPrice);
                    String END = ticketLines.remove(ticketLines.size() - 1);
                    ticketLines.add(ticketToAdd);
                    ticketLines.add(END);

                    CentralCore.rewriteTickets(ticketLines);
                }
            }

            userfw.close();
            rw.close();
            ticketfw.close();
            errorfw.close();
        }catch (Exception e){
            System.out.println("Dtf apply error" + e + "line number: " + e.getStackTrace()[0].getLineNumber());
        }
    }
}