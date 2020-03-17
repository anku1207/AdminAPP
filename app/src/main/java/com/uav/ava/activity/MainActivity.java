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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.uav.ava.BO.CacheBO;
import com.uav.ava.adapter.RecyclerViewAdapterMenu;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.permission.Session;
import com.uav.ava.util.ExceptionHandler;
import com.uav.ava.util.Utility;
import com.uav.ava.vo.DataAdapterVO;
import com.uav.ava.vo.PaymentTypeVO;
import com.uav.ava.volley.volley.VolleyResponseListener;
import com.uav.ava.volley.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.uav.ava.R;

public class MainActivity extends AppCompatActivity {
    TextView mainactivitytitle;

    ImageView logoutbtn;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    RecyclerViewAdapterMenu recyclerViewAdapterMenu;
    JSONObject  jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        getSupportActionBar().hide();



        getFlightNoSaleReasonscache();
        getServerDateCache();
        getPrincipalsCache();
        getPaymentCardList();
        getUnixTimeStamp();



        mainactivitytitle=findViewById(R.id.mainactivitytitle);
        logoutbtn=(ImageView)findViewById(R.id.logoutbtn);

        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,Context.MODE_PRIVATE);
        String resp=sharedPreferences.getString(ApplicationConstant.CACHE_USERNAME,null);
        try {

            if(!sharedPreferences.contains(ApplicationConstant.CACHE_RAILWAY_OFFLINE_MODE)){
                SharedPreferences.Editor edit= sharedPreferences.edit();
                edit.putString( ApplicationConstant.CACHE_RAILWAY_OFFLINE_MODE,"false");
                edit.apply();
                edit.commit();
            }


            jsonObject =new JSONObject(resp);

            Log.w("Mainactivi123",jsonObject.toString());

            mainactivitytitle.setText(jsonObject.getString("userName"));

        } catch (Exception e) {
            Utility.exceptionAlertDialog(MainActivity.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
        getPaymentTypes();
        getEmployeeName();

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ApplicationConstant.CACHE_USERNAME);
                editor.commit();
                startActivity(new Intent(MainActivity.this,Login_Activity.class));
                finish();

            }
        });

        ArrayList<DataAdapterVO> dataList =  getDataList();
        recyclerView= (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //creating recyclerview adapter
        recyclerViewAdapterMenu = new RecyclerViewAdapterMenu(this, dataList);
        recyclerView.setAdapter(recyclerViewAdapterMenu);

    }
    public  ArrayList<DataAdapterVO> getDataList(){
        ArrayList<DataAdapterVO> datalist = new ArrayList<>();
        try{

            DataAdapterVO dataAdapterVO ;


            if(jsonObject.getString("officeTypeId").equals("1")){


                dataAdapterVO=new DataAdapterVO();
                dataAdapterVO.setText("Aviation Sales");
                dataAdapterVO.setAssociatedValue(ApplicationConstant.AVIATION_SALE);
                dataAdapterVO.setActivityname(Flight_Scan.class);
                dataAdapterVO.setImage(R.drawable.aviation_sales);
                datalist.add(dataAdapterVO);

                dataAdapterVO = new DataAdapterVO();
                dataAdapterVO.setText("  Flight No  Protocol");
                dataAdapterVO.setActivityname(FlightNoProtocol.class);
                dataAdapterVO.setImage(R.drawable.flight);
                datalist.add(dataAdapterVO);


                dataAdapterVO=new DataAdapterVO();
                dataAdapterVO.setText("Rail Sales");
                dataAdapterVO.setAssociatedValue(null);
                dataAdapterVO.setActivityname(Railway_Sales.class);
                dataAdapterVO.setImage(R.drawable.retail_sales);
                datalist.add(dataAdapterVO);

            }else if(jsonObject.getString("officeTypeId").equals("6")){
                if(jsonObject.getString("verticalTypeId").equals("4")){
                    dataAdapterVO=new DataAdapterVO();
                    dataAdapterVO.setText("Rail Sales");
                    dataAdapterVO.setAssociatedValue(null);
                    dataAdapterVO.setActivityname(Railway_Sales.class);
                    dataAdapterVO.setImage(R.drawable.retail_sales);
                    datalist.add(dataAdapterVO);
                }else if(jsonObject.getString("verticalTypeId").equals("8")){
                    dataAdapterVO=new DataAdapterVO();
                    dataAdapterVO.setText("Retail Sales");
                    dataAdapterVO.setAssociatedValue(ApplicationConstant.AVIATION_SALE);
                    dataAdapterVO.setActivityname(Retail_Sales.class);
                    dataAdapterVO.setImage(R.drawable.retail_sales);
                    datalist.add(dataAdapterVO);
                }

                dataAdapterVO = new DataAdapterVO();
                dataAdapterVO.setText("Cancel Invoice");
                dataAdapterVO.setActivityname(Cancel_Invoice.class);
                dataAdapterVO.setImage(R.drawable.cancel_invoice);
                datalist.add(dataAdapterVO);

            }

            dataAdapterVO = new DataAdapterVO();
            dataAdapterVO.setText("Previous Invoices");
            //dataAdapterVO.setAssociatedValue(ApplicationConstant.AVIATION_SALE);
            dataAdapterVO.setImage(R.drawable.comingsoon);
            dataAdapterVO.setActivityname(InvoiceList.class);
            datalist.add(dataAdapterVO);

            dataAdapterVO = new DataAdapterVO();
            dataAdapterVO.setText("Setting");
            //dataAdapterVO.setAssociatedValue(ApplicationConstant.AVIATION_SALE);
            dataAdapterVO.setImage(R.drawable.setting);
            dataAdapterVO.setActivityname(SettingApp.class);
            datalist.add(dataAdapterVO);

        /*
        dataAdapterVO = new DataAdapterVO();
        dataAdapterVO.setText("ComingSoon");
        dataAdapterVO.setAssociatedValue(ApplicationConstant.MENU_MAIN_TOP_KEY_AWBQUERY);
        dataAdapterVO.setImage(R.drawable.comingsoon);
        datalist.add(dataAdapterVO);
        */

        }catch (Exception e){

            Utility.exceptionAlertDialog(MainActivity.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }

        return  datalist;

    }

    public void  getFlightNoSaleReasonscache(){
        VolleyUtils.makeJsonObjectRequest(this, CacheBO.getFlightNoSaleReasonscache(), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;
                if(response.get("status").equals("fail")){
                    VolleyUtils.furnishErrorMsg(  "Forwarder" ,response, MainActivity.this);
                    return;
                }
                SharedPreferences.Editor edit= sharedPreferences.edit();
                edit.putString( ApplicationConstant.CACHE_FLIGHTNOSALEREASONS, response.toString());
                edit.apply();
                edit.commit();
            }
        });
    }

    public void  getPrincipalsCache(){
        VolleyUtils.makeJsonObjectRequest(this, CacheBO.getPrincipals(), new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;
                if(response.get("status").equals("fail")){
                    VolleyUtils.furnishErrorMsg(  "Error !" ,response, MainActivity.this);
                    return;
                }
                SharedPreferences.Editor edit= sharedPreferences.edit();
                edit.putString( ApplicationConstant.PRINCIPAL_CACHE, response.toString());
                edit.apply();
                edit.commit();
            }
        });
    }


    public void  getServerDateCache(){
        VolleyUtils.makeJsonObjectRequest(this, CacheBO.getServerDate(), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;
                SharedPreferences.Editor edit= sharedPreferences.edit();
                edit.putString( ApplicationConstant.CACHE_SERVER_DATE, response.toString());
                edit.apply();
                edit.commit();
            }
        });
    }

    public void  getPaymentTypes(){
        try {
            VolleyUtils.makeJsonObjectRequest(this, CacheBO.getPaymentTypes(Integer.parseInt(jsonObject.getString("userId"))), new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }
                @Override
                public void onResponse(Object resp) throws JSONException {
                    JSONObject response = (JSONObject) resp;
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.d("Payment Type",jsonArray.toString());

                    JSONArray swappArray = new JSONArray();
                    for(int i=0; i< jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.getInt("paymentTypeID") == PaymentTypeVO.CASH || jsonObject.getInt("paymentTypeID") == PaymentTypeVO.EZETAP|| jsonObject.getInt("paymentTypeID") == PaymentTypeVO.UPI){//|| jsonObject.getInt("paymentTypeID") == PaymentTypeVO.EZETAP
                            if(jsonObject.getInt("paymentTypeID") == PaymentTypeVO.EZETAP){ //jsonObject.getInt("paymentTypeID") == PaymentTypeVO.EZETAP
                                jsonObject.put("paymentTypeName","Credit Card");
                            }
                            swappArray.put(jsonObject);
                        }
                    }
                    response.put("data", swappArray);

                    Log.w("payre",response.toString());
                    SharedPreferences.Editor edit= sharedPreferences.edit();
                    edit.putString( ApplicationConstant.CACHE_PAYMENT_TYPE, response.toString());
                    edit.apply();
                    edit.commit();
                }
            });
        } catch (Exception e) {
            Utility.exceptionAlertDialog(MainActivity.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
    }




    public void  getPaymentCardList(){
        try {
            VolleyUtils.makeJsonObjectRequest(this, CacheBO.getPaymentCardList(), new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }
                @Override
                public void onResponse(Object resp) throws JSONException {


                    JSONObject response = (JSONObject) resp;

                    Log.w("cardlist",response.toString());
                    SharedPreferences.Editor edit= sharedPreferences.edit();
                    edit.putString( ApplicationConstant.CACHE_PAYMENT_CARD_LIST, response.toString());
                    edit.apply();
                    edit.commit();
                }
            });
        } catch (Exception e) {
            Utility.exceptionAlertDialog(MainActivity.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }


    }



    public void  getEmployeeName(){



        try {
                 VolleyUtils.makeJsonObjectRequest(this, CacheBO.getEmployeeName(Integer.parseInt(jsonObject.getString("userId"))), new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }
                @Override
                public void onResponse(Object resp) throws JSONException {
                    JSONObject response = (JSONObject) resp;
                    Log.w("employee",response.toString());
                    SharedPreferences.Editor edit= sharedPreferences.edit();
                    edit.putString( ApplicationConstant.CACHE_EMPLOYEE_LIST, response.toString());
                    edit.apply();
                    edit.commit();
                }
            });
        } catch (Exception e) {
            Utility.exceptionAlertDialog(MainActivity.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
    }

    public void  getUnixTimeStamp(){
        VolleyUtils.makeJsonObjectRequest(this, CacheBO.getUnixTimeStamp(), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp)  {
                JSONObject response = (JSONObject) resp;
                SharedPreferences.Editor edit= sharedPreferences.edit();
                edit.putString( ApplicationConstant.CACHE_UNIXTIME_STAMP, response.toString());
                edit.apply();
                edit.commit();
            }
        });
    }
}

