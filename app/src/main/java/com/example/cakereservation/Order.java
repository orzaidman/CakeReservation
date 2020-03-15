package com.example.cakereservation;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Order {
    final int IN_EDITING = 0, WAITING_FOR_APPLY = 1,IN_PROGRESS = 2,DONE= 3;
    private String number1;
    private String number2;
    private Map<String, Boolean> allTops;
    private int status;
    private String userID;
    private Price price;
    private String currentDate;
    private String reservationDate;

    public Order(Order other) {
        this.number1 = other.number1;
        this.number2 = other.number2;
        this.allTops = other.allTops;
        this.status = other.status;
        this.userID = other.userID;
        this.price = other.price;
        this.currentDate = other.currentDate;
        this.reservationDate = other.reservationDate;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Order(String data) {
        this(createPlayersFromString(data));
    }

    public void updateStatus(Order order){
        if(order.getStatus() < DONE)
        order.setStatus(order.getStatus()+1);
    }

    public void updatePrice(Order order){
        setPrice(new Price(order));
    }

    public Order() {
        this.number1 = "0";
        this.number2 = " ";
        this.status = IN_EDITING;
        this.price = new Price(this);
        this.reservationDate = "NA";
        this.currentDate = new SimpleDateFormat("dd-MM", Locale.getDefault()).format(new Date());
        this.allTops = new HashMap<String, Boolean>();
        allTops.put("strawberry",false);
        allTops.put("kiwi",false);
        allTops.put("pineapple",false);
        allTops.put("black kinder",false);
        allTops.put("white kinder",false);
        allTops.put("hershey",false);
        allTops.put("hippo",false);
        allTops.put("oreo",false);
        allTops.put("click",false);
        allTops.put("ferrero",false);
        allTops.put("reese",false);
        allTops.put("chocolate",false);

    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    private static Order createPlayersFromString(String data) {
        Order tempP;
        if (data == "NA") {
            tempP = new Order();
        }
        else {
            tempP = new Gson().fromJson(data, Order.class);
        }
        return tempP;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public void clearAllTops(){
        allTops.put("strawberry",false);
        allTops.put("kiwi",false);
        allTops.put("pineapple",false);
        allTops.put("black kinder",false);
        allTops.put("white kinder",false);
        allTops.put("hershey",false);
        allTops.put("hippo",false);
        allTops.put("oreo",false);
        allTops.put("click",false);
        allTops.put("ferrero",false);
        allTops.put("reese",false);
        allTops.put("chocolate",false);
    }

    public void  addOneToNumber1(){
        int x = Integer.parseInt(number1);
        if(x <9)
            x++;
        setNumber1(x+"");

    }
    public void  addOneToNumber2(){
        int x;
        if(number2.equals(" "))
            x = -1;
        else
         x = Integer.parseInt(number2);
        if(x <9)
            x++;
        setNumber2(x+"");
        updatePrice(this);
    }

    public void  subOneToNumber1(){
        int x = Integer.parseInt(number1);
        if(x >0)
            x--;
        setNumber1(x+"");
    }

    public void  subOneToNumber2(){

        if(number2.equals(" ")){
            updatePrice(this);
            return;
        }

        int x = Integer.parseInt(number2);
        if(x >0) {
            x--;
            setNumber2(x + "");

        }
        else
            setNumber2(" ");
        updatePrice(this);
    }

    public String getNumber1() {
        return number1;
    }

    public void setNumber1(String number1) {
        this.number1 = number1;
    }

    public String getNumber2() {
        return number2;
    }

    public void setNumber2(String number2) {
        this.number2 = number2;
    }


    public Map<String, Boolean> getAllTops() {
        return allTops;
    }

    public void setAllTops(Map<String, Boolean> allTops) {
        this.allTops = allTops;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
