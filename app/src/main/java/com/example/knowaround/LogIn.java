package com.example.knowaround;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import services.Auth;

public class LogIn extends AppCompatActivity {

    Button btnLogin, btnSignup;
    TextInputEditText etEmail, etPassword;
    Auth auth = Auth.getInstance();
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        btnLogin = findViewById(R.id.btnLoginogIn);
        btnSignup = findViewById(R.id.btMoveToSignUp);
        etEmail = findViewById(R.id.inputEditText_email);
        etPassword = findViewById(R.id.inputEditText_password);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    MaterialAlertDialogBuilder buildr = new MaterialAlertDialogBuilder(LogIn.this);
                    buildr.setTitle("Misssing Fields");
                    buildr.setMessage("Please fill all the fields");
                    buildr.setPositiveButton("OK", (dialogInterface, i) -> {

                    });

                    buildr.show();
                    return;
                }

                if (password.length() < 6) {
                    MaterialAlertDialogBuilder buildr = new MaterialAlertDialogBuilder(LogIn.this);
                    buildr.setTitle("Password too short");
                    buildr.setMessage("Please enter a password with at least 6 characters");
                    buildr.setPositiveButton("OK", (dialogInterface, i) -> {

                    });

                    buildr.show();
                    return;
                }
                auth.login(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LogIn.this, Map.class);
                                startActivity(intent);
                                Toast.makeText(LogIn.this, "Hurray!", Toast.LENGTH_SHORT).show();
                            } else {
                                MaterialAlertDialogBuilder buildr = new MaterialAlertDialogBuilder(LogIn.this);
                                buildr.setTitle("Error Loging In");
                                buildr.setMessage(task.getException().getMessage());
                                buildr.setPositiveButton("OK", (dialogInterface, i) -> {

                                });

                                buildr.show();
                            }
                        })
                        .addOnFailureListener(err -> {
                            Toast.makeText(LogIn.this, err.getMessage(), Toast.LENGTH_LONG).show();
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