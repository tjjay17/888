
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
    private String refundUser;
    private String addCredUser;
    private String buyerUser;
    private int ticketQuantity;
    private double price;
    private double credit;
    private double refund;
    
    //for a sell
    DailyTransaction(int code, String eventName, String sellerUser, int ticketQuantity, double price){
        this.code = code;
        this.eventName = eventName;
        this.sellerUser = sellerUser;
        this.ticketQuantity = ticketQuantity;
        this.price = price;
    }
    
    //for a buy transaction
    DailyTransaction(int code, String eventName, String sellerUser, String buyerUser, int ticketQuantity, double price){
        this.code = code;
        this.eventName = eventName;
        this.sellerUser = sellerUser;
        this.buyerUser = buyerUser;
        this.ticketQuantity = ticketQuantity;
        this.price = price;
    }

    //add credit
    DailyTransaction(int code, String addCredUser, double credit){
        this.code = code;
        this.addCredUser = addCredUser;
        this.credit = credit;
    }

    //refunds
    DailyTransaction(int code, double refund, String refundUser){
        this.code = code;
        this.refundUser = refundUser;
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

    public String getRefundUser(){
        return this.refundUser;
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

