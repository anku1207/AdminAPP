package com.uav.ava.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mosambee.lib.TRACE;
import com.uav.ava.BO.FlightBO;
import com.uav.ava.BO.ProductBO;
import com.uav.ava.MosambeeBarcode.HendleMessageScan;
import com.uav.ava.MosambeeBarcode.MosambeePrintScanTransactionResult;
import com.uav.ava.MosambeeBarcode.SerialPortIOManage;
import com.uav.ava.MosambeeBarcode.SerialPortService;
import com.uav.ava.R;
import com.uav.ava.adapter.Offer_Recyclerview;
import com.uav.ava.adapter.Product_Scan_Adapter;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.permission.PermissionHandler;
import com.uav.ava.util.ExceptionHandler;
import com.uav.ava.util.Utility;
import com.uav.ava.vo.DataAdapterVO;
import com.uav.ava.vo.DiaglogVO;
import com.uav.ava.volley.volley.VolleyResponseListener;
import com.uav.ava.volley.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Product_Scan extends AppCompatActivity implements MosambeePrintScanTransactionResult {
        String barCodeElement=null;

       private MyBroadcastReceiver myReceiver;
        SharedPreferences  sharedPreferences;
        JSONObject jsonObject,passengerdataObjec,unixobj;
        EditText productcode;
        Button productscnaactivity;
        int  RESULT_CODE =1001,REQ_BARCODE=1002;
        ImageView scanproductcode;
        JSONArray jsonArraydata;

        Product_Scan_Adapter product_scan_adapter;


        ArrayList<DataAdapterVO> dataList;//ava
        RecyclerView recyclerView;//ava

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__scan);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));


        ImageView back_activity_button=findViewById(R.id.back_activity_button);
        TextView activitytitle=findViewById(R.id.activitytitle);
        getSupportActionBar().hide();
        productcode=findViewById(R.id.productcode);

        scanproductcode=findViewById(R.id.scanproductcode);


        recyclerView = findViewById(R.id.recycler);//ava
        dataList=new ArrayList<DataAdapterVO>();

        productscnaactivity=findViewById(R.id.productscnaactivity);
     //  stk = (TableLayout) findViewById(R.id.table_main);

        productscnaactivity.setVisibility(View.GONE);

        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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


        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String passengerdata=sharedPreferences.getString(ApplicationConstant.CACHE_PASSENGER_DETAIL,null);
        String unixobject=sharedPreferences.getString(ApplicationConstant.CACHE_UNIXTIME_STAMP,null);

        String resp=sharedPreferences.getString(ApplicationConstant.CACHE_USERNAME,null);
        try {
            passengerdataObjec=new JSONObject(passengerdata);
            unixobj=new JSONObject(unixobject);


            jsonObject =new JSONObject(resp);
            activitytitle.setText(jsonObject.getString("userName"));
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Product_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }


        productscnaactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferencesproductdata();
                if(jsonArraydata.length()!=0){
                    getPromotionsPrincipalByWef();
                }else {
                    Toast.makeText(Product_Scan.this, "Please scan the product", Toast.LENGTH_SHORT).show();
                }

                //startActivity(new Intent(Product_Scan.this,Payment.class));
            }
        });





        scanproductcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionHandler.cameraPermission(Product_Scan.this)) {
                    return;
                }

                switch (ApplicationConstant.BARCODE_SCANNER) {
                    case ApplicationConstant.BARCODE_DETECTOR_CODE:
                        startActivityForResult(new Intent(Product_Scan.this,BarcodeScanner.class), REQ_BARCODE);
                        break;

                    case ApplicationConstant.BARCODE_ZXING_CODE:
                        zxingBarCode();
                        break;
                    default:
                        break;
                }

            }
        });





        // Keyboard enter  event
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

    }






    public  void getPromotionsPrincipalByWef(){
        try {
            VolleyUtils.makeJsonObjectRequest(this, ProductBO.getPromotionsPrincipalByWef(passengerdataObjec.getString("principalno"),passengerdataObjec.getString("flightdate")), new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }

                @Override
                public void onResponse(Object resp) throws JSONException {
                    JSONObject response = (JSONObject) resp;
                    Log.w("resp",response.toString());

                    JSONArray jsonArray=response.getJSONArray("promotion");
                    if(jsonArray.length()==0){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(ApplicationConstant.CACHE_PROMOTION);
                        editor.commit();
                        startActivity(new Intent(Product_Scan.this,Payment.class));
                    }else {
                        SharedPreferences.Editor edit= sharedPreferences.edit();
                        edit.putString( ApplicationConstant.CACHE_PROMOTION, jsonArray.toString());
                        edit.apply();
                        edit.commit();
                        startActivity(new Intent(Product_Scan.this,Offer.class));
                    }


                }
            });
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Product_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }



    }





    public  void  Scanproduct(String code){
        try {
            Log.w("data",unixobj.getString("timestamp")+"=="+Utility.changeDateFormat(passengerdataObjec.getString("saledate"),Product_Scan.this)+"=="+productcode.getText().toString()+"=="+Integer.parseInt(jsonObject.getString("userId")));
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Product_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
        try {
            VolleyUtils.makeJsonObjectRequest(this, ProductBO.getBindProductList(unixobj.getString("timestamp"),Utility.changeDateFormat(passengerdataObjec.getString("saledate"),Product_Scan.this),code,Integer.parseInt(jsonObject.getString("userId")),null,passengerdataObjec.getString("principalno")), new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }
                @Override
                public void onResponse(Object resp) throws JSONException {

                        JSONObject response = (JSONObject) resp;

                    Log.w("resp",response.toString());

                        if (response.has("ErrorMessage")) {
                            productcode.setError(Utility.getErrorSpannableString(getApplicationContext(), response.getString("ErrorMessage")));

                            return;
                        } else {

                            List<String> keysArray =new ArrayList<String>();
                            Iterator<String> keys = response.keys();
                            while (keys.hasNext()) {
                                String objectkey = keys.next();
                                keysArray.add(objectkey);
                            }


                          //  String key = Utility.getJsonKey(response);

                            if(keysArray.size()<2){
                                JSONObject jsonObject = response.getJSONObject(keysArray.get(0));
                                JSONObject jsonObject1 = jsonObject.getJSONObject("ProductDetail");
                                JSONObject jsonObject2 = jsonObject.getJSONObject("Pcs");

                                String passengerdata=sharedPreferences.getString(ApplicationConstant.CACHE_PASSENGER_DETAIL,null);
                                JSONObject object=new JSONObject(passengerdata);

                                String mop;
                                Object aObj = jsonObject1.get("IndigoMOP");
                                if(Integer.parseInt(object.getString("principalno"))==12 && aObj instanceof JSONObject){
                                    JSONObject jsonObject3 =jsonObject1.getJSONObject("IndigoMOP");

                                    if(!jsonObject3.getString("mop").equals("null")){
                                        mop=jsonObject3.getString("mop");
                                        response.put("moptag",jsonObject3.getString("attribute_code"));
                                    }else{
                                        mop=jsonObject1.getString("ProductMOP");
                                    }
                                        response.put("TotalAmount",mop);
                                }else{
                                    mop=jsonObject1.getString("ProductMOP");
                                    response.put("TotalAmount",jsonObject1.getString("ProductMOP"));

                                }





                                addProductinit(jsonObject1.getString("ProductSKU"), jsonObject1.getString("ProductName"), jsonObject1.getString("ProductMRP"),mop,mop,jsonObject2.getString("PcsID"),response,jsonObject.getString("Type"));
                                productcode.setText("");
                                productscnaactivity.setEnabled(true);
                            }else {
                               // char keyee=Character.toUpperCase(key.charAt(key.length() - 1));
                                JSONArray jsonArray =new JSONArray();




                                for (int i = 0; i < keysArray.size(); i++){
                                    JSONObject comboprojson = response.getJSONObject(keysArray.get(i));
                                    jsonArray.put(comboprojson);
                                }
                                SharedPreferences.Editor edit= sharedPreferences.edit();

                                JSONObject object =new JSONObject();
                                object.put(keysArray.get(0),jsonArray.getJSONObject(0));
                                edit.putString( ApplicationConstant.CACHE_COMBO_PRIMARY_PRODUCT, object.toString());
                                edit.putString( ApplicationConstant.CACHE_COMBO_PRODUCT, jsonArray.toString());
                                edit.apply();
                                edit.commit();
                                startActivityForResult(new Intent(Product_Scan.this,Combo_Offer.class),RESULT_CODE);

                            }

                            if(dataList.size()!=0){
                                productscnaactivity.setVisibility(View.VISIBLE);
                            }else{
                                productscnaactivity.setVisibility(View.GONE);
                            }
                        }
                }
            });
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Product_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
    }



    private void sharedPreferencesproductdata(){
            jsonArraydata=new JSONArray();

            Log.w("dataList",dataList.toString());

            for(DataAdapterVO dataAdapterVO :dataList){
                JSONObject productscandata = new JSONObject();

                try {
                    productscandata.put(dataAdapterVO.getPcsid(),dataAdapterVO.getResponse());
                    jsonArraydata.put(productscandata);
                } catch (Exception e) {
                    Utility.exceptionAlertDialog(Product_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                }

            }

            SharedPreferences.Editor edit= sharedPreferences.edit();
            edit.putString( ApplicationConstant.CACHE_PRODUCT_SCAN, jsonArraydata.toString());
            edit.apply();
            edit.commit();
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
            if(type.equals("Primary")){
                dataAdapterVO.setImage(R.drawable.cancel);
            }else {
                DataAdapterVO dataAdapterVO1= dataList.get(dataList.size()-1);
                dataAdapterVO1.setAssociativeCombo(1);
            }
            dataList.add(dataAdapterVO);
            renderlist(dataList);
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Product_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }

    }
    //ava
    public   void renderlist(ArrayList<DataAdapterVO> datalist) throws JSONException {
        recyclerView.setLayoutManager(new LinearLayoutManager( Product_Scan.this,LinearLayoutManager.VERTICAL,false));
        product_scan_adapter = new Product_Scan_Adapter(Product_Scan.this, dataList,true);
        recyclerView.setAdapter(product_scan_adapter);
//        recyclerView.setHasFixedSize(true);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (ApplicationConstant.BARCODE_SCANNER) {
            case ApplicationConstant.BARCODE_ZXING_CODE:
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    if (result.getContents() == null) {
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                    }else{
                        String scandata=result.getContents();
                        productcode.setText(scandata);
                        Scanproduct(scandata);
                    }
                    return;
                }

                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_BARCODE) {
                String scandata=data.getStringExtra("key");
                productcode.setText(scandata);
                Scanproduct(scandata);

            }

            if(requestCode==RESULT_CODE){
                productcode.setText("");

                try {

                    JSONObject response;

                    if(!data.getStringExtra("key").equals("")){
                        response=new JSONObject(data.getStringExtra("key"));


                        Log.w("reskasdfadf",response.toString());

                        List<String> keysArray =new ArrayList<String>();
                        Iterator<String> keys = response.keys();
                        while (keys.hasNext()) {
                            String objectkey = keys.next();
                            keysArray.add(objectkey);
                        }


                        for (int i = 0; i < keysArray.size(); i++){
                            JSONObject comboprojson = response.getJSONObject(keysArray.get(i));
                            JSONObject jsonObject1=comboprojson.getJSONObject("ProductDetail");
                            JSONObject jsonObject2 =comboprojson.getJSONObject("Pcs");
                            JSONObject saleobj=comboprojson.getJSONObject("SaleSchemeProductDetail");
                            JSONObject datajson=new JSONObject();
                            datajson.put(keysArray.get(i),comboprojson);
                            if(comboprojson.getString("Type").equals("Primary")){

                                String passengerdata=sharedPreferences.getString(ApplicationConstant.CACHE_PASSENGER_DETAIL,null);
                                JSONObject object=new JSONObject(passengerdata);


                                String mop=null;
                                Object aObj = jsonObject1.get("IndigoMOP");
                                if(Integer.parseInt(object.getString("principalno"))==12 && aObj instanceof JSONObject){
                                    JSONObject jsonObject3 =jsonObject1.getJSONObject("IndigoMOP");

                                    if(!jsonObject3.getString("mop").equals("null")){
                                        mop=jsonObject3.getString("mop");
                                        datajson.put("moptag",jsonObject3.getString("attribute_code"));
                                    }else {
                                        mop=jsonObject1.getString("ProductMOP");
                                    }

                                    response.put("TotalAmount",mop);


                                    datajson.put("TotalAmount",mop);
                                }else{
                                    mop=jsonObject1.getString("ProductMOP");
                                    datajson.put("TotalAmount",jsonObject1.getString("ProductMOP"));
                                }
                                addProductinit(jsonObject1.getString("ProductSKU"),jsonObject1.getString("ProductName") ,jsonObject1.getString("ProductMRP") ,mop,mop,jsonObject2.getString("PcsID"),datajson,comboprojson.getString("Type"));
                            }else if(comboprojson.getString("Type").equals("Bind")){
                                datajson.put("TotalAmount",saleobj.getString("BindDiscountRate"));
                                addProductinit(jsonObject1.getString("ProductSKU"),jsonObject1.getString("ProductName") ,jsonObject1.getString("ProductMRP") ,jsonObject1.getString("ProductMOP"),saleobj.getString("BindDiscountRate"),jsonObject2.getString("PcsID"),datajson,comboprojson.getString("Type"));
                            }

                        }


                    }else {


                        String unixobject=sharedPreferences.getString(ApplicationConstant.CACHE_COMBO_PRIMARY_PRODUCT,null);
                        JSONObject object =new JSONObject(unixobject);


                        String key=Utility.getJsonKey(object);
                        JSONObject i=object.getJSONObject(key);

                        JSONObject jsonObject1=i.getJSONObject("ProductDetail");
                        JSONObject jsonObject2 =i.getJSONObject("Pcs");

                        String passengerdata=sharedPreferences.getString(ApplicationConstant.CACHE_PASSENGER_DETAIL,null);
                        JSONObject object1=new JSONObject(passengerdata);


                        String mop=null;
                        Object aObj = jsonObject1.get("IndigoMOP");
                        if(Integer.parseInt(object1.getString("principalno"))==12 && aObj instanceof JSONObject) {
                            JSONObject jsonObject3 = jsonObject1.getJSONObject("IndigoMOP");

                            if(!jsonObject3.getString("mop").equals("null")){
                                mop=jsonObject3.getString("mop");
                                object.put("moptag",jsonObject3.getString("attribute_code"));
                            }else {
                                mop=jsonObject1.getString("ProductMOP");
                            }
                            object.put("TotalAmount", mop);
                        }else {
                            mop = jsonObject1.getString("ProductMOP");
                            object.put("TotalAmount",jsonObject1.getString("ProductMOP"));
                        }

                        addProductinit(jsonObject1.getString("ProductSKU"),jsonObject1.getString("ProductName") ,jsonObject1.getString("ProductMRP") ,mop,mop,jsonObject2.getString("PcsID"),object,i.getString("Type"));


                    }




                    if(dataList.size()!=0){
                        productscnaactivity.setVisibility(View.VISIBLE);
                    }else{
                        productscnaactivity.setVisibility(View.GONE);
                    }




                } catch (Exception e) {
                    Utility.exceptionAlertDialog(Product_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                }
            }
        }
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
                            Intent startIntent2 = new Intent(Product_Scan.this, SerialPortService.class);
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
                            SerialPortIOManage.getInstance().setCallBackActivity(Product_Scan.this);
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

    private void zxingBarCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode");
        integrator.setOrientationLocked(false);
        integrator.setCameraId(0);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);

        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();

    }

}
