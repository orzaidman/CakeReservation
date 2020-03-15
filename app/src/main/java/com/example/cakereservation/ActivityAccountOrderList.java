package com.example.cakereservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.aloj22.listenerswiperefreshlayout.ListenerSwipeRefreshLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ActivityAccountOrderList extends AppCompatActivity {
    public static final String KEY_OBJECT = "object";
    public static final String KEY_UUID = "uuid";
    public static final String KEY_ORDER = "orderId";
    public static final String KEY_MSP  = "msp2";

    private AllAccounts allAccounts;
    private MySheredP msp;
    private View view;
    private Gson gson = new Gson();
    private String s;
    private Account account;
    private ListView  order_LIST_all_orders;

    private List list = new ArrayList();
    private ArrayAdapter arrayAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("message");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_order_list);



        s =  getIntent().getStringExtra(ActivityMainClient.KEY_ORDER);
        msp = new MySheredP(this);
        findViews(view);
        allAccounts = getFromMSP();



        Account accountTemp = new Gson().fromJson(s, Account.class);
        account = accountTemp;
        copyList();

        arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.mylist, list);
        order_LIST_all_orders.setAdapter(arrayAdapter);


        ListenerSwipeRefreshLayout swipeRefreshLayout = (ListenerSwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new ListenerSwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getFromDB();
            }

            @Override
            public void onRefreshStarted() {

            }

            @Override
            public void onRefreshCanceled() {

            }

            @Override
            public void onRefreshFinished() {

            }

        });
    }

    private void openNewActivityMain() {
        Intent intent = new Intent(this, ActivityMainClient.class);
        String json = gson.toJson(allAccounts);
        Bundle extras = new Bundle();
        extras.putString(KEY_OBJECT,json);
        extras.putString(KEY_UUID,account.getUuidAccount());
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }

    private void copyList() {
        list.add("Cur. Date | Res. Date |  Pattern  |  Price   |   Status");
        for (Order order: account.getAllOrders()) {
            if (!((order.getNumber1().equals('0'))&& (order.getNumber2().equals(' '))))
                list.add(printOrder(order));
        }

    }



    private String printOrder(Order order) {
        String res ="";
        res =  "   "+order.getCurrentDate() + "   |   "+ order.getReservationDate() + "   |    " + order.getNumber1()+"";
        res += order.getNumber2().equals(" ")? " ": order.getNumber2();
        res +=  "     |  "+  order.getPrice().getTotalPrice() + "₪  |   "+ printStatus(order.getStatus());
        return res;
    }
    private void openNewActivityRefresh() {
        list.clear();
        arrayAdapter.clear();
        Intent intent = new Intent(this, ActivityAccountOrderList.class);
        String s = new Gson().toJson(account);
        intent.putExtra(KEY_ORDER, s);
        finish();
        startActivity(intent);
    }

    private void getFromDB() {
        allAccounts.getAllAccounts().clear();
        // Read from the database
        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Account tempNewAccount = ds.getValue(Account.class);
                        allAccounts.add(tempNewAccount);
                    }

                }
                String temp = account.getUuidAccount();
                account = allAccounts.getAccountByUUid(temp);
                openNewActivityRefresh();


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("or", "Failed to read value.", error.toException());
            }
        });
    }


    private  String printStatus(int x) {
        switch (x) {
            case (0):
                return "Editing";
            case (1):
                return "Waiting";
            case(2):
                return "progress";
            case(3):
                return "Done";
        }
        return "NA";
    }
    private void findViews(View view) {
        order_LIST_all_orders = findViewById(R.id.order_LIST_all_orders);
    }

    private void putOnMSP(){
        String json = gson.toJson(allAccounts);
        msp.putString(KEY_MSP,json);
    }

    private AllAccounts getFromMSP(){
        String data  = msp.getString(KEY_MSP, "NA");
        allAccounts = new AllAccounts(data);
        return allAccounts;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityMainClient.class);
        String json = gson.toJson(allAccounts);
        Bundle extras = new Bundle();
        extras.putString(KEY_OBJECT,json);
        extras.putString(KEY_UUID,account.getUuidAccount());
        intent.putExtras(extras);
        finish();
        startActivity(intent);
    }
}
