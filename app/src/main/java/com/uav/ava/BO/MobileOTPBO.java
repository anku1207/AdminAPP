package com.uav.ava.BO;

import com.uav.ava.vo.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class MobileOTPBO implements Serializable {

    public static ConnectionVO getOTPForRedeemCLP(String mobileno,String userId){
        HashMap<String, Object> params = new HashMap<String, Object>();
        ConnectionVO connectionVO = new ConnectionVO();
        params.put("mobileNo",mobileno);
        params.put("userId",userId);
        connectionVO.setMethodName("getOTPForRedeemCLP");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;

    }


    public static ConnectionVO verifyOTPForRedeemCLP(String otpid,String otp){
        HashMap<String, Object> params = new HashMap<String, Object>();
        ConnectionVO connectionVO = new ConnectionVO();
        params.put("OTPId",otpid);
        params.put("OTPNo",otp);
        connectionVO.setMethodName("verifyOTPForRedeemCLP");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;

    }





    public static ConnectionVO verifyOTPAddCustomer(String otpid,String otp ,String userid){
        HashMap<String, Object> params = new HashMap<String, Object>();
        ConnectionVO connectionVO = new ConnectionVO();
        params.put("OTPId",otpid);
        params.put("OTPNo",otp);
        params.put("userId",userid);
        connectionVO.setMethodName("verifyOTPAddCustomer");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;

    }
}
