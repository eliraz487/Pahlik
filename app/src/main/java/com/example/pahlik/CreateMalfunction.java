package com.example.pahlik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;


import com.example.pahlik.bean.Malfunction;
import com.example.pahlik.bean.MalfunctionType;

import com.example.pahlik.bean.User;
import com.example.pahlik.helper.CustomSpinner;
import com.example.pahlik.helper.MalfunctionTypeAdapter;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateMalfunction extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ValueEventListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private Spinner spinner_system;
    private Spinner spinner_malfunction;

    private String malfunction_id;
    Button save_malfunction_button;

    FirebaseDatabase database;
    DatabaseReference myRef;
    Map<String, ArrayList<String>> malfunction_map;

    FirebaseUser currentUser;


    ArrayList<String> malfunction_map_keys;

    EditText editText_location;
    EditText editText_description;
    EditText editText_malfunctionName;
    EditText editText_solution;

    String time_malfunction="";
    TextInputLayout textInputLayout_solution;
    TextInputLayout contact;

    User user;
    Switch sovled_Switch;
    private EditText editTextContact;
    private EditText editTextContactPhoneNumber;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_malfunction);
        initWindow();
        Query query = myRef.child("system").orderByChild("system").getRef();
        query.addValueEventListener(this);
        save_malfunction_button.setOnClickListener(this);

    }

    private void loadMalfunction() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;

        }
        Malfunction malfunction=(Malfunction) intent.getSerializableExtra("malfunction_key");
        malfunction_id=malfunction.getId();
        editText_solution.setText(malfunction.getSolution());

        String system_key=malfunction.getMalfunctionType().getSystem();
        if(malfunction_map_keys.contains(system_key)){
            spinner_system.setSelection(malfunction_map_keys.indexOf(system_key));
            String malfunction_key=malfunction.getMalfunctionType().getMalfunction_type();
           if( malfunction_map.get(system_key).contains(malfunction_key)){
               int malfunction_postion=malfunction_map.get(system_key).indexOf(malfunction_key);
               time_malfunction=malfunction.getLocalDateTime();
               editText_malfunctionName.setText(malfunction.getName_malfunction());
               spinner_malfunction.setSelection(malfunction_postion);
               editText_location.setText(malfunction.getLocation());
               editText_description.setText(malfunction.getDescription());
               editTextContact.setText(malfunction.getContact());
               editTextContactPhoneNumber.setText(malfunction.getContact_phonenumber());
               sovled_Switch.setVisibility(View.VISIBLE);
               sovled_Switch.setOnCheckedChangeListener(this);
               sovled_Switch.setChecked(malfunction.isIs_solved());
           }
        }

    }

    private void initWindow() {
        textInputLayout_solution=findViewById(R.id.solution_boxTextInputLayout);
        spinner_system = findViewById(R.id.spinner_system);
        editText_location = findViewById(R.id.location);
        editText_description = findViewById(R.id.Description);
        editText_malfunctionName = findViewById(R.id.malfunctionName);
        editText_solution=findViewById(R.id.solution);
        editTextContact=findViewById(R.id.contact);
        editTextContactPhoneNumber=findViewById(R.id.contact_phone);
        spinner_malfunction = findViewById(R.id.spinner_malfunction);
        save_malfunction_button = findViewById(R.id.saveMalfunction);

        sovled_Switch =findViewById(R.id.switch_solved);
        malfunction_map = new HashMap<>();
        malfunction_map_keys = new ArrayList<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        sovled_Switch.setVisibility(View.GONE);
        textInputLayout_solution.setVisibility(View.GONE);
        myRef.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                user.decode();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void creteSpinner() {
        MalfunctionTypeAdapter systemArrayAdapter = new MalfunctionTypeAdapter(this, R.layout.malfunction_type_list ,malfunction_map_keys);
        spinner_system.setAdapter(systemArrayAdapter);

        spinner_system.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        MalfunctionTypeAdapter malfunctionArrayAdapter;
        String key = malfunction_map_keys.get(position);

        ArrayList<String> malfunction_list = malfunction_map.get(key);

        malfunctionArrayAdapter = new MalfunctionTypeAdapter(CreateMalfunction.this, R.layout.malfunction_type_list , malfunction_list);
        spinner_malfunction.setAdapter(malfunctionArrayAdapter);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        for (DataSnapshot malfunctionSnapshot : snapshot.getChildren()) {
            MalfunctionType malfunctionType = malfunctionSnapshot.getValue(MalfunctionType.class);
            if (malfunctionType != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        malfunctionType.decode();
                        ArrayList<String> malfunction_list = new ArrayList<>();
                        String key_temp=malfunctionType.getSystem();
                        if (!malfunction_map.containsKey(key_temp)) {
                            malfunction_map.put(key_temp, malfunction_list);
                            malfunction_map_keys.add(key_temp);

                        }
                        malfunction_list = malfunction_map.get(key_temp);
                        String temp = malfunctionType.getMalfunction_type();
                        if (!malfunction_list.contains(temp)) {
                            malfunction_list.add(temp);
                        }
                        malfunction_map.put(key_temp, malfunction_list);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
        creteSpinner();
        loadMalfunction();
        // Now 'uniqueSystems' set contains all the different "system" types
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onClick(View v) {
        if (v == save_malfunction_button) {
            Malfunction malfunction;
            String location = editText_location.getText().toString();
            String description = editText_description.getText().toString();
            String malfunctionName = editText_malfunctionName.getText().toString();
            String system = malfunction_map_keys.get(spinner_system.getSelectedItemPosition());
            String malfunction_type = malfunction_map.get(system).get(spinner_malfunction.getSelectedItemPosition());
            MalfunctionType malfunctionType = new MalfunctionType(system, malfunction_type);
            String contact=editTextContact.getText().toString();
            String contact_phone_number=editTextContactPhoneNumber.getText().toString();
            user.setId(currentUser.getUid());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                malfunction = new Malfunction(user, malfunctionName, malfunctionType, location, description, LocalDateTime.now(), false,contact,contact_phone_number);
                malfunction.encode();
                if (malfunction_id == null) {

                    saveMalfunction(malfunction);
                    return;
                }
                malfunction.setLocalDateTime(time_malfunction);
                editMalfunction(malfunction);
            }
            finish();
        }
    }
    private void editMalfunction(Malfunction malfunction) {
        malfunction.setIs_solved(sovled_Switch.isChecked());
        if (sovled_Switch.isChecked()) {
            malfunction.setSolution(editText_solution.getText().toString());
        }
        myRef.child("Malfunction").child(malfunction_id).setValue(malfunction);

    }

    private void saveMalfunction(Malfunction malfunction) {
        myRef.child("Malfunction").push().setValue(malfunction);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            textInputLayout_solution.setVisibility(View.VISIBLE);
            return;
        }

            textInputLayout_solution.setVisibility(View.GONE);

    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("אתה בטוח שאתה רוצה לצאת?")
                .setMessage("יציאה ללא שמירה תמחוק את המידע ולא היה ניתן לשחזר אותו")
                .setPositiveButton("כן אני בטוח", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the action for 'Yes' button here
                        finish(); // if you want to close the current activity
                    }
                })
                .setNegativeButton("לא תשאיר אותי בדף", null) // dismisses the dialog when 'No' is clicked
                .show();
    }
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }
}