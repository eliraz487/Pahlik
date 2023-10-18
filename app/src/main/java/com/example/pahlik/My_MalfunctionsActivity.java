package com.example.pahlik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.pahlik.bean.Malfunction;
import com.example.pahlik.helper.MalfunctionAdapter;
import com.example.pahlik.helper.OnDeleteButtonClickListener;
import com.example.pahlik.helper.OnEditButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class My_MalfunctionsActivity extends AppCompatActivity implements ValueEventListener, AdapterView.OnItemClickListener {
    ArrayList<Malfunction> malfunctionList;
    FirebaseUser currentUser;//used to store current user of account
    FirebaseAuth mAuth;//Used for firebase authentication
    FirebaseDatabase database;
    ListView expandableListView;
    DatabaseReference myRef;


    private MalfunctionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_malfunctions);
        malfunctionList = new ArrayList<>();
        expandableListView = findViewById(R.id.expandableListView);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Query getAllMalfunction = myRef.child("Malfunction").orderByChild("user/id").equalTo(currentUser.getUid());
        getAllMalfunction.addValueEventListener(this);


    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        malfunctionList.clear();
        for (DataSnapshot child : snapshot.getChildren()) {
            add_Malfunction_From_Database(child);
        }
        Collections.reverse(malfunctionList);
        adapter = new MalfunctionAdapter(this, malfunctionList); // malfunctionList is a List<Malfunction>
        expandableListView.setAdapter(adapter);
        expandableListView.setOnItemClickListener(this);
    }

    private void add_Malfunction_From_Database(DataSnapshot child) {
        Malfunction malfunction = child.getValue(Malfunction.class);
        if (malfunction != null) {
            malfunction.decode();
            malfunction.setId(child.getKey());
            malfunctionList.add(malfunction);
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(this, CreateMalfunction.class);
        i.putExtra("malfunction_key", malfunctionList.get(position));
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}