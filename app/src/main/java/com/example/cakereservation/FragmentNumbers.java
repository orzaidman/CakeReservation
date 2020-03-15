package com.example.cakereservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;


public class FragmentNumbers extends Fragment {
    public static final String KEY_MSP  = "msp2";
    
    private  View view;
    private ImageView numbers_IMG_first,numbers_IMG_sec;
    private Button numbers_BTN_up1, numbers_BTN_up2, numbers_BTN_down1,numbers_BTN_down2;
    private Order myCake;
    private Account account;
    private MySheredP msp;
    private  Gson gson = new Gson();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("message");
    private AllAccounts allAccounts;
    private ProgressBar numbers_loading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_numbers, container, false);

        String strtext = getArguments().getString("edttext");

        msp = new MySheredP(getActivity());
        findViews(view);
        allAccounts = getFromMSP();

        Account accountTemp = new Gson().fromJson(strtext, Account.class);
        String phone = accountTemp.getUserPhoneNumber();
        account = allAccounts.getAccountByPhone(phone);
        myCake = account.checkOpenOrder();

        if (myCake == null) {
            myCake = new Order();
            allAccounts.getAccountByPhone(phone).addOrder(myCake);
        }
    printChosen(myCake.getNumber1(), 1);

        if(myCake.getNumber2()!= " ") {
            numbers_IMG_sec.setVisibility(view.VISIBLE);
            printChosen(myCake.getNumber2(), 2);

        }
        else
            numbers_IMG_sec.setVisibility(view.INVISIBLE);



        numbers_BTN_up1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               myCake.addOneToNumber1();
                printChosen(myCake.getNumber1(),1);
                putOnMSP();
            }
        });

        numbers_BTN_up2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCake.addOneToNumber2();
             if(!myCake.getNumber2().equals(" "))
                 numbers_IMG_sec.setVisibility(view.VISIBLE);
             else
                 numbers_IMG_sec.setVisibility(view.INVISIBLE);
                 printChosen(myCake.getNumber2(),2);
                putOnMSP();
            }
        });

        numbers_BTN_down1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               myCake.subOneToNumber1();
                printChosen(myCake.getNumber1(),1);
                putOnMSP();
            }
        });

        numbers_BTN_down2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCake.subOneToNumber2();
                if(myCake.getNumber2().equals(" "))
                    numbers_IMG_sec.setVisibility(view.INVISIBLE);
                else {
                    numbers_IMG_sec.setVisibility(view.VISIBLE);
                    printChosen(myCake.getNumber2(), 2);
                }
                putOnMSP();
            }

        });
        return view;
    }



    private void printChosen(String chose, int id) {
        switch(chose) {
            case "0":
                if(id == 1) {
                    numbers_IMG_first.setImageResource(R.drawable.img_zero_first);
                    myCake.setNumber1("0");

                }else {
                    numbers_IMG_sec.setImageResource(R.drawable.img_zero_first);
                    myCake.setNumber2("0");

                }
                    break;

            case "1":
                if(id == 1) {
                    numbers_IMG_first.setImageResource(R.drawable.img_one_first);
                    myCake.setNumber1("1");
                }  else {
                    numbers_IMG_sec.setImageResource(R.drawable.img_one_first);
                    myCake.setNumber2("1");
                }
                break;
            case "2":
                if(id == 1) {
                    numbers_IMG_first.setImageResource(R.drawable.img_two_first);
                    myCake.setNumber1("2");
                }else {
                    numbers_IMG_sec.setImageResource(R.drawable.img_two_first);
                    myCake.setNumber2("2");
                }
                 
                break;
            case "3":
                if(id == 1) {
                    numbers_IMG_first.setImageResource(R.drawable.img_three_first);
                    myCake.setNumber1("3");
                }else {
                    numbers_IMG_sec.setImageResource(R.drawable.img_three_first);
                    myCake.setNumber2("3");
                     
                }break;
            case "4":
                if(id == 1) {
                    numbers_IMG_first.setImageResource(R.drawable.img_four_first);
                    myCake.setNumber1("4");
                }else {
                    numbers_IMG_sec.setImageResource(R.drawable.img_four_first);
                    myCake.setNumber2("4");
                }
                 
                break;

            case "5":
                if(id == 1) {
                    numbers_IMG_first.setImageResource(R.drawable.img_five_first);
                    myCake.setNumber1("5");
                }else {
                    numbers_IMG_sec.setImageResource(R.drawable.img_five_first);
                    myCake.setNumber2("5");
                }
                 
                break;

            case "6":
                if(id == 1) {
                    numbers_IMG_first.setImageResource(R.drawable.img_six_first);
                    myCake.setNumber1("6");
                }else {
                    numbers_IMG_sec.setImageResource(R.drawable.img_six_first);
                    myCake.setNumber2("6");
                }
                 
                break;
            case "7":
                if(id == 1) {
                    numbers_IMG_first.setImageResource(R.drawable.img_seven_first);
                    myCake.setNumber1("7");
                }else {
                    numbers_IMG_sec.setImageResource(R.drawable.img_seven_first);
                    myCake.setNumber2("7");
                }
                 
                break;
            case "8":
                if(id == 1) {
                    numbers_IMG_first.setImageResource(R.drawable.img_eight_first);
                    myCake.setNumber1("8");
                }else {
                    numbers_IMG_sec.setImageResource(R.drawable.img_eight_first);
                    myCake.setNumber2("8");
                }
                 
                break;
            case "9":
                if(id == 1) {
                    numbers_IMG_first.setImageResource(R.drawable.img_nine_first);
                    myCake.setNumber1("9");
                }else {
                    numbers_IMG_sec.setImageResource(R.drawable.img_nine_first);
                    myCake.setNumber2("9");
                }
                 
                break;
            default:

        }
    }


    private void findViews(View view) {
        numbers_IMG_first = view.findViewById(R.id.numbers_IMG_first);
        numbers_IMG_sec = view.findViewById(R.id.numbers_IMG_sec);
        numbers_BTN_up1 = view.findViewById(R.id.numbers_BTN_up1);
        numbers_BTN_up2= view.findViewById(R.id.numbers_BTN_up2);
        numbers_BTN_down1 = view.findViewById(R.id.numbers_BTN_down1);
        numbers_BTN_down2 = view.findViewById(R.id.numbers_BTN_down2);
        numbers_loading = view.findViewById(R.id.numbers_loading);
    }

private void  saveToDB(){
    myRef.child("Users").child(account.getUserPhoneNumber()).setValue(account);

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
    public void onStop() {
        putOnMSP();
        putOnDB();
        super.onStop();
    }


    private void putOnDB() {
        String phone = account.getUserPhoneNumber();
        myRef.child("Users").child(phone).setValue(account);
    }
}

