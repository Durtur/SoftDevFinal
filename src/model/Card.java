/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Unnur Bjarnadóttir <uab1@hi.is>
 */
public class Card {
    private String fullName, expiryDate;
    private long cardNumber;
    private int securityNumber;
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public long getCardNumber() {
        return cardNumber;
    }
    
    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    public int getSecurityNumber() {
        return securityNumber;
    }
    
    public void setSecurityNumber(int securityNumber) {
        this.securityNumber = securityNumber;
    }
}

