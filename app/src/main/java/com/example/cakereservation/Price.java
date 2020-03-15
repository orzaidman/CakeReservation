package com.example.cakereservation;

public class Price {
    public static final int FIRST_LETTER_PRICE = 150;
    public static final int SEC_LETTER_PRICE = 50;
    private int totalPrice;

    public Price() {
        this.totalPrice = 0;
    }

    public Price(Order myCake) {

           this.totalPrice += FIRST_LETTER_PRICE;
        if(! myCake.getNumber2().equals(" "))
            this.totalPrice += SEC_LETTER_PRICE;
    }



    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
