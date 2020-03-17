package com.uav.ava.BO;

import com.uav.ava.vo.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class FlightBO implements Serializable {



    public static ConnectionVO flightverify(String userid, String flightno){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userid);
        params.put("flightCode", flightno);
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getFlightNoSalesInfo");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;
    }


    public static ConnectionVO getFlight(String userid, String flightno,String flightdate ){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userid);
        params.put("flightNumber", flightno);
        params.put("flightDate", flightdate);
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getFlight");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;
    }


    public static ConnectionVO getIndigoOnFlightPayment(Integer principalId, String thirdPartyTransactionId,String ThirdPartyTransactionDate ){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("principalId", principalId);
        params.put("thirdPartyTransactionId", thirdPartyTransactionId);
        params.put("ThirdPartyTransactionDate", ThirdPartyTransactionDate);
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getIndigoOnFlightPayment");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;
    }








    public static ConnectionVO saveNoFlightReason(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("saveFlightNoSaleReasons");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;
    }




}
