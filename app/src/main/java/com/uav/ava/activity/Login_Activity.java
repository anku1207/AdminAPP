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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.uav.ava.BO.LoginBO;

import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.util.ExceptionHandler;
import com.uav.ava.util.Utility;
import com.uav.ava.volley.volley.VolleyResponseListener;
import com.uav.ava.volley.volley.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import com.uav.ava.R;

public class Login_Activity extends AppCompatActivity {
    ImageButton showpassword;
    EditText password,username;
    Button loginbtn;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        getSupportActionBar().hide();



        showpassword=(ImageButton)findViewById(R.id.showpassbtn);
        password=(EditText)findViewById(R.id.password);
        loginbtn=(Button)findViewById(R.id.loginbtn);
        username=(EditText)findViewById(R.id.username);







        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);


        showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    password.setInputType( InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showpassword.setImageResource(R.drawable.ava_showpin);

                }else {
                    password.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                    showpassword.setImageResource(R.drawable.ava_hidepin);

                }
                password.setSelection(password.getText().length());

            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean valid=true;



                if(username.getText().toString().equals("")){
                    username.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
                    valid=false;
                }


                if(password.getText().toString().equals("")){
                    password.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
                    valid=false;
                }


                if(!valid){
                    return;
                }else {
                    verifyuser();
                }

            }
        });

    }


    public  void verifyuser(){
        VolleyUtils.makeJsonObjectRequest(this, LoginBO.verifyuser(username.getText().toString(),password.getText().toString() ), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;
                if(response.get("status").equals("fail")){
                     VolleyUtils.furnishErrorMsg(  "Fail" ,response, Login_Activity.this);

                }else {
                    Log.w("data",response.toString());
                    JSONObject jsonObject=response.getJSONObject("data");
                    SharedPreferences.Editor edit= sharedPreferences.edit();
                    edit.putString( ApplicationConstant.CACHE_USERNAME, jsonObject.toString());
                    edit.apply();
                    edit.commit();
                    startActivity(new Intent(Login_Activity.this,MainActivity.class));
                    finish();
                    overridePendingTransition(0, 0);

                }
            }
        });
    }
}
