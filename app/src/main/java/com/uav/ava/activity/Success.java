package com.uav.ava.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mosambee.lib.ResultData;
import com.uav.ava.BO.InvoiceBO;
import com.uav.ava.MosambeePrinterScanner;
import com.uav.ava.R;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.permission.Session;
import com.uav.ava.util.ExceptionHandler;
import com.uav.ava.util.Utility;
import com.uav.ava.volley.volley.VolleyResponseListener;
import com.uav.ava.volley.volley.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Success extends AppCompatActivity {
    TextView transactionid;
    SharedPreferences sharedPreferences;
    JSONObject jsonObject,CLPJson;
    String amount = null;
    ResultData staff;
    String result;
    String posSaleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        transactionid = findViewById(R.id.transactionid);




        getSupportActionBar().hide();

        TextView mainactivitytitle = findViewById(R.id.mainactivitytitle);
        ImageView logoutbtn = findViewById(R.id.logoutbtn);
        Button Continues = findViewById(R.id.Continues);
        Button print=findViewById(R.id.print);
        Button printInvoice=findViewById(R.id.PrintInvoice);

        Intent intent = getIntent();
        posSaleId =  intent.getStringExtra("tran_id");
        transactionid.setText("Transaction Id :- " + intent.getStringExtra("tran_id"));
        result=intent.getStringExtra("result");



        print.setVisibility(View.GONE);
        if(!result.equals("null")){



            print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(Success.this, MosambeePrinterScanner.class);
                    intent1.setAction("Printer");
                    intent1.putExtra("result", result);
                    startActivity(intent1);
                }
            });
            print.performClick();

        }

        printInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Success.this, PrintInvoice.class);
                intent.putExtra("posSaleId", posSaleId);
                startActivity(intent);
            }
        });
        print.performClick();






        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String resp = sharedPreferences.getString(ApplicationConstant.CACHE_USERNAME, null);
        try {
            jsonObject = new JSONObject(resp);
            CLPJson = new JSONObject(sharedPreferences.getString(ApplicationConstant.CACHE_PASSENGERINFO, null));
            mainactivitytitle.setText(jsonObject.getString("userName"));

            if(!CLPJson.getString("email").equals("")){
                 sendEmail(CLPJson.getString("email"));
            }


        } catch (Exception e) {
            Utility.exceptionAlertDialog(Success.this, "Alert!", "Something went wrong, Please try again!", "Report", Utility.getStackTrace(e));
        }
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ApplicationConstant.CACHE_USERNAME);
                editor.commit();
                startActivity(new Intent(Success.this, Login_Activity.class));
                finish();
            }
        });

        Continues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    Intent newIntent=new Intent();

                    if(jsonObject.getString("officeTypeId").equals("1")){
                         newIntent = new Intent(Success.this, Flight_Scan.class);
                    }else if(jsonObject.getString("officeTypeId").equals("6")){
                        newIntent = new Intent(Success.this, Retail_Sales.class);
                    }
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);
                    finish();
                }catch (Exception e){
                    Utility.exceptionAlertDialog(Success.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                }

            }
        });


    }

    @Override
    public void onBackPressed() {

        Intent newIntent = new Intent(Success.this, MainActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
        finish();

    }


    private void sendEmail(String email){
        VolleyUtils.makeJsonObjectRequest(Success.this, InvoiceBO.sendEmail(Session.getUserId(Success.this),email,posSaleId), new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;
                if(response.get("status").equals("fail")){
                    VolleyUtils.furnishErrorMsg(  "Error !" ,response, Success.this);
                    return;
                }

            }
        });

    }






}