package com.example.cakereservation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Activity_LogIn extends AppCompatActivity {
    public static final String KEY_OBJECT = "object";
    public static final String KEY_UUID = "uuid";
    public static final String KEY_MSP  = "msp2";

    private LinearLayout login;
    private View view;
    private Button  login_BTN_send_code, login_BTN_signin;
    private FirebaseAuth mAuth;
    private EditText login_EDT_phone, login_EDT_code;
    private String codeSent, uuid;
    private AllAccounts allAccounts =  new AllAccounts();;
    private Account newAccount;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("message");
    private MySheredP msp;
    private  Gson gson = new Gson();
    private  boolean flag = false;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        msp = new MySheredP(this);

        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            isPermissionGranted();
        }


         uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
   //    user.delete();


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
                newAccount = allAccounts.getAccountByUUid(uuid);
                findViewById(R.id.login_loading).setVisibility(View.INVISIBLE);
                putOnMSP();
                if(flag)
                   openActivityLogedIn();


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("or", "Failed to read value.", error.toException());
            }
        });

        findViews(view);
        login_BTN_signin.setEnabled(false);
        login_BTN_send_code.setEnabled(false);

        login_EDT_code.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                login_BTN_signin.setEnabled(true);
                return false;
            }
        });

        login_EDT_phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                login_BTN_send_code.setEnabled(true);
                return false;
            }
        });

        login_BTN_send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                sendVerificationCode();
            }
        });

        login_BTN_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                verifySignInCode();
            }
        });

        if(user!= null){
            login.setVisibility(View.INVISIBLE);
            flag =true;
        }
    }


    private void verifySignInCode() {
        String code = login_EDT_code.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //here you can open new activity
                            newAccount = new Account(bulidPhoneNumber(),uuid);
                            allAccounts.add(newAccount);
                            myRef.child("Users").child(newAccount.getUserPhoneNumber()).setValue(newAccount);
                            putOnMSP();
                            Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_LONG).show();
                            openActivityLogedIn();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private String bulidPhoneNumber() {
        String phone = login_EDT_phone.getText().toString();
        String newPhone = "+972";
        if(phone.charAt(0) != '0')
            newPhone += phone.charAt(0);

        for (int i =1; i<phone.length(); i++)
        {
            newPhone += phone.charAt(i);
        }
        return newPhone;
        }


    private void sendVerificationCode() {
        String phoneT= login_EDT_phone.getText().toString();
        String phone = "+972" + phoneT;

        if(phone.length()<12)
        {
            login_EDT_phone.setText("enter a valid phone number! ");
            login_EDT_phone.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }
    };




    private  void openActivityLogedIn(){
        Account t = new Account();
        if(t.checkManager(newAccount.getUserPhoneNumber()))
            openNewActivityManager();
        else
            openNewActivityClient();
    }

    private void openNewActivityClient() {
        Intent intent = new Intent(this, ActivityMainClient.class);
        String json = gson.toJson(allAccounts);
        Bundle extras = new Bundle();
        extras.putString(KEY_OBJECT,json);
        extras.putString(KEY_UUID,uuid);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }

    private void openNewActivityManager() {
        Intent intent = new Intent(this, ActivityMainManager.class);
        startActivity(intent);
        finish();
    }


    private void findViews(View view) {
        login_BTN_send_code = findViewById(R.id.login_BTN_send_code);
        login_BTN_signin = findViewById(R.id.login_BTN_signin);
        login_EDT_phone = findViewById(R.id.login_EDT_phone);
        login_EDT_code = findViewById(R.id.login_EDT_code);
        login = findViewById(R.id.login);
    }


    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }

    private void refreshList(ArrayList<Account> users) {
        // TODO: 2020-01-08 refresh list
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
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
}