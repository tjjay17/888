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
public class Ticket {

    // instance variables   
    String eventName;
    String sellerUsername;
    int ticketsinStock;
    double ticketPrice;
    
    // constructor
    public Ticket(String eventName, String sellerUsername, int ticketsinStock, double ticketPrice){
        this.eventName = eventName;
        this.sellerUsername = sellerUsername;
        this.ticketPrice = ticketPrice;
        this.ticketsinStock = ticketsinStock;
    }
    
    // setters and getters methods for instance variables

    public static String formatEventName(String eventName){
        while(eventName.length() < 26){
            eventName = eventName + " ";
        }

        return eventName;
    }

    public static String formatTicketQuantity(int ticketQuantity){
        String ticketStr = Integer.toString(ticketQuantity);
        while(ticketStr.length() < 3){
            ticketStr = "0" + ticketStr;
        }

        return ticketStr + " ";
    }

    public static String formatSellerName(String sellerName){
        while(sellerName.length() < 15){
            sellerName = sellerName + " ";
        }

        return sellerName;
    }

    public static String formatSellerPrice(double price){
        String priceStr = Double.toString(price);
        while(priceStr.length() < 6){
            priceStr = priceStr + "0";
        }

        return priceStr;

    }


    
    public void setEventName (String eventName){
        this.eventName = eventName;
    }
    
    public String getEventName () {
        return eventName;
    }
    
    public void setSellerUsername(String sellerUsername){
        this.sellerUsername = sellerUsername;
    }
    
    public String getSellerUsername(){
        return sellerUsername;
    }
    
    public void setTicketsinStock(int ticketsinStock){
        this.ticketsinStock = ticketsinStock;  
    }
    
    public int getTicketsinStock(){
        return ticketsinStock;
    }
    
    public void setTicketPrice(){
        this.ticketPrice = ticketPrice;
    }
    
    public double getTicketPrice(){
        return ticketPrice;
    }
}