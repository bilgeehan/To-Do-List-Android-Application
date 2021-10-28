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

public class Login extends AppCompatActivity {
    EditText mail;
    EditText password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        password = (EditText) findViewById(R.id.inputPasswordd);
        mail = (EditText) findViewById(R.id.inputMaill);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onClickLogin(View view) {
        String stringMail = mail.getText().toString();
        String stringPassword = password.getText().toString();
        mAuth.signInWithEmailAndPassword(stringMail, stringPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "You are successfully logged in", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(Login.this, MainActivity.class);
                    startActivity(loginIntent);
                } else {
                    Toast.makeText(Login.this, "!!!ERROR!!! Please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}