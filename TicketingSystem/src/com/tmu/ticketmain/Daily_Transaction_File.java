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

    //var used to track the last transaction written in the session - no need to rewrite multiple transactions in file
    static int lastTransWrittenPtr = 0;

    public static void createOrUpdateDailyFile(){
        //daily trans file written based on date
        Date d = new Date();
        String year = Integer.toString(d.getYear());
        String month = Integer.toString(d.getMonth());
        String date = Integer.toString(d.getDate());
        List<DailyTransaction> transList = CentralCore.getTransactions();

        //file format - dtf_year_month_date.txt
        File dtf = new File("../DailyTransactionFiles/dtf" + "_" +  year + "_" + month + "_" + date + ".txt");
        try{
            BufferedWriter fw = new BufferedWriter( new FileWriter(dtf, true));
            for(int i = lastTransWrittenPtr; i < transList.size(); i++){
                DailyTransaction currentTrans = transList.get(i);
                //create or delete
                if(currentTrans.getCode() == 1 || currentTrans.getCode() == 2){
                    //then select if it is create OR delete
                    String codeToUse = (currentTrans.getCode() == 1) ? createCode : deleteCode;
                    String transUsername = currentTrans.getGenUser();
                    User transUser = CentralCore.findUser(transUsername);
                    String dtfLine = codeToUse + "_" + currentTrans.getGenUser() + "_" + transUser.getUsertype() + "_" + transUser.getCredit();
                    if(dtf.length() != 0){
                        fw.newLine();
                    }
                    fw.append(dtfLine);
                //add credit
                }else if(currentTrans.getCode() == 6){
                    String addCredUser = currentTrans.getAddCredUser();
                    User transUser = CentralCore.findUser(addCredUser);
                    //this assumes that we use the price field as the credit amount
                    String credit = Double.toString(currentTrans.getCredit());
                    String dtfLine = addCreditCode + "_" + addCredUser + "_" + transUser.getUsertype() + "_" + credit;
                    if(dtf.length() != 0){
                        fw.newLine();
                    }
                    fw.append(dtfLine);
                //buy or sell
                }else if(currentTrans.getCode() == 3 || currentTrans.getCode() == 4){
                    //then select if it is buy OR sell
                    String codeToUse = (currentTrans.getCode() == 3) ? buyCode : sellCode; 
                    String eventName = currentTrans.getEventName();
                    String sellerUserName = currentTrans.getSellerUser();
                    String numberTicketsSale = Integer.toString(currentTrans.getTicketQuantity());
                    String pricePerTicket = Double.toString(currentTrans.getPrice());
                    String dtfLine = codeToUse + "_" + eventName + "_" + sellerUserName + "_" + numberTicketsSale + "_" + pricePerTicket;
                    if(dtf.length() != 0){
                        fw.newLine();
                    }
                    fw.append(dtfLine);
                }else if(currentTrans.getCode() == 5){
                    String buyerUser = currentTrans.getBuyerUser();
                    String sellerUser = currentTrans.getSellerUser();
                    double refund = currentTrans.getRefund();
                    String dtfLine = refundCode + "_" + buyerUser +  "_" + sellerUser + "_" + refund; 
                    if(dtf.length() != 0){
                        fw.newLine();
                    }
                    fw.append(dtfLine);
                }else if(currentTrans.getCode() == 0){
                    String userName = currentTrans.getGenUser();
                    String userType = CentralCore.findUser(userName).getUsertype();
                    double credit = currentTrans.getCredit();
                    String dtfLine = endOfSessionCode + "_" + userName + "_" + userType + "_" + credit;
                    if(dtf.length() != 0){
                        fw.newLine();
                    }
                    fw.append(dtfLine);
                }
                lastTransWrittenPtr++;
            }
            fw.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
