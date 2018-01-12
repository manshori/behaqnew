package com.example.hp.behaq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn_analyze, btn_laporkan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_analyze = findViewById(R.id.btn_analyzeMain);
        btn_laporkan = findViewById(R.id.btn_laporkanMain);

        btn_analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AnalyzeActivity.class);
                startActivity(i);
            }
        });

        btn_laporkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(MainActivity.this, LaporkanActivity.class);
                startActivity(j);
            }
        });
    }
}
