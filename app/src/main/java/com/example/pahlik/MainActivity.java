package com.example.pahlik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.window.SplashScreen;

import com.example.pahlik.bean.MalfunctionType;
import com.example.pahlik.bean.User;
import com.example.pahlik.code.SecretKeyEnum;
import com.example.pahlik.code.SharedKeyHebrewEncoderDecoder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity implements ValueEventListener, View.OnClickListener {
    FirebaseUser currentUser;//used to store current user of account
    FirebaseAuth mAuth;//Used for firebase authentication
    Button logout;
    FirebaseDatabase database;
    DatabaseReference myRef;
    GridLayout gridLayout;
    CardView create_malfunction_button;
    CardView all_malfunctions_button;
    CardView all_users_button;
    CardView my_malfunction_button;
    CardView my_user_button;
    CardView logout_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivity();



    }

   private void createSplash(){
       SplashScreen splashScreen = null;
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
           splashScreen = getSplashScreen();

           splashScreen.setOnExitAnimationListener((splashScreenProvider) -> {
               // Define custom animation here
               ObjectAnimator fadeOut = ObjectAnimator.ofFloat(
                       splashScreenProvider.getIconView(), "alpha", 1f, 0f);
               fadeOut.setDuration(500L);
               fadeOut.addListener(new AnimatorListenerAdapter() {
                   @Override
                   public void onAnimationEnd(Animator animation) {
                       // Remove the SplashScreen from the view hierarchy
                       splashScreenProvider.remove();
                   }
               });
               fadeOut.start();
           });
       }
    }
    private void initActivity() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        gridLayout = findViewById(R.id.grid_layout);
        setGridLayoutAttributes(gridLayout);

        all_malfunctions_button=findViewById(R.id.all_malfunctions_button_card);
        all_users_button=findViewById(R.id.all_users_button_card);
        my_malfunction_button=findViewById(R.id.my_malfunction_button_card);
        my_user_button=findViewById(R.id.my_user_button_card);
        create_malfunction_button=findViewById(R.id.create_malfunction_button_card);
        logout_button=findViewById(R.id.logout_button_card);
        all_malfunctions_button.setOnClickListener(this);
        all_users_button.setOnClickListener(this);
        my_malfunction_button.setOnClickListener(this);
        my_user_button.setOnClickListener(this);
        create_malfunction_button.setOnClickListener(this);
        logout_button.setOnClickListener(this);

    }

    @Override
    protected void onStart() {


        super.onStart();
        currentUser = mAuth.getCurrentUser();

        //Check if user has already signed in if not send user to Login Activity
        if (currentUser == null) {
            sendToLoginActivity();
        } else {
            myRef.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        user.decode();
                        if (user.isManager()) {
                            all_users_button.setVisibility(View.VISIBLE);
                            all_malfunctions_button.setVisibility(View.VISIBLE);
                        }
                        else {
                            all_users_button.setVisibility(View.GONE);
                            all_malfunctions_button.setVisibility(View.GONE
                            );
                        }
                        setGridLayoutAttributes();
                        return;
                    }
                    Intent loginIntent = new Intent(MainActivity.this, CreateUser.class);
                    startActivity(loginIntent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void add_System() {


        try {
            InputStream inputStream = this.getResources().openRawResource(R.raw.mashoa);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            SharedKeyHebrewEncoderDecoder.SECRET_KEY = SecretKeyEnum.SYSTEM;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    MalfunctionType malfunctionType = new MalfunctionType();
                    malfunctionType.setSystem("משואה");
                    malfunctionType.setMalfunction_type(line);
                    malfunctionType.encode();
                    myRef.child("system")
                            .push()
                            .setValue(malfunctionType);
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public void setGridLayoutAttributes() {
        int totalChildCount = gridLayout.getChildCount();
        int row = 0;
        int column = 0;

        for (int i = 0; i < totalChildCount; i++) {
            View child = gridLayout.getChildAt(i);

            if (child instanceof CardView) {
                GridLayout.LayoutParams params = (GridLayout.LayoutParams) child.getLayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(column);
                child.setLayoutParams(params);

                // Adjust the row and column for the next view
                column++;
                if (column == gridLayout.getColumnCount()) {
                    column = 0;
                    row++;
                }
            }
        }
    }

    private void sendToLoginActivity() {
        //To send user to Login Activity
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
    }


    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Intent loginIntent = new Intent(this, CreateUser.class);
        startActivity(loginIntent);
    }
    public void setGridLayoutAttributes(GridLayout gridLayout) {
        int totalChildCount = gridLayout.getChildCount();
        int row = 0;
        int column = 0;

        for (int i = 0; i < totalChildCount; i++) {
            View child = gridLayout.getChildAt(i);

            if (child instanceof CardView) {
                GridLayout.LayoutParams params = (GridLayout.LayoutParams) child.getLayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(column);
                child.setLayoutParams(params);

                // Adjust the row and column for the next view
                column++;
                if (column == gridLayout.getColumnCount()) {
                    column = 0;
                    row++;
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v == create_malfunction_button) {
            Intent start_create_malfunction = new Intent(this, CreateMalfunction.class);
            startActivity(start_create_malfunction);
        }

        if (v == all_malfunctions_button) {
            Intent start_create_malfunction = new Intent(this, All_Malfunction.class);
            startActivity(start_create_malfunction);
        }
        if (v == my_malfunction_button) {
            Intent start_create_malfunction = new Intent(this, My_MalfunctionsActivity.class);
            startActivity(start_create_malfunction);
        }
        if (v == all_users_button) {
            Intent start_create_malfunction = new Intent(this, AllUsersActivity.class);
            startActivity(start_create_malfunction);
        }
        if (v == my_user_button) {
            Intent start_create_user = new Intent(this, CreateUser.class);
            startActivity(start_create_user);
        }
        if (v == logout_button) {
            FirebaseAuth.getInstance().signOut();
            //After logging out send user to Login Activity to login again
            sendToLoginActivity();
        }
    }
    @Override
    public void onBackPressed() {
    }

}