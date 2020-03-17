package com.uav.ava.vo;

import java.io.Serializable;

public class PosSaleRefernceVO implements Serializable {

    private  String SalesDevice;
    private String ThirdPartyTransactionId;
    private String ThirdPartyTransactionDate ;
    private String ThirdPartyUserId;
    private String AdvanceRemarks;
    private String AdvanceAmount;
    private String CrewIdImageURL;
    private String PaymentCardId;
    private String Mosambeeresp;
    private String PromotionId;
    private String BoardingPassURL;


    public PosSaleRefernceVO(){

    }

    public String getSalesDevice() {
        return SalesDevice;
    }

    public void setSalesDevice(String salesDevice) {
        SalesDevice = salesDevice;
    }

    public String getThirdPartyTransactionId() {
        return ThirdPartyTransactionId;
    }

    public void setThirdPartyTransactionId(String thirdPartyTransactionId) {
        ThirdPartyTransactionId = thirdPartyTransactionId;
    }

    public String getThirdPartyTransactionDate() {
        return ThirdPartyTransactionDate;
    }

    public void setThirdPartyTransactionDate(String thirdPartyTransactionDate) {
        ThirdPartyTransactionDate = thirdPartyTransactionDate;
    }

    public String getThirdPartyUserId() {
        return ThirdPartyUserId;
    }

    public void setThirdPartyUserId(String thirdPartyUserId) {
        ThirdPartyUserId = thirdPartyUserId;
    }

    public String getAdvanceRemarks() {
        return AdvanceRemarks;
    }

    public void setAdvanceRemarks(String advanceRemarks) {
        AdvanceRemarks = advanceRemarks;
    }

    public String getAdvanceAmount() {
        return AdvanceAmount;
    }

    public void setAdvanceAmount(String advanceAmount) {
        AdvanceAmount = advanceAmount;
    }

    public String getCrewIdImageURL() {
        return CrewIdImageURL;
    }

    public void setCrewIdImageURL(String crewIdImageURL) {
        CrewIdImageURL = crewIdImageURL;
    }

    public String getPaymentCardId() {
        return PaymentCardId;
    }

    public void setPaymentCardId(String paymentCardId) {
        PaymentCardId = paymentCardId;
    }


    public String getMosambeeresp() {
        return Mosambeeresp;
    }

    public void setMosambeeresp(String mosambeeresp) {
        Mosambeeresp = mosambeeresp;
    }


    public String getPromotionId() {
        return PromotionId;
    }

    public void setPromotionId(String promotionId) {
        PromotionId = promotionId;
    }


    public String getBoardingPassURL() {
        return BoardingPassURL;
    }

    public void setBoardingPassURL(String boardingPassURL) {
        BoardingPassURL = boardingPassURL;
    }
}
