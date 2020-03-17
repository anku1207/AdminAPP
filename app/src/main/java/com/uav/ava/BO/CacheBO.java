package com.uav.ava.BO;

import com.uav.ava.vo.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class CacheBO implements Serializable {

    public static ConnectionVO getFlightNoSaleReasonscache(){
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getFlightNoSaleReasons");
        connectionVO.setRequestType(ConnectionVO.REQUEST_GET);
        return connectionVO;
    }
    public static ConnectionVO getUnixTimeStamp(){
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getUnixTimeStamp");
        connectionVO.setRequestType(ConnectionVO.REQUEST_GET);
        return connectionVO;
    }
    public static ConnectionVO getServerDate(){
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getServerDate");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;
    }
    public static ConnectionVO getPrincipals(){
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getPrincipals");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;
    }
    public static ConnectionVO getPaymentTypes(Integer userId){
        ConnectionVO connectionVO = new ConnectionVO();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId",userId);
        connectionVO.setMethodName("getPaymentTypes");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;
    }

    public static ConnectionVO getPaymentCardList(){
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getPaymentCardList");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;
    }

    public static ConnectionVO getEmployeeName(Integer userId){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId",userId);
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getEmployeeName");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;
    }


}
