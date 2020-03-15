package com.example.cakereservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class ActivityMainClient extends AppCompatActivity {
    public static final String KEY_ORDER = "orderId";

    private Button main_BTN_new_order,main_BTN_my_cakes,main_BTN_check_status;
    private View view;
    private Account account;
    private AllAccounts allAccounts = new AllAccounts();
    private TextView main_TXT_title;
    private String uuid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        allAccounts =  new Gson().fromJson(extras.getString(Activity_LogIn.KEY_OBJECT),AllAccounts.class);
        uuid = extras.getString(Activity_LogIn.KEY_UUID);

       account = allAccounts.getAccountByUUid(uuid);

        setView(view);



        main_BTN_new_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openNewActivityOrder();
            }
        });

        main_BTN_my_cakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivityGallery();
            }
        });

        main_BTN_check_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivityViewStatus();
            }
        });
    }

    private void openNewActivityViewStatus() {
        Intent intent = new Intent(this, ActivityAccountOrderList.class);
        String s = new Gson().toJson(account);
        intent.putExtra(KEY_ORDER, s);
        finish();
        startActivity(intent);
    }

    private void openNewActivityGallery() {
        Intent intent = new Intent(this, FragmentGallery.class);
        finish();
        startActivity(intent);
    }

    private void openNewActivityOrder() {
        Intent intent = new Intent(this, ActivityMakeOrder.class);
            String s = new Gson().toJson(account);
            intent.putExtra(KEY_ORDER, s);
        finish();
        startActivity(intent);
    }




    private void setView(View view)
    {
        main_BTN_new_order= findViewById(R.id.main_BTN_new_order);
        main_BTN_my_cakes= findViewById(R.id.main_BTN_my_cakes);
        main_BTN_check_status= findViewById(R.id.main_BTN_check_status);
        main_TXT_title= findViewById(R.id.main_TXT_title);
    }
}
