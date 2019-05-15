package com.dev.eventmanagement;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.dev.eventmanagement.databinding.ActivityMainBinding;
import com.dev.eventmanagement.model.LoginModel;
import com.dev.eventmanagement.model.User;
import com.dev.eventmanagement.utilities.AppUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private FirebaseDatabase mDatabase;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);
        binding.setLoginModel(new LoginModel());
        binding.getLoginModel().setEmailId("test@gmail.com");
        initFirebase();
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference().child("users");
    }

    public void onSignUpClick() {
        LoginModel loginModel = binding.getLoginModel();
        if (TextUtils.isEmpty(loginModel.getEmailId())) {
            Toast.makeText(this, getResources().getString(R.string.enter_email_id), Toast.LENGTH_LONG).show();
        } else if (!AppUtils.isEmailValid(loginModel.getEmailId())) {
            Toast.makeText(this, getResources().getString(R.string.enter_valid_email_id), Toast.LENGTH_LONG).show();
        } else {
            binding.getLoginModel().setSignUpEnable(false);
            insertToDatabase(loginModel.getEmailId());
        }
    }

    private void insertToDatabase(String emailId) {
        if (null != mDatabase && null != ref) {
            ref.orderByChild("email").equalTo(emailId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        //email id exist in the system
                        redirectToListingPage(emailId);
                    } else {
                        //new Email id
                        registerInDb(emailId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void redirectToListingPage(String emailId) {
        Intent intent = new Intent(MainActivity.this, EventListingActivity.class);
        intent.putExtra(EventListingActivity.KEY_ARGS, emailId);
        startActivity(intent);
    }

    private void registerInDb(String emailId) {
        String userId = ref.push().getKey();
        ref.child(Objects.requireNonNull(userId))
                .setValue(new User(emailId))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        redirectToListingPage(emailId);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.error_msg), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

