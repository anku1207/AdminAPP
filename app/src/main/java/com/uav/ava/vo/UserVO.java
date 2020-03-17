package com.uav.ava.vo;

import java.io.Serializable;

public class UserVO  implements Serializable {

    private String userId;
    private String userName;
    private String  mposid;

    public UserVO(){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getMposid() {
        return mposid;
    }

    public void setMposid(String mposid) {
        this.mposid = mposid;
    }
}
