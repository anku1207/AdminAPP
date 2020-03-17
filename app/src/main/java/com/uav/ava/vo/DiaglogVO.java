package com.uav.ava.vo;

import java.io.Serializable;

public class DiaglogVO implements Serializable {



    public final static String BTN_YES="yes";
    public final static String BTN_NO="No";
    public final static String BTN_CANCEL="Cancel";
    public final static String BTN_OK="ok";




    private String title;
    private String message;
    private String button1;
    private String button2;
    private String button3;

    public DiaglogVO(String btn1){
        this.button1 = btn1;

    }

    public DiaglogVO(String btn1, String btn2){
        this.button1 = btn1;
        this.button2= btn2;
    }

    public DiaglogVO(String btn1, String btn2, String btn3){
        this.button1 = btn1;
        this.button2= btn2;
        this.button3=btn3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getButton1() {
        return button1;
    }

    public void setButton1(String button1) {
        this.button1 = button1;
    }

    public String getButton2() {
        return button2;
    }

    public void setButton2(String button2) {
        this.button2 = button2;
    }

    public String getButton3() {
        return button3;
    }

    public void setButton3(String button3) {
        this.button3 = button3;
    }


}
