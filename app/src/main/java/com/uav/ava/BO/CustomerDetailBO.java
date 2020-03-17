package com.uav.ava.BO;

import com.uav.ava.vo.ConnectionVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class CustomerDetailBO implements Serializable {

    public static ConnectionVO getCLPCustomerDetail(String customerno){
        HashMap<String, Object> params = new HashMap<String, Object>();
        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("ContactNumber",customerno);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("CustomerRegistrationVO", jsonObject);

        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getCLPCustomerDetail");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;
    }
}
