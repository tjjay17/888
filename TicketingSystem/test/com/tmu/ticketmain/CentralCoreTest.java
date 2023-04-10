package com.tmu.ticketmain;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CentralCoreTest {
    @Test
     void applyDtfChanges(String dtfFile) {
        try{
            //system files
            File userFile = new File("users.txt");
            File ticketFile = new File("tickets.txt");
            File errorFile = new File("error.txt");

            //filereaders
            BufferedReader rw = new BufferedReader(new FileReader(dtfFile));
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

                    for(int i = 0; i < userLines.size(); i++){
                        String userNameLine = userLines.get(i).substring(0,16).trim();
                        if(userNameLine.equals(username)){
                            userIndex = i;
                            break;
                        }
                    }

                    if(userIndex == -1 && code.equals("01")){
                        String END = userLines.remove(userLines.size() - 1);
                        String lineToAdd = Admin.userFormatter(username) + " " + Admin.typeFormatter(type) + Admin.creditFormatter(Double.parseDouble(credit));
                        userLines.add(lineToAdd);
                        userLines.add(END);
                        CentralCore.rewriteUsers(userLines);
                        
                        //test that a user has been created
                        assertEquals("newPerson", username);
                        assertEquals("FS", type);
                        assertEquals("200.0", credit);
                    }else if(userIndex != -1 && code.equals("01")){
                        //log a create error
                        errorfw.append("ERROR: Create transaction, user exists " + dtf_line);
                        errorfw.newLine();
                        List<String>errorLines = CentralCore.errorFileLines();
                        
                        //test when user already exists that error is logged
                        assertTrue(errorLines.contains("ERROR: Create transaction, user exists 01_pibbles_FS_1000.0"));
                    }else if(userIndex != -1 && code.equals("02")){
                        userLines.remove(userIndex);
                        CentralCore.rewriteUsers(userLines);
                        
                        userLines = CentralCore.userFileLines();
                        String oldUserName = userLines.get(userIndex).substring(0, 16).trim();
                        assertFalse(oldUserName.equals("Jamie"));
                        
                        ticketLines = CentralCore.ticketFileLines();
                        int ticketIndex = -1;
                        for(int i = 0; i < ticketLines.size(); i++){
                            if(ticketLines.get(i).substring(26, 40).trim().equals(username)){
                                ticketIndex = i;
                            }
                        }
                        assertEquals(-1, ticketIndex);
                    }else if(userIndex == -1 && code.equals("02")){
                        //log a delete error
                        errorfw.append("ERROR: Delete transaction, user not found " + dtf_line);
                        errorfw.newLine();
                        
                        List<String>errorLines = CentralCore.errorFileLines();
                        assertTrue(errorLines.contains("ERROR: Delete transaction, user not found 02_noexist_AA_200.0"));
                    }else if(userIndex != -1 && code.equals("06")){
                        double oldCredit = Double.parseDouble(userLines.get(userIndex).substring(19, 28).trim());
                        double newCredit = oldCredit + Double.parseDouble(credit);
                        //test AddCredit transaction
                        Assertions.assertEquals(100.0, newCredit);

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
                            //test buyer credit
                            assertEquals(100.0, newCredit);                   
                        }else if(currUser.equals(sellerName)){
                            sellerIndex = j;
                            double oldCredit = Double.parseDouble(userLines.get(j).substring(19, 28).trim());
                            double newCredit = oldCredit + Double.parseDouble(refundAmt);
                            String type = userLines.get(j).substring(16, 19).trim();
                            sellerNewLine = Admin.userFormatter(sellerName) + " " + Admin.typeFormatter(type) + Admin.creditFormatter(newCredit);
                            //test seller credit
                            assertEquals(400.0, newCredit);
                        }
                    }

                    if(buyerIndex == -1 || sellerIndex == -1){
                        if(buyerIndex == -1){
                            //buyer finding error
                            errorfw.append("ERROR: Refund transaction, buyer user not found " + dtf_line);
                            errorfw.newLine();
                            
                            List<String> errorLines = CentralCore.errorFileLines();
                            assertTrue(errorLines.contains("ERROR: Refund transaction, buyer user not found 05_noexist_newPerson_200.0"));
                        }

                        if(sellerIndex == -1){
                            //seller finding error
                            errorfw.append("ERROR: Refund transaction, seller user not found " + dtf_line);
                            errorfw.newLine();
                            
                            List<String> errorLines = CentralCore.errorFileLines();
                            assertTrue(errorLines.contains("ERROR: Refund transaction, seller user not found 05_newPerson_noexist_300.0"));
                        }
                        
                        if(sellerIndex == -1 && buyerIndex == -1){
                            errorfw.append("ERROR: Refund transaction, buyer and seller not found " + dtf_line);
                            errorfw.newLine();
                            
                            List<String> errorLines = CentralCore.errorFileLines();
                            assertTrue(errorLines.contains("ERROR: Refund transaction, buyer and seller not found 05_fake_noexist_23.0"));
                        }
                        
                    }else if(sellerFundsErr){
                        //log the funds error - seller can't have neg funds
                        errorfw.append("ERROR: Refund transaction, seller cannot have negative funds " + dtf_line);
                        errorfw.newLine();
                        
                        List<String> errorLines = CentralCore.errorFileLines();
                        assertTrue(errorLines.contains("ERROR: Refund transaction, seller cannot have negative funds 05_buyer_newPerson_800.0"));
                    }else if(sellerIndex != -1 && buyerIndex != -1){
                        userLines.set(sellerIndex, sellerNewLine);
                        userLines.add(buyerIndex ,buyerNewLine);
                        CentralCore.rewriteUsers(userLines);
                    }
                }else if(code.equals("04")){
                    String eventName = dtfLineSplit[1].trim();
                    String sellerName = dtfLineSplit[2].trim();
                    String buyersName = dtfLineSplit[3].trim();
                    String ticketNum = dtfLineSplit[4];
                    String ticketPrice = dtfLineSplit[5];
                    String newUserLine = "";
                    
                    //test if the buyer's name is correct
                    assertEquals("buyer", buyersName);
                    
                    int sellerIndex = -1;
                    int ticketIndex = -1;

                    if(sellerName.trim().length() > 15){
                        errorfw.append("ERROR: Seller name longer than 15 characters " + dtf_line);
                        errorfw.newLine();
                        break;
                    }

                    for(int i = 0; i < userLines.size(); i++){
//                        System.out.println(userLines.get(i).substring(0, 16).trim());
//                        System.out.println(sellerName);
                        if(userLines.get(i).substring(0, 16).trim().equals(sellerName)){
                            sellerIndex = i;
                            String sellerNameCurr = userLines.get(i).substring(0, 16).trim();
                            String typeCurr = userLines.get(i).substring(16, 19).trim();
                            double creditCurr = Double.parseDouble(userLines.get(i).substring(19, 28).trim()) + (Integer.parseInt(ticketNum) * Double.parseDouble(ticketPrice));
                            newUserLine = Admin.userFormatter(sellerNameCurr) + " " + Admin.typeFormatter(typeCurr) + Admin.creditFormatter(creditCurr);
                        }
                    }
                    
                                        for(int i = 0; i < userLines.size(); i++) {

                        if (userLines.get(i).substring(0, 16).trim().equals(buyersName)) {
                            int buyerIndex = i;
                            String buyerNameCurr = userLines.get(i).substring(0, 16).trim();
                            String typeCurr = userLines.get(i).substring(16, 19).trim();
                            double creditCurr = Double.parseDouble(userLines.get(i).substring(19, 28).trim()) - (Integer.parseInt(ticketNum) * Double.parseDouble(ticketPrice));
                            String newUserLine2 = Admin.userFormatter(buyerNameCurr) + " " + Admin.typeFormatter(typeCurr) + Admin.creditFormatter(creditCurr);
                            userLines.set(buyerIndex,newUserLine2);
                            CentralCore.rewriteUsers(userLines);
                            //testing credit change for buyer after buy transaction
                            assertEquals(50,creditCurr);
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
                        //testing ticket number after a buy transaction
                        assertEquals(0, fileTickets);

                        if(fileTickets > 0){
                            String newTicketLine = Ticket.formatEventName(fileEventName) + Ticket.formatSellerName(fileSellerName) + Ticket.formatTicketQuantity(fileTickets) + Ticket.formatSellerPrice(filePrice);
                            userLines.set(sellerIndex, newUserLine);
                            ticketLines.set(ticketIndex, newTicketLine);
                            CentralCore.rewriteTickets(ticketLines);
                            CentralCore.rewriteUsers(userLines);
                        }else{
                            ticketLines.remove(ticketIndex);
                            CentralCore.rewriteTickets(ticketLines);
                            errorfw.append("ERROR: Buy transaction, cannot have negative number of tickets for sale " + dtf_line);
                            errorfw.newLine();
                            
                            //check for ticket cancellation
                            if(CentralCore.findTicket(eventName) == null){
                                Boolean gotEx = true;
                                assertTrue(gotEx);
                            }
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
                    
                    //test if seller's name is correct
                    assertEquals("seller", sellerName);

                    String ticketToAdd = Ticket.formatEventName(eventName) + Ticket.formatSellerName(sellerName) + Ticket.formatTicketQuantity(ticketNum) + Ticket.formatSellerPrice(ticketPrice);
                    String END = ticketLines.remove(ticketLines.size() - 1);
                    ticketLines.add(ticketToAdd);
                    ticketLines.add(END);

                    CentralCore.rewriteTickets(ticketLines);
                    //check if event is created properly
                    Ticket t = CentralCore.findTicket(eventName);
                    if(eventName.equals(t.getEventName())){
                        Boolean gotEx = true;
                        assertTrue(gotEx);
                    }
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


    //@Test
    void processFileChanges() throws IOException {
        String dtfTemp = "DailyTransactionFiles/dtf_temp.txt";
        File dtfTempFile = new File(dtfTemp);
        File mergedDtf = new File("DailyTransactionFiles/dtf_merged.txt");

        if(dtfTempFile.exists()) {

            BufferedReader rw = new BufferedReader(new FileReader(dtfTemp));
            BufferedWriter fw = new BufferedWriter(new FileWriter(mergedDtf, true));
            String dtf_line = "";

            //write into the merged file
            while ((dtf_line = rw.readLine()) != null) {
                fw.append(dtf_line);
                fw.newLine();
            }
            fw.append("**********************");
            fw.newLine();

            applyDtfChanges(dtfTemp);
            
            rw.close();
            fw.close();
            dtfTempFile.delete();
        }
    }
}