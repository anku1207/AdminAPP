package com.uav.ava.BO;

import android.util.Log;

import com.uav.ava.vo.ConnectionVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class ProductBO  implements Serializable {

    public static ConnectionVO getBindProductList(String UniquePcsID, String InvoiceDate,String PrimaryPcsNumber,Integer userId ,String BindPcsNumber ,String PrincipalID){
        HashMap<String, Object> params = new HashMap<String, Object>();

        JSONObject jsonObject =new JSONObject();
        ConnectionVO connectionVO = new ConnectionVO();
        try {
            jsonObject.put("UniquePcsID", UniquePcsID);
            jsonObject.put("InvoiceDate", InvoiceDate);
            jsonObject.put("PrimaryPcsNumber", PrimaryPcsNumber);
            jsonObject.put("BindPcsNumber",BindPcsNumber);
            jsonObject.put("PrincipalID",PrincipalID);


            params.put("userId", userId);
            params.put("POSSaleVO",jsonObject);
            connectionVO.setMethodName("getBindProductList");
            connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
            connectionVO.setParams(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return connectionVO;

    }



    public static ConnectionVO getPromotionsPrincipalByWef(String principalId, String flightDate){
        HashMap<String, Object> params = new HashMap<String, Object>();
        ConnectionVO connectionVO = new ConnectionVO();
            params.put("principalId", principalId);
            params.put("flightDate",flightDate);
            connectionVO.setMethodName("getPromotionsPrincipalByWef");
            connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
            connectionVO.setParams(params);
        return connectionVO;

    }


    public static ConnectionVO applyAviationCoupon(String mobileNo, String couponcode ,String invoiceAmount,String principalId ){
        HashMap<String, Object> params = new HashMap<String, Object>();
        ConnectionVO connectionVO = new ConnectionVO();
        params.put("mobileNo", mobileNo);
        params.put("couponcode",couponcode);
        params.put("invoiceAmount",invoiceAmount);
        params.put("principalId",principalId);

        Log.w("applyAviationCoupon",params.toString());

        connectionVO.setMethodName("applyAviationCoupon");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);

        Log.w("applyAviationCoupon",params.toString());
        return connectionVO;

    }
}
