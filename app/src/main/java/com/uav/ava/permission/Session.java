package com.uav.ava.permission;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;


import com.google.gson.Gson;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.util.Utility;
import com.uav.ava.vo.UserVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Session {
    public static String EZETAPPRINTER="EZETAPPRINTER";

    public static String getUserId(Context context)   {
       SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
       String resp=sharedPreferences.getString(ApplicationConstant.CACHE_USERNAME,null);
       Gson gson = new Gson();

        UserVO userVO = gson.fromJson(resp, UserVO.class);


        return  userVO.getUserId();
    }

    public static String getUserName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String resp=sharedPreferences.getString(ApplicationConstant.CACHE_USERNAME,null);
        Gson gson = new Gson();
        UserVO userVO = gson.fromJson(resp, UserVO.class);
        return userVO.getUserName();
    }


    public static String getMPOSId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String resp=sharedPreferences.getString(ApplicationConstant.CACHE_USERNAME,null);
        Gson gson = new Gson();
        UserVO userVO = gson.fromJson(resp, UserVO.class);
        return userVO.getMposid();
    }

    public static void resetEzeTapPrinterPref(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String resp=sharedPreferences.getString(EZETAPPRINTER,null);
        if(resp!=null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(EZETAPPRINTER);
            editor.commit();

        }

    }

    public static void setEzeTapPrinterPref(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String resp=sharedPreferences.getString(EZETAPPRINTER,null);
        if(resp==null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(EZETAPPRINTER, "initialized");
            editor.apply();
            editor.commit();

        }

    }


    public static boolean isEzeTapPrinterPref(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String resp=sharedPreferences.getString(EZETAPPRINTER,null);
        if(resp==null){
            return false;
        }
        return true;

    }


    public static String getEmployeecodebyId(Context context , String employeeId){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String resp=sharedPreferences.getString(ApplicationConstant.CACHE_EMPLOYEE_LIST,null);
        String code ="";
        try {
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(resp);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i=1; i< jsonArray.length(); i ++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if(jsonObject1.getString("EmployeeID").equalsIgnoreCase(employeeId)){
                    code= jsonObject1.getString("Code");
                    break;
                }
            }
        } catch (JSONException e) {
            Utility.exceptionAlertDialog(context,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));
        }
        return  code;
    }

    public static String getSessionByKey(Context context,String cacheKey){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String resp=sharedPreferences.getString(cacheKey,null);
        return resp;

    }
}
