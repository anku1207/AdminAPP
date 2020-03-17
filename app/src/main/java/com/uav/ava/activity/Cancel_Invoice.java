package com.uav.ava.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uav.ava.BO.InvoiceBO;
import com.uav.ava.R;
import com.uav.ava.adapter.CancelInvoiceListAdapter;
import com.uav.ava.adapter.InvoiceListAdapter;
import com.uav.ava.permission.Session;
import com.uav.ava.vo.DataAdapterVO;
import com.uav.ava.volley.volley.VolleyResponseListener;
import com.uav.ava.volley.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cancel_Invoice extends AppCompatActivity {

    RecyclerView recyclerView;//ava
    ArrayList<DataAdapterVO> dataList;//ava
    CancelInvoiceListAdapter invoiceListAdapter =new CancelInvoiceListAdapter();
    boolean isScrolling=false;
    LinearLayoutManager manager;

    int currentItems;
    int totalItems;
    int scrollOutItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel__invoice);

        ImageView backButton=findViewById(R.id.back_activity_button);
        recyclerView = findViewById(R.id.recycler);//ava


        manager=new LinearLayoutManager(Cancel_Invoice.this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(manager);


        dataList=new ArrayList<DataAdapterVO>();
        getSupportActionBar().hide();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addDate();
/*
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                    // Now I have to check if the user has scrolled down or up.
                    isScrolling=true;

                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                 currentItems=manager.getChildCount();
                 totalItems=manager.getItemCount();
                 scrollOutItems=manager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems+scrollOutItems ==totalItems)) {
                    isScrolling = false;
                    addDate();
                }


            }
        });
*/




    }

    public void addDate(){
        VolleyUtils.makeJsonObjectRequest(this, InvoiceBO.getInvoiceList(Session.getUserId(Cancel_Invoice.this),"0","10"), new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;
                if(response.get("status").equals("fail")){
                    VolleyUtils.furnishErrorMsg(  "Error !" ,response, Cancel_Invoice.this);
                    return;
                }

                JSONArray jsonArray = response.getJSONArray("data");
                for (int i=0; i< jsonArray.length(); i++){
                    Gson gson = new Gson();
                    DataAdapterVO dataAdapterVO =  gson.fromJson(jsonArray.getJSONObject(i).toString(), DataAdapterVO.class);
                    dataList.add(dataAdapterVO);
                   // invoiceListAdapter.notifyDataSetChanged();
                }
                invoiceListAdapter = new CancelInvoiceListAdapter(Cancel_Invoice.this, dataList,true);
                recyclerView.setAdapter(invoiceListAdapter);

                //invoiceListAdapter.notifyDataSetChanged();




            }
        });


    }
}
