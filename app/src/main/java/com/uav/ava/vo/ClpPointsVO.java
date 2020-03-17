package com.uav.ava.vo;

import java.io.Serializable;

public class ClpPointsVO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private  String CustomerID;
    private  Integer PurchasePoints;
    private  Integer RedimPoints;

    public ClpPointsVO(){

    }


    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public Integer getPurchasePoints() {
        return PurchasePoints;
    }

    public void setPurchasePoints(Integer purchasePoints) {
        PurchasePoints = purchasePoints;
    }

    public Integer getRedimPoints() {
        return RedimPoints;
    }

    public void setRedimPoints(Integer redimPoints) {
        RedimPoints = redimPoints;
    }
}
