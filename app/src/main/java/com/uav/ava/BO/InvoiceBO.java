package com.uav.ava.BO;

import com.uav.ava.vo.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class InvoiceBO implements Serializable {
    public static ConnectionVO getInvoiceList( String userId ,String pageNo, String pageSize){
        HashMap<String, Object> params = new HashMap<String, Object>();
        ConnectionVO connectionVO = new ConnectionVO();
        params.put("pageNo",pageNo);
        params.put("pageSize",pageSize);
        params.put("userId",userId);
        connectionVO.setMethodName("getInvoiceList");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;

    }

    public static ConnectionVO sendEmail( String userId ,String emailId, String posSaleId){
        HashMap<String, Object> params = new HashMap<String, Object>();
        ConnectionVO connectionVO = new ConnectionVO();
        params.put("posSaleId",posSaleId);
        params.put("emailId",emailId);
        params.put("userId",userId);
        connectionVO.setMethodName("sendInvoiceonMail");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        connectionVO.setLoader(true);
        return connectionVO;

    }


    public static ConnectionVO cancelRetailInvoice(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("cancelRetailInvoice");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;

    }

}
