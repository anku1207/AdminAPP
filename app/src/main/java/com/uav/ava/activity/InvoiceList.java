package com.uav.ava.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uav.ava.BO.CacheBO;
import com.uav.ava.BO.InvoiceBO;
import com.uav.ava.BO.MobileOTPBO;
import com.uav.ava.R;
import com.uav.ava.adapter.InvoiceListAdapter;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.permission.Session;
import com.uav.ava.vo.ConnectionVO;
import com.uav.ava.vo.DataAdapterVO;
import com.uav.ava.vo.UserVO;
import com.uav.ava.volley.volley.VolleyResponseListener;
import com.uav.ava.volley.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InvoiceList extends AppCompatActivity {

    RecyclerView recyclerView;//ava
    ArrayList<DataAdapterVO> dataList;//ava
    InvoiceListAdapter invoiceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);

        ImageView backButton=findViewById(R.id.back_activity_button);
        recyclerView = findViewById(R.id.recycler);//ava
        dataList=new ArrayList<DataAdapterVO>();
        getSupportActionBar().hide();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Gson gson = new Gson();




        VolleyUtils.makeJsonObjectRequest(this, InvoiceBO.getInvoiceList(Session.getUserId(InvoiceList.this),"0","10"), new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;
                if(response.get("status").equals("fail")){
                    VolleyUtils.furnishErrorMsg(  "Error !" ,response, InvoiceList.this);
                    return;
                }

                JSONArray jsonArray = response.getJSONArray("data");
                for (int i=0; i< jsonArray.length(); i++){
                    Gson gson = new Gson();
                    DataAdapterVO dataAdapterVO =  gson.fromJson(jsonArray.getJSONObject(i).toString(), DataAdapterVO.class);
                    dataList.add(dataAdapterVO);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager( InvoiceList.this,LinearLayoutManager.VERTICAL,false));
                invoiceListAdapter = new InvoiceListAdapter(InvoiceList.this, dataList,true);
                recyclerView.setAdapter(invoiceListAdapter);

            }
        });




    }
}
