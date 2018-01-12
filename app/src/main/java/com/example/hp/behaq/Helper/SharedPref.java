package com.example.hp.behaq.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by hp on 09/12/2017.
 */

public class SharedPref {

    static Activity activity;
    static Context context;

    public SharedPref(Context context) {
        this.context = context;
    }


    public void saveData(String name, String value){
        SharedPreferences prefs;
        if(context==null){
            prefs = activity.getSharedPreferences("UserData", 0);
        }else{
            prefs = context.getSharedPreferences("UserData", 0);
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        Log.d("Reponse", name + " masuk :"+ value);
        editor.commit();
    }

    public  String loadData(String name){
        SharedPreferences prefs;
        if(context==null){
            prefs = activity.getSharedPreferences("UserData", 0);
        }else{
            prefs = context.getSharedPreferences("UserData", 0);
        }
        String data = prefs.getString(name,"");
        Log.d("Reponse", name + " keluar:"+ data);
        return data;
    }

//    public void logout() {
//        SharedPreferences prefs;
//        prefs = activity.getSharedPreferences("UserData", 0);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.clear();
//        editor.apply();
//        context.startActivity(new Intent((Context) prefs, LoginActivity.class));
//    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

}
