package com.uav.ava.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uav.ava.R;
import com.uav.ava.adapter.Offer_Recyclerview;
import com.uav.ava.adapter.RecyclerViewAdapterMenu;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.util.ExceptionHandler;
import com.uav.ava.util.Utility;
import com.uav.ava.vo.DataAdapterVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Offer extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        RecyclerView recyclerView = findViewById(R.id.recycler);

        getSupportActionBar().hide();


        TextView activitytitle=findViewById(R.id.activitytitle);
        ImageView back_activity_button=findViewById(R.id.back_activity_button);



        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String resp=sharedPreferences.getString(ApplicationConstant.CACHE_USERNAME,null);
        try {
           JSONObject jsonObject =new JSONObject(resp);
            activitytitle.setText(jsonObject.getString("userName"));
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Offer.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        ArrayList<DataAdapterVO> dataList =  getDataList();

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        Offer_Recyclerview recyclerViewAdapterMenu = new Offer_Recyclerview(this, dataList);
        recyclerView.setAdapter(recyclerViewAdapterMenu);
        recyclerView.setHasFixedSize(true);


        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Offer.this,Payment.class));
            }
        });
    }

    public  ArrayList<DataAdapterVO> getDataList(){
        ArrayList<DataAdapterVO> datalist = new ArrayList<>();
        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String jsonString= (String)sharedPreferences.getString( ApplicationConstant.CACHE_PROMOTION,null);

        String CARDLIST= (String)sharedPreferences.getString( ApplicationConstant.CACHE_PAYMENT_CARD_LIST,null);


        try {

            JSONObject cardobj=new JSONObject(CARDLIST);

            Log.w("data",jsonString.toString());


            JSONArray jsonArraycard=cardobj.getJSONArray("data");



            JSONArray jsonArray =new JSONArray(jsonString);
            DataAdapterVO dataAdapterVO = new DataAdapterVO();

            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                dataAdapterVO = new DataAdapterVO();
                dataAdapterVO.setText(jsonObject1.getString("PromoMessage"));

                for(int j=0;j<jsonArraycard.length();j++){

                    JSONObject jsonObject=jsonArraycard.getJSONObject(j);

                    if(Integer.parseInt(jsonObject1.getString("PaymentCardId"))==Integer.parseInt(jsonObject.getString("CardID"))){

                        if((jsonObject.getString("CardName")).toLowerCase().contains("master")){
                            dataAdapterVO.setImage(R.drawable.mastercard);
                        }else if((jsonObject.getString("CardName")).toLowerCase().contains("visa")){
                            dataAdapterVO.setImage(R.drawable.visa);
                        }else {
                            dataAdapterVO.setImage(R.drawable.comingsoon);
                        }
                    }
                }
                datalist.add(dataAdapterVO);
            }
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Offer.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
        return  datalist;
    }

}
