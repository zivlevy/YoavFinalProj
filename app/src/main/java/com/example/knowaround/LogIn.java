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
import com.google.android.material.textview.MaterialTextView;

import services.Auth;

public class LogIn extends AppCompatActivity {

    Button btnLogin, btnSignup;
    TextInputEditText etEmail, etPassword;
    Auth auth = Auth.getInstance();
    String email, password;
    MaterialTextView gotoSignupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        btnLogin = findViewById(R.id.btnLoginogIn);
        gotoSignupText=findViewById(R.id.textView_gotosignup);
        etEmail = findViewById(R.id.inputEditText_email);
        etPassword = findViewById(R.id.inputEditText_password);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                email = "zivilevy@gmail.com";
                password = "zivi1503";

                if (email.isEmpty() || password.isEmpty()) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(LogIn.this);
                    builder.setTitle("Misssing Fields");
                    builder.setMessage("Please fill all the fields");
                    builder.setPositiveButton("OK", (dialogInterface, i) -> {

                    });

                    builder.show();
                    return;
                }

                if (password.length() < 6) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(LogIn.this);
                    builder.setTitle("Password too short");
                    builder.setMessage("Please enter a password with at least 6 characters");
                    builder.setPositiveButton("OK", (dialogInterface, i) -> {

                    });

                    builder.show();
                    return;
                }
                auth.login(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LogIn.this, Map.class);
                                startActivity(intent);
                                Toast.makeText(LogIn.this, "Hurray!", Toast.LENGTH_SHORT).show();
                            } else {
                                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(LogIn.this);
                                builder.setTitle("Error Loging In");
                                builder.setMessage(task.getException().getMessage());
                                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                                });

                                builder.show();
                            }
                        })
                        .addOnFailureListener(err -> {
                            Toast.makeText(LogIn.this, err.getMessage(), Toast.LENGTH_LONG).show();
                        });

            }
        });

        gotoSignupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LogIn.this,SignUp.class);
                startActivity(intent);
            }
        });
    }
}