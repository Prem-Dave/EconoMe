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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetapp.dataclass.UserProfile;
import com.example.budgetapp.home.HomeMainPage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class LoginPage extends AppCompatActivity {

    TextInputEditText emailTextInput, passwordTextInput ;
    TextInputLayout emailTextInputLayout, passwordTextInputLayout;
    Button loginButton;
    TextView goToRegister;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    SharedPreferences sh;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        emailTextInput = findViewById(R.id.nameTextInput);
        passwordTextInput = findViewById(R.id.passwordTextInput);

        emailTextInputLayout = findViewById(R.id.nameLayout);
        passwordTextInputLayout = findViewById(R.id.passwordLayout);

        loginButton = findViewById(R.id.logInButton);
        goToRegister = findViewById(R.id.goToRegister);

        sh = getSharedPreferences("UserData",MODE_PRIVATE);
        editor = sh.edit();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        loginButton.setOnClickListener(v->{

            emailTextInputLayout.setHelperText("");
            passwordTextInputLayout.setHelperText("");

            emailTextInputLayout.setHelperTextEnabled(true);
            passwordTextInputLayout.setHelperTextEnabled(true);

            String email = emailTextInput.getText().toString();
            String password = passwordTextInput.getText().toString();
//
            Pattern lowerCasePatten = Pattern.compile("[a-z ]");
            Pattern digitCasePatten = Pattern.compile("[0-9 ]");

            boolean isEmailValid = true;
            boolean isPasswordValid = true;

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
            if(isEmailValid && isPasswordValid ){

                emailTextInputLayout.setHelperTextEnabled(false);
                passwordTextInputLayout.setHelperTextEnabled(false);

                loginUser(email,password);
            }

        });

    }

    void loginUser(String email, String password){


        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setView(R.layout.aleart_dialog);

        AlertDialog alertDialog=dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(false);


        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                database.getReference().child("UserProfile").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {

                        FirebaseUser user = authResult.getUser();

                        for(DataSnapshot tempData : dataSnapshot.getChildren()){

                            UserProfile userProfile = tempData.getValue(UserProfile.class);

                            if(userProfile.getId().equals(user.getUid())){
                                setSharedPreferences(userProfile.getId(), userProfile.getName(), email, password, userProfile.getKey(), userProfile.getProfilePicture());

                                alertDialog.dismiss();
                                Intent i = new Intent(LoginPage.this, HomeMainPage.class);
                                startActivity(i);
                                finishAffinity();
                            }

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginPage.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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