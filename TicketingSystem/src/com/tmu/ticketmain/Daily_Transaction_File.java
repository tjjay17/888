package com.tmu.ticketmain;
import java.io.*;
import java.util.*;

public class Daily_Transaction_File {
    private static final String createCode = "01";
    private static final String deleteCode = "02";
    private static final String buyCode = "03";
    private static final String sellCode = "04";
    private static final String refundCode = "05";
    private static final String addCreditCode = "06";
    private static final String endOfSessionCode = "00";

    public static void createOrUpdateDailyFile(){
        Date d = new Date();
        String year = Integer.toString(d.getYear());
        String month = Integer.toString(d.getMonth());
        String date = Integer.toString(d.getDate());
        List<DailyTransaction> transList = CentralCore.getTransactions();

        File dtf = new File("../DailyTransactionFiles/dtf" + year + "_" + month + "_" + date + ".txt");
        try{
            FileWriter fw = new FileWriter(dtf, true);
            for(int i = 0; i < transList.size(); i++){
                DailyTransaction currentTrans = transList.get(i);
                if(currentTrans.getCode() == 1 || currentTrans.getCode() == 2 || currentTrans.getCode() == 6 || currentTrans.getCode() == 0){
                    String codeToUse = deleteCode;
                    if(currentTrans.getCode() == 1){
                        codeToUse = createCode;
                    }else if(currentTrans.getCode() == 6){
                        codeToUse = addCreditCode;
                    }else if(currentTrans.getCode() == 0){
                        codeToUse = endOfSessionCode;
                    }

                    String transUsername = currentTrans.getBuyerUser();
                    User transUser = CentralCore.findUser(transUsername);
                    String dtfLine = codeToUse + "_" + currentTrans.getBuyerUser() + "_" + transUser.getUsertype() + "_" + transUser.getCredit();
                    fw.append(dtfLine);
                }else if(currentTrans.getCode() == 5){
                    String sellerUserName = currentTrans.getSellerUser();
                    String buyerUserName = currentTrans.getBuyerUser();
                    //this assumes that we use the price field as the credit amount
                    String credit = Double.toString(currentTrans.getPrice());
                    String dtfLine = refundCode + "_" + buyerUserName + "_" + sellerUserName + "_" + credit;
                    fw.append(dtfLine);
                }else if(currentTrans.getCode() == 3 || currentTrans.getCode() == 4){
                    String codeToUse = sellCode;
                    if(currentTrans.getCode() == 3){
                        codeToUse = buyCode;
                    }

                    String eventName = currentTrans.getEventName();
                    String sellerUserName = currentTrans.getSellerUser();
                    String numberTicketsSale = Integer.toString(currentTrans.getTicketQuantity());
                    String pricePerTicket = Double.toString(currentTrans.getPrice());
                    String dtfLine = codeToUse + "_" + eventName + "_" + sellerUserName + "_" + numberTicketsSale + "_" + pricePerTicket;
                    fw.append(dtfLine);
                }
            }
            fw.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
