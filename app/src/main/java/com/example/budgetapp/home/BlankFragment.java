package com.example.budgetapp.home;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetapp.Adapters.RvFirstAdapter;
import com.example.budgetapp.Extra.AddExpensesActivity;
import com.example.budgetapp.R;
import com.example.budgetapp.dataclass.Expense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BlankFragment extends Fragment {


    String[] cou = new String[]{"Prem", "Dave", "The Prem", "The Dave"};

    FloatingActionButton addExpense;

    String todayDate;

    RecyclerView rv;

    ArrayList<Expense> dataList;
    RvFirstAdapter adapter;

    FirebaseDatabase database;

    FirebaseUser user;

    PieChart pieChart;

    int profit = 0, loss = 0;

    Map<String, Long> expenseTypeListMap;

    TextView profitText, lossText, totalText;

    ArrayList<String> arrayListDemo = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        addExpense = view.findViewById(R.id.addExpense);
        pieChart = view.findViewById(R.id.piChart);

        arrayListDemo.add("#ffea00");
        arrayListDemo.add("#ffa407");
        arrayListDemo.add("#ff5722");
        arrayListDemo.add("#e91e63");
        arrayListDemo.add("#9C27B0");
        arrayListDemo.add("#2196f3");
        arrayListDemo.add("#00BCD4");
        arrayListDemo.add("#8BC34A");
        arrayListDemo.add("#CDDC39");
        arrayListDemo.add("#3bffc7");
        arrayListDemo.add("#ff9800");
        arrayListDemo.add("#D32F2F");
        arrayListDemo.add("#673AB7");
        arrayListDemo.add("#3F51B5");
        arrayListDemo.add("#0a780f");
        arrayListDemo.add("#3e7a0a");
        arrayListDemo.add("#455a64");
        arrayListDemo.add("#ad2003");
        arrayListDemo.add("#670170");


        Resources resources = getActivity().getResources();
        String packageName = getActivity().getPackageName();


        lossText = view.findViewById(R.id.profit);
        profitText = view.findViewById(R.id.loss);
        totalText = view.findViewById(R.id.total);

        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        dataList = new ArrayList<>();
        adapter = new RvFirstAdapter(dataList);

        rv.setAdapter(adapter);

        expenseTypeListMap = new HashMap<>();

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNewAddExpenseActivity();
            }
        });
        
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);


        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        todayDate = df.format(c);


        DatabaseReference expensesData = database.getReference().child("ExpensesData").child(user.getUid()).child(getTodaysDate());

        expensesData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dataList.clear();
                expenseTypeListMap.clear();
                loss = 0;
                profit = 0;

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Expense ecExpense = dataSnapshot.getValue(Expense.class);
                    dataList.add(ecExpense);

                    expenseTypeListMap.put(
                            ecExpense.getExpenseType(),
                            Math.abs(changeValue(Integer.parseInt(ecExpense.getAmount()),ecExpense.getExpenseType(), ecExpense.getType()))
                    );


                    if(ecExpense.getType().equals("Expanse")){
                        profit+=Integer.parseInt(ecExpense.getAmount());
                    }
                    else{
                        loss+=Integer.parseInt(ecExpense.getAmount());
                    }

//                    Log.e("Hello", ecExpense.getAmount());

                }

                int i=1;

                for (String key : expenseTypeListMap.keySet()) {

                    int colorResourceId = resources.getIdentifier("a"+i, "color", packageName);

                    Log.d("TAG", expenseTypeListMap.get(key)+" "+i);

                    i++;

                    pieChart.addPieSlice(
                            new PieModel(
                                    key, expenseTypeListMap.get(key), getResources().getColor(colorResourceId)));

                }

                profitText.setText(String.valueOf(profit));
                lossText.setText(String.valueOf(loss));

                totalText.setText(String.valueOf(Math.abs(profit-loss)));
                if(profit-loss>0){
                    totalText.setTextColor(Color.RED);
                }
                else{
                    totalText.setTextColor(Color.GREEN);
                }

                pieChart.startAnimation();

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        pieChart.addPieSlice(new PieModel("Freetime", 15, Color.parseColor("#FE6DA8")));
//        pieChart.addPieSlice(new PieModel("Sleep", 25, Color.parseColor("#56B7F1")));
//        pieChart.addPieSlice(new PieModel("Work", 35, Color.parseColor("#CDA67F")));
//        pieChart.addPieSlice(new PieModel("Eating", 9, Color.parseColor("#FED70E")));

//        pieChart.startAnimation();

        return view;
    }

    private Long changeValue(int amount, String key, String addOrSub) {


        Long currentData = 0L;

        if(expenseTypeListMap.containsKey(key)){
            currentData = expenseTypeListMap.get(key);
        }

        if(addOrSub.equals("Expanse")){
            currentData+=amount;
        }
        else{
            currentData-=amount;
        }


        return currentData;
    }

    private void gotoNewAddExpenseActivity() {
        Intent i = new Intent(getActivity(), AddExpensesActivity.class);
        startActivity(i);
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year)
    {
        return day + " - " + month + " - " + year;
    }


}