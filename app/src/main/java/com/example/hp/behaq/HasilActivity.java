package com.example.hp.behaq;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.behaq.Helper.AppConfig;
import com.example.hp.behaq.Helper.SharedPref;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HasilActivity extends AppCompatActivity {

    Gson gson;
    RequestQueue requestQueue;
    ProgressDialog progress;
    TextView searchbarHasil, hasilAnalyze, hasilAnalyzecontent, hasilSentiment, hasilSentimentcontent;
    LinearLayout linearData, linearSentiment;
    RelativeLayout TopRelative;
    ScrollView TopScroll;
    Button btn_searchHasil;
    String data;
    ImageView iv_dataHasil, iv_sentimentHasil;
    SharedPref sharedPref;

    int Hoax = Color.parseColor("#FFFAABAB");
    int Verified = Color.parseColor("#93f9b0");
    int Positive = Color.parseColor("#93f9b0");
    int Negative = Color.parseColor("#f57c7c");
    int Neutral = Color.parseColor("#f7fa99");

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);

        //inisialisasi progressBar
        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.setCanceledOnTouchOutside(true);

        //inisialisasi inputan dan button
        searchbarHasil = findViewById(R.id.et_inputSearchHasil);
        btn_searchHasil = findViewById(R.id.btn_analyzeHasil);
        hasilAnalyze = findViewById(R.id.tv_dataHasil);
        hasilAnalyzecontent = findViewById(R.id.tv_datacontentHasil);
        hasilSentiment = findViewById(R.id.tv_sentimentHasil );
        hasilSentimentcontent = findViewById(R.id.tv_sentimentcontentHasil);

        //inisialisasi ImageView
        iv_dataHasil = findViewById(R.id.img_dataHasil);
        iv_sentimentHasil = findViewById(R.id.img_sentimentHasil);

        //inisialisasi LinearLayout
        linearData = (LinearLayout) findViewById(R.id.ToplinearLayout);
        linearSentiment = findViewById(R.id.ToplinearLayout3);

        TopRelative = findViewById(R.id.TopRelativeLayout);
        TopScroll = findViewById(R.id.TopScrollView);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        data = (String) b.get("dataSearch");
        Log.d("dataSearch", data+"");
        progress.show();
        analyze2();

        btn_searchHasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                analyze();
            }
        });

    }

    private void analyze2() {
        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        final String url = AppConfig.analyze;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progress.hide();
                        Log.d("Response", response);
                        ResponAnalyze posts = new ResponAnalyze();
                        try {
                            posts = gson.fromJson(response,ResponAnalyze.class);
                        } catch (Exception e){
                            Log.d("response", e.toString());
                        }
                        Log.d("Response Post:",posts.getMessages());

                        if(posts.getData() != null){
                            if (Objects.equals(posts.getData(), "HOAX")){
                                linearData.setBackgroundColor(Hoax);
                                iv_dataHasil.setImageResource(R.drawable.hoax);
                                hasilAnalyze.setText(R.string.hoax);
                                hasilAnalyzecontent.setText(R.string.hoax_content);
                            }else {
                                linearData.setBackgroundColor(Verified);
                                iv_dataHasil.setImageResource(R.drawable.verified);
                                hasilAnalyze.setText(R.string.verified);
                                hasilAnalyzecontent.setText(R.string.verified_content);
                            }

                            if (Objects.equals(posts.getSentiment(), "positive")){
                                linearSentiment.setBackgroundColor(Positive);
                                iv_sentimentHasil.setImageResource(R.drawable.positive);
                                hasilSentiment.setText(R.string.positive);
                                hasilSentimentcontent.setText(R.string.positive_content);
                            }else if (Objects.equals(posts.getSentiment(), "negative")){
                                linearSentiment.setBackgroundColor(Negative);
                                iv_sentimentHasil.setImageResource(R.drawable.negative);
                                hasilSentiment.setText(R.string.negative);
                                hasilSentimentcontent.setText(R.string.negative_content);
                            }else{
                                linearSentiment.setBackgroundColor(Neutral);
                                iv_sentimentHasil.setImageResource(R.drawable.neutral);
                                hasilSentiment.setText(R.string.neutral);
                                hasilSentimentcontent.setText(R.string.neutral_content);
                            }
                        }else{
                            Toast.makeText(HasilActivity.this, "Tidak bisa", Toast.LENGTH_SHORT).show();
                            Log.d("Response :",response);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("token", "2bc97120-38ef-4066-9699-989fa846b54b");
                params.put("content",data+"");
                return params;
            }
        };
        int x=2;
        postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void analyze() {
        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        final String url = AppConfig.analyze;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progress.hide();
                        Log.d("Response", response);
                        ResponAnalyze posts = new ResponAnalyze();
                        try {
                            posts = gson.fromJson(response,ResponAnalyze.class);
                        } catch (Exception e){
                            Log.d("response", e.toString());
                        }
                        Log.d("Response Post:",posts.getMessages());

                        if(posts.getData() != null){
                            if (Objects.equals(posts.getData(), "HOAX")){
                                iv_dataHasil.setImageResource(R.drawable.hoax);
                                hasilAnalyze.setText(R.string.hoax);
                                hasilAnalyzecontent.setText(R.string.hoax_content);
                                linearData.setBackgroundColor(Hoax);
                            }else {
                                iv_dataHasil.setImageResource(R.drawable.verified);
                                hasilAnalyze.setText(R.string.verified);
                                hasilAnalyzecontent.setText(R.string.verified_content);
                                linearData.setBackgroundColor(Verified);
                            }

                            if (Objects.equals(posts.getSentiment(), "positive")){
                                iv_sentimentHasil.setImageResource(R.drawable.positive);
                                hasilSentiment.setText(R.string.positive);
                                hasilSentimentcontent.setText(R.string.positive_content);
                                linearSentiment.setBackgroundColor(Positive);
                            }else if (Objects.equals(posts.getSentiment(), "negative")){
                                iv_sentimentHasil.setImageResource(R.drawable.negative);
                                hasilSentiment.setText(R.string.negative);
                                hasilSentimentcontent.setText(R.string.negative_content);
                                linearSentiment.setBackgroundColor(Negative);
                            }else{
                                iv_sentimentHasil.setImageResource(R.drawable.neutral);
                                hasilSentiment.setText(R.string.neutral);
                                hasilSentimentcontent.setText(R.string.neutral_content);
                                linearSentiment.setBackgroundColor(Neutral);
                            }
                        }else{
                            Toast.makeText(HasilActivity.this, "Tidak bisa", Toast.LENGTH_SHORT).show();
                            Log.d("Response :",response);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("token", "2bc97120-38ef-4066-9699-989fa846b54b");
                params.put("content",searchbarHasil.getText().toString());
                return params;
            }
        };
        int x=2;
        postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    public class ResponAnalyze {
        private String data;
        private String id;
        private String messages;
        private String sentiment;
        private double time;

        public ResponAnalyze(String data, String id, String messages, String sentiment, double time) {
            this.data = data;
            this.id = id;
            this.messages = messages;
            this.sentiment = sentiment;
            this.time = time;
        }

        public ResponAnalyze() {
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessages() {
            return messages;
        }

        public void setMessages(String messages) {
            this.messages = messages;
        }

        public String getSentiment() {
            return sentiment;
        }

        public void setSentiment(String sentiment) {
            this.sentiment = sentiment;
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }
    }
}
