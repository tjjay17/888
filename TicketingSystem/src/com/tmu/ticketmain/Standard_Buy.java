package com.tmu.ticketmain;

public class Standard_Buy extends User{
    private String userType;
    private String username;
    private double credit;

    public Standard_Buy(String usertype, String username, double credit) {
        super(usertype, username, credit);
    }

}
