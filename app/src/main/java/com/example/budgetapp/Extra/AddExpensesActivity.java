package com.example.budgetapp.Extra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.budgetapp.R;
import com.example.budgetapp.dataclass.Expense;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddExpensesActivity extends AppCompatActivity {

    private Button dateButton, submitButton;
    private DatePickerDialog datePickerDialog;

    TextInputLayout amountLayout, noteLayout;
    TextInputEditText amountField, noteField;

    RadioGroup inputType;
    RadioButton r1;
    String amount, type, note, date, expenseType;

    FirebaseDatabase database;
    FirebaseUser user;

    ProgressBar pb;
    LinearLayout processLayoutVisibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        submitButton = findViewById(R.id.submit);
        amountField = findViewById(R.id.amount);
        amountLayout = findViewById(R.id.amountLayout);
        noteField = findViewById(R.id.note);
        noteLayout = findViewById(R.id.noteLayout);
        processLayoutVisibility = findViewById(R.id.processLayoutVisibility);
        inputType = findViewById(R.id.inputType);
        pb = findViewById(R.id.pb);

//        ProgressBar spinner = new android.widget.ProgressBar(
//                getApplicationContext(),
//                null,+
//                android.R.attr.progressBarStyle);

        pb.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        processLayoutVisibility.setVisibility(View.INVISIBLE);

        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        dateButton.setText(getTodaysDate());

        submitButton.setOnClickListener(v->{

            processLayoutVisibility.setVisibility(View.VISIBLE);

            date = dateButton.getText().toString();

            amount = amountField.getText().toString();
            note = noteField.getText().toString();
            expenseType = "Bills";

            int selectedId = inputType.getCheckedRadioButtonId();
            r1 = findViewById(selectedId);

            type = r1.getText().toString();

            amountLayout.setHelperTextEnabled(false);
            amountLayout.setHelperText("");

            if(amount.trim().isEmpty()){
                amountLayout.setHelperTextEnabled(true);

                amountLayout.setHelperText("*Required");
            }
            else{

                Expense expense = new Expense(note, date, type, amount, expenseType);

                DatabaseReference expensesData = database.getReference().child("ExpensesData").child(user.getUid()).child(date.replace("/", "-")).push();

                expensesData.setValue(expense).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddExpensesActivity.this, "Nice", Toast.LENGTH_SHORT).show();
                        processLayoutVisibility.setVisibility(View.INVISIBLE);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddExpensesActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        processLayoutVisibility.setVisibility(View.INVISIBLE);
                    }
                });

            }


        });



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
        return day + " / " + month + " / " + year;
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}