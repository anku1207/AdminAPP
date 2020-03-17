package com.uav.ava.vo;

import java.io.Serializable;

public class CouponVO implements Serializable {

    public CouponVO() {
    }

    private String coupon_id;
    private String couponHash;
    private String code;


    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getCouponHash() {
        return couponHash;
    }

    public void setCouponHash(String couponHash) {
        this.couponHash = couponHash;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
