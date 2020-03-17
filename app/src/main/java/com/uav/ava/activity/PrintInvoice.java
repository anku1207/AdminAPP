package com.uav.ava.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eze.api.EzeAPI;
import com.pax.dal.IDAL;
import com.pax.neptunelite.api.DALProxyClient;

import com.uav.ava.R;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.permission.Session;
import com.uav.ava.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;

public class PrintInvoice extends AppCompatActivity {

    private static Context context;
    private AlertDialog alertDialogWebView;
    private final int REQUEST_CODE_INITIALIZE = 10001;
    private final int REQUEST_CODE_PRINT_BITMAP = 10011;

    WebView webView;

    static ImageView image;

    private Bitmap invoiceBitmap;
    IDAL dal ;


    private static Bitmap getWebViewBitmap(View view) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), View.MeasureSpec.AT_MOST);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), View.MeasureSpec.AT_MOST);
        view.measure(widthSpec, heightSpec);
        int left = view.getLeft();
        int top = view.getTop();
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        view.layout(left, top, width + left, height + top);

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        //setContentView(R.layout.activity_print_invoice);


        //image=findViewById(R.id.image);

        try{

            Intent extra = getIntent();

            String posSaleId=   extra.getStringExtra("posSaleId");
            openWebView(ApplicationConstant.HTTP_PRINT_INVOICE_URL +"?posSaleId="+posSaleId); //http://d.eze.cc/r/o/0zfATRE7
        } catch (Exception e) {
            Utility.exceptionAlertDialog(context,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
            return;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            /*Ezee Tap*/
            switch (requestCode) {
                case REQUEST_CODE_PRINT_BITMAP:
                    Log.w("Print","Done");
                    break;
                default:
                    break;
            }
            /* End */

        }catch (Exception e) {
            Utility.exceptionAlertDialog(PrintInvoice.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    void openWebView(final String receiptUrl) {

        setContentView(R.layout.activity_print_invoice);
        final WebView webView = (WebView) findViewById(R.id.receipt_webview);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setMinimumFontSize(16);
        final ProgressDialog progressBar = ProgressDialog.show(context, null, "Loading Invoice. Please wait...", false, false);
        webView.setDrawingCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
        invoiceBitmap = null;


        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
                if(!url.equalsIgnoreCase(receiptUrl)) {
                    showError("Not a valid URL");
                }
            }

            @SuppressWarnings("deprecation")
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
                showError(description);
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
                showError((String) error.getDescription());
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
                showError(errorResponse.getReasonPhrase().toString());
            }
        });
        webView.loadUrl(receiptUrl); //receiptUrl

        Button printReceiptButton = (Button) findViewById(R.id.print_receipt_button);
        Button donePrintingButton = (Button) findViewById(R.id.done_printing_button);

        printReceiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print(webView); //
            }
        });

        donePrintingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //alertDialogWebView.cancel();
                Intent intent = new Intent();
                intent.putExtra("response", "success");
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });
    }

    private void showError(String description) {
       /* new AlertDialog.Builder(PrintInvoice.this)
                .setTitle("Error")
                .setMessage(description)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
*/

        Log.w("weberror",description);

    }

    private void print(final View view) {

        /* paxcode
        try {
            Bitmap bitmap = getWebViewBitmap(view);
            dal =DALProxyClient.getInstance().getDal(this);
            dal.getPrinter().init();
            dal.getPrinter().printBitmapWithMonoThreshold(bitmap, 230);
            dal.getPrinter().step(200);
            dal.getPrinter().start();

        } catch (Exception e) {
            Utility.exceptionAlertDialog(context,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
            return;
        }
        */
        printEzeTap(view);


    }

    public Bitmap getResizedBitmap(Bitmap image, int maxWidth) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        width = maxWidth;
        height = (int) (width / bitmapRatio);
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void printEzeTap(final View view){
        /*ezetap api */

        try {
            JSONObject jsonRequest = new JSONObject();
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

        try {
            JSONObject jsonRequest = new JSONObject();
            JSONObject jsonImageObj = new JSONObject();

            Bitmap bitmap =  getWebViewBitmap(view); //getWebViewBitmapEzeTap(view);//
            bitmap = getResizedBitmap(bitmap, 384);//384

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 1, stream);
            byte[] byteFormat = stream.toByteArray();
            String encodedImageData = Base64.encodeToString(byteFormat, Base64.NO_WRAP); //Base64.NO_WRAP


            jsonImageObj.put("imageData", encodedImageData);
            jsonImageObj.put("imageType", "JPEG");
            // jsonImageObj.put("height", "");// optional
            // jsonImageObj.put("weight", "");// optional
            jsonRequest.put("image", jsonImageObj); // Pass this attribute when you have a valid captured signature image

            EzeAPI.printBitmap(this, REQUEST_CODE_PRINT_BITMAP, jsonRequest);




        } catch (Exception e) {
            Utility.exceptionAlertDialog(context,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
            return;
        }

    }
}
