package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        name = (EditText) findViewById(R.id.inputName);
        surname = (EditText) findViewById(R.id.inputSurname);
        email = (EditText) findViewById(R.id.inputMail);
        password = (EditText) findViewById(R.id.inputPassword);
    }

    public void onClickButton(View view) {
        String stringName = name.getText().toString();
        String stringSurname = surname.getText().toString();
        String stringEmail = email.getText().toString();
        String stringPassword = password.getText().toString();
        if (stringName.isEmpty() || stringSurname.isEmpty() || stringEmail.isEmpty() || stringPassword.isEmpty()) {
            Toast.makeText(Register.this, "Please fill in the blank spaces", Toast.LENGTH_LONG).show();
        } else {
            if (stringPassword.length() >= 6) {
                mAuth.createUserWithEmailAndPassword(stringEmail, stringPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("name").setValue(stringName);
                            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("surname").setValue(stringSurname);
                            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("email").setValue(stringEmail);
                            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("num").setValue(0);
                            Toast.makeText(Register.this, "User successfully created", Toast.LENGTH_SHORT).show();
                            Intent registerIntent = new Intent(Register.this, Login.class);
                            startActivity(registerIntent);
                        } else {
                            Toast.makeText(Register.this, "User cannot be created.Please try again", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            } else {
                Toast.makeText(Register.this, "Please create a password of at least 6 characters", Toast.LENGTH_SHORT).show();
            }
        }
    }
}