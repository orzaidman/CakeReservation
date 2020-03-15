package com.example.cakereservation;

import java.util.ArrayList;

public class Account {
    final String MANAGER = "+972523976905";
    final int MANAGER_PERMISSION = 0, CLIENT_PERMISSION = 1;
    final int IN_EDITING = 0, WAITING_FOR_APPLY = 1,IN_PROGRESS = 2,DONE= 3;

    private String userPhoneNumber;
    private int permission;
    private ArrayList<Order> allOrders;
    private String uuidAccount;

    public Account() {
    }

    public Account(String userPhoneNumber, String uuidAccount) {
        this.uuidAccount = uuidAccount;
        this.userPhoneNumber = userPhoneNumber;
        if(userPhoneNumber.equals(MANAGER))
            permission = MANAGER_PERMISSION;
        else {
            permission = CLIENT_PERMISSION;
            allOrders = new ArrayList<Order>();

            addOrder(new Order());
        }
    }

    public boolean checkManager(String phone){
        if(phone.equals(MANAGER ))
            return true;
        return false;
    }
    public void setAllOrders(ArrayList<Order> allOrders) {
        this.allOrders = allOrders;
    }

    public String getUuidAccount() {
        return uuidAccount;
    }

    public void setUuidAccount(String uuidAccount) {
        this.uuidAccount = uuidAccount;
    }

    public Order checkOpenOrder(){
        for (Order order: allOrders) {
            if(order.getStatus() == IN_EDITING)
                return  order;
        }
        return null;
    }

    public void addOrder(Order orderToAdd){
        allOrders.add(orderToAdd);
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public ArrayList<Order> getAllOrders() {
        return allOrders;
    }

}
