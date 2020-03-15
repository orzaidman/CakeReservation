package com.example.cakereservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class ActivityMakeOrder extends AppCompatActivity {
    public static final String KEY_MSP  = "msp2";
    public static final String KEY_OBJECT = "object";
    public static final String KEY_UUID = "uuid";

    private FrameLayout order_FRAME_main;
    private View view;
    private Button order_BTN_top,order_BTN_numbers,order_BTN_details;
    private Order order;
    private Account account;
    private AllAccounts allAccounts;
    private MySheredP msp;
    private  Gson gson = new Gson();
    private ProgressBar order_loading;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("message");
    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        msp = new MySheredP(this);
        s =  getIntent().getStringExtra(ActivityMainClient.KEY_ORDER);
        findViews(view);



        order_BTN_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTop();
            }
        });

        order_BTN_numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                     showNumbers();
            }
        });

        order_BTN_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetails();
            }
        });

        account =  new Gson().fromJson(s,  Account.class);
        String tempPhoneNumber = account.getUserPhoneNumber();
        allAccounts = getFromMSP();


        account = allAccounts.getAccountByPhone(tempPhoneNumber);
        Order orderT = account.checkOpenOrder();
        if( orderT != null)
            order =orderT;

        else{
            order = new Order();
            account.addOrder(order);
        }
        showNumbers();
    }

    public void initButtons(){
        order_BTN_details.setBackgroundResource(R.drawable.checklist);
        order_BTN_numbers.setBackgroundResource(R.drawable.idea);
        order_BTN_top.setBackgroundResource(R.drawable.candy);

    }

    @Override
    public void onBackPressed() {
        putOnMSP();
        Intent intent = new Intent(this, ActivityMainClient.class);
        String json = gson.toJson(allAccounts);
        Bundle extras = new Bundle();
        extras.putString(KEY_OBJECT,json);
        extras.putString(KEY_UUID,account.getUuidAccount());
        intent.putExtras(extras);
        startActivity(intent);
        finish();
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

    private void showTop() {
        initButtons();
        order_BTN_top.setBackgroundResource(R.drawable.candynew);
        Bundle bundle = new Bundle();
        bundle.putString("edttext", s);

        FragmentTop topFragment = new FragmentTop();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.order_FRAME_main,topFragment);
        topFragment.setArguments(bundle);

        transaction.commit();
    }

    @Override
    protected void onStop() {
        findViewById(R.id.order_loading).setVisibility(View.VISIBLE);
        putOnMSP();
        putOnDB();
        findViewById(R.id.order_loading).setVisibility(View.GONE);
        super.onStop();
    }

    private void putOnDB() {
        String phone = account.getUserPhoneNumber();
        myRef.child("Users").child(phone).setValue(account);
    }

    private void showNumbers() {
        initButtons();
        order_BTN_numbers.setBackgroundResource(R.drawable.ideanew);

        Bundle bundle = new Bundle();
        bundle.putString("edttext", s);

        FragmentNumbers numbersFragment = new FragmentNumbers();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.order_FRAME_main,numbersFragment);
        numbersFragment.setArguments(bundle);
        transaction.commit();
    }

    private void showDetails() {
        initButtons();
        order_BTN_details.setBackgroundResource(R.drawable.checklistnew);
        Bundle bundle = new Bundle();
        bundle.putString("edttext", s);

        FragmentOrderDetails detailsFragment = new FragmentOrderDetails();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.order_FRAME_main,detailsFragment);
        detailsFragment.setArguments(bundle);
        transaction.commit();
    }




    private void findViews(View view) {
        order_FRAME_main = findViewById(R.id.order_FRAME_main);
        order_BTN_numbers = findViewById(R.id.order_BTN_numbers);
        order_BTN_top = findViewById(R.id.order_BTN_top);
        order_BTN_details = findViewById(R.id.order_BTN_details);
        order_loading = findViewById(R.id.order_loading);
    }
}
