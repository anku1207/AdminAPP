/*
package com.uav.ava.service;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;

import com.mosambee.lib.MosCallback;
import com.mosambee.lib.ResultData;
import com.mosambee.lib.TransactionResult;
import com.uav.ava.activity.Payment;
import com.uav.ava.vo.PosSaleVO;

import org.json.JSONObject;


public class MosambeeChannel implements TransactionResult {
    private Context context;
    MosCallback moscCallback;
    private Activity _activity;
    Payment handlerActivity;
    PosSaleVO posSaleVO;
    JSONObject userJsonObject;
    public void startProcess(String user, String pswd, String transType, FrameLayout container, PosSaleVO posSaleVO,JSONObject userJsonObject) {
        this.posSaleVO= posSaleVO;
        this.userJsonObject = userJsonObject;

        moscCallback = new MosCallback(context);
        //  moscCallback.setActivityForJUNO(_activity);
        moscCallback.initialise(user, pswd, this);
        // initializeSignatureView(FrameLayout, primaryColor, secondaryColor);
        moscCallback.initializeSignatureView(container, "#55004A", "#750F5A");
        moscCallback.initialiseFields(transType, posSaleVO.getContactNumber(), "cGjhE$@fdhj4675riesae", false, posSaleVO.getEmail(), "", "serial", posSaleVO.getInvoiceDate(), posSaleVO.getEWalletTxnId().toString());
        moscCallback.processTransaction(posSaleVO.getEWalletTxnId().toString(), Double.valueOf(  posSaleVO.getNetAmount()), Double.valueOf(  posSaleVO.getNetAmount()),null);
        // moscCallback.getLocation();
    }

    public void getReceipt(String invoiceNo, String pswd) {
        moscCallback = new MosCallback(context);
        moscCallback = new MosCallback(context);
        moscCallback.initialise("", pswd, this);
        moscCallback.getMetaData(invoiceNo);
    }

    public void setContext(Context context) {
        this.context = context;

    }

    public void setActivity(Activity act) {
        _activity = act;
    }

    @Override
    public void onCommand(String command) {
        //System.out.println("command is--------------------" + command);
        handlerActivity =  new Payment();
        handlerActivity.setCommand(command);
    }


    @Override
    public void onResult(ResultData result) {
        //System.out.println();
        //System.out.println("\n-----Result: "+result.getResult()+ "\n-----Reason: " + result.getReason()+"\n-----Data: "+ result.getTransactionData() );
        result.getResult();
        handlerActivity =  new Payment();
        handlerActivity.setData(result, this.posSaleVO, this.userJsonObject);

    }

    public void stopProcess() {
        moscCallback = new MosCallback(context);
        moscCallback.posReset();
    }


}
*/
