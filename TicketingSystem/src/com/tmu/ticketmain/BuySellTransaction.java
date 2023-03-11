package com.tmu.ticketmain;

public class BuySellTransaction {
    int code;
    String eventName;
    String sellerUser;
    String buyerUser;
    int ticketQuantity;
    double price;

    BuySellTransaction(int code, String eventName, String buyerUser, String sellerUser, int ticketQuantity, double price){
        this.code = code;
        this.eventName = eventName;
        this.sellerUser = sellerUser;
        this.buyerUser = buyerUser;
        this.ticketQuantity = ticketQuantity;
        this.price = price;
    }
}
