package com.uav.ava.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;


public class PermissionHandler {

    public static String permissiontype;


    public  static boolean cameraPermission(Context context){

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(  (Activity) context, new String[]{android.Manifest.permission.CAMERA}, 50);
            return false;
        }


        return  true;

    }
//        ActivityCompat.requestPermissions(  (Activity) context, new String[]{android.Manifest.permission.CAMERA}, 50);
//            activity =context;



    public  static boolean readWritePermission(Context context){
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(  (Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 50);
            return false;
        }
        return  true;
    }




    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults , Context context) {

        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                //denied
                Log.e("denied", permission);
                PermissionHandler.permissiontype="denied";

            } else {
                if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                    //allowed
                    Log.e("allowed", permission);
                    PermissionHandler.permissiontype="allowed";

                } else {
                    //set to never ask again


                    Log.e("set to never ask again", permission);
                    PermissionHandler.permissiontype="set to never ask again";
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + context.getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        }
    }
}
