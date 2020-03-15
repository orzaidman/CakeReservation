package com.example.cakereservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Map;


public class FragmentTop extends Fragment {
    public static final String KEY_MSP  = "msp2";
    private ImageButton[] allTopsGui;
    private ImageView details_IMG_first, details_IMG_sec;
    Order myCake;
    private MySheredP msp;
    private  Gson gson = new Gson();
    private View view;
    private ImageButton top_BTN_strawberry, top_BTN_kiwi, top_BTN_pineapple, top_BTN_kinder,top_BTN_hershey, top_BTN_hippo
            ,top_BTN_oreo,top_BTN_click,top_BTN_ferrero,top_BTN_reese,top_BTN_chocolate,top_BTN_kinder2;
    private  Button top_BTN_clear;
    private Account account;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("message");
    private AllAccounts allAccounts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view == null)
            view = inflater.inflate(R.layout.fragment_top,container,false);
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        String strtext = getArguments().getString("edttext");
        msp = new MySheredP(getActivity());

        allAccounts = getFromMSP();

        findViews(view);

        Account accountTemp = new Gson().fromJson(strtext,  Account.class);
        String phone = accountTemp.getUserPhoneNumber();

        account = allAccounts.getAccountByPhone(phone);
        myCake = account.checkOpenOrder();


        allTopsGui = new ImageButton[]{
        view.findViewById(R.id.top_BTN_strawberry),
        view.findViewById(R.id.top_BTN_kiwi),
        view.findViewById(R.id.top_BTN_pineapple),
        view.findViewById(R.id.top_BTN_kinder),
        view.findViewById(R.id.top_BTN_kinder2),
        view.findViewById(R.id.top_BTN_hershey),
        view.findViewById(R.id.top_BTN_hippo),
        view.findViewById(R.id.top_BTN_oreo),
        view.findViewById(R.id.top_BTN_click),
        view.findViewById(R.id.top_BTN_ferrero),
        view.findViewById(R.id.top_BTN_reese),
        view.findViewById(R.id.top_BTN_chocolate)
        };


        clearTops();

        updateTops(myCake.getAllTops());

        if(myCake.getNumber1() !=  " ")
            printChosen(myCake.getNumber1(),1);

        if(myCake.getNumber2() !=  " ")
            printChosen(myCake.getNumber2(),2);

        top_BTN_strawberry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTopInList("strawberry",top_BTN_strawberry );
            }
        });
        top_BTN_kiwi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTopInList("kiwi",top_BTN_kiwi);
            }
        });
        top_BTN_pineapple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTopInList("pineapple",top_BTN_pineapple);
            }
        });
        top_BTN_kinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTopInList("black kinder",top_BTN_kinder);
            }
        });

        top_BTN_kinder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTopInList("white kinder",top_BTN_kinder2);
            }
        });

        top_BTN_hershey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTopInList("hershey",top_BTN_hershey);
            }
        });
        top_BTN_hippo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTopInList("hippo", top_BTN_hippo);
            }
        });
        top_BTN_oreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTopInList("oreo",top_BTN_oreo);
            }
        });
        top_BTN_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTopInList("click",top_BTN_click);

            }
        });
        top_BTN_ferrero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTopInList("ferrero",top_BTN_ferrero);

            }
        });
        top_BTN_reese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTopInList("reese",top_BTN_reese);

            }
        });
        top_BTN_chocolate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTopInList("chocolate",top_BTN_chocolate);

            }
        });

        top_BTN_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCake.clearAllTops();
               putOnMSP();
                clearTops();


            }
        });
        return view;
    }



    private void clearTops()
    {
        for (int i=0; i< allTopsGui.length;i++ )

            allTopsGui[i].setBackgroundResource(0);
    }

    private void updateTops(Map<String, Boolean> allTops) {
        clearTops();
        for (String val: allTops.keySet()) {
           if(allTops.get(val))
               updateButtons(val);
           }
    }
    private void  saveToDB(){
        myRef.child("Users").child(account.getUserPhoneNumber()).setValue(account);

    }
    private void updateButtons(String top) {
    switch (top)
    {
        case ("strawberry"):
            top_BTN_strawberry.setBackgroundResource(R.drawable.my_btn);
            break;

        case ("kiwi"):
            top_BTN_kiwi.setBackgroundResource(R.drawable.my_btn);
            break;

        case ("pineapple"):
            top_BTN_pineapple.setBackgroundResource(R.drawable.my_btn);
            break;

        case ("black kinder"):
            top_BTN_kinder.setBackgroundResource(R.drawable.my_btn);
            break;

        case ("white kinder"):
            top_BTN_kinder2.setBackgroundResource(R.drawable.my_btn);
            break;

        case ("hershey"):
            top_BTN_hershey.setBackgroundResource(R.drawable.my_btn);
            break;

        case ("hippo"):
            top_BTN_hippo.setBackgroundResource(R.drawable.my_btn);
            break;

        case ("oreo"):
            top_BTN_oreo.setBackgroundResource(R.drawable.my_btn);
            break;

        case ("click"):
            top_BTN_click.setBackgroundResource(R.drawable.my_btn);
            break;

        case ("ferrero"):
            top_BTN_ferrero.setBackgroundResource(R.drawable.my_btn);
            break;

        case ("reese"):
            top_BTN_reese.setBackgroundResource(R.drawable.my_btn);
            break;

        case ("chocolate"):
            top_BTN_chocolate.setBackgroundResource(R.drawable.my_btn);

            break;
    }

    }

    private void setTopInList(String top, ImageButton button){
        Boolean temp = myCake.getAllTops().get(top);
        myCake.getAllTops().put(top,!temp);
        putOnMSP();
     updateTops( myCake.getAllTops());
    }

    private void putOnMSP(){
        String json = gson.toJson(allAccounts);
        msp.putString(KEY_MSP,json);
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

    private AllAccounts getFromMSP(){
        String data  = msp.getString(KEY_MSP, "NA");
        allAccounts =  new Gson().fromJson(data,  AllAccounts.class);
        return allAccounts;
    }



    private void printChosen(String chose, int id) {
        if (id == 1) {
            switch (chose) {

                case "0":
                    details_IMG_first.setBackgroundResource(R.drawable.img_zero_last);
                    break;

                case "1":
                    details_IMG_first.setBackgroundResource(R.drawable.img_one_last);
                    break;

                case "2":
                    details_IMG_first.setBackgroundResource(R.drawable.img_two_last);
                    break;

                case "3":
                    details_IMG_first.setBackgroundResource(R.drawable.img_three_last);
                    break;

                case "4":
                    details_IMG_first.setBackgroundResource(R.drawable.img_four_last);
                    break;

                case "5":
                    details_IMG_first.setBackgroundResource(R.drawable.img_five_last);
                    break;

                case "6":
                    details_IMG_first.setBackgroundResource(R.drawable.img_six_last);
                    break;

                case "7":
                    details_IMG_first.setBackgroundResource(R.drawable.img_seven_last);
                    break;

                case "8":
                    details_IMG_first.setBackgroundResource(R.drawable.img_eight_last);
                    break;

                case "9":
                    details_IMG_first.setBackgroundResource(R.drawable.img_nine_last);
                    break;
                default:

            }
        } else {
            switch (chose) {

                case "0":
                    details_IMG_sec.setBackgroundResource(R.drawable.img_zero_last);
                    break;

                case "1":
                    details_IMG_sec.setBackgroundResource(R.drawable.img_one_last);
                    break;

                case "2":
                    details_IMG_sec.setBackgroundResource(R.drawable.img_two_last);
                    break;

                case "3":
                    details_IMG_sec.setBackgroundResource(R.drawable.img_three_last);
                    break;

                case "4":
                    details_IMG_sec.setBackgroundResource(R.drawable.img_four_last);
                    break;

                case "5":
                    details_IMG_sec.setBackgroundResource(R.drawable.img_five_last);
                    break;

                case "6":
                    details_IMG_sec.setBackgroundResource(R.drawable.img_six_last);
                    break;

                case "7":
                    details_IMG_sec.setBackgroundResource(R.drawable.img_seven_last);
                    break;

                case "8":
                    details_IMG_sec.setBackgroundResource(R.drawable.img_eight_last);
                    break;

                case "9":
                    details_IMG_sec.setBackgroundResource(R.drawable.img_nine_last);
                    break;
                default:
            }
        }
    }

    private void findViews(View view) {
         top_BTN_strawberry = view.findViewById(R.id.top_BTN_strawberry);
        top_BTN_kiwi = view.findViewById(R.id.top_BTN_kiwi);
        top_BTN_pineapple = view.findViewById(R.id.top_BTN_pineapple);
        top_BTN_kinder = view.findViewById(R.id.top_BTN_kinder);
        top_BTN_kinder2 = view.findViewById(R.id.top_BTN_kinder2);
        top_BTN_hershey = view.findViewById(R.id.top_BTN_hershey);
        top_BTN_hippo = view.findViewById(R.id.top_BTN_hippo);
        top_BTN_oreo = view.findViewById(R.id.top_BTN_oreo);
        top_BTN_click = view.findViewById(R.id.top_BTN_click);
        top_BTN_ferrero = view.findViewById(R.id.top_BTN_ferrero);
        top_BTN_reese = view.findViewById(R.id.top_BTN_reese);
        top_BTN_chocolate = view.findViewById(R.id.top_BTN_chocolate);
        top_BTN_clear = view.findViewById(R.id.top_BTN_clear);
        details_IMG_first = view.findViewById(R.id.details_IMG_first);
        details_IMG_sec = view.findViewById(R.id.details_IMG_sec);
    }
}
