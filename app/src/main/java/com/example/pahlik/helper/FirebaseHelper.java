package com.example.pahlik.helper;

import android.os.Build;

import com.example.pahlik.bean.User;
import com.example.pahlik.code.SecretKeyEnum;
import com.example.pahlik.code.SharedKeyHebrewEncoderDecoder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private DatabaseReference databaseReference;


    public FirebaseHelper() {
        // Initialize Firebase Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(); // "users" is the database node to store user data
    }

    public void saveUser(User user) {
        // Generate a unique key for the user

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        user.encode();

         databaseReference.child("users").child(currentUser.getUid()).setValue(user);
    }
}

