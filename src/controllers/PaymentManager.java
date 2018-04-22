package controllers;

import java.util.Objects;
import model.Card;

/**
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class PaymentManager {
    
    /**
     * @param cardInfo
     * @return true if inputs into Card are valid (to a certain extent) 
     */
    
    public boolean processPayment(Card cardInfo) {
        boolean success = false;
        String cardNumberString;
        cardNumberString = Long.toString(cardInfo.getCardNumber());
        int securityNum;
        securityNum = cardInfo.getSecurityNumber();
        if (Objects.equals(cardInfo.getFullName(), "") && 
                Objects.equals(cardInfo.getExpiryDate(), "") && 
                cardNumberString.length() == 16 && 
                securityNum == 3) {
            success = true;
        }
        return success;
    }
}
