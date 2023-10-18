
package com.example.pahlik;

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

public class All_Malfunction extends AppCompatActivity implements ValueEventListener, AdapterView.OnItemClickListener {
    ArrayList<Malfunction> malfunctionList;

    FirebaseDatabase database;
    ListView expandableListView;
    DatabaseReference myRef;

    EditText username_filter;
    private MalfunctionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_malfunction);
        malfunctionList=new ArrayList<>();
        expandableListView = findViewById(R.id.expandableListView);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        username_filter=findViewById(R.id.username_filter);
        Query getAllMalfunction = myRef.child("Malfunction").orderByChild("localDateTime");
        getAllMalfunction.addValueEventListener(this);
        username_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called to notify you that, within s,
                // the count characters beginning at start are about to be replaced by new text with length after.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called to notify you that, within s,
                // the count characters beginning at start have just replaced old text that had length before.
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called to notify you that, somewhere within s, the text has been changed.
                // You can use this callback to perform actions such as validating the input.
            }
        });

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        malfunctionList.clear();
        for (DataSnapshot child : snapshot.getChildren()) {
            add_Malfunction_From_Database(child);
        }
        Collections.reverse(malfunctionList);
         adapter = new MalfunctionAdapter(this,malfunctionList ); // malfunctionList is a List<Malfunction>
        expandableListView.setAdapter(adapter);
        expandableListView.setOnItemClickListener(this);
    }

    private void add_Malfunction_From_Database(DataSnapshot child) {
        Malfunction malfunction=child.getValue(Malfunction.class);
        if(malfunction!=null) {
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

        Intent i = new Intent(this,CreateMalfunction.class);
        i.putExtra("malfunction_key", malfunctionList.get(position));
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}