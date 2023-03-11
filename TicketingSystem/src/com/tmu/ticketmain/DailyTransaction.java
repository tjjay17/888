
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmu.ticketmain;

/**
 *
 * @author Tj
 */
public class DailyTransaction {
    private int code;
    private String eventName;
    private String sellerUser;
    private String addCredUser;
    private String buyerUser;
    private int ticketQuantity;
    private double price;
    private double credit;
    private double refund;
    
    //for a sell
    DailyTransaction(int code, String eventName, String transUser, int ticketQuantity, double price){
        this.code = code;
        this.eventName = eventName;
        this.ticketQuantity = ticketQuantity;
        this.price = price;

        if(code == 3){
            this.buyerUser = transUser;
        }else if(code == 4){
            this.sellerUser = transUser;
        }
    }

    //add credit
    DailyTransaction(int code, String addCredUser, double credit){
        this.code = code;
        this.addCredUser = addCredUser;
        this.credit = credit;
    }

    //refunds
    DailyTransaction(int code, double refund, String buyerUser, String sellerUser){
        this.code = code;
        this.buyerUser = buyerUser;
        this.sellerUser = sellerUser;
        this.refund = refund;
    }

    //can be used for logout
    DailyTransaction(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }

    public String getEventName(){
        return this.eventName;
    }

    public String getSellerUser(){
        return this.sellerUser;
    }

    public int getTicketQuantity(){
        return this.ticketQuantity;
    }

    public double getPrice(){
        return this.price;
    }

    public String getBuyerUser(){
        return this.buyerUser;
    }

    public String getAddCredUser(){
        return this.addCredUser;
    }

    public double getCredit(){
        return this.credit;
    }

    public double getRefund(){
        return this.refund;
    }

}

