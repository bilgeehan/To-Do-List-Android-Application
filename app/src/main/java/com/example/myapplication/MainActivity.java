package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText addActivity;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ArrayList<String> arrActivityKeys;
    ArrayList<String> arrActivities;
    ArrayAdapter<String> adapter;
    int chosenActivityNum;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addActivity = findViewById(R.id.addActivityy);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        arrActivityKeys = new ArrayList<>();
        arrActivities = new ArrayList<>();
        chosenActivityNum = 0;
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getUid() == null) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
        list = (ListView) findViewById(R.id.listActivities);
        mDatabase.child("activities").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists()) {
                    arrActivities.clear();
                    arrActivityKeys.clear();
                    adapter = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, arrActivities);
                    list.setAdapter(adapter);
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        arrActivityKeys.add(String.valueOf(snapshot.getKey()));
                        arrActivities.add(String.valueOf(snapshot.getValue()));
                    }
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            System.out.println(position);
                            chosenActivityNum = position;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickAddActivityButton(View view) {
        String addActivityString = addActivity.getText().toString();
        if (!addActivityString.equals("")) {
            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User current = new User();
                    current = snapshot.getValue(User.class);
                    int currentUserNum = current.num;
                    mDatabase.child("activities").child(mAuth.getCurrentUser().getUid()).child("activity" + current.num).setValue(addActivityString).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("num").setValue(currentUserNum + 1);
                                Toast.makeText(MainActivity.this, "Activity added successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Please enter an activity", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickDeleteActivityButton(View view) {
//        Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
//        startActivity(intent);
        mDatabase.child("activities").child(mAuth.getCurrentUser().getUid()).child(arrActivityKeys.get(chosenActivityNum)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (arrActivityKeys.size() <= 1 && arrActivities.size() <= 1) {
            adapter.clear();
        }
    }

    public void onClickLogout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }
}