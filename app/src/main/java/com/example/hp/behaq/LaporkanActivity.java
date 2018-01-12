package com.example.hp.behaq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.behaq.Helper.AppConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class LaporkanActivity extends AppCompatActivity {

    EditText et_email, et_search, et_url;
    Button btn_laporkan;
    RequestQueue requestQueue;
    Gson gson;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporkan);

        //progres dialog
        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.setCanceledOnTouchOutside(true);

        //inisialisasi variabel
        et_email = findViewById(R.id.et_inputEmailLaporkan);
        et_search = findViewById(R.id.et_inputSearchLaporkan);
        et_url = findViewById(R.id.et_inputUrlLaporkan);

        btn_laporkan = findViewById(R.id.btn_laporkanLaporkan);

        btn_laporkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laporkan();
            }
        });
    }

    private void laporkan() {
        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        final String url = AppConfig.laporkan;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progress.hide();
                        Log.d("Response", response);

                        ResponLaporkan posts = new ResponLaporkan();
                        try {
                            posts = gson.fromJson(response,ResponLaporkan.class);
                        } catch (Exception e){
                            Log.d("response", e.toString());
                        }

                        if(posts.isData() == true){
                            Intent intent = new Intent(LaporkanActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LaporkanActivity.this, "Berhasil Melaporkan", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LaporkanActivity.this,"Tidak dapat melaporkan", Toast.LENGTH_SHORT).show();
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
                params.put("user_id", et_email.getText()+"");
                params.put("user_type", "email");
                params.put("content", et_search.getText()+"");
                params.put("url", et_url+"");
                return params;
            }
        };
        int x=2;
        postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    public class ResponLaporkan{
        private boolean data;
        private String datetime;
        private int id;
        private String messages;

        public ResponLaporkan(boolean data, String datetime, int id, String messages) {
            this.data = data;
            this.datetime = datetime;
            this.id = id;
            this.messages = messages;
        }

        public ResponLaporkan() {
        }

        public boolean isData() {
            return data;
        }

        public void setData(boolean data) {
            this.data = data;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMessages() {
            return messages;
        }

        public void setMessages(String messages) {
            this.messages = messages;
        }
    }
}
