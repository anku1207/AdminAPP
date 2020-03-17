package com.uav.ava.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.uav.ava.constant.ApplicationConstant;

import com.uav.ava.R;
import com.uav.ava.permission.Session;
import com.uav.ava.util.ExceptionHandler;
import com.uav.ava.util.Utility;

public class Splash_Screen extends AppCompatActivity {
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));


        ImageView imageView = (ImageView) findViewById( R.id.appstarticon);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);





        if(!Utility.isNetworkAvailable(Splash_Screen.this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Sorry, no Internet Connectivity detected. Please reconnect and try again ")
                    .setTitle("No Internet Connection!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCanceledOnTouchOutside(false);
            alert.setCancelable(false);
            alert.show();
            return;
        }





        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dowork();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                staarapp();
                finish();
            }
        }).start();

    }

    private  void dowork() throws InterruptedException {
        for(int i=0; i<100;i+=10){
            Thread.sleep(100);
            progressBar.setProgress(i);

        }
    }



    private  void staarapp(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);



        if(!sharedPreferences.contains(ApplicationConstant.CACHE_USERNAME) ){
            Intent intent=new Intent(this,Login_Activity.class);
            finish();
            startActivity(intent);
        }else {
            Intent intent=new Intent(this,MainActivity.class);
            finish();
            startActivity(intent);
        }

    }

}
