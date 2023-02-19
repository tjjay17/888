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
    String eventName;
    String ticketID;
    int ticketPrice;
    String ticketSeller;
    
    public Ticket(String eventName, String ticketID, int ticketPrice, String ticketSeller){
        this.eventName = eventName;
        this.ticketID = ticketID;
        this.ticketPrice = ticketPrice;
        this.ticketSeller = ticketSeller;
    }
}
