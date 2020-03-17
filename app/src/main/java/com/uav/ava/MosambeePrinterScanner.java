package com.uav.ava;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mmsc.serial.SerialPortIOManage;
import com.mmsc.serial.SerialPortService;
import com.mosambee.lib.ResultData;
import com.mosambee.lib.SingleOnClickListener;
import com.mosambee.lib.TRACE;
import com.mosambee.lib.TransactionResult;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;
import com.uav.ava.util.Utility;

import org.json.JSONObject;

import java.io.File;

public class MosambeePrinterScanner extends AppCompatActivity  {
    private Button buttonPrinter, buttonScanner, buttonStopScan, buttonService, buttonServiceStop;
    public PrinterInstance myPrinter;
    private boolean isConnected;
    public static TextView tvScannarData;
    private String devicesName;
    private String devicesAddress;
    private int baudrate;
    private MosambeeBroadCastReceiver myReceiver;
    private Intent intent;
    ResultData resultactivity;

    String amount=null;


    String transactionId,businessName,terminalId,time,merchantId,invoiceNumber,
            cardType,tgName,cardNumber,orderId,transType,date,
            address1,batchNumber,transactionMode,expiryDate,approvalCode,cardHolderName;
    JSONObject object ;






    private Handler mHandler = new Handler() {
        @SuppressLint("ShowToast")
        @Override
        public void handleMessage(Message msg) {

            System.out.println("@@@@@@@@@@@@" + msg.what);
            switch (msg.what) {
                case PrinterConstants.Connect.SUCCESS:
                    isConnected = true;
                    System.out.println("isConnected status:::;" + isConnected);
                    break;
                case PrinterConstants.Connect.FAILED:
                    isConnected = false;

                    break;
                case PrinterConstants.Connect.CLOSED:
                    isConnected = false;
                    Toast.makeText(getApplicationContext(), "Connection closed", Toast.LENGTH_SHORT).show();
                    break;
                case PrinterConstants.Connect.NODEVICE:
                    isConnected = false;
                    Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), "-1", Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(getApplicationContext(), "-2", Toast.LENGTH_SHORT).show();
                    break;
                case -3:
                    Toast.makeText(getApplicationContext(), "-3", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    public static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 888:
                    SerialPortIOManage.getInstance().sendDataToDevice("00");
                    sendEmptyMessageDelayed(1000, 20);
                    break;
                case 999:
                    SerialPortIOManage.getInstance().sendDataToDevice("00");
                    sendEmptyMessageDelayed(1001, 20);
                    break;
                case 1000:
                    SerialPortIOManage.getInstance().sendDataToDevice("04 E4 04 00 FF 14");
                    break;
                case 1001:
                    SerialPortIOManage.getInstance().sendDataToDevice("07 C6 04 00 FF 8A 08 FD 9E");
                    //SerialPortIOManage.getInstance().sendDataToDevice("07 C6 04 80 14 8A 01 FE 10");
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosambee_printer_scanner);
        buttonPrinter = (Button) findViewById(R.id.buttonPrinter);
        buttonScanner = (Button) findViewById(R.id.buttonScanner);
        buttonStopScan = (Button) findViewById(R.id.buttonStopScan);
        buttonService = (Button) findViewById(R.id.buttonService);
        buttonServiceStop = (Button) findViewById(R.id.buttonServiceStop);
        // buttonPrinter.setOnClickListener(singleOnclick);
           /* buttonScanner.setOnClickListener(singleOnclick);
            buttonStopScan.setOnClickListener(singleOnclick);
            buttonService.setOnClickListener(singleOnclick);
            buttonServiceStop.setOnClickListener(singleOnclick);*/
        tvScannarData = (TextView) findViewById(R.id.tvScannerData);
        tvScannarData.setTag("scanning");

        myReceiver = new MosambeeBroadCastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MY_ACTION");
        registerReceiver(myReceiver, intentFilter);

        Intent extera = getIntent();
        if (extera.getAction().equalsIgnoreCase("scanner")) {
            try {
                intent = new Intent();
                intent.putExtra("openPort", true);
                intent.putExtra("deviceType", "Scanner");
                intent.setClassName("com.mosambee.printService", "com.mosambee.printService.PrinterService");
                startService(intent);

            } catch (Exception e) {
                Utility.exceptionAlertDialog(MosambeePrinterScanner.this, "Alert!", "Something went wrong, Please try again!", "Report", Utility.getStackTrace(e));
            }

        } else if (extera.getAction().equals("Printer")) {
            try {

                intent = new Intent();
                String result = extera.getStringExtra("result");
                Gson gson = new Gson();
                // 2. JSON to Java object, read it from a Json String.
                resultactivity = gson.fromJson(result, ResultData.class);
                // JSON to JsonElement, convert to String later.

                transactionId = resultactivity.getTransactionId();
                JSONObject object = new JSONObject(resultactivity.getTransactionData());
                businessName = object.getString("businessName");
                terminalId = object.getString("terminalId");
                amount = object.getString("amount");
                time = object.getString("time");
                merchantId = object.getString("merchantId");
                invoiceNumber = object.getString("invoiceNumber");
                cardType = object.getString("cardType");
                tgName = object.getString("tgName");
                cardNumber = object.getString("cardNumber");
                orderId = object.getString("orderId");
                transType = object.getString("transType");
                date = object.getString("date");
                address1 = object.getString("address1");
                batchNumber = object.getString("batchNumber");
                transactionMode = object.getString("transactionMode");
                expiryDate = object.getString("expiryDate");
                approvalCode = object.getString("approvalCode");
                cardHolderName = object.getString("cardHolderName");

                intent.putExtra("openPort", true);
                intent.putExtra("deviceType", "Printer");
                intent.setClassName("com.mosambee.printService", "com.mosambee.printService.PrinterService");
                startService(intent);

            } catch (Exception e) {
                Utility.exceptionAlertDialog(MosambeePrinterScanner.this, "Alert!", "Something went wrong, Please try again!", "Report", Utility.getStackTrace(e));
            }

        finish() ;


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, resultCode, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStop() {
        TRACE.i("===== onStop");
        Toast.makeText(this, "Onstop", Toast.LENGTH_SHORT).show();
        System.out.println("===== onStop " );
        unregisterReceiver(myReceiver);

        super.onStop();
    }




    private void printData() {

        myPrinter.initPrinter();

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 1, 0);
        myPrinter.printText(tgName + "\n");

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(1, 0, 0, 0, 0);
        myPrinter.printText(businessName+"\n");

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(1, 0, 0, 0, 0);
        myPrinter.printText(address1+" \n");


        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
        myPrinter.setFont(1, 0, 0, 0, 0);
        myPrinter.printText("DATE : "+date);

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
        myPrinter.setFont(1, 0, 0, 0, 0);
        myPrinter.printText("     TIME : "+time+" \n");


        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
        myPrinter.setFont(1, 0, 0, 0, 0);
        myPrinter.printText("MID : "+merchantId+"\n");

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
        myPrinter.setFont(1, 0, 0, 0, 0);
        myPrinter.printText("TID : "+terminalId+"\n");





        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
        myPrinter.setFont(1, 0, 0, 0, 0);
        myPrinter.printText("BATCH NUM : "+batchNumber);

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
        myPrinter.setFont(1, 0, 0, 0, 0);
        myPrinter.printText("     INV. NUM : "+invoiceNumber+" \n");



        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 1, 0);
        myPrinter.printText(  "TRANSACTION APPROVED \n");



        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 1, 0);
        myPrinter.printText(transType + "\n");



        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
        myPrinter.setFont(1, 0, 0, 0, 0);
        myPrinter.printText("CARD NUM : "+cardNumber);

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
        myPrinter.setFont(1, 0, 0, 0, 0);
        myPrinter.printText("     "+transactionMode+" \n");




        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
        myPrinter.setFont(1, 0, 0, 0, 0);
        myPrinter.printText("EXP DATE : "+expiryDate+"\n");

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
        myPrinter.setFont(1, 0, 0, 0, 0);
        myPrinter.printText("CARD TYPE : "+cardType+" \n");

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
        myPrinter.setFont(1, 0, 0, 0, 0);
        myPrinter.printText("APPR CODE : "+approvalCode+" \n");


        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
        myPrinter.setFont(0, 0, 0, 1, 0);
        myPrinter.printText("TOTAL AMOUNT   : Rs. "+ amount+"\n");

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 0, 0);
        myPrinter.printText("--------------------\n");
        myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, 1);



        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 0, 0);
        myPrinter.printText("PIN VERIFIED OK \n");


        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 0, 0);
        myPrinter.printText("SIGNATURE NOT REQUIRED\n");

        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 0, 0);
        myPrinter.printText("--------------------\n");


        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 0);
        myPrinter.setFont(0, 0, 0, 0, 0);
        myPrinter.printText(cardHolderName+"\n");



        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 0, 0);
        myPrinter.printText("I AGREE TO PAY AS PER THE CARD SURE AGREEMENT \n");


        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 0, 0);
        myPrinter.printText("***  CUSTOMER COPY *** \n");



        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setFont(0, 0, 0, 1, 0);
        myPrinter.printText("1.1.1");

        myPrinter.setFont(0, 0, 0, 0, 0);
        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, 1);
        myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);

    }



    public class MosambeeBroadCastReceiver extends BroadcastReceiver {

        public MosambeeBroadCastReceiver() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String deviceType = bundle.getString("deviceType");
            int deviceState = bundle.getInt("deviceState");
            int deviceOpen1 = bundle.getInt("deviceOpen1");
            int deviceOpen2 = bundle.getInt("deviceOpen2");


            assert deviceType != null;
            switch (deviceType){
                case "Printer":
                    if (deviceState == 0 && deviceOpen1 == 0 && deviceOpen2 == 0) {
                        buttonPrinter.setEnabled(true);
                        buttonPrinter.setBackgroundColor(getResources().getColor(R.color.mosambeeDeviceBtnEnable));
                        Toast.makeText(getApplicationContext(), ""+deviceType +"\ndeviceState: " + deviceState + "\ndeviceOpen1: " + deviceOpen1 + "\ndeviceOpen2: " + deviceOpen2  , Toast.LENGTH_LONG).show();
                        goForPrint();




                    }else {
                        Toast.makeText(getApplicationContext(), ""+deviceType +"\n else", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "Scanner":
                    if (deviceState == 0 && deviceOpen1 == 0) {
                        try {
//                            Toast.makeText(getApplicationContext(), ""+deviceType +"\ndeviceState: " + deviceState + "\ndeviceOpen1: " + deviceOpen1 , Toast.LENGTH_LONG).show();

                            System.out.println("-----------in openTheSerialPort");
                            Intent startIntent2 = new Intent(getApplicationContext(), SerialPortService.class);
                            startIntent2.putExtra("serial", "dev/ttyMT1");
                            startService(startIntent2);
                            handler.removeMessages(1000);
                            handler.removeMessages(1001);
                            handler.removeMessages(999);
                            handler.sendEmptyMessageDelayed(999, 10000);

                            System.out.println("-----------in openTheScanHead");
                            SerialPortIOManage.getInstance().resetBuffer();
                            handler.removeMessages(888);
                            handler.removeMessages(999);
                            handler.removeMessages(1000);
                            handler.removeMessages(1001);
                            handler.sendEmptyMessageDelayed(888, 1000);


                        } catch (NoSuchMethodError | Exception er) {
                            Toast.makeText(getApplicationContext(), "Connection to scanner failed." + er.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }else {
                        Toast.makeText(getApplicationContext(), ""+deviceType +"\n else", Toast.LENGTH_LONG).show();
                    }

                        break;

            }
        }

        //Monochromatic image pass to printImage()

        public void goForPrint(){
            System.out.println("----------buttonPrint--------");
            devicesName = "Serial device";
            devicesAddress = "/dev/ttyMT0";
            String com_baudrate = "115200";
            baudrate = Integer.parseInt(com_baudrate);
            myPrinter = PrinterInstance.getPrinterInstance(new File(devicesAddress), baudrate, 0, mHandler);
            System.out.println("myPrinter.getCurrentStatus()-" + myPrinter.getCurrentStatus());
            boolean b = myPrinter.openConnection();
            System.out.println("-----------" + b);
            if (b && myPrinter != null /*&& myPrinter.getCurrentStatus() == 0*/) {

                printData();
                /*
                Drawable myDrawable = getResources().getDrawable(R.drawable.ava_hidepin);
                Bitmap anImage      = ((BitmapDrawable) myDrawable).getBitmap();
                myPrinter.printImage(anImage, PrinterConstants.PAlign.CENTER,1,false);*/
            }else
                Toast.makeText(getApplicationContext(), "Connection to printer failed.", Toast.LENGTH_SHORT).show();

        }


    }

}
