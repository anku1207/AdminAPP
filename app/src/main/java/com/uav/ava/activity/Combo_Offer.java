package com.uav.ava.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mosambee.lib.TRACE;
import com.uav.ava.BO.ProductBO;
import com.uav.ava.MosambeeBarcode.HendleMessageScan;
import com.uav.ava.MosambeeBarcode.MosambeePrintScanTransactionResult;
import com.uav.ava.MosambeeBarcode.SerialPortIOManage;
import com.uav.ava.MosambeeBarcode.SerialPortService;
import com.uav.ava.R;
import com.uav.ava.adapter.Product_Scan_Adapter;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.permission.PermissionHandler;
import com.uav.ava.util.Utility;
import com.uav.ava.vo.DataAdapterVO;
import com.uav.ava.volley.volley.VolleyResponseListener;
import com.uav.ava.volley.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Templates;

public class Combo_Offer extends AppCompatActivity implements MosambeePrintScanTransactionResult {
    private MyBroadcastReceiver myReceiver;
    String barCodeElement=null;


    JSONObject jsonObject,passengerdataObjec,unixobj;
    SharedPreferences sharedPreferences;
    JSONArray jsonArraydata;
    Product_Scan_Adapter product_scan_adapter;
    ArrayList<DataAdapterVO> dataList;//ava
    RecyclerView recyclerView;//ava
    EditText productcode;
    JSONArray newProductarry;

    int REQ_BARCODE=1001;
    JSONArray proarry;
    String Primarycode=null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo__offer);
        getSupportActionBar().hide();
        TextView activitytitle=findViewById(R.id.activitytitle);
        recyclerView = findViewById(R.id.recycler);//ava
        dataList=new ArrayList<DataAdapterVO>();
        productcode=findViewById(R.id.productcode);
        ImageView scanproductcode=findViewById(R.id.scanproductcode);
        ImageView back_activity_button=findViewById(R.id.back_activity_button);


        newProductarry=new JSONArray();

        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String comboproductcache=sharedPreferences.getString(ApplicationConstant.CACHE_COMBO_PRODUCT,null);
