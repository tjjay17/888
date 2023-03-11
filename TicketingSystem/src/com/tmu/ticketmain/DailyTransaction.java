
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
    private String buyerUser;
    //for add credit transaction
    private String addCredUser;
    private String userType;
    //general user is used for create/delete transactions
    private String generalUser;

    private int ticketQuantity;
    private double price;
    private double credit;
    private double refund;
    
    //for a sell or buy
    DailyTransaction(int code, String eventName, String transUser, int ticketQuantity, double price){
        this.code = code;
        this.eventName = eventName;
        this.ticketQuantity = ticketQuantity;
        this.price = price;
        this.sellerUser = transUser;

        // if(code == 3){
        //     this.buyerUser = transUser;
        // }else if(code == 4){
        //     this.sellerUser = transUser;
        // }
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
    DailyTransaction(int code, String generalUser, String userType, double credit){
        this.code = code;
        this.generalUser = generalUser;
        this.userType = userType;
        this.credit = credit;
    }

    //create user transaction
    DailyTransaction(int code, String userName, double credit, String userType){
        this.code = code;
        this.generalUser = userName;
        this.credit = credit;
        this.userType = userType;
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

    public String getGenUser(){
        return this.generalUser;
    }

}

