package com.uav.ava.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uav.ava.BO.CustomerBO;
import com.uav.ava.BO.CustomerDetailBO;
import com.uav.ava.BO.MobileOTPBO;
import com.uav.ava.R;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.util.ExceptionHandler;
import com.uav.ava.util.Utility;
import com.uav.ava.vo.ConnectionVO;
import com.uav.ava.volley.volley.VolleyResponseListener;
import com.uav.ava.volley.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Passenger_Info extends AppCompatActivity {

    static  String customerrequired = "Mobile Number is Required";
    static  String customernotactivity = "Customer Not Active";

    SharedPreferences sharedPreferences;
    JSONObject jsonObject;
    CardView cardView;
    ImageView getinfo;
    EditText email,mobileno,onetimepass,customertitle,customertitlevalue,newcustomername,mobilenumber
            ,newemail,newdob,verifycutomerotp;
    Button detailsave;
    TextView customername,dob,mobilenores,avaclubno,totalearnedpoints,
            totalredeempoints,availableCLPpoints,verifyotpcustomer;
    CheckBox simpleCheckBox;
    TextView getotp,sendotp;
    String otpid=null,custoermotpverifyid=null;
    boolean discountflag=false;

    int REQ_TITLE=1001;

    TextView verifyotpcustomerresend;
    ImageView cutomerverifyicon;

    LinearLayout newcustomer,customerpoint,btncutomerverify,customerverifylayout;


    String resppassneger=null,NextActivityAction=null;

    JSONObject  passengerinfodata= new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger__info);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));



        getSupportActionBar().hide();
        ImageView back_activity_button=findViewById(R.id.back_activity_button);
        detailsave=findViewById(R.id.detailsave);
        TextView activitytitle=findViewById(R.id.activitytitle);
        cardView=findViewById(R.id.cardeview);
        getinfo=findViewById(R.id.getinfo);
        email=findViewById(R.id.email);
        mobileno=findViewById(R.id.mobileno);
        simpleCheckBox=findViewById(R.id.simpleCheckBox);
        getotp=findViewById(R.id.getotp);
        sendotp=findViewById(R.id.sendotp);
        onetimepass=findViewById(R.id.onetimepass);


        customername=findViewById(R.id.customername);
        dob=findViewById(R.id.dob);
        mobilenores=findViewById(R.id.mobilenores);
        avaclubno=findViewById(R.id.avaclubno);
        totalearnedpoints=findViewById(R.id.totalearnedpoints);
        totalredeempoints=findViewById(R.id.totalredeempoints);
        availableCLPpoints=findViewById(R.id.availableCLPpoints);


        newcustomer=findViewById(R.id.newcustomer);
        customerpoint=findViewById(R.id.customerpoint);
        customertitle=findViewById(R.id.customertitle);
        customertitlevalue=findViewById(R.id.customertitlevalue);

        newcustomername=findViewById(R.id.newcustomername) ;
        mobilenumber=findViewById(R.id.mobilenumber);
        newemail=findViewById(R.id.newemail);
        newdob=findViewById(R.id.newdob);
        btncutomerverify=findViewById(R.id.btncutomerverify);
        customerverifylayout=findViewById(R.id.customerverifylayout);
        cutomerverifyicon=findViewById(R.id.cutomerverifyicon);
        verifyotpcustomer=findViewById(R.id.verifyotpcustomer);
        verifyotpcustomerresend=findViewById(R.id.verifyotpcustomerresend);

        verifycutomerotp=findViewById(R.id.verifycutomerotp);







        newcustomer.setVisibility(View.GONE);
        customerpoint.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);

        //Required

        NextActivityAction= customerrequired;


        detailsave.setVisibility(View.GONE);

      //  detailsave.setEnabled(false);

        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String resp=sharedPreferences.getString(ApplicationConstant.CACHE_USERNAME,null);
        try {
            jsonObject =new JSONObject(resp);
            activitytitle.setText(jsonObject.getString("userName"));




            JSONObject title=new JSONObject();
            JSONArray titlearry= new JSONArray();
            JSONObject data=new JSONObject();
            data.put("name","Mr.");
            data.put("value",1);
            titlearry.put(data);
            data=new JSONObject();
            data.put("name","Ms.");
            data.put("value",2);
            titlearry.put(data);
            title.put("data",titlearry);
            SharedPreferences.Editor edit= sharedPreferences.edit();
            edit.putString( ApplicationConstant.CACHE_TITLE, title.toString());
            edit.apply();
            edit.commit();
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Passenger_Info.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }


        simpleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getotp.setVisibility(View.VISIBLE);
                }else {
                    getotp.setVisibility(View.GONE);
                }


            }
        });

        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpid=null;

                if(!mobileno.getText().toString().equals("") && simpleCheckBox.isChecked()){
                    getotp.setVisibility(View.GONE);
                    simpleCheckBox.setEnabled(false);
                    getotpbyserver();
                }
            }
        });








        getinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(!filedverify()){

                        return;
                    }else {

                        getinformation();
                    }
            }
        });



        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!onetimepass.getText().toString().equals("")){
                    verifyotp();
                }else {
                    onetimepass.setError(Utility.getErrorSpannableString(getApplicationContext(), "OTP is Required"));
                    return;
                }

            }
        });





        customertitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    ConnectionVO connectionVO = new ConnectionVO();
                    connectionVO.setTitle("Title");
                    connectionVO.setSharedPreferenceKey(ApplicationConstant.CACHE_TITLE);
                    connectionVO.setEntityIdKey("value");
                    connectionVO.setEntityTextKey("name");
                    Intent intent = new Intent(getApplicationContext(),ListViewSingleText.class);
                    intent.putExtra(ApplicationConstant.INTENT_EXTRA_CONNECTION,  connectionVO);
                    startActivityForResult(intent,REQ_TITLE);
                }
                return true; // return is important...
            }
        });


        newdob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(MotionEvent.ACTION_UP == event.getAction()) {
                    int mYear, mMonth, mDay, mHour, mMinute;
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(Passenger_Info.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {


                                    DecimalFormat df = new DecimalFormat("00");
                                    int m=monthOfYear+1;
                                    String month = df.format(m);
                                    newdob.setText(dayOfMonth + "/" + month + "/" +year);
                                    newdob.setError(null);
                                }
                            }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                }
                return false;
            }


        });


        cutomerverifyicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcustomer();

            }
        });
        verifyotpcustomerresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOTPAddCustomer();
            }
        });

        verifyotpcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(verifycutomerotp.getText().toString().equals("")){

                    verifycutomerotp.setError(Utility.getErrorSpannableString(getApplicationContext(), "OTP is Empty"));

                    return;
                }

                verifyOTPAddCustomer();
            }
        });








        detailsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NextActivityAction.equals(customerrequired)){
                      Utility.alertDialog(Passenger_Info.this,"Validation!", customerrequired,"OK");
                      return;
                }else if (NextActivityAction.equals(customernotactivity)){
                     Utility.alertDialog(Passenger_Info.this,"Validation!", customernotactivity,"OK");
                     return;
                }









                try {
                    SharedPreferences.Editor edit= sharedPreferences.edit();
                    passengerinfodata.put("mobileno",mobileno.getText().toString());
                    passengerinfodata.put("email",email.getText().toString());
                    passengerinfodata.put("resppassenger",resppassneger);
                        if(discountflag){
                            passengerinfodata.put("point",availableCLPpoints.getText());
                                edit.putString( ApplicationConstant.CACHE_PASSENGERINFO, passengerinfodata.toString());
                                edit.apply();
                                edit.commit();

                        }else {
                            passengerinfodata.put("point",0);

                            edit.putString( ApplicationConstant.CACHE_PASSENGERINFO, passengerinfodata.toString());
                            edit.apply();
                            edit.commit();

                        }
                 } catch (Exception e) {
                    Utility.exceptionAlertDialog(Passenger_Info.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                }
                startActivity(new Intent(Passenger_Info.this,Product_Scan.class));
            }
        });

    }


    public  void verifyOTPAddCustomer(){

        try {
            VolleyUtils.makeJsonObjectRequest(this, MobileOTPBO.verifyOTPAddCustomer(custoermotpverifyid,verifycutomerotp.getText().toString(),jsonObject.getString("userId")), new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }

                @Override
                public void onResponse(Object resp) throws JSONException {
                    JSONObject response = (JSONObject) resp;


                    Log.w("res1223",response.toString());


                    if(!response.has("ErrorMessage")){
                        verifyotpcustomerresend.setVisibility(View.GONE);
                        verifyotpcustomer.setVisibility(View.GONE);
                        verifycutomerotp.setVisibility(View.GONE);
                      //  NextActivityAction= customernotactivity;
                        getinfo.performClick();
                    }else {
                        verifycutomerotp.setError(Utility.getErrorSpannableString(getApplicationContext(), response.getString("ErrorMessage")));
                        verifyotpcustomerresend.setVisibility(View.VISIBLE);
                        verifyotpcustomer.setVisibility(View.VISIBLE);
                        verifycutomerotp.setEnabled(true);
                    }
                }
            });
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Passenger_Info.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }


    }


    public  void addcustomer(){



        try {
            if(!cutomerverifyvalidation()){
                return;
            }else {

                VolleyUtils.makeJsonObjectRequest(Passenger_Info.this,newcustomerdata("add"), new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                    }

                    @Override
                    public void onResponse(Object resp) throws JSONException {
                        JSONObject response = (JSONObject) resp;


                        Log.w("respdata",response.toString());

                        if(response.has("otpid")){
                            customerverifylayout.setVisibility(View.VISIBLE);
                            custoermotpverifyid=response.getString("otpid");
                            addCustomerElementToggle(false);
                            cutomerverifyicon.setVisibility(View.GONE);

                            NextActivityAction= "Active";
                            resppassneger=response.toString();
                            getinfo.performClick();

                           // NextActivityAction= customernotactivity;
                        }else {
                            customerverifylayout.setVisibility(View.GONE);
                            addCustomerElementToggle(true);
                            cutomerverifyicon.setVisibility(View.VISIBLE);
                            resppassneger=null;
                            //NextActivityAction= customerrequired;
                        }


                    }
                });
            }
        }catch(Exception e){
            Utility.exceptionAlertDialog(Passenger_Info.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
    }





    public  void resendOTPAddCustomer(){
        try {
            if(!cutomerverifyvalidation()){
                return;
            }else {

                VolleyUtils.makeJsonObjectRequest(Passenger_Info.this,newcustomerdata("resend"), new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                    }

                    @Override
                    public void onResponse(Object resp) throws JSONException {
                        JSONObject response = (JSONObject) resp;
                        NextActivityAction= "Active";

                        if(response.has("otpid")){
                            customerverifylayout.setVisibility(View.VISIBLE);
                            custoermotpverifyid=response.getString("otpid");
                            addCustomerElementToggle(false);
                            //NextActivityAction= customernotactivity;
                        }else {
                            customerverifylayout.setVisibility(View.GONE);
                            addCustomerElementToggle(true);
                            //NextActivityAction= customerrequired;

                        }
                    }
                });
            }
        }catch(Exception e){
            Utility.exceptionAlertDialog(Passenger_Info.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
    }

    public ConnectionVO newcustomerdata(String type){


        ConnectionVO connectionVO=null;

        if(type.equals("add")){
            connectionVO = CustomerBO.verifyNewCustomer();
        }
        if(type.equals("resend")){
            connectionVO = CustomerBO.resendOTPAddCustomer();
        }

        try {




            HashMap<String, Object> params = new HashMap<String, Object>();

            JSONObject customerdataobj =new JSONObject();
            customerdataobj.put("Prefix",customertitlevalue.getText().toString());
            customerdataobj.put("CustomerName",newcustomername.getText().toString());
            customerdataobj.put("Gender",null);
            customerdataobj.put("ContactNumberPrefix","+91");
            customerdataobj.put("ContactNumber",mobilenumber.getText().toString());
            customerdataobj.put("EmailID",newemail.getText().toString());
            customerdataobj.put("DOB",newdob.getText().toString());


            params.put("userId",jsonObject.getString("userId"));
            params.put("CustomerRegistrationVO",customerdataobj);
            connectionVO.setParams(params);
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Passenger_Info.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }


        return connectionVO;
    }





    public void verifyotp(){

            VolleyUtils.makeJsonObjectRequest(this, MobileOTPBO.verifyOTPForRedeemCLP(otpid,onetimepass.getText().toString()), new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }

                @Override
                public void onResponse(Object resp) throws JSONException {
                    JSONObject response = (JSONObject) resp;
                    if(!response.has("ErrorMessage")){
                        onetimepass.setEnabled(false);
                        sendotp.setEnabled(false);
                        sendotp.setVisibility(View.GONE);
                        getotp.setVisibility(View.GONE);
                        discountflag=true;

                    }else {
                        discountflag=false;
                        onetimepass.setEnabled(true);
                        sendotp.setEnabled(true);
                        sendotp.setVisibility(View.VISIBLE);
                        onetimepass.setError(Utility.getErrorSpannableString(getApplicationContext(), response.getString("ErrorMessage")));
                    }

                    Log.w("res",response.toString());
                }
            });
        }




    public void addCustomerElementToggle(boolean ele ){

            customertitle.setEnabled(ele);
            newcustomername.setEnabled(ele);
            mobilenumber.setEnabled(ele);
            newemail.setEnabled(ele);
            newdob.setEnabled(ele);
            cutomerverifyicon.setEnabled(ele);


    }




    public  void getotpbyserver(){

        try {
            VolleyUtils.makeJsonObjectRequest(this, MobileOTPBO.getOTPForRedeemCLP(mobileno.getText().toString(),jsonObject.getString("userId")), new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }

                @Override
                public void onResponse(Object resp) throws JSONException {
                    JSONObject response = (JSONObject) resp;


                    if(response.getString("status").equals("OTP has been sent.")){
                        sendotp.setVisibility(View.VISIBLE);
                        onetimepass.setVisibility(View.VISIBLE);


                        detailsave.setEnabled(true);
                        getinfo.setEnabled(false);
                        email.setEnabled(false);
                        mobileno.setEnabled(false);
                        otpid=response.getString("otpid");
                    }else {
                        sendotp.setVisibility(View.GONE);
                        onetimepass.setVisibility(View.GONE);
                        simpleCheckBox.setEnabled(true);
                        getinfo.setEnabled(true);
                        email.setEnabled(true);
                        mobileno.setEnabled(true);
                        otpid=null;
                        Toast.makeText(Passenger_Info.this, ""+response.getString("error"), Toast.LENGTH_LONG).show();
                        onetimepass.setError(Utility.getErrorSpannableString(getApplicationContext(), response.getString("error")));
                    }
                    getotp.setVisibility(View.VISIBLE);



                }
            });
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Passenger_Info.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }


    }




    public void getinformation(){
        VolleyUtils.makeJsonObjectRequest(this, CustomerDetailBO.getCLPCustomerDetail(mobileno.getText().toString()), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;


                Log.w("data1234",response.toString());
                detailsave.setVisibility(View.VISIBLE);

                cardView.setVisibility(View.VISIBLE);
                if(response.has("MessageStatus") && response.getString("MessageStatus").equals("Add")){
                    JSONArray jsonArray=new JSONArray();
                    JSONObject  jsonObject1= new JSONObject();
                    jsonObject1.put("error",jsonArray.put(response.getString("ErrorMessage")));

                    mobilenumber.setText(mobileno.getText().toString());
                    //added by vijay@uav on 2019-04-04
                    newemail.setText(email.getText().toString()  );
                    customerpoint.setVisibility(View.GONE);
                    newcustomer.setVisibility(View.VISIBLE);
                    mobileno.setEnabled(false);
                    NextActivityAction= customernotactivity;
                    resppassneger=null;
                } else if(response.has("MessageStatus") && response.getString("MessageStatus").equals("OTP")){


                    NextActivityAction= customernotactivity;
                    mobilenumber.setText(mobileno.getText().toString());
                    if(response.has("EmailID"))  newemail.setText(response.getString("EmailID"));

                    if(response.has("DOB"))  newdob.setText(response.getString("DOB"));
                    customertitle.setText( (Integer.parseInt(response.getString("Gender"))==1?"Mr.":"Mrs." ));
                    customertitlevalue.setText(response.getString("Gender"));
                    newcustomername.setText(response.getString("CustomerName"));
                    customerpoint.setVisibility(View.GONE);
                    newcustomer.setVisibility(View.VISIBLE);
                    mobileno.setEnabled(false);
                    resppassneger=null;
                    addCustomerElementToggle(false);
                    cutomerverifyicon.setVisibility(View.GONE);
                    customerverifylayout.setVisibility(View.VISIBLE);
                    NextActivityAction= "Active";
                    resppassneger=response.toString();

                }
                else {
                    customerpoint.setVisibility(View.VISIBLE);
                    newcustomer.setVisibility(View.GONE);


                    JSONObject jsonObject=response.getJSONObject("CustomerRegistrationVO");
                    customername.setText(jsonObject.getString("CustomerName"));
                    dob.setText(jsonObject.getString("DOB"));
                    mobilenores.setText(jsonObject.getString("ContactNumber"));
                    avaclubno.setText(jsonObject.getString("MemberShipNumber"));
                    totalearnedpoints.setText(jsonObject.getString("TotalEarnedPoints"));
                    totalredeempoints.setText(jsonObject.getString("TotalRedeemPoints"));
                    availableCLPpoints.setText(jsonObject.getString("AvailableClpPoints"));

                    //changed on 2019-12-12
                    if(jsonObject.getString("EmailID") !=null && !jsonObject.getString("EmailID").equals("")) {
                        email.setText(jsonObject.getString("EmailID"));
                    }
                    mobileno.setEnabled(false);
                    resppassneger=response.toString();
                    NextActivityAction= "Active";
                }
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {

                if(requestCode==REQ_TITLE){
                    customertitle.setText(data.getStringExtra("valueName"));
                    customertitlevalue.setText(data.getStringExtra("valueId"));

                    customertitle.setError(null);
                }
            }
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Passenger_Info.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));        }
    }

    public boolean filedverify(){
        boolean valid=true;


        if(mobileno.getText().toString().equals("")){
            mobileno.setError(Utility.getErrorSpannableString(getApplicationContext(), "Number is Required"));
            valid=false;
        }
        if(mobileno.getText().length()!=10){
            mobileno.setError(Utility.getErrorSpannableString(getApplicationContext(), "Number is Wrong"));

            valid=false;
        }

        return valid;
    }





    public boolean cutomerverifyvalidation(){
        boolean valid=true;
        if(customertitle.getText().toString().equals("")){
            customertitle.setError(Utility.getErrorSpannableString(getApplicationContext(), "Title is Required"));
            valid=false;
        }

        if(newcustomername.getText().toString().equals("")){
            newcustomername.setError(Utility.getErrorSpannableString(getApplicationContext(), "Customer Name is Required"));
            valid=false;
        }

        if(mobilenumber.getText().toString().equals("")){
            mobilenumber.setError(Utility.getErrorSpannableString(getApplicationContext(), "Mobile is Required"));
            valid=false;
        }
        if(mobileno.getText().length()!=10){
            mobileno.setError(Utility.getErrorSpannableString(getApplicationContext(), "Number is Wrong"));

            valid=false;
        }



        return valid;
    }
}
