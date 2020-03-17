package com.uav.ava.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eze.api.EzeAPI;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mosambee.lib.MosCallback;
import com.mosambee.lib.ResultData;
import com.mosambee.lib.TransactionResult;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;
import com.uav.ava.BO.AviationSaleBO;
import com.uav.ava.BO.CacheBO;
import com.uav.ava.BO.CustomerDetailBO;
import com.uav.ava.BO.FlightBO;
import com.uav.ava.BO.OfferBO;
import com.uav.ava.BO.ProductBO;
import com.uav.ava.R;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.permission.PermissionHandler;


import com.uav.ava.permission.Session;
import com.uav.ava.util.ExceptionHandler;
import com.uav.ava.util.Utility;
import com.uav.ava.vo.ClpPointsVO;
import com.uav.ava.vo.ConnectionVO;
import com.uav.ava.vo.CouponVO;
import com.uav.ava.vo.DataAdapterVO;
import com.uav.ava.vo.PaymentTypeVO;
import com.uav.ava.vo.PosSalePcsVO;
import com.uav.ava.vo.PosSaleRefernceVO;
import com.uav.ava.vo.PosSaleVO;
import com.uav.ava.volley.volley.VolleyResponseListener;
import com.uav.ava.volley.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Payment extends AppCompatActivity implements TransactionResult{
    private static Handler handler;   //mosambee
    private static Context context; //mosambee


    Boolean promotionflag=false,couponflag=false;

    ResultData result=null;

    JSONObject CLPJson;
    JSONObject flightJson;
   // JSONObject passengerInfoJson;
    JSONArray productJson;
    JSONObject indigoJson;
    PosSaleVO posSaleVO;
    ClpPointsVO clpPointsVO;
    PosSaleRefernceVO posSaleRefernceVO;

    CouponVO couponVO;

    SharedPreferences sharedPreferences;
    JSONObject userJsonObject;
    EditText paymenttype,paymenttypeid,cardid,cardname,grosstotaltext,employeecode,employeeid,hfredeempoint,discountremarks,specialdiscount,invoicediscount,
            netamount,advanceamount,advanceremarks,lastdigit,batchno,approvalcode,coupon;
    int REQ_PAYMENT_TYPE=1001,REQ_PAYMENT_CARD_LIST=1002,REQ_EMPLOYEE_LIST=1003;
    LinearLayout cardlayout,batchlayout,couponlayout;

    double invoicedis=0.0,specialdis=0.0;
    double AdvanceAmount=0.0;
    int clpdiscountamt=0;

    int Productqty=0;
    Button getoffer;
    Integer PromotionId=null;
    String promoHash=null;
    double grosstotal =0.0;


    Button saveaviation,applycoupon;
    MosCallback moscCallback;
    private boolean isConnected;

    public PrinterInstance myPrinter;



    /* EzeeTap Request code
     * The response is sent back to your activity with a result code and request
     * code based
     */
    private final int REQUEST_CODE_INITIALIZE = 10001;
    private final int REQUEST_CODE_PREPARE_ = 10002;
    private final int REQUEST_CODE_SALE_TXN = 10006;
    private final int REQUEST_CODE_CASH_TXN = 10009;
    private final int REQUEST_CODE_SEARCH = 10010;
    private final int REQUEST_CODE_VOID = 10011;

    private final int EZETAP_INITIALIZE_ACTION=1001;
    private final int EZETAP_SALE_TXN_PREPARE_ACTION=1002;
    private final int EZETAP_SALE_TXN_ACTION=1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = this;
        setContentView(R.layout.activity_payment);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        getSupportActionBar().hide();
        ImageView back_activity_button=findViewById(R.id.back_activity_button);

        TextView activitytitle=findViewById(R.id.activitytitle);
        paymenttype=findViewById(R.id.paymenttype);
        paymenttypeid=findViewById(R.id.paymenttypeid);
        cardlayout=findViewById(R.id.cardlayout);
        batchlayout=findViewById(R.id.batchlayout);
        cardid=findViewById(R.id.cardid);
        cardname=findViewById(R.id.cardname);
        grosstotaltext=findViewById(R.id.grosstotal);
        employeecode=findViewById(R.id.employeecode);
        employeeid=findViewById(R.id.employeeid);
        hfredeempoint=findViewById(R.id.hfredeem);
        discountremarks=findViewById(R.id.discountremarks);
        specialdiscount=findViewById(R.id.specialdiscount);
        invoicediscount=findViewById(R.id.invoicediscount);
        netamount=findViewById(R.id.netamount);
        getoffer=findViewById(R.id.getoffer);
        advanceamount=findViewById(R.id.advanceamount);
        advanceremarks=findViewById(R.id.advanceremarks);
        saveaviation=findViewById(R.id.saveaviation);
        lastdigit=findViewById(R.id.lastdigit);
        batchno=findViewById(R.id.batchno);
        approvalcode=findViewById(R.id.approvalcode);
        applycoupon=findViewById(R.id.applycoupon);
        couponlayout=findViewById(R.id.couponlayout);
        coupon=findViewById(R.id.coupon);



        //set accessablity of elements
        grosstotaltext.setEnabled(false);
        hfredeempoint.setEnabled(false);
        netamount.setEnabled(false);
        specialdiscount.setEnabled(true);
        specialdiscount.setFocusable(true);
        invoicediscount.setEnabled(false);
        advanceremarks.setEnabled(false);
        advanceamount.setEnabled(false);
        discountremarks.setEnabled(false);



        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);


        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });










        //get shared preferneces and set json
        try {
            CLPJson = new JSONObject(sharedPreferences.getString(ApplicationConstant.CACHE_PASSENGERINFO, null));
            flightJson = new JSONObject(sharedPreferences.getString(ApplicationConstant.CACHE_PASSENGER_DETAIL, null)) ;
           // passengerInfoJson = new JSONObject(sharedPreferences.getString(ApplicationConstant.CACHE_PASSENGERINFO, null)) ;

            productJson = new JSONArray(sharedPreferences.getString(ApplicationConstant.CACHE_PRODUCT_SCAN, null)) ;
             if(sharedPreferences.getString(ApplicationConstant.CACHE_INDIGOONFLIGHTPAYMENT, null) !=null) {
                JSONObject dataJson = new JSONObject(sharedPreferences.getString(ApplicationConstant.CACHE_INDIGOONFLIGHTPAYMENT, null));
                indigoJson =dataJson.getJSONObject("data");
            }
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Payment.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }


        try {




            JSONObject manualpayment=new JSONObject();
            JSONArray manualpaymentarry= new JSONArray();
            JSONObject data=new JSONObject();
            data.put("paymentTypeName","Online Paid");
            data.put("paymentTypeID",15);
            manualpaymentarry.put(data);
            manualpayment.put("data",manualpaymentarry);
            SharedPreferences.Editor edit= sharedPreferences.edit();
            edit.putString( ApplicationConstant.CACHE_PAYMENT_TYPE_MANUAL, manualpayment.toString());
            edit.apply();
            edit.commit();




            //get username
            String resp=sharedPreferences.getString(ApplicationConstant.CACHE_USERNAME,null);
            userJsonObject =new JSONObject(resp);
            activitytitle.setText(userJsonObject.getString("userName"));

            //set  Indigo Advances
            if(indigoJson!=null){
                String discremarks=indigoJson.getString("message");
                AdvanceAmount= indigoJson.getInt("AdvanceAmount");
                advanceamount.setText(String.valueOf(AdvanceAmount));
                advanceremarks.setText(discremarks);
            }


             Log.w("productJson",productJson.toString());

            //product data
            List<PosSalePcsVO> productlist=new ArrayList<>();
            grosstotal=0.0;
            for (int i = 0 ; i < productJson.length(); i++) {
                JSONObject jsonObject =productJson.getJSONObject(i);
                String key= Utility.getJsonKey(jsonObject);
                JSONObject jsonObject1=jsonObject.getJSONObject(key);
                String key2= Utility.getJsonKey(jsonObject1);
                JSONObject productjson=jsonObject1.getJSONObject(key2);
                JSONObject jsonObject2=productjson.getJSONObject("ProductDetail");
                JSONObject pcsjson=productjson.getJSONObject("Pcs");

                PosSalePcsVO posSalePcsVO=new PosSalePcsVO();
                posSalePcsVO.setTotalAmount(jsonObject1.getString("TotalAmount"));
                posSalePcsVO.setPcsID(pcsjson.getString("PcsID"));
                posSalePcsVO.setProductID(jsonObject2.getString("ProductID"));
                posSalePcsVO.setPcsNumber(pcsjson.getString("PcsNumber"));
                posSalePcsVO.setProductCode(jsonObject2.getString("ProductSKU"));
                posSalePcsVO.setProductName(jsonObject2.getString("ProductName"));

                if(jsonObject1.has("moptag")){
                    posSalePcsVO.setMOPTag(jsonObject1.getString("moptag"));
                }

                if(!productjson.getString("SaleSchemeProductDetail").equals("null")){

                    Log.w("SaleSchemeCode",productjson.getString("SaleSchemeProductDetail"));

                    JSONObject object =new JSONObject(productjson.getString("SaleSchemeProductDetail"));
                    posSalePcsVO.setSaleSchemeCode(object.getString("SaleSchemeCode"));
                }

                posSalePcsVO.setMRP(jsonObject2.getString("ProductMRP"));
                posSalePcsVO.setMOP(jsonObject2.getString("ProductMOP"));

                productlist.add(posSalePcsVO);

                grosstotal  += Double.parseDouble(jsonObject1.getString("TotalAmount"));
                Productqty ++;
            }




            // set value in VO
            posSaleVO =  new PosSaleVO();





            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Timestamp ts = new Timestamp(((Date)df.parse(flightJson.getString("saledate"))).getTime());
            Date date = new Date();
            date.setTime(ts.getTime());
            posSaleVO.setInvoiceDate(new SimpleDateFormat("yyyy-MM-dd").format(date));




            ts = new Timestamp(((Date)df.parse(flightJson.getString("flightdate"))).getTime());
            date=new Date();
            date.setTime(ts.getTime());
            posSaleVO.setFlightDate(new SimpleDateFormat("yyyy-MM-dd").format(date));





            posSaleVO.setPassengerName(flightJson.getString("passengername"));
            posSaleVO.setFlightOrigin(flightJson.getString("flightoriginname"));
            posSaleVO.setFlightDestination(flightJson.getString("flightdestinationname"));
            posSaleVO.setTravelJulianDay(flightJson.getString("travelday"));
            posSaleVO.setSeatNumber(flightJson.getString("seatno"));
            posSaleVO.setPrincipalID(flightJson.getString("principalno"));
            posSaleVO.setFlightCode(flightJson.getString("flightnumber"));
            posSaleVO.setClassID(null);

            posSaleVO.setInvoiceDiscount((int) invoicedis);
            posSaleVO.setSpecialDiscount((int) specialdis);
            posSaleVO.setGrossTotal((int) grosstotal );
            posSaleVO.setCLPDiscount(clpdiscountamt);

            posSaleVO.setEmail(CLPJson.getString("email"));

            JSONObject flightresp=new JSONObject(flightJson.getString("resp"));
            JSONObject flightrepdata=flightresp.getJSONObject("data");
            posSaleVO.setFlightId(flightrepdata.getString("flightId"));
            posSaleVO.setIsVirtualFlight(flightrepdata.getString("isVirtualFlight"));


            posSaleRefernceVO=new PosSaleRefernceVO();
            posSaleRefernceVO.setThirdPartyTransactionId(flightJson.getString("referenceno"));
            posSaleRefernceVO.setSalesDevice("MOBILEAPP");
            posSaleRefernceVO.setAdvanceAmount(String.valueOf(AdvanceAmount));

/*
            if(flightJson.has("referencedate") && !flightJson.getString("referencedate").equals("")){
                ts = new Timestamp(((Date)df.parse(flightJson.getString("referencedate"))).getTime());
                date=new Date();
                date.setTime(ts.getTime());
                posSaleRefernceVO.setThirdPartyTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
            }*/
            posSaleRefernceVO.setThirdPartyTransactionDate(flightJson.getString("referencedate"));



            Log.w("date",posSaleRefernceVO.getThirdPartyTransactionDate());



            posSaleRefernceVO.setThirdPartyUserId(flightJson.getString("crewid"));

            if(flightJson.has("crewidimage")){
                posSaleRefernceVO.setCrewIdImageURL(flightJson.getString("crewidimage"));
            }


            if(flightJson.has("boardingPassImage")){
                posSaleRefernceVO.setBoardingPassURL(flightJson.getString("boardingPassImage"));
            }

            posSaleVO.setContactNumber(CLPJson.getString("mobileno"));

            ;
            posSaleVO.setPosSalePcsVO(productlist);
            posSaleVO.setPosSaleRefernceVO(posSaleRefernceVO);
            grosstotaltext.setText(String.valueOf(posSaleVO.getGrossTotal()));
            invoicediscount.setText(String.valueOf(posSaleVO.getInvoiceDiscount()));
            specialdiscount.setText(String.valueOf(posSaleVO.getSpecialDiscount()));
            computeAndSetCLP();

            if(posSaleVO.getCLPDiscount()>0){
                couponlayout.setVisibility(View.GONE);
            }

            grosstotaltext.setText(String.valueOf(posSaleVO.getGrossTotal()));
            invoicediscount.setText(String.valueOf(posSaleVO.getInvoiceDiscount()));
            specialdiscount.setText(String.valueOf(posSaleVO.getSpecialDiscount()));
            hfredeempoint.setText(String.valueOf(posSaleVO.getCLPDiscount()));

        } catch (Exception e) {
            Utility.exceptionAlertDialog(Payment.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }




        couponVO =new CouponVO();
        applycoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VolleyUtils.makeJsonObjectRequest(Payment.this, ProductBO.applyAviationCoupon(posSaleVO.getContactNumber(),coupon.getText().toString(),netamount.getText().toString(),posSaleVO.getPrincipalID()) , new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                    }
                    @Override
                    public void onResponse(Object resp)  {
                        JSONObject response = (JSONObject) resp;

                        try {
                            if(response.getString("status").equals("fail")){
                                applycoupon.setVisibility(View.VISIBLE);
                                coupon.setEnabled(true);

                                posSaleVO.setSpecialDiscount(0);
                                specialdis=0;
                                specialdiscount.setText(""+0);
                                discountremarks.setText("");
                                promotionflag=false;
                                couponflag=false;

                                couponVO.setCoupon_id(null);
                                couponVO.setCode(null);
                                couponVO.setCouponHash(null);

                                VolleyUtils.furnishErrorMsg(  "Error " ,response, Payment.this);
                            }else {

                                applycoupon.setVisibility(View.GONE);
                                coupon.setEnabled(false);




                                specialdiscount.setEnabled(false);
                                int amountcou=Integer.valueOf(response.getString("couponAmount"));

                                int coupondis=amountcou>Integer.parseInt(netamount.getText().toString())?Integer.parseInt(netamount.getText().toString()):amountcou;
                                specialdiscount.setText(""+coupondis);
                                posSaleVO.setSpecialDiscount(coupondis);
                                specialdis=coupondis;

                                discountremarks.setText(response.getString("couponDesc"));
                                couponVO.setCoupon_id(response.getString("couponId"));
                                couponVO.setCode(coupon.getText().toString());
                                couponVO.setCouponHash(response.getString("couponHash"));

                                promotionflag=false;
                                couponflag=true;
                                getoffer.setVisibility(View.GONE);
                                Log.w("coupondes",response.toString());
                            }

                            computeAndSetCLP();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });



        specialdiscount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
             @Override
             public void onFocusChange(View v, boolean hasFocus) {
                 if (!hasFocus) {

                     posSaleVO.setSpecialDiscount(Integer.parseInt(specialdiscount.getText().toString()));
                     if(Integer.parseInt(specialdiscount.getText().toString())>0){
                         discountremarks.setEnabled(true);
                     }else{
                         discountremarks.setEnabled(false);
                     }
                     computeAndSetCLP();
                 }else{
                     Log.d("Spl Discount","yes");
                 }

             }
         });





        getoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(promoHash!=null && PromotionId!=null && Productqty!=0){
                      VolleyUtils.makeJsonObjectRequest(Payment.this, OfferBO.getOfferByCard(PromotionId,promoHash,Productqty,grosstotaltext.getText().toString()), new VolleyResponseListener() {
                          @Override
                          public void onError(String message) {
                          }

                          @Override
                          public void onResponse(Object resp) throws JSONException {
                              JSONObject response = (JSONObject) resp;
                                Log.w("offer",response.toString());

                                if(response.getString("status").equals("SUCCESS")){
                                    JSONObject jsonObject=response.getJSONObject("data");
                                    specialdiscount.setEnabled(false);
                                    specialdiscount.setText(jsonObject.getString("discount"));
                                    posSaleVO.setSpecialDiscount(Integer.valueOf(jsonObject.getString("discount")));
                                    specialdis=jsonObject.getInt("discount");
                                    discountremarks.setText(jsonObject.getString("promoMessage"));
                                    promotionflag=true;
                                }else{
                                    posSaleVO.setSpecialDiscount(null);
                                    specialdiscount.setText(String.valueOf(specialdis));
                                    discountremarks.setText("");
                                    promotionflag=false;


                                }
                              computeAndSetCLP();
                          }
                      });
                  }
            }
        });




        saveaviation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posSaleVO.setSpecialDiscount(Integer.parseInt(specialdiscount.getText().toString()));
                computeAndSetCLP();


                if(!validatePaymentType()) return;






                saveaviation.setEnabled(false);
                //set posale object
                posSaleVO.setPaymentTypeID(paymenttypeid.getText().toString());
                posSaleVO.setDiscountRemarks(discountremarks.getText().toString());
                posSaleVO.setGrossTotal(Integer.parseInt(grosstotaltext.getText().toString()));
                posSaleVO.setInvoiceDiscount(Integer.parseInt(invoicediscount.getText().toString()));
                posSaleVO.setSpecialDiscount(Integer.parseInt(specialdiscount.getText().toString()));
                posSaleVO.setSalesEmplID(employeeid.getText().toString());
                posSaleVO.setCreditCardNumber(lastdigit.getText().toString());
                posSaleVO.setCreditCardBatchNumber(batchno.getText().toString());
                posSaleVO.setCreditCardApprovalCode(approvalcode.getText().toString());
                posSaleVO.getPosSaleRefernceVO().setPaymentCardId(cardid.getText().toString());
                posSaleVO.setNetAmount(Integer.parseInt(netamount.getText().toString())+(int)AdvanceAmount);


                if(promotionflag){
                    posSaleVO.getPosSaleRefernceVO().setPromotionId(String.valueOf(PromotionId));
                }

                if(Integer.parseInt(posSaleVO.getPaymentTypeID())== PaymentTypeVO.MOSAMBEE ||  Integer.parseInt(posSaleVO.getPaymentTypeID())== PaymentTypeVO.EZETAP  ||  Integer.parseInt(posSaleVO.getPaymentTypeID())== PaymentTypeVO.CASH ||  Integer.parseInt(posSaleVO.getPaymentTypeID())== PaymentTypeVO.ONLINE){
                    getUniqueTxnId();
                }else{
                    doSaveInvoice(null);
                }
            }
        });



        paymenttype.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {

                    if(!couponflag){
                        posSaleVO.setSpecialDiscount(0);
                        specialdis=0;
                        specialdiscount.setText(""+0);
                        discountremarks.setText("");

                        computeAndSetCLP();
                    }


                    if(posSaleVO.getPrincipalID().equals("32")){
                        ConnectionVO connectionVO = new ConnectionVO();
                        connectionVO.setTitle("Payment Type");
                        connectionVO.setSharedPreferenceKey(ApplicationConstant.CACHE_PAYMENT_TYPE_MANUAL);
                        connectionVO.setEntityIdKey("paymentTypeID");
                        connectionVO.setEntityTextKey("paymentTypeName");
                        Intent intent = new Intent(getApplicationContext(),ListViewSingleText.class);
                        intent.putExtra(ApplicationConstant.INTENT_EXTRA_CONNECTION,  connectionVO);
                        startActivityForResult(intent,REQ_PAYMENT_TYPE);
                    }else {

                        ConnectionVO connectionVO = new ConnectionVO();
                        connectionVO.setTitle("Payment Type");
                        connectionVO.setSharedPreferenceKey(ApplicationConstant.CACHE_PAYMENT_TYPE);
                        connectionVO.setEntityIdKey("paymentTypeID");
                        connectionVO.setEntityTextKey("paymentTypeName");
                        Intent intent = new Intent(getApplicationContext(),ListViewSingleText.class);
                        intent.putExtra(ApplicationConstant.INTENT_EXTRA_CONNECTION,  connectionVO);
                        startActivityForResult(intent,REQ_PAYMENT_TYPE);
                    }


                }
                return true; // return is important...
            }
        });

        cardname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {


                    if(!couponflag){
                        posSaleVO.setSpecialDiscount(0);
                        specialdis=0;
                        specialdiscount.setText(""+0);
                        discountremarks.setText("");

                        computeAndSetCLP();
                    }



                    ConnectionVO connectionVO = new ConnectionVO();
                    connectionVO.setTitle("Card Type");
                    connectionVO.setSharedPreferenceKey(ApplicationConstant.CACHE_PAYMENT_CARD_LIST);
                    connectionVO.setEntityIdKey("CardID");
                    connectionVO.setEntityTextKey("CardName");
                    Intent intent = new Intent(getApplicationContext(),ListViewSingleText.class);
                    intent.putExtra(ApplicationConstant.INTENT_EXTRA_CONNECTION,  connectionVO);
                    startActivityForResult(intent,REQ_PAYMENT_CARD_LIST);
                }
                return true; // return is important...
            }
        });


        employeecode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    ConnectionVO connectionVO = new ConnectionVO();
                    connectionVO.setTitle("Employee");
                    connectionVO.setSharedPreferenceKey(ApplicationConstant.CACHE_EMPLOYEE_LIST);
                    connectionVO.setEntityIdKey("EmployeeID");
                    connectionVO.setEntityTextKey("EmployeeName");
                    Intent intent = new Intent(getApplicationContext(),ListViewSingleText.class);
                    intent.putExtra(ApplicationConstant.INTENT_EXTRA_CONNECTION,  connectionVO);
                    startActivityForResult(intent,REQ_EMPLOYEE_LIST);
                }
                return true; // return is important...
            }
        });



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

        /*Ezee Tap*/
            switch (requestCode) {
                case REQUEST_CODE_INITIALIZE:
                    doEzeTapAction(EZETAP_SALE_TXN_PREPARE_ACTION,resultCode, data);
                    break;
                case REQUEST_CODE_CASH_TXN:
                    doEzeTapAction(EZETAP_SALE_TXN_ACTION,resultCode, data);
                    break;

                case REQUEST_CODE_SALE_TXN:
                    doEzeTapAction(EZETAP_SALE_TXN_ACTION,resultCode, data);
                    break;
                default:
                    break;
            }
        /* End */
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_PAYMENT_TYPE) {
                paymenttype.setError(null);

               // Toast.makeText(this, ""+data.getStringExtra("valueId"), Toast.LENGTH_SHORT).show();

                paymenttype.setText(data.getStringExtra("valueName"));
                paymenttypeid.setText(data.getStringExtra("valueId"));
                if( Integer.parseInt(data.getStringExtra("valueId"))==2 ||Integer.parseInt(data.getStringExtra("valueId"))== PaymentTypeVO.MOSAMBEE ||Integer.parseInt(data.getStringExtra("valueId"))== PaymentTypeVO.EZETAP){
                    batchlayout.setVisibility(View.VISIBLE);
                    cardlayout.setVisibility(View.VISIBLE);

                }else {
                    batchlayout.setVisibility(View.GONE);
                    cardlayout.setVisibility(View.GONE);
                }

                if(Integer.parseInt(data.getStringExtra("valueId"))==8){
                    discountremarks.setEnabled(true);
                }else {
                    discountremarks.setEnabled(true);
                }

                if(Integer.parseInt(data.getStringExtra("valueId"))== PaymentTypeVO.MOSAMBEE || Integer.parseInt(data.getStringExtra("valueId"))== PaymentTypeVO.EZETAP){
                    approvalcode.setEnabled(false);
                    batchno.setEnabled(false);
                    lastdigit.setEnabled(false);
                }else {
                    approvalcode.setEnabled(true);
                    batchno.setEnabled(true);
                    lastdigit.setEnabled(true);
                }


                promotionflag=false;
                PromotionId=null;
                promoHash=null;
                cardname.setText("");
                cardid.setText("");
                getoffer.setVisibility(View.GONE);

                /*
                discountremarks.setText("");
                posSaleVO.setSpecialDiscount(0);
                specialdiscount.setText(String.valueOf(posSaleVO.getSpecialDiscount()));
                computeAndSetCLP();
                */
            }
            if (requestCode == REQ_PAYMENT_CARD_LIST) {
                cardname.setError(null);
                cardname.setText(data.getStringExtra("valueName"));
                cardid.setText(data.getStringExtra("valueId"));


                promotionflag=false;
                PromotionId=null;
                promoHash=null;
                String jsonString= (String)sharedPreferences.getString( ApplicationConstant.CACHE_PROMOTION,null);


                if(jsonString!=null){

                    JSONArray jsonArray =new JSONArray(jsonString);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        if(Double.parseDouble(jsonObject1.getString("CartTotal"))<=Double.parseDouble(grosstotaltext.getText().toString()) && Integer.parseInt(jsonObject1.getString("Nos"))<=Productqty){
                            if(jsonObject1.has("PaymentCardId")){
                                if(Integer.parseInt(jsonObject1.getString("PaymentCardId"))==Integer.parseInt(data.getStringExtra("valueId"))){
                                    PromotionId= Integer.parseInt(jsonObject1.getString("PromotionId"));
                                    promoHash=jsonObject1.getString("promoHash");
                                    break;
                                }
                            }else {
                                PromotionId= Integer.parseInt(jsonObject1.getString("PromotionId"));
                                promoHash=jsonObject1.getString("promoHash");
                            }
                        }
                    }
                }


                /*
                discountremarks.setText("");
                posSaleVO.setSpecialDiscount(0);
                specialdiscount.setText(String.valueOf(posSaleVO.getSpecialDiscount()));
                computeAndSetCLP();
                */


                if(PromotionId!=null && !couponflag){
                    getoffer.setVisibility(View.VISIBLE);
                }else {
                    getoffer.setVisibility(View.GONE);
                }
            }

            if(requestCode==REQ_EMPLOYEE_LIST){
                employeecode.setText(data.getStringExtra("valueName"));
                employeeid.setText(data.getStringExtra("valueId"));


                employeecode.setError(null);
            }
        }
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Payment.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));        }
    }


    public void computeAndSetCLP(){
        try {
            clpPointsVO=new ClpPointsVO();

            clpPointsVO.setRedimPoints(0);

            if (CLPJson.has("resppassenger")) {
                JSONObject jsonObjectclp = new JSONObject(CLPJson.getString("resppassenger"));

                if(jsonObjectclp.has("CustomerRegistrationVO")){
                    JSONObject CustomerRegistrationjson = jsonObjectclp.getJSONObject("CustomerRegistrationVO");
                    if(Integer.parseInt(CustomerRegistrationjson.getString("StatusID"))==1){
                        clpPointsVO.setCustomerID(CustomerRegistrationjson.getString("CustomerID"));
                    }
                }else{
                    if(Integer.parseInt(jsonObjectclp.getString("StatusID"))==4){
                        clpPointsVO.setCustomerID(null);
                    }

                }



            }
            if (Integer.parseInt(CLPJson.getString("point")) > 0) {
                clpPointsVO.setRedimPoints(Integer.parseInt(CLPJson.getString("point")));
            }
            //set clp discount
            if (clpPointsVO.getRedimPoints() != 0) {
                double clpApplicableAmt = posSaleVO.getGrossTotal() - posSaleVO.getInvoiceDiscount() - posSaleVO.getSpecialDiscount();
                JSONObject jsonObjectclp = new JSONObject(CLPJson.getString("resppassenger"));
                JSONObject CLPConfigVOjson = jsonObjectclp.getJSONObject("CLPConfigVO");
                int clpApplicablePoints = (int) Math.round((clpApplicableAmt / Double.parseDouble(CLPConfigVOjson.getString("RedimConversionValue"))) * Double.parseDouble(CLPConfigVOjson.getString("RedimConversionPoints")));
                if (clpPointsVO.getRedimPoints() > clpApplicablePoints) {
                    CLPJson.put("point", clpPointsVO.getRedimPoints());
                    CLPJson.put("CLPDisocunt", clpApplicableAmt);
                    posSaleVO.setCLPDiscount((int) clpApplicableAmt);
                    clpPointsVO.setRedimPoints(clpPointsVO.getRedimPoints());
                } else {
                    int clpDiscount = (int) Math.round((clpPointsVO.getRedimPoints() / Double.parseDouble(CLPConfigVOjson.getString("RedimConversionPoints"))) * Double.parseDouble(CLPConfigVOjson.getString("RedimConversionValue")));
                    CLPJson.put("CLPDisocunt", clpDiscount);
                    posSaleVO.setCLPDiscount((int) clpDiscount);
                }
            }
            // set PurchasePoints discount

            if (clpPointsVO.getCustomerID() != null) {
                int netAmount;
                netAmount = posSaleVO.getGrossTotal() - posSaleVO.getSpecialDiscount() - posSaleVO.getInvoiceDiscount() - posSaleVO.getCLPDiscount();
                /*if (posSaleVO.getCLPDiscount() != null) {
                    netAmount = posSaleVO.getGrossTotal() - posSaleVO.getSpecialDiscount() - posSaleVO.getInvoiceDiscount() - posSaleVO.getCLPDiscount();
                } else {
                    netAmount = posSaleVO.getGrossTotal() - posSaleVO.getSpecialDiscount() - posSaleVO.getInvoiceDiscount() - 0;
                }*/
                if (netAmount != 0) {
                    JSONObject jsonObjectclp = new JSONObject(CLPJson.getString("resppassenger"));
                    JSONObject CLPConfigVOjson = jsonObjectclp.getJSONObject("CLPConfigVO");
                    int CLPPurchasePoints = (int) Math.round((netAmount / Double.parseDouble(CLPConfigVOjson.getString("PurchaseConversionValue"))) * Double.parseDouble(CLPConfigVOjson.getString("PurchaseConversionPoints")));
                    clpPointsVO.setPurchasePoints(CLPPurchasePoints);
                    posSaleVO.setClpPointsVO(clpPointsVO);
                }
            }else {
                /*posSaleVO.setCLPDiscount(0);
                clpPointsVO.setPurchasePoints(0);*/

                posSaleVO.setClpPointsVO(null);

            }
           /* if (clpPointsVO.getCustomerID() != null){
                posSaleVO.setClpPointsVO(clpPointsVO);
            }else {
                posSaleVO.setClpPointsVO(null);
            }*/


            calculateNetAmt();
        }catch (Exception e){
            Utility.exceptionAlertDialog(Payment.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
    }
    public void calculateNetAmt(){
      int netAmount=  posSaleVO.getGrossTotal() - posSaleVO.getSpecialDiscount() - posSaleVO.getInvoiceDiscount() - posSaleVO.getCLPDiscount();
      netamount.setText(String.valueOf(netAmount-(int)AdvanceAmount));
    }


    private boolean validatePaymentType(){
        boolean valid=true;
        cardname.setError(null);
        lastdigit.setError(null);
        batchno.setError(null);
        approvalcode.setError(null);







        if(paymenttypeid.getText().toString().equals("") || employeeid.getText().toString().equals("")){

            if(paymenttypeid.getText().toString().equals("")){
                paymenttype.setError(Utility.getErrorSpannableString(getApplicationContext(), "Payment Type  is Required"));

            }


            if(employeeid.getText().toString().equals("")){
                employeecode.setError(Utility.getErrorSpannableString(getApplicationContext(), "Employee is Required"));
            }
            valid=false;
        }
        if(!paymenttypeid.getText().toString().equals("")){
            if(Integer.parseInt(paymenttypeid.getText().toString())==2){
                if(cardid.getText().toString().equals("") || lastdigit.getText().toString().equals("") || batchno.getText().toString().equals("") || approvalcode.getText().toString().equals("")){
                    if(cardid.getText().toString().equals("")){
                        cardname.setError(Utility.getErrorSpannableString(getApplicationContext(), "Card is Required"));
                        cardname.setText("");
                        cardid.setText("");
                    }
                    if(lastdigit.getText().toString().equals("")){
                        lastdigit.setError(Utility.getErrorSpannableString(getApplicationContext(), "Last 4 digit is Required"));
                        lastdigit.setText("");

                    }
                    if(batchno.getText().toString().equals("")){
                        batchno.setError(Utility.getErrorSpannableString(getApplicationContext(), "BatchNo is Required"));
                        batchno.setText("");

                    }
                    if(approvalcode.getText().toString().equals("")){
                        approvalcode.setError(Utility.getErrorSpannableString(getApplicationContext(), "approvalcode is Required"));
                        approvalcode.setText("");
                    }
                    valid=false;
                }
            }else if(Integer.parseInt(paymenttypeid.getText().toString())==PaymentTypeVO.MOSAMBEE || Integer.parseInt(paymenttypeid.getText().toString())==PaymentTypeVO.EZETAP){
                if(cardid.getText().toString().equals("")){

                    cardname.setText("");
                    cardid.setText("");
                    cardname.setError(Utility.getErrorSpannableString(getApplicationContext(), "Card is Required"));
                    valid=false;
                }

            }

            int netAmt=  Integer.parseInt(netamount.getText().toString());
            int splDiscount = Integer.parseInt(specialdiscount.getText().toString());
            if( splDiscount>0  && discountremarks.getText().toString().equals("")){
                discountremarks.setError(Utility.getErrorSpannableString(getApplicationContext(), "Discount remarks is Required"));
                valid=false;
            }

            if( netAmt<0){
                Utility.alertDialog(Payment.this,"Validation!","Spl. discount is greater than the gross amount","OK");
                valid= false;
            }
        }


        if(posSaleVO.getPrincipalID().equals("32")){
            if(discountremarks.getText().toString().equals("")){
                discountremarks.setError(Utility.getErrorSpannableString(getApplicationContext(), "Discount Remarks is Required"));
                valid=false;
            }

        }



        /*if(!paymenttypeid.getText().toString().equals("")){
            if(couponflag &&  Integer.valueOf(paymenttypeid.getText().toString())==1){
                valid= false;
            }


            if(couponflag && Integer.valueOf(paymenttypeid.getText().toString())==1){
                new AlertDialog.Builder(context)
                        .setTitle("Alert")
                        .setMessage("Please select payment type as Credit Card to redeem coupon")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.ok, null)

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .show();
                valid= false;
            }
        }*/




            return  valid;
    }

    private void getUniqueTxnId(){
        VolleyUtils.makeJsonObjectRequest(Payment.this, AviationSaleBO.getUniqeTxnId(), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;
                Log.w("offer123",response.toString());

                if(response.getString("status").equalsIgnoreCase("SUCCESS")){
                    posSaleVO.setEWalletTxnId(Integer.parseInt(response.getString("txnId")));

                     switch (Integer.parseInt(posSaleVO.getPaymentTypeID())) {
                        case PaymentTypeVO.MOSAMBEE:
                            doMosambeeAction();
                            break;
                        case PaymentTypeVO.EZETAP:
                            doEzeTapAction(EZETAP_INITIALIZE_ACTION, 0,null);

                            //local test on  mobile for uncomment this line
                           // doSaveInvoice(null);
                            break;
                        case PaymentTypeVO.CASH:
                            doEzeTapAction(EZETAP_INITIALIZE_ACTION, 0,null);
                            //doSaveInvoice(null);
                            break;
                        case PaymentTypeVO.ONLINE:

                            doEzeTapAction(EZETAP_INITIALIZE_ACTION, 0,null);
                           // doSaveInvoice(null);
                            break;

                        default:
                            break;
                    }


                }else{
                    posSaleVO.setEWalletTxnId(null);
                    Utility.alertDialog(Payment.this,"Validation!","Please try again","OK");
                }

            }
        });


    }

    private void doMosambeeAction(){
        int REQUEST_CODE_PERMISSION = 2;
         //check permission
        String[] mPermission = {Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};

        ActivityCompat.requestPermissions(this, mPermission, REQUEST_CODE_PERMISSION);

        handler = new Handler();
         FrameLayout container = new FrameLayout(getApplicationContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT  );
        // Apply the Layout Parameters for FrameLayout

        container.setLayoutParams(lp);
        startProcess("9811705982", "1234", "sale", container,posSaleVO);


    }



    public void setData(final ResultData result) {
        this.result=result;
       /* this.posSaleVO = posSaleVO;
        this.userJsonObject = userJsonObject;
*/
        if (result == null) {
            Utility.alertDialog(context,"Gateway Response!","Please try again","OK");
        } else {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }

                    });
                }
            };
            new Thread(runnable).start();

            if(!result.getResult()){
                String msg="Result: " + result.getResult()
                        + "\nReason code: "
                        + result.getReasonCode() + "\nReason: "
                        + result.getReason() + "\nTransaction Id: "
                        + result.getTransactionId()
                        + "\nTransaction amount: "
                        + result.getAmount()
                        + "\nTransaction data: "
                        + result.getTransactionData();
                Utility.alertDialog(context,"Gateway Response!",msg,"OK");
                saveaviation.setEnabled(true);
            }else if( (int)Double.parseDouble(result.getAmount() )!= posSaleVO.getNetAmount()-AdvanceAmount){
                Utility.alertDialog(context,"Gateway Response!","Amount not matched","OK");
                saveaviation.setEnabled(true);
            }else{
                try {
                    JSONObject jsonObject = new JSONObject(result.getTransactionData());

                    if(!jsonObject.getString("orderId").equalsIgnoreCase( Integer.toString(posSaleVO.getEWalletTxnId()))){
                        Utility.alertDialog(context,"Gateway Rsponse!","Order Id not matched","OK");
                        saveaviation.setEnabled(true);
                        return;
                    }

                    posSaleVO.setCreditCardNumber(jsonObject.getString("cardNumber"));
                    posSaleVO.setCreditCardBatchNumber(jsonObject.getString("batchNumber"));
                    posSaleVO.setCreditCardApprovalCode(jsonObject.getString("approvalCode"));
                    Gson gson = new Gson();



                    doSaveInvoice(gson.toJson(result));
                }catch (Exception e){
                    Utility.exceptionAlertDialog(context,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));

                    Log.w("error",Utility.getStackTrace(e));
                }
            }

         }
    }

    public void setCommand(final String command) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        saveaviation.setText(command);
                    }
                });
            }
        };
        new Thread(runnable).start();

    }


    private void doSaveInvoice(String ccResp){
        ConnectionVO connectionVO = AviationSaleBO.saveAviationSale();
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            Gson gson = new Gson();
            if(ccResp !=null){
                posSaleVO.getPosSaleRefernceVO().setMosambeeresp(ccResp);
            }


            String json = gson.toJson(posSaleVO);
            params.put("POSSaleVO", json);

            if(couponflag){
                params.put("couponObj",gson.toJson(couponVO));
            }


            params.put("userId",userJsonObject.getString("userId").toString());
            connectionVO.setParams(params);

            Log.w("datacreate",params.toString());

        } catch (Exception e) {
            Utility.exceptionAlertDialog(context,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
            return;
        }


        VolleyUtils.makeJsonObjectRequest(context,connectionVO , new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                Log.w("resp",response.toString());

                if(response.has("status")){

                    Utility.alertDialog(context,"Validation!",response.getString("error"),"OK");
                    saveaviation.setEnabled(false);

                }else {
                    Intent intent =new Intent(Payment.this,Success.class);
                    intent.putExtra("tran_id",response.getString("LastInsertedPosSaleID"));
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    intent.putExtra("result",json);
                    startActivity(intent);
                }

            }
        });


    }

        public void startProcess(String user, String pswd, String transType, FrameLayout container, PosSaleVO posSaleVO) {


            moscCallback = new MosCallback(context);
            //  moscCallback.setActivityForJUNO(_activity);
            moscCallback.initialise(user, pswd, this);
            // initializeSignatureView(FrameLayout, primaryColor, secondaryColor);
            moscCallback.initializeSignatureView(container, "#55004A", "#750F5A");
            moscCallback.initialiseFields(transType, posSaleVO.getContactNumber(), "cGjhE$@fdhj4675riesae", false, posSaleVO.getEmail(), "", "serial", posSaleVO.getInvoiceDate(), posSaleVO.getEWalletTxnId().toString());
            moscCallback.processTransaction(posSaleVO.getEWalletTxnId().toString(),  posSaleVO.getNetAmount()-AdvanceAmount,   posSaleVO.getNetAmount()-AdvanceAmount,null);
            // moscCallback.getLocation();
        }

        public void getReceipt(String invoiceNo, String pswd) {
            moscCallback = new MosCallback(context);
            moscCallback = new MosCallback(context);
            moscCallback.initialise("", pswd, this);
            moscCallback.getMetaData(invoiceNo);
        }


        @Override
        public void onCommand(String command) {
            //System.out.println("command is--------------------" + command);
            setCommand(command);
        }
        @Override
        public void onResult(ResultData result) {
            //System.out.println();
            //System.out.println("\n-----Result: "+result.getResult()+ "\n-----Reason: " + result.getReason()+"\n-----Data: "+ result.getTransactionData() );
            result.getResult();
            setData(result);

        }
        public void stopProcess() {
            moscCallback = new MosCallback(context);
            moscCallback.posReset();
        }




        /*ezetap process start*/
        private void doEzeTapAction(int action, int resultCode, Intent intent){
            try {
                switch (action) {
                    case EZETAP_INITIALIZE_ACTION:
                        ezeTabpDeviceInitialize();
                        break;

                    case EZETAP_SALE_TXN_PREPARE_ACTION:
                        if(Integer.parseInt(posSaleVO.getPaymentTypeID())==PaymentTypeVO.EZETAP) {
                            ezeTapCreditCardPrepareAction(resultCode, intent);
                        }else if(Integer.parseInt(posSaleVO.getPaymentTypeID())==PaymentTypeVO.CASH || Integer.parseInt(posSaleVO.getPaymentTypeID())==PaymentTypeVO.ONLINE) {
                            ezeTapCashPrepareAction(resultCode, intent);
                        }
                        break;
                    case EZETAP_SALE_TXN_ACTION:
                        if(Integer.parseInt(posSaleVO.getPaymentTypeID())==PaymentTypeVO.EZETAP) {
                            ezeTapCreditCardAction(resultCode, intent);
                        }else if(Integer.parseInt(posSaleVO.getPaymentTypeID())==PaymentTypeVO.CASH || Integer.parseInt(posSaleVO.getPaymentTypeID())==PaymentTypeVO.ONLINE) {
                            ezeTapCashAction(resultCode, intent);
                        }
                        break;

                    default:
                        break;
                }
            }catch (Exception e){
                Utility.exceptionAlertDialog(context,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                return;

            }
        }

        private void ezeTabpDeviceInitialize(){
            JSONObject jsonRequest = new JSONObject();
            try {
                if(ApplicationConstant.IS_PRODUCTION_ENVIRONMENT){
                    jsonRequest.put("prodAppKey", ApplicationConstant.EZETAP_PROD_APPKEY);
                    jsonRequest.put("demoAppKey", ApplicationConstant.EZETAP_DEMO_APPKEY);
                    jsonRequest.put("appMode", "PROD");
                }else{
                    jsonRequest.put("demoAppKey", ApplicationConstant.EZETAP_DEMO_APPKEY);
                    jsonRequest.put("prodAppKey", ApplicationConstant.EZETAP_PROD_APPKEY);
                    jsonRequest.put("appMode", "DEMO");
                }

                jsonRequest.put("merchantName", ApplicationConstant.EZETAP_MERCHANTNAME);
                jsonRequest.put("userName", Session.getMPOSId(context));
                jsonRequest.put("currencyCode", "INR");
                jsonRequest.put("captureSignature", "true");
                jsonRequest.put("prepareDevice", "false");
                EzeAPI.initialize(this, REQUEST_CODE_INITIALIZE, jsonRequest);
            } catch (JSONException e) {
                Utility.exceptionAlertDialog(context,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                return;
            }

        }

        private void ezeTapCreditCardPrepareAction(int resultCode, Intent intent){
            if (resultCode == RESULT_OK) {
                JSONObject jsonRequest = new JSONObject();
                try {

                    JSONObject response = new JSONObject(intent.getStringExtra("response"));
                    if(response.getString("status").equalsIgnoreCase("success")){
                        JSONObject jsonOptionalParams = new JSONObject();
                        JSONObject jsonReferences = new JSONObject();
                        JSONObject jsonCustomer = new JSONObject();

                        jsonRequest.put("mode", "SALE");//Card payment Mode
                        jsonRequest.put("amount", posSaleVO.getNetAmount());
                      //  jsonRequest.put("externalRefNumber",  posSaleVO.getEWalletTxnId());


                        jsonCustomer.put("mobileNo", posSaleVO.getContactNumber());
                        jsonCustomer.put("email",posSaleVO.getEmail());

                        jsonReferences.put("reference1", posSaleVO.getEWalletTxnId());
                        jsonReferences.put("reference2",  Session.getEmployeecodebyId(context, posSaleVO.getSalesEmplID().toString()) );
                        jsonOptionalParams.put("references",jsonReferences);
                        jsonOptionalParams.put("customer",jsonCustomer);

                        jsonRequest.put("options", jsonOptionalParams);


                        EzeAPI.cardTransaction(this, REQUEST_CODE_SALE_TXN, jsonRequest);
                    }
                } catch (JSONException e) {
                    Utility.exceptionAlertDialog(context,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                    return;
                }
            }

        }

        private void ezeTapCreditCardAction(int resultCode, Intent intent){
            JSONObject jsonRequest = new JSONObject();
            try {
                if (resultCode == RESULT_OK) {
                    JSONObject response = new JSONObject(intent.getStringExtra("response"));

                    if(response.getString("status").equalsIgnoreCase("failure")){
                        String errorMessage = response.getString("message");
                        Utility.alertDialog(context,"Gateway Response!",errorMessage,"OK");
                        saveaviation.setEnabled(true);
                        return;
                    }
                    JSONObject result = response.getJSONObject("result");
                    JSONObject txn = result.getJSONObject("txn");
                    if( (int)Double.parseDouble(txn.getString("amount") )!= posSaleVO.getNetAmount()-AdvanceAmount) {
                        Utility.alertDialog(context, "Gateway Response!", "Amount not matched", "OK");
                        saveaviation.setEnabled(true);
                        return;
                    }

                   /* JSONObject references = response.getJSONObject("references");
                    if(!references.getString("reference").equalsIgnoreCase( Integer.toString(posSaleVO.getEWalletTxnId()))){
                        Utility.alertDialog(context,"Gateway Rsponse!","Order Id not matched","OK");
                        saveaviation.setEnabled(true);
                        return;
                    }
                    */
                    JSONObject card = result.getJSONObject("cardDetails");
                    posSaleVO.setCreditCardNumber(card.getString("maskedCardNo"));
                    posSaleVO.setCreditCardBatchNumber(txn.getString("batchNumber"));
                    posSaleVO.setCreditCardApprovalCode(txn.getString("authCode"));

                    doSaveInvoice(intent.getStringExtra("response"));
                } else if (resultCode == RESULT_CANCELED) {
                    JSONObject response = new JSONObject(intent.getStringExtra("response"));
                    response = response.getJSONObject("error");
                    String errorCode = response.getString("code");
                    String errorMessage = response.getString("message");
                    Utility.alertDialog(context,"Gateway Response!",errorMessage,"OK");
                    saveaviation.setEnabled(true);
                }
            } catch (JSONException e) {
                Utility.exceptionAlertDialog(context,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                saveaviation.setEnabled(true);
                return;
            }

        }


    private void ezeTapCashPrepareAction(int resultCode, Intent intent){
        if (resultCode == RESULT_OK) {
            JSONObject jsonRequest = new JSONObject();
            try {

                JSONObject response = new JSONObject(intent.getStringExtra("response"));
                if(response.getString("status").equalsIgnoreCase("success")){
                    JSONObject jsonOptionalParams = new JSONObject();
                    JSONObject jsonReferences = new JSONObject();
                    JSONObject jsonCustomer = new JSONObject();


                    jsonRequest.put("amount", posSaleVO.getNetAmount());
                    jsonRequest.put("externalRefNumber",  posSaleVO.getEWalletTxnId());

                    jsonCustomer.put("mobileNo", posSaleVO.getContactNumber());
                    jsonCustomer.put("email",posSaleVO.getEmail());

                    jsonReferences.put("reference1", posSaleVO.getEWalletTxnId());
                    jsonReferences.put("reference2",  Session.getEmployeecodebyId(context, posSaleVO.getSalesEmplID().toString()) );

                    jsonOptionalParams.put("references",jsonReferences);
                    jsonOptionalParams.put("customer",jsonCustomer);

                    jsonRequest.put("options", jsonOptionalParams);


                    EzeAPI.cashTransaction(this, REQUEST_CODE_CASH_TXN, jsonRequest);
                }
            } catch (JSONException e) {
                Utility.exceptionAlertDialog(context,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                return;
            }
        }

    }

    private void ezeTapCashAction(int resultCode, Intent intent){
        JSONObject jsonRequest = new JSONObject();
        try {
            if (resultCode == RESULT_OK) {
                JSONObject response = new JSONObject(intent.getStringExtra("response"));

                if(response.getString("status").equalsIgnoreCase("failure")){
                    String errorMessage = response.getString("message");
                    Utility.alertDialog(context,"Gateway Response!",errorMessage,"OK");
                    saveaviation.setEnabled(true);
                    return;
                }
                JSONObject result = response.getJSONObject("result");
                JSONObject txn = result.getJSONObject("txn");
                if( (int)Double.parseDouble(txn.getString("amount") )!= posSaleVO.getNetAmount()-AdvanceAmount) {
                    Utility.alertDialog(context, "Gateway Response!", "Amount not matched", "OK");
                    saveaviation.setEnabled(true);
                    return;
                }

                   /* JSONObject references = response.getJSONObject("references");
                    if(!references.getString("reference").equalsIgnoreCase( Integer.toString(posSaleVO.getEWalletTxnId()))){
                        Utility.alertDialog(context,"Gateway Rsponse!","Order Id not matched","OK");
                        saveaviation.setEnabled(true);
                        return;
                    }
                    */
                   /*
                JSONObject card = result.getJSONObject("cardDetails");
                posSaleVO.setCreditCardNumber(card.getString("maskedCardNo"));
                posSaleVO.setCreditCardBatchNumber(txn.getString("batchNumber"));
                posSaleVO.setCreditCardApprovalCode(txn.getString("authCode"));
                */
                doSaveInvoice(intent.getStringExtra("response"));
            } else if (resultCode == RESULT_CANCELED) {
                JSONObject response = new JSONObject(intent.getStringExtra("response"));
                response = response.getJSONObject("error");
                String errorCode = response.getString("code");
                String errorMessage = response.getString("message");
                Utility.alertDialog(context,"Gateway Response!",errorMessage,"OK");
                saveaviation.setEnabled(true);
            }
        } catch (JSONException e) {
            Utility.exceptionAlertDialog(context,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
            saveaviation.setEnabled(true);
            return;
        }

    }


        /*ezetap process end*/
}

