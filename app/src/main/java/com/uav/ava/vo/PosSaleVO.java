package com.uav.ava.vo;

import com.android.volley.toolbox.StringRequest;

import java.io.Serializable;
import java.util.List;

public class PosSaleVO implements Serializable {


    private  String InvoiceDate;
    private  String FlightDate;
    private  String ClassID;
    private  String PassengerName;
    private  String FlightOrigin;
    private  String FlightDestination;
    private  String FlightId;
    private  String TravelJulianDay;
    private  String SeatNumber;
    private  String PrincipalID;
    private  String ContactNumber;
    private  String PaymentTypeID;
    private  Integer NetAmount;
    private  String IsVirtualFlight;
    private  String FlightCode;
    private  String SalesEmplCode;
    private  String SalesEmplID;
    private  Integer SpecialDiscount;
    private  Integer InvoiceDiscount;
    private  String email;
    private  String DiscountRemarks;
    private  Integer GrossTotal;
    private  Integer CLPDiscount;
    private String EcomSalesDate;

    private String UserId;
    private String invoiceCancelRemark;
    private String posSaleID;



    private PosSaleRefernceVO PosSaleRefernceVO;
    private  ClpPointsVO ClpPointsVO;

    private String creditCardNumber;
    private String creditCardBatchNumber;
    private String creditCardApprovalCode;
    private Integer EWalletTxnId;

    private List<PosSalePcsVO>  PosSalePcsVO;


    private String OrderNumber;

    public PosSaleVO(){

    }




    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public String getFlightDate() {
        return FlightDate;
    }

    public void setFlightDate(String flightDate) {
        FlightDate = flightDate;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String classID) {
        ClassID = classID;
    }

    public String getPassengerName() {
        return PassengerName;
    }

    public void setPassengerName(String passengerName) {
        PassengerName = passengerName;
    }

    public String getFlightOrigin() {
        return FlightOrigin;
    }

    public void setFlightOrigin(String flightOrigin) {
        FlightOrigin = flightOrigin;
    }

    public String getFlightDestination() {
        return FlightDestination;
    }

    public void setFlightDestination(String flightDestination) {
        FlightDestination = flightDestination;
    }

    public String getFlightId() {
        return FlightId;
    }

    public void setFlightId(String flightId) {
        FlightId = flightId;
    }

    public String getTravelJulianDay() {
        return TravelJulianDay;
    }

    public void setTravelJulianDay(String travelJulianDay) {
        TravelJulianDay = travelJulianDay;
    }

    public String getSeatNumber() {
        return SeatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        SeatNumber = seatNumber;
    }

    public String getPrincipalID() {
        return PrincipalID;
    }

    public void setPrincipalID(String principalID) {
        PrincipalID = principalID;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getPaymentTypeID() {
        return PaymentTypeID;
    }

    public void setPaymentTypeID(String paymentTypeID) {
        PaymentTypeID = paymentTypeID;
    }

    public Integer getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(Integer netAmount) {
        NetAmount = netAmount;
    }

    public String getIsVirtualFlight() {
        return IsVirtualFlight;
    }

    public void setIsVirtualFlight(String isVirtualFlight) {
        IsVirtualFlight = isVirtualFlight;
    }

    public String getFlightCode() {
        return FlightCode;
    }

    public void setFlightCode(String flightCode) {
        FlightCode = flightCode;
    }

    public String getSalesEmplCode() {
        return SalesEmplCode;
    }

    public void setSalesEmplCode(String salesEmplCode) {
        SalesEmplCode = salesEmplCode;
    }

    public String getSalesEmplID() {
        return SalesEmplID;
    }

    public void setSalesEmplID(String salesEmplID) {
        SalesEmplID = salesEmplID;
    }

    public Integer getSpecialDiscount() {
        return SpecialDiscount;
    }

    public void setSpecialDiscount(Integer specialDiscount) {
        SpecialDiscount = specialDiscount;
    }

    public Integer getInvoiceDiscount() {
        return InvoiceDiscount;
    }

    public void setInvoiceDiscount(Integer invoiceDiscount) {
        InvoiceDiscount = invoiceDiscount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiscountRemarks() {
        return DiscountRemarks;
    }

    public void setDiscountRemarks(String discountRemarks) {
        DiscountRemarks = discountRemarks;
    }

    public Integer getGrossTotal() {
        return GrossTotal;
    }

    public void setGrossTotal(Integer grossTotal) {
        GrossTotal = grossTotal;
    }

    public Integer getCLPDiscount() {
        return CLPDiscount;
    }

    public void setCLPDiscount(Integer CLPDiscount) {
        this.CLPDiscount = CLPDiscount;
    }

    public com.uav.ava.vo.PosSaleRefernceVO getPosSaleRefernceVO() {
        return PosSaleRefernceVO;
    }

    public void setPosSaleRefernceVO(com.uav.ava.vo.PosSaleRefernceVO posSaleRefernceVO) {
        PosSaleRefernceVO = posSaleRefernceVO;
    }

    public com.uav.ava.vo.ClpPointsVO getClpPointsVO() {
        return ClpPointsVO;
    }

    public void setClpPointsVO(com.uav.ava.vo.ClpPointsVO clpPointsVO) {
        ClpPointsVO = clpPointsVO;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardBatchNumber() {
        return creditCardBatchNumber;
    }

    public void setCreditCardBatchNumber(String creditCardBatchNumber) {
        this.creditCardBatchNumber = creditCardBatchNumber;
    }

    public String getCreditCardApprovalCode() {
        return creditCardApprovalCode;
    }

    public void setCreditCardApprovalCode(String creditCardApprovalCode) {
        this.creditCardApprovalCode = creditCardApprovalCode;
    }

    public List<com.uav.ava.vo.PosSalePcsVO> getPosSalePcsVO() {
        return PosSalePcsVO;
    }

    public void setPosSalePcsVO(List<com.uav.ava.vo.PosSalePcsVO> posSalePcsVO) {
        PosSalePcsVO = posSalePcsVO;
    }

    public Integer getEWalletTxnId() {
        return EWalletTxnId;
    }

    public void setEWalletTxnId(Integer EWalletTxnId) {
        this.EWalletTxnId = EWalletTxnId;
    }


    public String getEcomSalesDate() {
        return EcomSalesDate;
    }

    public void setEcomSalesDate(String ecomSalesDate) {
        EcomSalesDate = ecomSalesDate;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getInvoiceCancelRemark() {
        return invoiceCancelRemark;
    }

    public void setInvoiceCancelRemark(String invoiceCancelRemark) {
        this.invoiceCancelRemark = invoiceCancelRemark;
    }

    public String getPosSaleID() {
        return posSaleID;
    }

    public void setPosSaleID(String posSaleID) {
        this.posSaleID = posSaleID;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }
}
