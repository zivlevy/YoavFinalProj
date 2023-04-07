package com.example.knowaround;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import services.Auth;

public class SignUp extends AppCompatActivity {

    Button btnSignUp;
    EditText etEmail, etPassword, etUserName;
    String email, password, name;
    MaterialTextView gotoLoginText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Auth auth = Auth.getInstance();
        btnSignUp=findViewById(R.id.button_login);
        etEmail=findViewById(R.id.inputEditText_email);
        etPassword=findViewById(R.id.inputEditText_password);
        etUserName=findViewById(R.id.inputEditText_username);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = etUserName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    MaterialAlertDialogBuilder buildr = new MaterialAlertDialogBuilder(SignUp.this);
                    buildr.setTitle("Misssing Fields");
                    buildr.setMessage("Please fill all the fields");
                    buildr.setPositiveButton("OK", (dialogInterface, i) -> {

                    });

                    buildr.show();
                    return;
                }

                if (password.length() < 6) {
                    MaterialAlertDialogBuilder buildr = new MaterialAlertDialogBuilder(SignUp.this);
                    buildr.setTitle("Password too short");
                    buildr.setMessage("Please enter a password with at least 6 characters");
                    buildr.setPositiveButton("OK", (dialogInterface, i) -> {

                    });

                    buildr.show();
                    return;
                }
                auth.signUp(email, password, name)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent intent=new Intent(SignUp.this,Map.class);
                                startActivity(intent);
                            } else {
                                MaterialAlertDialogBuilder buildr = new MaterialAlertDialogBuilder(SignUp.this);
                                buildr.setTitle("Error Signing Up");
                                buildr.setMessage(task.getException().getMessage());
                                buildr.setPositiveButton("OK", (dialogInterface, i) -> {
                                });

                                buildr.show();
                            }
                        });
            }
        });


        gotoLoginText=findViewById(R.id.textView_gotologin);
        gotoLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUp.this,LogIn.class);
                startActivity(intent);
            }
        });
    }
}