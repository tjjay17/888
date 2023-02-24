
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
    private int ticketQuantity;
    private double price;
    
    TransactionStream(int code, String eventName, String sellerUser, int ticketQuantity, double price){
        this.code = code;
        this.eventName = eventName;
        this.sellerUser = sellerUser;
        this.ticketQuantity = ticketQuantity;
        this.price = price;
    }
}

