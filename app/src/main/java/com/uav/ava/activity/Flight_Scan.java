package com.uav.ava.activity;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mosambee.lib.TRACE;
import com.printer.sdk.PrinterInstance;
import com.uav.ava.BO.CacheBO;
import com.uav.ava.BO.FlightBO;
import com.uav.ava.MosambeeBarcode.HendleMessageScan;
import com.uav.ava.MosambeeBarcode.MosambeePrintScanTransactionResult;
import com.uav.ava.MosambeeBarcode.SerialPortIOManage;
import com.uav.ava.MosambeeBarcode.SerialPortService;
import com.uav.ava.MosambeePrinterScanner;
import com.uav.ava.R;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.permission.PermissionHandler;
import com.uav.ava.util.ExceptionHandler;
import com.uav.ava.util.Utility;
import com.uav.ava.volley.volley.VolleyResponseListener;
import com.uav.ava.volley.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class Flight_Scan extends AppCompatActivity implements MosambeePrintScanTransactionResult {
    String barCodeElement=null;
    private MyBroadcastReceiver myReceiver;

    SharedPreferences sharedPreferences;
    JSONObject jsonObject;
    TextView activitytitle;



    private AlertDialog alertDialog;




    EditText saledate,flightdate,scanedittext,passengername,flightoriginname,flightdestinationname,flightnumber,travelday,
            seatno,principalname,referencedate,referenceno,crewid,classtype,classtypeid;
    String currentdate;
    ImageView boardingimage,flightget;
    int REQ_BARCODE=1001,REQ_DIALOG=1002,REQ_IMAGE=1003,REQ_CLASS_TYPE=1004 ,REQ_BARCODEIMAGE=1005;
    ScrollView scrollView;
    Button  nextpassengeractivity;
    LinearLayout crewidlayout,referencedatelayout,referencenolayout,principalnamelayout;
    Integer principalid=null;

    ImageButton crewidimage, boardingpass;
    ImageView crewidimageview ,boardingimageview ;
    Uri mImageUri,boardingImageUri;
    Bitmap bmp, bmpBoadingPass;
    boolean principaltype =false;

    String serverresponce;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight__scan);
        getSupportActionBar().hide();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        getUnixTimeStamp();


        activitytitle=findViewById(R.id.activitytitle);
        ImageView logoutbtn=findViewById(R.id.logoutbtn);
        saledate=findViewById(R.id.saledate);
        flightdate=findViewById(R.id.flightdate);
        ImageView  back_activity_button=findViewById(R.id.back_activity_button);
        nextpassengeractivity=findViewById(R.id.nextpassengeractivity);
        scanedittext=findViewById(R.id.scanedittext);
        boardingimage=findViewById(R.id.boardingimage);
        flightget=findViewById(R.id.flightget);
        scrollView=findViewById(R.id.scrollview);
        principalname=findViewById(R.id.principalname);
        referencedate=findViewById(R.id.referencedate);
        referenceno=findViewById(R.id.referenceno);
        crewid=findViewById(R.id.crewid);
        crewidimage=findViewById(R.id.crewidimage);
        crewidimageview=findViewById(R.id.crewidimageview);

        boardingpass=findViewById(R.id.boardingpass);
        boardingimageview=findViewById(R.id.boardingimageview);



        passengername=findViewById(R.id.passengername);
        flightoriginname=findViewById(R.id.flightoriginname);
        flightdestinationname=findViewById(R.id.flightdestinationname);
        flightnumber=findViewById(R.id.flightnumber);
        travelday=findViewById(R.id.travelday);
        seatno=findViewById(R.id.seatno);
        crewidlayout =findViewById(R.id.crewidlayout);
        referencedatelayout=findViewById(R.id.referencedatelayout);
        referencenolayout=findViewById(R.id.referencenolayout);
        principalnamelayout=findViewById(R.id.principalnamelayout);
        //classtype=findViewById(R.id.classtype);
       // classtypeid=findViewById(R.id.classtypeid);


        crewidlayout.setVisibility(View.GONE);
        referencedatelayout.setVisibility(View.GONE);
        referencenolayout.setVisibility(View.GONE);
        principalnamelayout.setVisibility(View.GONE);


        scanedittext.setEnabled(false);
        nextpassengeractivity.setEnabled(false);

        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);

        String resp=sharedPreferences.getString(ApplicationConstant.CACHE_USERNAME,null);
        try {
            jsonObject =new JSONObject(resp);
            activitytitle.setText(jsonObject.getString("userName"));
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Flight_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
        String current=sharedPreferences.getString(ApplicationConstant.CACHE_SERVER_DATE,null);
        try {
            JSONObject datejsone=new JSONObject(current);
            currentdate=datejsone.getString("date");
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Flight_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ApplicationConstant.CACHE_USERNAME);
                editor.commit();
                startActivity(new Intent(Flight_Scan.this,Login_Activity.class));
                finish();
            }
        });

        scanedittext.setEnabled(true);
        scanedittext.setShowSoftInputOnFocus(false);
        scanedittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                barCodeElement ="barcode";
                Intent intent = new Intent();
                intent.putExtra("openPort",true);
                intent.putExtra("deviceType","Scanner");
                intent.setClassName("com.mosambee.printService", "com.mosambee.printService.PrinterService");
                startService(intent);



                return false;
            }
        });








        try {
            JSONObject classobje=new JSONObject();
            JSONArray claary= new JSONArray();



            JSONObject classdata=new JSONObject();
            classdata.put("classname","Economy Class");
            classdata.put("classid",1);
            claary.put(classdata);
            classdata=new JSONObject();
            classdata.put("classname","Executive Class");
            classdata.put("classid",2);
            claary.put(classdata);
            classobje.put("data",claary);

            SharedPreferences.Editor edit= sharedPreferences.edit();
            edit.putString( ApplicationConstant.CACHE_CLASS_TYPE, classobje.toString());
            edit.apply();
            edit.commit();





        } catch (Exception e) {
            Utility.exceptionAlertDialog(Flight_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }


        crewidimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!PermissionHandler.cameraPermission(Flight_Scan.this)){
                    return;
                }
                if(!PermissionHandler.readWritePermission(Flight_Scan.this)){
                    return;
                }


                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File photo;
                try
                {
                    // place where to store camera taken picture
                    photo = VolleyUtils.createTemporaryFile("picture", ".jpg");
                    photo.delete();
                    mImageUri = Uri.fromFile(photo);
                    /*Uri mImageUri = CustomProvider.getPhotoUri(photo);
                     */
                    Uri mImageUri = FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()
                            + ".provider", photo);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, REQ_IMAGE);
                }
                catch(Exception e)
                {
                    Utility.exceptionAlertDialog(Flight_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                    return;
                }
            }
        });



        crewidimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Flight_Scan.this, SingleImage.class);
                intent.setAction(ApplicationConstant.SINGLE_IMAGE_ACTION_USER);
                intent.putExtra(ApplicationConstant.SINGLE_IMAGE_ACTION_USER, mImageUri.toString());
                startActivity(intent);
            }
        });




        boardingpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!PermissionHandler.cameraPermission(Flight_Scan.this)){
                    return;
                }
                if(!PermissionHandler.readWritePermission(Flight_Scan.this)){
                    return;
                }


                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File photo;
                try
                {
                    // place where to store camera taken picture
                    photo = VolleyUtils.createTemporaryFile("picture", ".jpg");
                    photo.delete();
                    boardingImageUri = Uri.fromFile(photo);
                    /*Uri mImageUri = CustomProvider.getPhotoUri(photo);
                     */
                    Uri mImageUri = FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()
                            + ".provider", photo);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, REQ_BARCODEIMAGE);
                }
                catch(Exception e)
                {
                    Utility.exceptionAlertDialog(Flight_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                    return;
                }
            }
        });



        boardingimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Flight_Scan.this, SingleImage.class);
                intent.setAction(ApplicationConstant.SINGLE_IMAGE_ACTION_USER);
                intent.putExtra(ApplicationConstant.SINGLE_IMAGE_ACTION_USER, boardingImageUri.toString());
                startActivity(intent);
            }
        });



        flightnumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //onFocus
                if (gainFocus) {
                    //set the row background to a different color
                }
                //onBlur
                else {
                    if (!flightnumber.getText().toString().equals("")) {
                       String output= (flightnumber.getText().toString()).replaceAll("[- ]", "");
                       if(output.length()<3){
                           Utility.alertDialog(Flight_Scan.this,"Validation!", "Flight Number Not Valid","OK");
                           return;
                       }
                        String flightcode=output.substring(0,2);
                        String number=output.substring(2);

                        if (number.matches("[0-9]+")){
                            DecimalFormat df = new DecimalFormat("0000");
                            String c = df.format(Integer.parseInt(number));
                            flightnumber.setText(flightcode+"-"+c);
                        }else {
                            Utility.alertDialog(Flight_Scan.this,"Validation!", "Flight Number Not Valid","OK");
                            return;
                        }
                    }
                }
            }
        });














        nextpassengeractivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(ApplicationConstant.CACHE_INDIGOONFLIGHTPAYMENT);
                    editor.commit();

                    boolean validflight=true;
                    if((referencedate.getText().toString().equals("") && !referenceno.getText().toString().equals(""))|| (!referencedate.getText().toString().equals("") && referenceno.getText().toString().equals(""))){

                        if(referencedate.getText().toString().equals("")){
                            referencedate.setError(Utility.getErrorSpannableString(getApplicationContext(), "Reference Date  is Required"));

                        }
                        if(referenceno.getText().toString().equals("")){
                            referenceno.setError(Utility.getErrorSpannableString(getApplicationContext(), "Reference Number is Required"));
                        }

                        validflight=false;

                    }

                    if((crewid.getText().toString().equals("") && crewidimageview.getDrawable()!=null)|| (!crewid.getText().toString().equals("") && crewidimageview.getDrawable()==null)){

                        if(crewid.getText().toString().equals("")){
                            crewid.setError(Utility.getErrorSpannableString(getApplicationContext(), "CrewId  is Required"));

                        }
                        if(crewidimageview.getDrawable()==null){

                           Utility.alertDialog(Flight_Scan.this,"Validation!","Crew Image is Required","OK");
                        }

                        validflight=false;

                    }


                    if(!validflight){
                        return;
                    }else {

                        activitydatasavecache();

                        if(!referencedate.getText().toString().equals("")&& !referenceno.getText().toString().equals("")){
                            getIndigoOnFlightPayment();
                        }else {
                            startActivity(new Intent(Flight_Scan.this,Passenger_Info.class));
                        }

                    }
                    /*else{

                        if(!referencedate.getText().toString().equals("")&& !referenceno.getText().toString().equals("")){
                            getIndigoOnFlightPayment();
                        }else {
                            startActivity(new Intent(Flight_Scan.this,Passenger_Info.class));
                        }

                    }
*/
                }catch (Exception e){
                    Utility.exceptionAlertDialog(Flight_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));

                }

            }
        });



        saledate.setText(currentdate);
        flightdate.setText(currentdate);



        flightdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(MotionEvent.ACTION_UP == event.getAction()) {

                    int mYear, mMonth, mDay, mHour, mMinute;
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(Flight_Scan.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {


                                    DecimalFormat df = new DecimalFormat("00");
                                    int m=monthOfYear+1;
                                    String month = df.format(m);

                                    String day=df.format(dayOfMonth);
                                    flightdate.setText( day+ "/" + month+ "/" +year);
                                    flightdate.setError(null);
                                }
                            }, mYear, mMonth, mDay);
                    try {
                        Date bookingDate = new SimpleDateFormat("dd/MM/yyyy").parse(currentdate);
                        datePickerDialog.getDatePicker().setMinDate(bookingDate.getTime()- 86400000);
                        datePickerDialog.getDatePicker().setMaxDate(bookingDate.getTime());
                        datePickerDialog.show();
                    } catch (ParseException e) {
                        Utility.exceptionAlertDialog(Flight_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                    }
                }
                return false;
            }


        });


        saledate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(MotionEvent.ACTION_UP == event.getAction()) {
                    int mYear, mMonth, mDay, mHour, mMinute;
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(Flight_Scan.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    DecimalFormat df = new DecimalFormat("00");
                                    int m=monthOfYear+1;
                                    String month = df.format(m);


                                    String day=df.format(dayOfMonth);
                                    saledate.setText(day + "/" + month + "/" +year);
                                    saledate.setError(null);
                                }
                            }, mYear, mMonth, mDay);
                    try {
                        Date bookingDate = new SimpleDateFormat("dd/MM/yyyy").parse(currentdate);
                        datePickerDialog.getDatePicker().setMinDate(bookingDate.getTime());
                        datePickerDialog.getDatePicker().setMaxDate(bookingDate.getTime());
                        datePickerDialog.show();
                    } catch (ParseException e) {
                        Utility.exceptionAlertDialog(Flight_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                    }

                }
                return false;
            }


        });









        referencedate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(MotionEvent.ACTION_UP == event.getAction()) {
                    int mYear, mMonth, mDay, mHour, mMinute;
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(Flight_Scan.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {


                                    DecimalFormat df = new DecimalFormat("00");
                                    int m=monthOfYear+1;
                                    String month = df.format(m);
                                    String day=df.format(dayOfMonth);
                                    referencedate.setText(day + "/" + month + "/" +year);
                                    referencedate.setError(null);
                                }
                            }, mYear, mMonth, mDay);
                    try {
                        Date bookingDate = new SimpleDateFormat("dd/MM/yyyy").parse(currentdate);
                        //  datePickerDialog.getDatePicker().setMinDate(bookingDate.getTime()- 86400000);
                        datePickerDialog.getDatePicker().setMaxDate(bookingDate.getTime());
                        datePickerDialog.show();
                    } catch (ParseException e) {
                        Utility.exceptionAlertDialog(Flight_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                    }

                }
                return false;
            }


        });








       /* classtype.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    ConnectionVO connectionVO = new ConnectionVO();
                    connectionVO.setTitle("Class Type");
                    connectionVO.setSharedPreferenceKey(ApplicationConstant.CACHE_CLASS_TYPE);
                    connectionVO.setEntityIdKey("classid");
                    connectionVO.setEntityTextKey("classname");
                    Intent intent = new Intent(getApplicationContext(),ListViewSingleText.class);
                    intent.putExtra(ApplicationConstant.INTENT_EXTRA_CONNECTION,  connectionVO);
                    startActivityForResult(intent,REQ_CLASS_TYPE);
                }
                return true; // return is important...
            }
        });
*/






























        boardingimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionHandler.cameraPermission(Flight_Scan.this)) {

                    return;
                }

                /*if(classtype.getText().toString().equals("") || classtypeid.getText().toString().equals("")){
                    classtype.setError(Utility.getErrorSpannableString(getApplicationContext(), "Class is Required"));
                }else {
                    startActivityForResult(new Intent(Flight_Scan.this,BarcodeScanner.class), REQ_BARCODE);
                }*/


                switch (ApplicationConstant.BARCODE_SCANNER) {
                    case ApplicationConstant.BARCODE_DETECTOR_CODE:
                        startActivityForResult(new Intent(Flight_Scan.this,BarcodeScanner.class), REQ_BARCODE);
                        break;

                    case ApplicationConstant.BARCODE_ZXING_CODE:
                        zxingBarCode();
                        break;
                    default:
                        break;
                }



            }
        });

        flightget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!filedvaild()){
                    return;
                }else {
                    try {
                        getFlight(flightnumber.getText().toString());


                    } catch (Exception e) {
                        Utility.exceptionAlertDialog(Flight_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                    }
                }
            }
        });
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
                        setflightscandetail(scandata);
                    }
                    return;
                }

                break;
            default:
                break;
        }



        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if(requestCode==REQ_DIALOG){
                nextpassengeractivity.setEnabled(false);
                nextpassengeractivity.animate().alpha(1.0f).setDuration(3000).start();
            }

            if (requestCode == REQ_BARCODE) {
                String scandata=data.getStringExtra("key");
                setflightscandetail(scandata);
            }

            if (requestCode == REQ_IMAGE) {
                bmp =VolleyUtils.grabImage(mImageUri,Flight_Scan.this);
                if(bmp.getWidth()>bmp.getHeight()){
                    Matrix matrix =new Matrix();
                    matrix.postRotate(90);
                    bmp= Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
                }
                crewidimageview.setImageBitmap(bmp);
                crewidimageview.setVisibility(View.VISIBLE);
                View current = getCurrentFocus();
                if (current != null) current.clearFocus();
            }

            if (requestCode == REQ_BARCODEIMAGE) {
                Bitmap bmp =VolleyUtils.grabImage(boardingImageUri,Flight_Scan.this);
                if(bmp.getWidth()>bmp.getHeight()){
                    Matrix matrix =new Matrix();
                    matrix.postRotate(90);
                    bmp= Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
                }
                bmpBoadingPass =bmp;
                boardingimageview.setImageBitmap(bmp);
                boardingimageview.setVisibility(View.VISIBLE);
                View current = getCurrentFocus();
                if (current != null) current.clearFocus();

            }
          /*  if(requestCode==REQ_CLASS_TYPE){
                classtype.setText(data.getStringExtra("valueName"));
                classtypeid.setText(data.getStringExtra("valueId"));
                classtype.setError(null);
            }*/




        }
    }

    public void setflightscandetail(String scandata){
        passengername.setError(null);
        flightoriginname.setError(null);
        flightdestinationname.setError(null);
        flightnumber.setError(null);
        travelday.setError(null);
        seatno.setError(null);


        passengername.setText("");
        flightoriginname.setText("");
        flightdestinationname.setText("");
        flightnumber.setText("");
        travelday.setText("");
        seatno.setText("");

        if(scandata.length()<58) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }else {
            passengername.setText(scandata.substring(2, 22));
            flightoriginname.setText(scandata.substring(30, 33));
            flightdestinationname.setText(scandata.substring(33, 36));





            String flightcode=scandata.substring(36, 38).trim();
            String number=scandata.substring(38, 43).trim();
            DecimalFormat df = new DecimalFormat("0000");
            String c = df.format(Integer.parseInt(number));
            flightnumber.setText(flightcode+"-"+c);
            travelday.setText(scandata.substring(44, 47));
            seatno.setText(scandata.substring(48, 52));



            try {
                getFlight(flightnumber.getText().toString());
            } catch (Exception e) {
                Utility.exceptionAlertDialog(Flight_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));

            }
        }



    }



    public  void getFlight(final String flightno) throws JSONException {
        VolleyUtils.makeJsonObjectRequest(this, FlightBO.getFlight(jsonObject.getString("userId"),flightno,flightdate.getText().toString()), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object resp) throws JSONException {


                SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date1=new Date();


                    Date flightdatecale = myFormat.parse(flightdate.getText().toString());
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(flightdatecale);
                    String year = String.valueOf(calendar.get(Calendar.YEAR));
                    String inputString2 = "01/01/"+year;

                    Date startingdate = myFormat.parse(inputString2);
                    long diff =  flightdatecale.getTime()- startingdate.getTime();

                    int Totalday=((int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+1);

                    if(Integer.parseInt(travelday.getText().toString())!=Totalday){
                        Utility.alertDialog(Flight_Scan.this,"Alert!"," Flight Day And Travel Day  does't  Match !","ok");
                    }

                } catch (Exception e) {
                    Utility.exceptionAlertDialog(Flight_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                }



                JSONObject response = (JSONObject) resp;
                if(response.get("status").equals("fail") || response.get("status").equals("NOT FOUND") || response.get("status").equals("OPERATION-FAILED")){
                   /* Intent intent =  new Intent(Flight_Scan.this, Dialog.class);
                    DiaglogVO diaglogVO =new DiaglogVO(DiaglogVO.BTN_OK);
                    diaglogVO.setMessage((String) response.get("status"));
                    diaglogVO.setTitle("Alert");
                    intent.putExtra("dialog", diaglogVO);
                    intent.setAction(ApplicationConstant.ACTION_DIALOG_SINGLEBUTTON);
                    startActivityForResult(intent, REQ_DIALOG);
                    overridePendingTransition(0, 0);*/
                    serverresponce=null;
                    String mas= response.getString("status");

                    flightnumber.setError(Utility.getErrorSpannableString(getApplicationContext(),mas));
                    scanedittext.setEnabled(true);
                    return;
                }else {
                    nextpassengeractivity.setEnabled(true);

                     serverresponce=response.toString();


                    JSONObject dataflight= response.getJSONObject("data");
                    principalid= Integer.parseInt(dataflight.getString("principalId"));

                    String principalarry=sharedPreferences.getString(ApplicationConstant.PRINCIPAL_CACHE,null);
                    JSONObject principaljson= new JSONObject(principalarry);
                    String principalrep=principaljson.getString("data");
                    JSONArray jsonarry=new JSONArray(principalrep);

                   // 10/10/2019
                    if(principalid==12) {
                        referencedatelayout.setVisibility(View.VISIBLE);
                        referencenolayout.setVisibility(View.VISIBLE);
                        principaltype=true;

                    }
                    crewidlayout.setVisibility(View.VISIBLE);


                    for(int i=0 ;i< jsonarry.length() ;i++){
                        JSONObject jsn = jsonarry.getJSONObject(i);
                        if(Integer.parseInt(jsn.getString("principalId"))== principalid){
                            principalname.setText(jsn.getString("principalName"));
                            principalnamelayout.setVisibility(View.VISIBLE);
                            break;
                        }


                    }
                    saledate.setEnabled(false);
                    flightdate.setEnabled(false);
                    scanedittext.setEnabled(false);
                    boardingimage.setEnabled(false);
                    flightget.setEnabled(false);
                    principalname.setEnabled(false);

                    passengername.setEnabled(false);
                    flightoriginname.setEnabled(false);
                    flightdestinationname.setEnabled(false);
                    flightnumber.setEnabled(false);
                    travelday.setEnabled(false);
                    seatno.setEnabled(false);
                    //classtype.setEnabled(false);



                }

            }
        });
    }



    public void getIndigoOnFlightPayment(){

        VolleyUtils.makeJsonObjectRequest(this, FlightBO.getIndigoOnFlightPayment(principalid,referenceno.getText().toString(),referencedate.getText().toString()), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                if(response.getString("status").equals("SUCCESS")){
                    SharedPreferences.Editor edit= sharedPreferences.edit();
                    edit.putString( ApplicationConstant.CACHE_INDIGOONFLIGHTPAYMENT, response.toString());
                    edit.apply();
                    edit.commit();
                }
                startActivity(new Intent(Flight_Scan.this,Passenger_Info.class));
            }
        });



    }

    public  Boolean filedvaild(){
        boolean valid=true;


        if(passengername.getText().toString().equals("")){
            passengername.setError(Utility.getErrorSpannableString(getApplicationContext(), "Passenger Name is Required"));
            valid=false;
        }
        if(flightnumber.getText().toString().equals("")){
            flightnumber.setError(Utility.getErrorSpannableString(getApplicationContext(), "Flight Number is Required"));
            valid=false;
        }
        if(seatno.getText().toString().equals("")){
            seatno.setError(Utility.getErrorSpannableString(getApplicationContext(), "Seat Number is Required"));
            valid=false;
        }
        if(flightoriginname.getText().toString().equals("")){
            flightoriginname.setError(Utility.getErrorSpannableString(getApplicationContext(), "Origin  is Required"));
            valid=false;
        }

        if(flightdestinationname.getText().toString().equals("")){
            flightdestinationname.setError(Utility.getErrorSpannableString(getApplicationContext(), "Destination  is Required"));
            valid=false;
        }
        if(seatno.getText().toString().equals("")){
            seatno.setError(Utility.getErrorSpannableString(getApplicationContext(), "Seat Number is Required"));
            valid=false;
        }
        if(travelday.getText().toString().equals("")){
            travelday.setError(Utility.getErrorSpannableString(getApplicationContext(), "Travel Day is Required"));
            valid=false;
        }

       /* if(classtype.getText().toString().equals("") || classtypeid.getText().toString().equals("")){
            classtype.setError(Utility.getErrorSpannableString(getApplicationContext(), "Class is Required"));
            valid=false;
        }
*/

        return valid;
    }

    private void activitydatasavecache(){
      JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("saledate",saledate.getText().toString());
            jsonObject.put("flightdate",flightdate.getText().toString());
            jsonObject.put("passengername",passengername.getText().toString());
            jsonObject.put("flightoriginname",flightoriginname.getText().toString());
            jsonObject.put("flightdestinationname",flightdestinationname.getText().toString());
            jsonObject.put("flightnumber",flightnumber.getText().toString());
            jsonObject.put("travelday",travelday.getText().toString());
            jsonObject.put("seatno",seatno.getText().toString());
            jsonObject.put("principalno",principalid);
            jsonObject.put("referenceno",referenceno.getText().toString());
            jsonObject.put("referencedate",referencedate.getText().toString());
            jsonObject.put("crewid",crewid.getText().toString());
            jsonObject.put("principaltype",principaltype);


            if(bmp!=null){
                jsonObject.put("crewidimage",Utility.imagetostring(bmp));
            }

            if(bmpBoadingPass!=null){
                jsonObject.put("boardingPassImage",Utility.imagetostring(bmpBoadingPass));
            }

          //  jsonObject.put("classid","");
            jsonObject.put("resp",serverresponce);
            SharedPreferences.Editor edit= sharedPreferences.edit();
            edit.putString( ApplicationConstant.CACHE_PASSENGER_DETAIL, jsonObject.toString());
            edit.apply();
            edit.commit();
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Flight_Scan.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
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
        if(barCodeElement.equals("barcode")){
            setflightscandetail(data);
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
                            Intent startIntent2 = new Intent(Flight_Scan.this, SerialPortService.class);
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
                            SerialPortIOManage.getInstance().setCallBackActivity(Flight_Scan.this);
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
