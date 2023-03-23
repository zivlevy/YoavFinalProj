package com.example.knowaround;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import services.Auth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Auth auth = Auth.getInstance();
        auth.signIn("zivilevy@gmail.com", "zivi1503")   // this is a Task<AuthResult>
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "success", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "failure", Toast.LENGTH_LONG).show();
                    }
                });


        Intent intent= new Intent(MainActivity.this, LogIn.class);
        startActivity(intent);
      }



}