/*

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(productcode.getText().toString().equals("")){
                    productcode.setError(Utility.getErrorSpannableString(getApplicationContext(), "Code is Required"));
                    return;
                }
                Scanproduct(productcode.getText().toString());
            }
        });
*/



       // productcode.setShowSoftInputOnFocus(false);
        productcode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                barCodeElement ="product";
                Intent intent = new Intent();
                intent.putExtra("openPort",true);
                intent.putExtra("deviceType","Scanner");
                intent.setClassName("com.mosambee.printService", "com.mosambee.printService.PrinterService");
                startService(intent);
                return false;
            }
        });



        scanproductcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionHandler.cameraPermission(Combo_Offer.this)) {
                    return;
                }
                startActivityForResult(new Intent(Combo_Offer.this,BarcodeScanner.class), REQ_BARCODE);
            }
        });

        productcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(productcode.getText().toString().equals("")){
                        productcode.setError(Utility.getErrorSpannableString(getApplicationContext(), "Product Code is Required"));
                    }else {
                        Scanproduct(productcode.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });

        String unixobject=sharedPreferences.getString(ApplicationConstant.CACHE_UNIXTIME_STAMP,null);
        String passengerdata=sharedPreferences.getString(ApplicationConstant.CACHE_PASSENGER_DETAIL,null);


        String resp=sharedPreferences.getString(ApplicationConstant.CACHE_USERNAME,null);
        try {
            passengerdataObjec=new JSONObject(passengerdata);
            unixobj=new JSONObject(unixobject);
            jsonObject =new JSONObject(resp);
            activitytitle.setText(jsonObject.getString("userName"));


            proarry=new JSONArray(comboproductcache);
            for (int i = 0; i < proarry.length(); i++){
                JSONObject jsonObject =proarry.getJSONObject(i);
                JSONObject jsonObject1=jsonObject.getJSONObject("ProductDetail");
                JSONObject jsonObject2 =jsonObject.getJSONObject("Pcs");
                JSONObject saleobj=jsonObject.getJSONObject("SaleSchemeProductDetail");
                if(jsonObject.getString("Type").equals("Primary")){
                    Primarycode=jsonObject1.getString("ProductSKU");




                    String mop=null;
                    Object aObj = jsonObject1.get("IndigoMOP");
                    if(Integer.parseInt(passengerdataObjec.getString("principalno"))==12 && aObj instanceof JSONObject) {
                        JSONObject jsonObject3 = jsonObject1.getJSONObject("IndigoMOP");


                        if(!jsonObject3.getString("mop").equals("null")){
                            mop=jsonObject3.getString("mop");
                        }else {
                            mop=jsonObject1.getString("ProductMOP");
                        }

                    }else {
                        mop = jsonObject1.getString("ProductMOP");

                    }



                    addProductinit(jsonObject1.getString("ProductSKU"),jsonObject1.getString("ProductName") ,jsonObject1.getString("ProductMRP") ,mop,mop,jsonObject2.getString("PcsID"),jsonObject,jsonObject.getString("Type"));
                }else if(jsonObject.getString("Type").equals("Bind")){
                    addProductinit(jsonObject1.getString("ProductSKU"),jsonObject1.getString("ProductName") ,jsonObject1.getString("ProductMRP") ,jsonObject1.getString("ProductMOP"),saleobj.getString("BindDiscountRate"),jsonObject2.getString("PcsID"),jsonObject,jsonObject.getString("Type"));
                }
            }
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Combo_Offer.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }



        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backactivitydata();

            }
        });
    }


  @Override
  public void onBackPressed() {
        backactivitydata();
  }
  public  void addProductinit(String code,String name ,String mrp ,String mop,String Total ,String pcsid,JSONObject resp,String type){

        try {
            DataAdapterVO dataAdapterVO = new DataAdapterVO();
            dataAdapterVO.setProductname(name);
            dataAdapterVO.setMop(mop);
            dataAdapterVO.setTotal(Total);
            dataAdapterVO.setProductcode(code);
            dataAdapterVO.setPcsid(pcsid);
            dataAdapterVO.setResponse(resp);
            dataAdapterVO.setQuantity("1");
            dataAdapterVO.setProducttype(type);
            dataList.add(dataAdapterVO);
            renderlist(dataList);
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Combo_Offer.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }

    }


    public   void renderlist(ArrayList<DataAdapterVO> datalist) throws JSONException {
        recyclerView.setLayoutManager(new LinearLayoutManager( Combo_Offer.this,LinearLayoutManager.VERTICAL,false));
        product_scan_adapter = new Product_Scan_Adapter(Combo_Offer.this, dataList,false);
        recyclerView.setAdapter(product_scan_adapter);
//        recyclerView.setHasFixedSize(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_BARCODE) {
                String scandata=data.getStringExtra("key");
                productcode.setText(scandata);
                Scanproduct(scandata);

            }
        }
    }




    public  void  Scanproduct(String code){

        try {
            VolleyUtils.makeJsonObjectRequest(this, ProductBO.getBindProductList(unixobj.getString("timestamp"),Utility.changeDateFormat(passengerdataObjec.getString("saledate"),Combo_Offer.this),Primarycode,Integer.parseInt(jsonObject.getString("userId")),code,passengerdataObjec.getString("principalno")), new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }
                @Override
                public void onResponse(Object resp) throws JSONException {

                    JSONObject response = (JSONObject) resp;

                    Log.w("Comboresp",response.toString());

                    if (response.has("ErrorMessage")) {
                        productcode.setError(Utility.getErrorSpannableString(getApplicationContext(), response.getString("ErrorMessage")));

                        return;
                    } else {
                            Intent intent12 = new Intent();
                            intent12.putExtra("key",response.toString());
                            setResult(RESULT_OK,intent12);
                            finish() ;
                    }
                }
            });
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Combo_Offer.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
    }


    public void backactivitydata(){

        Intent intent12 = new Intent();
        intent12.putExtra("key","");
        setResult(RESULT_OK,intent12);
        finish() ;
    }

    @Override
    public void onStart() {
        myReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MY_ACTION");
        registerReceiver(myReceiver, intentFilter);
        TRACE.i("===== onStart");
        System.out.println("===== onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        TRACE.i("===== onResume");
        System.out.println("===== onResume ");

    }


    @Override
    public void onStop() {
        TRACE.i("===== onStop");
        System.out.println("===== onStop " );
        unregisterReceiver(myReceiver);
        super.onStop();
    }


    @Override
    public void onScanResult(String data) {
        if(barCodeElement.equals("product")){
            productcode.setText(data);
            Scanproduct(data);
        }
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {

        public MyBroadcastReceiver() {
            super();
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            TRACE.i("===== onReceive");
            Bundle bundle = intent.getExtras();
            String deviceType = bundle.getString("deviceType");
            int deviceState = bundle.getInt("deviceState");
            int deviceOpen1 = bundle.getInt("deviceOpen1");
            int deviceOpen2 = bundle.getInt("deviceOpen2");
            TRACE.i("===== "+deviceType);
            assert deviceType != null;
            switch (deviceType!=null?deviceType:""){
                case "Scanner":
                    TRACE.i("deviceState::"+deviceState+"deviceOpen1::"+deviceOpen1);
                    if (deviceState == 0 && deviceOpen1 == 0) {
                        try {
                            TRACE.i("-----------in openTheSerialPort");
                            Intent startIntent2 = new Intent(Combo_Offer.this, SerialPortService.class);
                            startIntent2.putExtra("serial", "dev/ttyMT1");
                            startService(startIntent2);
                            HendleMessageScan.handler.removeMessages(1000);
                            HendleMessageScan.handler.removeMessages(1001);
                            HendleMessageScan.handler.removeMessages(999);
                            HendleMessageScan.handler.sendEmptyMessageDelayed(999, 10);

                            TRACE.i("-----------in openTheScanHead");
                            SerialPortIOManage.getInstance().resetBuffer();
                            HendleMessageScan.handler.removeMessages(888);
                            HendleMessageScan.handler.removeMessages(999);
                            HendleMessageScan.handler.removeMessages(1000);
                            HendleMessageScan.handler.removeMessages(1001);
                            HendleMessageScan.handler.sendEmptyMessageDelayed(888, 1000);
                            SerialPortIOManage.getInstance().setCallBackActivity(Combo_Offer.this);
                        } catch (NoSuchMethodError | Exception er) {
                            Toast.makeText(getApplicationContext(), "Connection to scanner failed." + er.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), ""+deviceType +"\n else", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }





}
