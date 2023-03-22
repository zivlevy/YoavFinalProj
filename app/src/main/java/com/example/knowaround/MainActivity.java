package com.example.knowaround;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Auth example = Auth.getInstance();
        example.doubleIt();
        Toast.makeText(this, ""+example.getNum(), Toast.LENGTH_SHORT).show();
        example.subtract1();
        Toast.makeText(this, ""+example.getNum(), Toast.LENGTH_SHORT).show();
    }
}