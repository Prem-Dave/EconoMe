package com.example.budgetapp.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetapp.R;
import com.example.budgetapp.dataclass.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView userProfile;
    TextView userNameView, genderView, emailView, dobView;

    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        userProfile = v.findViewById(R.id.imageView2);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        String name = preferences.getString("name","");
        String email = preferences.getString("email","");
        String profilePicture = preferences.getString("profilePicture","");
        String dob = preferences.getString("dob","");
        String gender = preferences.getString("gender","");

        TextView nameTv = v.findViewById(R.id.textView6);
        TextView emailTv = v.findViewById(R.id.textView9);
        TextView genderTv = v.findViewById(R.id.textView8);
        TextView dobTv = v.findViewById(R.id.textView10);

        nameTv.setText(name);
        emailTv.setText(email);
        genderTv.setText(gender);
        dobTv.setText(dob);

        String uri = "@drawable/"+profilePicture;  // where myresource (without the extension) is the file

        int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());

        Drawable res = getResources().getDrawable(imageResource);
        userProfile.setImageDrawable(res);


        return v;

    }
}