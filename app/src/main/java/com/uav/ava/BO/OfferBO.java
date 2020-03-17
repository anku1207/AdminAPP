package com.uav.ava.BO;

import com.uav.ava.vo.ConnectionVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class OfferBO implements Serializable {

    public static ConnectionVO getOfferByCard(Integer promotionId, String promoHash, Integer nos, String cartTotal){
        HashMap<String, Object> params = new HashMap<String, Object>();


            ConnectionVO connectionVO = new ConnectionVO();
            params.put("promotionId",promotionId);
            params.put("promoHash",promoHash);
            params.put("nos",nos);
            params.put("cartTotal",cartTotal);
            connectionVO.setMethodName("getPromotionAction");
            connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
            connectionVO.setParams(params);
        return connectionVO;

    }
}
