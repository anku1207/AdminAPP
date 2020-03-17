package com.uav.ava.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;


import com.uav.ava.R;
import com.uav.ava.activity.Product_Scan;
import com.uav.ava.activity.SendLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class Utility {
    static int errorColor;

    static  Context contextact;
    static  String erroract;



    public static SpannableStringBuilder getErrorSpannableString(Context context, String msg){
        int version = Build.VERSION.SDK_INT;
        //Get the defined errorColor from color resource.
        if (version >= 23) {
            errorColor = ContextCompat.getColor(context, R.color.errorColor);
        } else {
            errorColor = context.getResources().getColor(R.color.errorColor);
        }

        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(msg);
        spannableStringBuilder.setSpan(foregroundColorSpan, 0, msg.length(), 0);

        return spannableStringBuilder;
    }


    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    public boolean isInternetAvailable() {
        try {
            final InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (Exception e) {
            // Log error
        }
        return false;
    }


    public static    String imagetostring (Bitmap bitmap){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        Bitmap  bmp1= Bitmap.createScaledBitmap(
                bitmap, 320, 500, false);
        bmp1.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes =outputStream.toByteArray();
        return Base64.encodeToString(imageBytes,Base64.DEFAULT);
    }


    public static Integer getVersioncode(Context context){
        Integer version =null;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionCode;
            Log.w("versioncode", String.valueOf(version));
        } catch (Exception e) {
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return version;
    }






    public static void  alertDialog(Context context,String title ,String msg , String buttonname){

        AlertDialog alertDialog;

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonname, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


            }
        });
        alertDialog.show();

    }






    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }



    public static void  exceptionAlertDialog(Context context,String title ,String msg , String buttonname,String error){

        contextact=context;
        erroract=error;

        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonname, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                sendLogFile(contextact,erroract);

            }
        });
        alertDialog.show();

    }

    private static void sendLogFile(Context context ,String error) {


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"manojshakya1207@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Error reported from MyAPP");
        intent.putExtra(Intent.EXTRA_TEXT, "Log file attached."+error); // do this so some email clients don't complain about empty body.
        context.startActivity(intent);
    }












    public  static   String getJsonKey(JSONObject jObj) throws JSONException {
        Iterator<?> keys = jObj.keys();
        String key = "";
        while (keys.hasNext() ){
            key = (String) keys.next();
            break;
        }
        return key;
        /*



        String key = "";


        Log.w("keys",keys.toString());


            while (keys.hasNext() && !key.equalsIgnoreCase(findKey)) {
                key = (String) keys.next();
                if (key.equalsIgnoreCase(findKey)) {
                    return jObj.getString(key);
                }
                if (jObj.get(key) instanceof JSONObject) {
                    return Utility.recurseKeys((JSONObject)jObj.get(key), findKey);
                }

        }
        return "";
        */
    }



    public static String changeDateFormat(String respdate,Context context){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Timestamp ts = null;
        String changedate=null;
        try {
            ts = new Timestamp(((Date)df.parse(respdate)).getTime());
            Date date = new Date();
            date.setTime(ts.getTime());
            changedate= new SimpleDateFormat("yyyy-MM-dd").format(date);

        } catch (Exception e) {
            Utility.exceptionAlertDialog(context,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }

        return changedate;

    }



    public static String currentDateFormat(){

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSSSSS");
        String currentTimeStamp=dateFormat.format(new Date());
        return  currentTimeStamp;

    }

    public static File storeCameraPhotoInSDCard(Bitmap bitmap , String currentdate){
        //  File outputFile = new File(Environment.getExternalStorageDirectory(),"photo_"+currentdate);
        File direct = new File(Environment.getExternalStorageDirectory() + "/ROF");
        if (!direct.exists()) {
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory()+"/"+"ROF/");
            wallpaperDirectory.mkdirs();
        }
        File file = new File(new File(Environment.getExternalStorageDirectory()+"/"+"ROF/"), "photo_"+currentdate+".JPEG");
        /*if (file.exists()) {
            file.delete();
        }*/
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
