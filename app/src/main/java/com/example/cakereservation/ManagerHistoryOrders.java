package com.example.cakereservation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
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

public class ManagerHistoryOrders extends AppCompatActivity {
    private AllAccounts allAccounts = new AllAccounts();
    private View view1;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("message");
    private Gson gson = new Gson();
    private ListView manager_history_LIST_waiting;
    private ListView manager_history_LIST_past;
    private List listWaiting = new ArrayList();
    private List listPast = new ArrayList();
    private ArrayAdapter arrayAdapterWaiting;
    private ArrayAdapter arrayAdapterPast;
    private Order orderToApply;
    private Order orderToReady;
    private Account accountToApply;
    private int chosenOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_hestory_orders);



        findViews(view1);
         getFromDB();

         manager_history_LIST_past.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                 chosenOrder = --pos;
                 orderToReady = findOrderPast(chosenOrder);

                 AlertDialog alertDialog = new AlertDialog.Builder(ManagerHistoryOrders.this).create();
                 alertDialog.setTitle("Done");
                 alertDialog.setMessage(" Are you sure you want to confirm that the order is ready? ");
                 alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                         new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 orderToReady.updateStatus(orderToReady);
                         myRef.child("Users").child(accountToApply.getUserPhoneNumber()).setValue(accountToApply);
                          clear();

                         copyList(1);
                         arrayAdapterPast = new ArrayAdapter(getApplicationContext(), R.layout.mylist, listPast);
                         manager_history_LIST_past.setAdapter(arrayAdapterPast);
                             }
                         });

                 alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel",
                         new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 dialog.dismiss();
                             }
                         });

                 alertDialog.show();
             }

         });

        manager_history_LIST_waiting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                chosenOrder = --pos;

                AlertDialog alertDialog = new AlertDialog.Builder(ManagerHistoryOrders.this).create();
                alertDialog.setTitle("Apply");
                alertDialog.setMessage("Are you sure you want to apply this order ? ");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                       orderToApply = findOrder(chosenOrder);
                                orderToApply.updateStatus(orderToApply);
                        myRef.child("Users").child(accountToApply.getUserPhoneNumber()).setValue(accountToApply);
                        clear();

                        copyList(1);
                        arrayAdapterWaiting = new ArrayAdapter(getApplicationContext(), R.layout.mylist, listWaiting);
                        arrayAdapterPast = new ArrayAdapter(getApplicationContext(), R.layout.mylist, listPast);

                        manager_history_LIST_waiting.setAdapter(arrayAdapterWaiting);
                        manager_history_LIST_past.setAdapter(arrayAdapterPast);
                        dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.show();
            }
        });






        ListenerSwipeRefreshLayout swipeRefreshLayout = (ListenerSwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new ListenerSwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                openNewActivityRefresh();
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


    @Override
    public void onBackPressed() {
        openNewActivityManager();
    }

    public void clear(){
        listPast.clear();
        listWaiting.clear();
        arrayAdapterWaiting.clear();
        arrayAdapterPast.clear();
    }

    private Order findOrder(int pos) {
        int cur = 0;
        for (int i = 0; i < allAccounts.getAllAccounts().size(); i++) {
            if (allAccounts.getAllAccounts().get(i).getPermission() == 1) {
                for (int j = 0; j < allAccounts.getAllAccounts().get(i).getAllOrders().size(); j++) {
                    if (allAccounts.getAllAccounts().get(i).getAllOrders().get(j).getStatus() == 1) {
                        if (cur == pos) {
                            accountToApply = allAccounts.getAllAccounts().get(i);
                            return allAccounts.getAllAccounts().get(i).getAllOrders().get(j);
                        }
                        cur++;
                    }
                }
            }
        }
        return null;
    }

    private Order findOrderPast(int pos) {
        int cur = 0;
        for (int i = 0; i < allAccounts.getAllAccounts().size(); i++) {
            if (allAccounts.getAllAccounts().get(i).getPermission() == 1) {
                for (int j = 0; j < allAccounts.getAllAccounts().get(i).getAllOrders().size(); j++) {
                    if (allAccounts.getAllAccounts().get(i).getAllOrders().get(j).getStatus() >= 2 ) {
                        if (cur == pos) {
                            accountToApply = allAccounts.getAllAccounts().get(i);
                            return allAccounts.getAllAccounts().get(i).getAllOrders().get(j);
                        }
                        cur++;
                    }
                }
            }
        }
        return null;
    }


    private void copyList(int index) {
        if(index != 2)
         listWaiting.add("Cur. Date | Res. Date |  Pattern  |  Price  |   Status");
        listPast.add("Cur. Date | Res. Date |  Pattern  |  Price  |   Status");
        for (int i = 0; i < allAccounts.getAllAccounts().size(); i++) {
            if (allAccounts.getAllAccounts().get(i).getPermission() == 1) {
                for (int j = 0; j < allAccounts.getAllAccounts().get(i).getAllOrders().size(); j++) {
                    if (allAccounts.getAllAccounts().get(i).getAllOrders().get(j).getStatus() == 1)
                        listWaiting.add(printOrder(allAccounts.getAllAccounts().get(i).getAllOrders().get(j)));

                    if (allAccounts.getAllAccounts().get(i).getAllOrders().get(j).getStatus() >= 2)
                    listPast.add(printOrder(allAccounts.getAllAccounts().get(i).getAllOrders().get(j)));

                }


            }
        }
    }

    private String printOrder(Order order) {
        String res ="";
        res =  order.getCurrentDate() + "  |    "+ order.getReservationDate() + "   |     " + order.getNumber1()+"";
        res += order.getNumber2().equals(" ")? " ": order.getNumber2();
        res +=  "  |  "+  order.getPrice().getTotalPrice() + "₪  |  "+ printStatus(order.getStatus());
    return res;
    }




    private void click(int index) {
        index--;
        int res = -1;
        for (int i = 0; i < allAccounts.getAllAccounts().size(); i++) {
            for (int j = 0; j < allAccounts.getAllAccounts().get(i).getAllOrders().size(); j++) {
                if (allAccounts.getAllAccounts().get(i).getAllOrders().get(j).getStatus() == 1)
                    res++;
                if (res == index) {
                    allAccounts.getAllAccounts().get(i).getAllOrders().get(j).updateStatus(allAccounts.getAllAccounts().get(i).getAllOrders().get(j));
                    putOnDB( allAccounts.getAllAccounts().get(i));
                     break;
                }

            }

        }

    }

    private void findViews(View view) {
        manager_history_LIST_waiting = findViewById(R.id.manager_history_LIST_waiting);
        manager_history_LIST_past = findViewById(R.id.manager_history_LIST_past);
        manager_history_LIST_past = findViewById(R.id.manager_history_LIST_past);

    }



        private void getFromDB(){
            // Read from the database
            myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            allAccounts.add(ds.getValue(Account.class));
                        }

                    }
                    findViewById(R.id.manager_history_loading).setVisibility(View.INVISIBLE);
                    copyList(1);
                    arrayAdapterWaiting = new ArrayAdapter(getApplicationContext(), R.layout.mylist, listWaiting);
                    arrayAdapterPast = new ArrayAdapter(getApplicationContext(), R.layout.mylist, listPast);
                    manager_history_LIST_waiting.setAdapter(arrayAdapterWaiting);
                    manager_history_LIST_past.setAdapter(arrayAdapterPast);


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("or", "Failed to read value.", error.toException());
                }
            });

        }



    private void putOnDB(Account account) {
        String phone = account.getUserPhoneNumber();
        myRef.child("Users").child(phone).setValue(account);
    }



    private String printStatus(int x) {
        switch (x) {
            case (0):
                return "EDITING";
            case (1):
                return "WAITING";
            case (2):
                return "PROGRESS";
            case (3):
                return "DONE";
        }
        return "NA";
    }

    private void openNewActivityRefresh() {
        Intent intent = new Intent(this, ManagerHistoryOrders.class);
        finish();
        startActivity(intent);
    }


    private void openNewActivityManager() {
        Intent intent = new Intent(this, ActivityMainManager.class);
        startActivity(intent);
        finish();
    }

}
