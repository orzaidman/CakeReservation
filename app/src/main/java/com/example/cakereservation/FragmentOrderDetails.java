package com.example.cakereservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FragmentOrderDetails extends Fragment {
    public static final String KEY_JSON = "json6";
    public static final String KEY_MSP  = "msp2";
    public static final String KEY_OBJECT = "object";
    public static final String KEY_UUID = "uuid";

    private View view;
    private MySheredP msp;
    Gson gson = new Gson();
    private ListView details_LIST_all;
    private List list = new ArrayList();
    private ArrayAdapter arrayAdapter;
    private Order myOrder;
    private Button details_BTN_send;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("message");
    private CalendarView details_DATE_calendar;
    private TextView details_TXT_price;
    private AllAccounts allAccounts;
    private Account account;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_numbers, container, false);


        View view = inflater.inflate(R.layout.fragment_details, container, false);
        String strtext = getArguments().getString("edttext");
        msp = new MySheredP(getActivity());

        allAccounts = getFromMSP();

        findViews(view);
        final String data = msp.getString(KEY_JSON, "NA");

        Account accountTemp = new Gson().fromJson(strtext,  Account.class);
        String phone = accountTemp.getUserPhoneNumber();

        account = allAccounts.getAccountByPhone(phone);
        myOrder = account.checkOpenOrder();
        details_BTN_send.setEnabled(false);
        copyList();

        arrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, list);
        details_LIST_all.setAdapter(arrayAdapter);


        details_TXT_price.setText("Total price: "+  myOrder.getPrice().getTotalPrice());
        details_BTN_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myOrder.updateStatus(myOrder);
                myOrder.updatePrice(myOrder);
                putOnDB();
                putOnMSP();
                   openNewActivityMain();
            }
        });


        details_DATE_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,int date) {

                Date check = new Date(year, month - 1, date, 0, 0);
                Date today = new Date();
                if(check.before(today)) {
                    Toast.makeText(getActivity(), "invalid date", Toast.LENGTH_LONG).show();
                    details_BTN_send.setEnabled(false);
                } else {
                    myOrder.setReservationDate(date + "-" + (month + 1)+"");
                    details_BTN_send.setEnabled(true);
                }
            }
        });

        return view;
    }

    private boolean checkValidDate(int date, int month, int year) {
       String cur = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault()).format(new Date());
        String arrCur[] = cur.split("-");
     if(arrCur[2].compareTo(year+"")==1)
         return false;
     if((arrCur[1].compareTo(month+"")==0)&&(arrCur[0].compareTo(date+"")==-1))
            return false;

     return  true;

    }

    private boolean checkTheDate(int dayToAdd, int monthToAdd, int yearToAdd) {
        Date date = null;
        String dtStart = yearToAdd+"/"+monthToAdd+"/"+dayToAdd;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            date = format.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = new Date().getTime() - date.getTime();
        if(diff > 0 )
            return false;

        return true;
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

    private void openNewActivityMain() {
        Intent intent = new Intent(getActivity(), ActivityMainClient.class);
        String json = gson.toJson(allAccounts);
        Bundle extras = new Bundle();
        extras.putString(KEY_OBJECT,json);
        extras.putString(KEY_UUID,account.getUuidAccount());
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void findViews(View view) {
        details_LIST_all = view.findViewById(R.id.details_LIST_all);
        details_TXT_price = view.findViewById(R.id.details_TXT_price);
        details_BTN_send = view.findViewById(R.id.details_BTN_send);
        details_DATE_calendar = view.findViewById(R.id.details_DATE_calendar);

    }

    private void copyList() {
        int check =0;
        for (String value: myOrder.getAllTops().keySet()) {
            if(myOrder.getAllTops().get(value)) {
                list.add(value);
                check++;
            }
        }
        if(check == 0)
            list.add("No Tops");
    }


    private void putOnMSP(){
        String json = gson.toJson(allAccounts);
        msp.putString(KEY_MSP,json);
    }

    private AllAccounts getFromMSP(){
        String data  = msp.getString(KEY_MSP, "NA");
        allAccounts =  new Gson().fromJson(data,  AllAccounts.class);
        return allAccounts;
    }

}