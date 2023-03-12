/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmu.ticketmain;

/**
 *
 * @author mdany
 */
class RefundRequest {

    private String sellerName;
    private String buyerName;
    private double sellerCredits;
    private double buyerCredits;
    private double refundAmount;

    // constructor 
    public RefundRequest(String sellerName, String buyerName, double sellerCredits, double buyerCredits, double refundAmount) {

        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.sellerCredits = sellerCredits;
        this.buyerCredits = buyerCredits;
        this.refundAmount = refundAmount;

    }

    // setters and getters methods for instance variables
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setSellerCredits(double sellerCredits) {
        this.sellerCredits = sellerCredits;
    }

    public double getSellerCredits() {
        return sellerCredits;
    }

    public void setBuyerCredits(double buyerCredits) {
        this.buyerCredits = buyerCredits;
    }

    public double getBuyerCredits() {
        return buyerCredits;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public double getRefundAmount() {
        return refundAmount;
    }
}
