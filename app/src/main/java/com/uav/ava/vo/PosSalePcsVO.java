package com.uav.ava.vo;

import java.io.Serializable;

public class PosSalePcsVO implements Serializable {

    private  String PcsID;
    private  String ProductID ;
    private  String PcsNumber ;
    private  String ProductCode ;
    private  String ProductName;
    private  String SaleSchemeCode ;
    private  String MRP;
    private  String MOP;
    private  String DiscountType;
    private  String DiscountValue;
    private  String DiscountRate;
    private  String BindDiscountType;
    private  String BindDiscountValue;
    private  String BindDiscountRate;
    private  String TotalAmount;
    private  String MOPTag;


    public PosSalePcsVO(){

    }

    public String getPcsID() {
        return PcsID;
    }

    public void setPcsID(String pcsID) {
        PcsID = pcsID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getPcsNumber() {
        return PcsNumber;
    }

    public void setPcsNumber(String pcsNumber) {
        PcsNumber = pcsNumber;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getSaleSchemeCode() {
        return SaleSchemeCode;
    }

    public void setSaleSchemeCode(String saleSchemeCode) {
        SaleSchemeCode = saleSchemeCode;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getMOP() {
        return MOP;
    }

    public void setMOP(String MOP) {
        this.MOP = MOP;
    }

    public String getDiscountType() {
        return DiscountType;
    }

    public void setDiscountType(String discountType) {
        DiscountType = discountType;
    }

    public String getDiscountValue() {
        return DiscountValue;
    }

    public void setDiscountValue(String discountValue) {
        DiscountValue = discountValue;
    }

    public String getDiscountRate() {
        return DiscountRate;
    }

    public void setDiscountRate(String discountRate) {
        DiscountRate = discountRate;
    }

    public String getBindDiscountType() {
        return BindDiscountType;
    }

    public void setBindDiscountType(String bindDiscountType) {
        BindDiscountType = bindDiscountType;
    }

    public String getBindDiscountValue() {
        return BindDiscountValue;
    }

    public void setBindDiscountValue(String bindDiscountValue) {
        BindDiscountValue = bindDiscountValue;
    }

    public String getBindDiscountRate() {
        return BindDiscountRate;
    }

    public void setBindDiscountRate(String bindDiscountRate) {
        BindDiscountRate = bindDiscountRate;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }


    public String getMOPTag() {
        return MOPTag;
    }

    public void setMOPTag(String MOPTag) {
        this.MOPTag = MOPTag;
    }
}
