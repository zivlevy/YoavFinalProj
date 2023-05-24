package com.example.knowaround;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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


//                // TODO: remove in production
                email = "yoav@gmail.com";
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
                                finish();
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
                finish();
            }
        });
    }


    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int num = item.getItemId();
        if (num== R.id.menuAbout)
        {
            Toast.makeText(this, "AA", Toast.LENGTH_SHORT).show();
        }
        if (num== R.id.menuExit)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Are you sure you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

        }
        return true;
    }
}