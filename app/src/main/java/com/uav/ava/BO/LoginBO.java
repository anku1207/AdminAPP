package com.uav.ava.BO;

import com.uav.ava.vo.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class LoginBO  implements Serializable {

    public static ConnectionVO verifyuser(String username,String pass){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userName", username);
        params.put("password", pass);


        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("adminLogin");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;

    }
}
