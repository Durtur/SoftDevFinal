package model;

/**
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class Card {
    private String fullName, expiryDate;
    private long cardNumber;
    private int securityNumber;
    
    public Card(String fullName, String expiryDate, long cardNumber, int securityNumber) {
    	this.fullName = fullName;
    	this.expiryDate = expiryDate;
    	this.cardNumber = cardNumber;
    	this.securityNumber = securityNumber;
    }
        
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

