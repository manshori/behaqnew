package com.example.hp.behaq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.hp.behaq.Helper.SharedPref;
import com.google.gson.Gson;

public class AnalyzeActivity extends AppCompatActivity {

    Gson gson;
    RequestQueue requestQueue;
    ProgressDialog progress;
    EditText searchbarAnalyze;
    Button btn_searchAnalyze;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.setCanceledOnTouchOutside(true);

        searchbarAnalyze = findViewById(R.id.et_inputSearchAnalyze);
        btn_searchAnalyze = findViewById(R.id.btn_searchAnalyze);
        final String data = searchbarAnalyze.getText()+"";

        btn_searchAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnalyzeActivity.this, HasilActivity.class);
                Log.d("jnh", searchbarAnalyze.getText()+"");
                intent.putExtra("dataSearch", searchbarAnalyze.getText()+"");
                startActivity(intent);
            }
        });
    }
}
