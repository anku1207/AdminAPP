package com.uav.ava.BO;

import com.uav.ava.vo.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class CustomerBO implements Serializable {



    public static ConnectionVO verifyNewCustomer(){
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("addCustomer");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;
    }


    public static ConnectionVO resendOTPAddCustomer(){
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("resendOTPAddCustomer");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;
    }

}
