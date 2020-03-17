package com.uav.ava.volley.volley;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.util.Utility;
import com.uav.ava.vo.ConnectionVO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class VolleyUtils {
    ProgressBar progressBar;
    static VolleyResponseListener responseListener;
    static String errorMessage;
    private static final String PROTOCOL_CHARSET="utf-8";
    private static Context mctx;

    public static void makeJsonObjectRequest(final Context context, ConnectionVO connectionVO, final VolleyResponseListener listener) {
        mctx = context;
        Map<String, Object> params = connectionVO.getParams();
        responseListener  = listener;
        JSONObject jsonParams = null;
        if(params!=null){
            jsonParams = new JSONObject(params);
        }

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Connecting ...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        Log.w("loader",connectionVO.getLoader().toString());

        if(!connectionVO.getLoader()){
            pDialog.show();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (connectionVO.getRequestType(), ApplicationConstant.getHttpURL(context) + connectionVO.getMethodName() , jsonParams, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            if(response.has("status") && response.getString("status").equalsIgnoreCase("versionfailed")){
                                final AlertDialog alertDialog;
                                alertDialog = new AlertDialog.Builder(context).create();
                                alertDialog.setTitle("Release Update");
                                alertDialog.setMessage(response.getString("error"));
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        alertDialog.dismiss();
                                        if(checkAndRequestPermission()) {
                                            UpdateApp atualizaApp = new UpdateApp();
                                            atualizaApp.setContext(mctx);
                                            atualizaApp.execute(ApplicationConstant.HTTP_APK_DOWNLOAD_URL);
                                        }
                                    }
                                });
                                alertDialog.show();
                                return;
                            }else {
                                listener.onResponse(response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                       // Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        boolean showerror=true;
                        pDialog.dismiss();
                        if (volleyError instanceof NetworkError) {
                            errorMessage = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ServerError) {
                            errorMessage = "The server could not be found. Please try again after some time!!";

                        } else if (volleyError instanceof AuthFailureError) {
                            errorMessage = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            errorMessage = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            errorMessage = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError) {
                            errorMessage = "Connection TimeOut! Please check your internet connection.";
                        }else {
                            errorMessage= volleyError.toString();
                        }
                        if(showerror) showError("Connection Error", errorMessage, context );
                    //    listener.onError(error.toString());
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Accept","*/*");
                params.put("Accept-Language","en");
                params.put("authkey","G4s4cCMx2aM7lky1");
                params.put("versioncode", String.valueOf(Utility.getVersioncode(context)));
                return params;
            }


            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                pDialog.dismiss();
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    return Response.success(new JSONObject(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };

        // Access the RequestQueue through singleton class.
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(ApplicationConstant.SOCKET_TIMEOUT_MILLISEC,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addTorequestque(jsonObjectRequest);
    }





    public static   void showError(String title, String error, final Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(error)
                .setTitle(title+"!")
                .setIcon(android.R.drawable.ic_dialog_alert)


                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent =new Intent();
                        responseListener.onError(errorMessage);
                    }
                });
        AlertDialog alert= builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();


    }


    public static  void furnishErrorMsg(String errorTitle, JSONObject jsonObject, Context context) throws JSONException {
        JSONArray error = jsonObject.getJSONArray("error");
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<error.length(); i++){
            sb.append(error.get(i)).append("\n");
        }
        VolleyUtils.showError(errorTitle, sb.toString(), context );


    }



    /// manoj shakya
    public static File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();

        tempDir=new File(tempDir.getAbsolutePath()+"/AVA/");
        if(!tempDir.exists())
        {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }


    public static Bitmap grabImage(Uri mImageUri, Context context)
    {
        //context.getContentResolver().notifyChange(mImageUri, null);



        ContentResolver cr = context.getContentResolver();
        Bitmap bitmap = null;
        try
        {
            bitmap  = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Failed to load", Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }


    public static class UpdateApp extends AsyncTask<String,Void,Void> {
        private Context context;
        ProgressDialog pdia;
        public void setContext(Context contextf){
            context = contextf;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(mctx);
            pdia.setMessage("Loading...");
            pdia.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pdia.dismiss();
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                URL url = new URL(arg0[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                String PATH = Environment.getExternalStorageDirectory() + "/ava-download/";
                String FileName = "avaupdate.apk";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, FileName);




                if(outputFile.exists()){
                    outputFile.delete();
                }
                FileOutputStream fos =  new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);

                }
                fos.close();
                is.close();


                file=new File(  Environment.getExternalStorageDirectory()+ "/ava-download/"+ "avaupdate.apk");  //
               // File file=new File(context.getFilesDir(),"avaupdate.apk");
                Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);





                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    mctx.startActivity(intent);

                }else{


                    Uri apkUri = Uri.fromFile(file);
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setDataAndType(apkUri,"application/vnd.android.package-archive");
                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mctx.startActivity(install);
                }

            } catch (Exception e) {
                Log.e("UpdateAPP", "Update error! " + e.getMessage());

            }
            return null;
        }


    }

    private  static boolean checkAndRequestPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            if(ActivityCompat.checkSelfPermission(mctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                Log.v("PERMISSION","Permission is granted");
                return true;
            } else {
                Log.v("PERMISSION","Permission is revoked");
                ActivityCompat.requestPermissions((Activity)mctx, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                return false;
            }
        } else {
            Log.v("PERMISSION","Permission is granted");
            return true;
        }
    }
}

