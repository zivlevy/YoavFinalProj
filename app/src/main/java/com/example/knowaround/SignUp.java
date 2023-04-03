package com.example.knowaround;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import services.Auth;

public class SignUp extends AppCompatActivity {

    Button btnSignUp;
    EditText etEmail, etPassword, etUserName;
    String email, password, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Auth auth = Auth.getInstance();
        btnSignUp=findViewById(R.id.btSignUp);
        etEmail=findViewById(R.id.SignUpEmail);
        etPassword=findViewById(R.id.SignUpPassword);
        etUserName=findViewById(R.id.SignUpUsername);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = etUserName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                auth.signUp(email, password, name, "0584288844")
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUp.this, "Hurray!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUp.this, "Error!", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}