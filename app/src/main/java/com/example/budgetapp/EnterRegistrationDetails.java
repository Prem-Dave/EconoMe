package com.example.budgetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetapp.dataclass.UserProfile;
import com.example.budgetapp.home.HomeMainPage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EnterRegistrationDetails extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;

    CircleImageView profilePhoto;

    String profileImageUrl, name, email, id, password, key, gender, birthdate;

    private Button dateButton, submitButton;

    RadioGroup genderGroup;

    TextInputEditText emailTextField, nameTextField;

    FirebaseDatabase database;
    RadioButton r1;

    SharedPreferences sh;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_registration_details);

        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        submitButton = findViewById(R.id.submitButton);
        profilePhoto = findViewById(R.id.profilePhoto);
        dateButton.setText(getTodaysDate());

        database = FirebaseDatabase.getInstance();

        sh = getSharedPreferences("UserData",MODE_PRIVATE);
        editor = sh.edit();

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        key = intent.getStringExtra("key");
        id = intent.getStringExtra("uId");

        nameTextField = findViewById(R.id.nameTextInput);
        emailTextField = findViewById(R.id.emailTextInput);
        genderGroup = findViewById(R.id.genderGroup);

//        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        nameTextField.setText(name);
        emailTextField.setText(email);

        gender= "male";

        profilePhoto.setOnClickListener(v->{
            showBottomSheet();
        });

        profileImageUrl = profilePhoto.getTag().toString();
        birthdate = getTodaysDate();


        submitButton.setOnClickListener(v->{

            profileImageUrl = profilePhoto.getTag().toString();


            int checkedRadioButtonId = genderGroup.getCheckedRadioButtonId();
            r1 = findViewById(checkedRadioButtonId);

//            Toast.makeText(this, r1.getText().toString(), Toast.LENGTH_SHORT).show();

            UserProfile user = new UserProfile(id, name, email, password, profileImageUrl, key, gender, birthdate);

            database.getReference().child("UserProfile").child(key).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {


                    editor.putString("profilePicture",profileImageUrl);
                    editor.putString("name",name);
                    editor.putString("dob",birthdate);
                    editor.putString("gender",gender);

                    editor.apply();
                    editor.commit();

                    Intent i = new Intent(getApplicationContext(), HomeMainPage.class);
                    startActivity(i);
                    finishAffinity();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EnterRegistrationDetails.this, "Something Went wrong try again", Toast.LENGTH_SHORT).show();
                }
            });



        });


    }

    void showBottomSheet(){
        showBottomSheetForAvatar();
    }

    void showBottomSheetForAvatar(){
        final Dialog dialog =new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ext_bottom_sheet_avatar);

        LinearLayout imageContainer =dialog.findViewById(R.id.image_container);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

//        hSV.setBackgroundColor(Color.WHITE);

        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.a1);
        list.add(R.drawable.a2);
        list.add(R.drawable.a3);
        list.add(R.drawable.a4);
        list.add(R.drawable.a5);
        list.add(R.drawable.a6);


        for(int i =0; i< list.size(); i++){


            TextView tv= new TextView(this);

            int a = i+1;
            tv.setText("Avatar" + a);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColor(R.color.white));
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

            layoutParams.setMargins(20,20,20,20);
            layoutParams.gravity = Gravity.CENTER;
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(list.get(i));
            imageView.setLayoutParams(new LinearLayout.LayoutParams(400,500));

            LinearLayout linearLayout =new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(tv);
            linearLayout.addView(imageView);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));


            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profilePhoto.setImageResource(list.get(a-1));
                    profilePhoto.setTag("a"+String.valueOf(a));



//                    Toast.makeText(EnterRegistrationDetails.this, String.valueOf(a), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            imageContainer.addView(linearLayout);
        }


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.bottomSheetStyle;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
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
        return getMonthFormat(month) + " " + day + " " + year;
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