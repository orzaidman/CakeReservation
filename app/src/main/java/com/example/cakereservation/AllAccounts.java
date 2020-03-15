package com.example.cakereservation;

import com.google.gson.Gson;

import java.util.ArrayList;

public class AllAccounts {
private ArrayList < Account> allAccounts;

    public AllAccounts() {
        this.allAccounts = new ArrayList<>();
    }

    public AllAccounts(AllAccounts other) {
        this.allAccounts = other.allAccounts;
    }

    public void add(Account account)
    {
       allAccounts.add(account);
    }
    public ArrayList<Account> getAllAccounts() {
        return allAccounts;
    }

    public AllAccounts(String data) {
        this(createPlayersFromString(data));
    }

    private static AllAccounts createPlayersFromString(String data) {
        AllAccounts tempP;
        if (data == "NA") {
            tempP = new AllAccounts();
        }
        else {
            tempP = new Gson().fromJson(data, AllAccounts.class);
        }
        return tempP;
    }

public int countOfOrders(){
        int count = 0;
    for(int i =0; i< allAccounts.size(); i++){
        count += allAccounts.get(i).getAllOrders().size();
    }
    return count-1;
}
    public int countOfWaitingOrders() {
        int count = 0;
        for (int i = 0; i < allAccounts.size(); i++) {
            for (int j = 0; j < allAccounts.get(i).getAllOrders().size(); j++) {
                if (allAccounts.get(i).getAllOrders().get(j).getStatus() == 1)
                    count++;
            }
        }
    return count;
    }

    public Account getAccountByPhone(String phoneNumber){
        for (Account a: allAccounts) {
            if(a.getUserPhoneNumber().equals(phoneNumber))
                return  a;
        }
        return null;
    }

    public Account getAccountByUUid(String uuid){
        for (Account a: allAccounts) {
            if(a.getUuidAccount().equals(uuid))
                return  a;
        }
        return null;
    }

    public void setAllAccounts(ArrayList<Account> allAccounts) {
        this.allAccounts = allAccounts;
    }

}
