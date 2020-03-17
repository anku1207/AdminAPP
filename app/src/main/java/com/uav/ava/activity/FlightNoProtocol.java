package com.uav.ava.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uav.ava.BO.FlightBO;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.permission.PermissionHandler;
import com.uav.ava.util.ExceptionHandler;
import com.uav.ava.util.Utility;
import com.uav.ava.vo.ConnectionVO;
import com.uav.ava.vo.DiaglogVO;
import com.uav.ava.volley.volley.VolleyResponseListener;
import com.uav.ava.volley.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.uav.ava.R;

public class FlightNoProtocol extends AppCompatActivity {
    private static int REQ_BARCODE = 1003,REQ_REASON=1001,REQ_IMAGE=1002,REQ_DIALOG=104,REQ_DIALOG_RESPONCE=105;
    EditText flightnoprotocol,passengername,flightoriginname,flightdestinationname,flightnumber,travelday,seatno, reasonname, reasonid,mobileno;
    SharedPreferences sharedPreferences;
    JSONObject jsonObject;
    ImageButton imageclick;
    ImageView image;
    private Uri mImageUri;
    Bitmap bmp;
    Button nosaleinfo;
    String FlightOperationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_no_protocol);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        getSupportActionBar().hide();
        ImageView logoutbtn=findViewById(R.id.logoutbtn);
        TextView mainactivitytitle = findViewById(R.id.mainactivitytitle);
        ImageButton cameraofawbno = (ImageButton) findViewById(R.id.cameraofawbno);
        passengername=findViewById(R.id.passengername);
        flightoriginname=findViewById(R.id.flightoriginname);
        flightdestinationname=findViewById(R.id.flightdestinationname);
        flightnumber=findViewById(R.id.flightnumber);
        travelday=findViewById(R.id.travelday);
        seatno=findViewById(R.id.seatno);
        flightnoprotocol=findViewById(R.id.scanedittext);
        imageclick=findViewById(R.id.imageclick);
        image=findViewById(R.id.image);
        mobileno=findViewById(R.id.mobileno);

        reasonname =findViewById(R.id.reasonname);
        reasonid =findViewById(R.id.reasonid);
        nosaleinfo=findViewById(R.id.nosaleinfo);


        flightnoprotocol.setEnabled(false);


        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,Context.MODE_PRIVATE);
        String resp=sharedPreferences.getString(ApplicationConstant.CACHE_USERNAME,null);
        try {
            jsonObject =new JSONObject(resp);
            mainactivitytitle.setText(jsonObject.getString("userName"));
        } catch (Exception e) {
            Utility.exceptionAlertDialog(FlightNoProtocol.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }









        imageclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!PermissionHandler.cameraPermission(FlightNoProtocol.this)){
                    return;
                }


                if(!PermissionHandler.readWritePermission(FlightNoProtocol.this)){
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
                    Log.d("Exception",e.getMessage());
                    Utility.exceptionAlertDialog(FlightNoProtocol.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                    return;
                }
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byteArray = stream.toByteArray();*/
                Intent intent = new Intent(FlightNoProtocol.this, SingleImage.class);
                intent.setAction(ApplicationConstant.SINGLE_IMAGE_ACTION_USER);
                intent.putExtra(ApplicationConstant.SINGLE_IMAGE_ACTION_USER, mImageUri.toString());

                startActivity(intent);
            }
        });
















        nosaleinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    noSaleInfoSave();
                } catch (Exception e) {
                    Utility.exceptionAlertDialog(FlightNoProtocol.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                }
            }
        });











       /* flightnumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //onFocus
                if (gainFocus) {
                    //set the row background to a different color
                }
                //onBlur
                else {
                    if (!flightnumber.getText().toString().equals("")) {
                        try {
                            FlightNoSalesInfo(flightnumber.getText().toString());
                        } catch (Exception e) {
                            Utility.exceptionAlertDialog(FlightNoProtocol.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                        }
                    }
                }
            }
        });
*/

        flightnumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {

                try {
                //onFocus
                if (gainFocus) {
                    //set the row background to a different color
                }
                //onBlur
                else {
                    if (!flightnumber.getText().toString().equals("")) {
                        String output= (flightnumber.getText().toString()).replaceAll("[- ]", "");
                        if(output.length()<3){
                            Utility.alertDialog(FlightNoProtocol.this,"Validation!", "Flight Number Not Valid","OK");
                            return;
                        }
                        String flightcode=output.substring(0,2);
                        String number=output.substring(2);

                        if (number.matches("[0-9]+")){
                            DecimalFormat df = new DecimalFormat("0000");
                            String c = df.format(Integer.parseInt(number));
                            flightnumber.setText(flightcode+"-"+c);


                            FlightNoSalesInfo(flightnumber.getText().toString());
                        }



                        }else {
                            Utility.alertDialog(FlightNoProtocol.this,"Validation!", "Flight Number Not Valid","OK");
                            return;
                        }
                    }
                }catch (Exception e) {
                    Utility.exceptionAlertDialog(FlightNoProtocol.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                }
            }
        });









        reasonname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(MotionEvent.ACTION_UP == event.getAction()) {

                    ConnectionVO connectionVO = new ConnectionVO();
                    connectionVO.setTitle("Reason");
                    connectionVO.setSharedPreferenceKey(ApplicationConstant.CACHE_FLIGHTNOSALEREASONS);
                    connectionVO.setEntityIdKey("ReasonID");
                    connectionVO.setEntityTextKey("Reason");
                    Intent intent = new Intent(getApplicationContext(),ListViewSingleText.class);
                    intent.putExtra(ApplicationConstant.INTENT_EXTRA_CONNECTION,  connectionVO);
                    startActivityForResult(intent,REQ_REASON);
                }
                return false;
            }


        });










        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ApplicationConstant.CACHE_USERNAME);
                editor.commit();
                startActivity(new Intent(FlightNoProtocol.this,Login_Activity.class));
                finish();


            }
        });





        ImageView back_activity_button =findViewById(R.id.back_activity_button);

        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        cameraofawbno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionHandler.cameraPermission(FlightNoProtocol.this)) {
                    Toast.makeText(FlightNoProtocol.this, "sddd", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivityForResult(new Intent(FlightNoProtocol.this,BarcodeScanner.class), REQ_BARCODE);

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == REQ_BARCODE) {

                String scandata=data.getStringExtra("key");

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
                        FlightNoSalesInfo(flightnumber.getText().toString());
                    } catch (Exception e) {
                        Utility.exceptionAlertDialog(FlightNoProtocol.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
                    }
                }


            }
            if (requestCode == REQ_REASON) {

                reasonname.setText(data.getStringExtra("valueName"));
                reasonid.setText(data.getStringExtra("valueId"));
            }






            if (requestCode == REQ_IMAGE) {
                    bmp =VolleyUtils.grabImage(mImageUri,FlightNoProtocol.this);
                    if(bmp.getWidth()>bmp.getHeight()){
                        Matrix matrix =new Matrix();
                        matrix.postRotate(90);
                        bmp= Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
                    }
                    image.setImageBitmap(bmp);
                    image.setVisibility(View.VISIBLE);
                    View current = getCurrentFocus();
                    if (current != null) current.clearFocus();
            }

            if(requestCode==REQ_DIALOG_RESPONCE){
                finish();
                startActivity(new Intent(this,FlightNoProtocol.class));
            }
        }
    }

    public  void FlightNoSalesInfo(String flightno) throws JSONException {
           VolleyUtils.makeJsonObjectRequest(this, FlightBO.flightverify(jsonObject.getString("userId"),flightno), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                Log.w("responce",response.toString());
                if(response.get("status").equals("fail")){
                    JSONArray error = response.getJSONArray("error");
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<error.length(); i++){
                        sb.append(error.get(i)).append("\n");
                    }
                    FlightOperationId="";
                    flightnumber.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    flightnumber.setError(Utility.getErrorSpannableString(getApplicationContext(), sb.toString()));
                    return;
                }
                flightnumber.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                JSONObject data=response.getJSONObject("data");
                if(data.getString("ReasonId")=="null"){
                    reasonname.setText("");
                    reasonid.setText("");
                    flightnumber.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);
                    reasonname.setEnabled(true);
                }else {
                    reasonname.setText(data.getString("ReasonName"));
                    reasonid.setText(data.getString("ReasonId"));
                    flightnumber.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);
                    reasonname.setEnabled(false);
                }

                FlightOperationId=data.getString("FlightOperationId");

            }
        });
    }



    public void  noSaleInfoSave() throws JSONException {

            Boolean valid=true;

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
            if(reasonname.getText().toString().equals("") || reasonid.getText().toString().equals("")){
                reasonname.setError(Utility.getErrorSpannableString(getApplicationContext(), "Reason is Required"));
                valid=false;
            }
            if(image.getDrawable() == null){
                Intent intent =  new Intent(FlightNoProtocol.this, Dialog.class);
                DiaglogVO diaglogVO =new DiaglogVO(DiaglogVO.BTN_OK);
                diaglogVO.setMessage("Image is Empty ");
                diaglogVO.setTitle("Alert");
                intent.putExtra("dialog", diaglogVO);
                intent.setAction(ApplicationConstant.ACTION_DIALOG_SINGLEBUTTON);
                startActivityForResult(intent, REQ_DIALOG);
                overridePendingTransition(0, 0);
                valid=false;
                return;
            }

            if(FlightOperationId=="" || jsonObject.getString("userId")=="" ||  jsonObject.getString("userId")==null){


                Intent intent =  new Intent(FlightNoProtocol.this, Dialog.class);
                DiaglogVO diaglogVO =new DiaglogVO(DiaglogVO.BTN_OK);
                diaglogVO.setMessage("Something is wrong");
                diaglogVO.setTitle("Alert");
                intent.putExtra("dialog", diaglogVO);
                intent.setAction(ApplicationConstant.ACTION_DIALOG_SINGLEBUTTON);
                startActivityForResult(intent, REQ_DIALOG);
                overridePendingTransition(0, 0);
                valid=false;
                return;
            }


            if(!valid){
                return;
            }else {
                HashMap<String, Object> params = new HashMap<String, Object>();
                ConnectionVO connectionVO = FlightBO.saveNoFlightReason();
                params.put("flightOperationId",FlightOperationId);
                params.put("userId",jsonObject.getString("userId"));
                params.put("passengerName",passengername.getText().toString());
                params.put("seatNo",seatno.getText().toString());
                params.put("reasonId",reasonid.getText().toString());
                params.put("mobileNo",mobileno.getText().toString().equals("") ? "--" : mobileno.getText().toString());
                params.put("image",imagetostring(bmp));
                connectionVO.setParams(params);



                VolleyUtils.makeJsonObjectRequest(this, connectionVO, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                    }
                    @Override
                    public void onResponse(Object resp) throws JSONException {
                        JSONObject response = (JSONObject) resp;

                        if(response.get("status").equals("fail")){
                            VolleyUtils.furnishErrorMsg(  "Save " ,response, FlightNoProtocol.this);
                            return;
                        }else {


                            Intent intent = new Intent(FlightNoProtocol.this,Dialog.class);
                            DiaglogVO diaglogVO = new DiaglogVO(DiaglogVO.BTN_OK);
                            diaglogVO.setMessage("Successfully  Save");
                            diaglogVO.setTitle("Success");
                            intent.putExtra("dialog", diaglogVO);
                            intent.setAction(ApplicationConstant.ACTION_DIALOG_TWOBUTTONS);
                            startActivityForResult(intent, REQ_DIALOG_RESPONCE);
                            overridePendingTransition(0, 0);

                        }

                    }
                });






            }
    }


    private  String imagetostring (Bitmap bitmap){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        Bitmap  bmp1= Bitmap.createScaledBitmap(
                bmp, 320, 500, false);
        bmp1.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes =outputStream.toByteArray();
        return Base64.encodeToString(imageBytes,Base64.DEFAULT);
    }
}