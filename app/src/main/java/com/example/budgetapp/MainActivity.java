package com.example.budgetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetapp.dataclass.AfterRegister;
import com.example.budgetapp.dataclass.UserProfile;
import com.example.budgetapp.home.HomeMainPage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
//
    TextInputEditText emailTextInput, passwordTextInput, nameTextInput;
    TextInputLayout emailTextInputLayout, passwordTextInputLayout, nameTextInputLayout;
    Button registerButton;
    TextView goToLogin;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    SharedPreferences sh;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailTextInput = findViewById(R.id.nameTextInput);
        passwordTextInput = findViewById(R.id.passwordTextInput);
        nameTextInput = findViewById(R.id.userNameTextInput);

        emailTextInputLayout = findViewById(R.id.nameLayout);
        passwordTextInputLayout = findViewById(R.id.passwordLayout);
        nameTextInputLayout = findViewById(R.id.userNameLayout);

        registerButton = findViewById(R.id.logInButton);
        goToLogin = findViewById(R.id.goToLogin);

//        boolean a = sh.getBoolean("isLogin", false);
//
//        if(a){
//            Intent i = new Intent(getApplicationContext(), HomeMainPage.class);
//            finishAffinity();
//        }
//

//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";

        sh = getSharedPreferences("UserData",MODE_PRIVATE);
        editor = sh.edit();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        goToLogin.setOnClickListener(v->{
            Intent i = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(i);
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailTextInputLayout.setHelperText("");
                passwordTextInputLayout.setHelperText("");
                nameTextInputLayout.setHelperText("");

                emailTextInputLayout.setHelperTextEnabled(true);
                passwordTextInputLayout.setHelperTextEnabled(true);
                nameTextInputLayout.setHelperTextEnabled(true);
//
                String name = nameTextInput.getText().toString();
                String email = emailTextInput.getText().toString();
                String password = passwordTextInput.getText().toString();
//
                Pattern lowerCasePatten = Pattern.compile("[a-z ]");
                Pattern digitCasePatten = Pattern.compile("[0-9 ]");

                boolean isEmailValid = true;
                boolean isPasswordValid = true;
                boolean isNameValid = true;

                if(name.trim().equals("")){
                    isEmailValid = false;
                    nameTextInputLayout.setHelperText("*required");
                }
                else if(name.length()<4){
                    nameTextInputLayout.setHelperText("Name must be 4 latter");
                }

                if(email.trim().equals("")){
                    isEmailValid = false;
                    emailTextInputLayout.setHelperText("*required");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    isEmailValid = false;
                    emailTextInputLayout.setHelperText("Enter Valid Email Id");
                }


                if(password.trim().equals("")) {
                    isPasswordValid = false;
                    passwordTextInputLayout.setHelperText("*required");
                }
                else if(password.length() < 8){
                    isPasswordValid = false;
//                    passwordTextInputLayout.setHelperText("Password need to be more then 8 character");
                    passwordTextInputLayout.setHelperText("Size must be more then 8");
                }
                else if(!lowerCasePatten.matcher(password).find() || !digitCasePatten.matcher(password).find()){
                    isPasswordValid = false;
//                    passwordHint.setText("Password need to contain digit and number");
                    passwordTextInputLayout.setHelperText("Password need to contain digit and number");
                }
                if(isEmailValid && isPasswordValid && isNameValid){

                    emailTextInputLayout.setHelperTextEnabled(false);
                    passwordTextInputLayout.setHelperTextEnabled(false);

                    registerUser(name, email,password);
                }
//
            }
        });


    }


    void registerUser(String name, String email, String password){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setView(R.layout.aleart_dialog);

        AlertDialog alertDialog=dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(false);

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {



                DatabaseReference databaseReference = database.getReference().child("UserProfile").push();

                String key = databaseReference.getKey();
                String uId = authResult.getUser().getUid();

                UserProfile user = new UserProfile(uId, name, email, password, " ", key, " ", " ");

                databaseReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {



                        setSharedPreferences(uId, name, email, password, key, "");

                        alertDialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), EnterRegistrationDetails.class);

                        i.putExtra("name", name);
                        i.putExtra("email", email);
                        i.putExtra("password", password);
                        i.putExtra("key", key);
                        i.putExtra("uId", uId);

                        startActivity(i);
                        finishAffinity();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Try Again something went wrong", Toast.LENGTH_SHORT).show();

                        authResult.getUser().delete();

                        alertDialog.dismiss();


                    }
                });



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getLocalizedMessage()+" ", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

    }

    void setSharedPreferences(String uId, String name, String email, String password, String key, String profilePicture){
        editor.putString("id",uId);
        editor.putString("name",name);
        editor.putString("email",email);
        editor.putString("password",password);
        editor.putString("key",key);
        editor.putString("profilePicture",profilePicture);
        editor.putBoolean("isLogin", true);

        editor.apply();
    }

}