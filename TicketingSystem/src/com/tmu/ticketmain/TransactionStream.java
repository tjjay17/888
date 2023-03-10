
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
public class TransactionStream {
    private int code;
    private String eventName;
    private String sellerUser;
    private String buyerUser;
    private int ticketQuantity;
    private double price;
    
    TransactionStream(int code, String eventName, String sellerUser, String buyerUser, int ticketQuantity, double price){
        this.code = code;
        this.eventName = eventName;
        this.sellerUser = sellerUser;
        this.buyerUser = buyerUser;
        this.ticketQuantity = ticketQuantity;
        this.price = price;
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

}

