package com.example.knowaround;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import services.Auth;

public class LogIn extends AppCompatActivity {

    Button btnLogin, btnSignup;
    EditText etEmail, etPassword;
    Auth auth = Auth.getInstance();
    String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        btnLogin=findViewById(R.id.btnLoginogIn);
        btnSignup=findViewById(R.id.btMoveToSignUp);
        etEmail=findViewById(R.id.LoginEmail);
        etPassword=findViewById(R.id.LoginPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                auth.login(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LogIn.this, Map.class);
                                startActivity(intent);
                                Toast.makeText(LogIn.this, "Hurray!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LogIn.this, "Error!", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}