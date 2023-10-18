package com.example.pahlik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pahlik.bean.User;
import com.example.pahlik.helper.OnSwitchClickListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
 import com.example.pahlik.helper.UserAdapter;
import java.util.ArrayList;
import java.util.List;

public class AllUsersActivity extends AppCompatActivity implements ValueEventListener, OnSwitchClickListener, OnSuccessListener<Void> {
    private ListView listViewUsers;
    private DatabaseReference databaseReference;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private UserAdapter UserAdapter;
    private ArrayList<User> userList;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        listViewUsers = findViewById(R.id.listViewUsers);
        editTextSearch = findViewById(R.id.editTextSearch);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        // Create and populate the list of User objects (you should load your data here)
        userList = new ArrayList<>();
        // Add more users as needed...

        // Initialize the custom adapter and set it to the ListView
        loadUsers();

        // Add a TextWatcher to the EditText for real-time filtering
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the list based on the entered text
                UserAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void loadUsers() {
        Query getAllUsers = myRef.child("users");
        getAllUsers.addValueEventListener(this);

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        userList.clear();
        for (DataSnapshot child : snapshot.getChildren()) {
            User user = child.getValue(User.class);
            user.decode();
            userList.add(user);
            user.setId(child.getKey());
        }
        UserAdapter = new UserAdapter(this, userList);
        listViewUsers.setAdapter(UserAdapter);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void OnSwitchClick(User user) {
        databaseReference.child("users").child(user.getId()).setValue(user).addOnSuccessListener(this);
    }

    @Override
    public void onSuccess(Void unused) {
        Toast.makeText(this, "user.getId(", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}