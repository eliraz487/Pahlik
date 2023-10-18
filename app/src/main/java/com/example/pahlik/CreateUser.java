package com.example.pahlik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.pahlik.bean.User;
import com.example.pahlik.code.SecretKeyEnum;
import com.example.pahlik.code.SharedKeyHebrewEncoderDecoder;
import com.example.pahlik.helper.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateUser extends AppCompatActivity implements ValueEventListener {
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextPhoneNumber;
    private EditText editTextContact;
    private EditText editTextContactPhoneNumber;

    private Spinner spinner_level;
    private Spinner spinner_job;


    private boolean isadmin=false;
    private Button buttonSave;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<String> ranks;
    ArrayList<String> jobs;
    private ArrayAdapter<String> rank_adapter;
    private ArrayAdapter<String> job_adapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);

        ranks = new ArrayList<>();
        jobs = new ArrayList<>();
        buttonSave = findViewById(R.id.buttonSave);
        spinner_level = findViewById(R.id.spinnerLevel);
        spinner_job = findViewById(R.id.spinnerJob);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
            // First Firebase query
            Query getAllRanks = myRef.child("Ranks");
            getAllRanks.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        add_Rank_From_Database(child);
                    }
                    rank_adapter = new ArrayAdapter<String>(CreateUser.this, android.R.layout.simple_list_item_1, ranks);
                    spinner_level.setAdapter(rank_adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            // Second Firebase query

            Query getAllLevels = myRef.child("Jobs");
            getAllLevels.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        add_Job_From_Database(child);
                    }
                    job_adapter = new ArrayAdapter<String>(CreateUser.this, android.R.layout.simple_list_item_1, jobs);
                    spinner_job.setAdapter(job_adapter
                    );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


// Shutdown the executor when you're done

        // Set a click listener for the "Save" button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paramsValid()) {
                    saveEmployeeDetails();
                    goMainActivate();
                }
            }
        });

        // Load existing employee details if needed
        loadEmployeeDetails();
    }

    private void goMainActivate() {
        Intent go=new Intent(this,MainActivity.class);
        startActivity(go);
    }

    private boolean paramsValid() {
        if(!isValidILPhoneNumber(editTextPhoneNumber.getText().toString())){
            editTextPhoneNumber.setError("the phone number not valid");
            return false;
        }
        if (editTextFirstName.getText().toString().equals("")){
            editTextFirstName.setError("can be null");
            return false;
        }
        if (editTextLastName.getText().toString().equals("")){
            editTextFirstName.setError("can be null");
            return false;
        }
        return true;
    }

    public static boolean isValidILPhoneNumber(String phoneNumber) {
        // Define the regular expression pattern for Israeli phone numbers
        String ilPhonePattern = "(\\+972-|0)[2-9]\\d{1}-?\\d{7}";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(ilPhonePattern);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(phoneNumber);

        // Check if the phone number matches the pattern
        return matcher.matches();
    }
    private void add_Job_From_Database(DataSnapshot child) {
        SharedKeyHebrewEncoderDecoder.SECRET_KEY = SecretKeyEnum.JOB;
        String decode = child.getValue(String.class).toString();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                decode = SharedKeyHebrewEncoderDecoder.decode(decode);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        jobs.add(decode);
    }

    private void add_Rank_From_Database(DataSnapshot child) {
        SharedKeyHebrewEncoderDecoder.SECRET_KEY = SecretKeyEnum.RANK;
        String decode = child.getValue(String.class).toString();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                decode = SharedKeyHebrewEncoderDecoder.decode(decode);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ranks.add(decode);
    }

    // Method to save employee details
    private void saveEmployeeDetails() {
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String rank = ranks.get(spinner_level.getSelectedItemPosition());
        String job = jobs.get(spinner_job.getSelectedItemPosition());
        User user = new User(firstName, lastName, phoneNumber, job, rank);
        user.setManager(isadmin);
        // Perform data validation here if needed
        FirebaseHelper firebaseHelper = new FirebaseHelper();

        firebaseHelper.saveUser(user);
        // Save the data (you can use SharedPreferences, a database, or other storage methods)
        // For demonstration purposes, we'll display a toast message with the saved data

    }

    // Method to load existing employee details for editing
    private void loadEmployeeDetails() {
        mAuth = FirebaseAuth.getInstance();
         currentUser=mAuth.getCurrentUser();

        // Load the data from storage and populate the EditText fields
        myRef.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(this);
        // This is where you would retrieve existing data if available
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()) {
            User user = snapshot.getValue(User.class);
            user.decode();
              editTextFirstName.setText(user.getFirstName());
              editTextLastName.setText(user.getLastName());
              editTextPhoneNumber.setText(user.getPhoneNumber());
              spinner_level.setSelection(ranks.indexOf(user.getRank()));
              spinner_job.setSelection(jobs.indexOf(user.getJobTitle()));;
              isadmin=user.isManager();

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}