package com.uav.ava.BO;

import com.uav.ava.vo.ConnectionVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class AviationSaleBO implements Serializable {

    public static ConnectionVO saveAviationSale(){
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("saveAviationSale");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;

    }

    public static ConnectionVO getUniqeTxnId(){
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getUniqueTxnId");
        connectionVO.setRequestType(ConnectionVO.REQUEST_GET);
        return connectionVO;

    }


    public static ConnectionVO saveRetailSale(){
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("saveRetailSale");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;

    }

}
