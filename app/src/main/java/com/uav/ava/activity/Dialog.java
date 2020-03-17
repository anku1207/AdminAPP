package com.uav.ava.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.PopupWindow;

import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.util.ExceptionHandler;
import com.uav.ava.vo.DiaglogVO;


public class Dialog extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        final PopupWindow popupWindow = new PopupWindow(this);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);


        Intent intent = getIntent();
        DiaglogVO diaglogVO = (DiaglogVO) intent.getSerializableExtra("dialog");


        if (getIntent().getAction() == ApplicationConstant.ACTION_DIALOG_SINGLEBUTTON) {
            onebuttonDialog(diaglogVO);
        }else if(getIntent().getAction() == ApplicationConstant.ACTION_DIALOG_TWOBUTTONS) {
            twobuttonDialog(diaglogVO);
        }else if(getIntent().getAction() == ApplicationConstant.ACTION_DIALOG_THREEBUTTONS) {
            threebuttonDialog(diaglogVO);
        }



    }


    public void onebuttonDialog (DiaglogVO diaglogVO ) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Dialog.this);
            builder.setTitle(diaglogVO.getTitle());
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setMessage(diaglogVO.getMessage());
            builder.setPositiveButton(diaglogVO.getButton1(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            // Get the alert dialog buttons reference
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            // Change the alert dialog buttons text and background color
            positiveButton.setTextColor(Color.parseColor("#FF0B8B42"));
            positiveButton.setBackgroundColor(Color.parseColor("#FFFFFF"));



    }
    public void twobuttonDialog (final DiaglogVO diaglogVO) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Dialog.this);
        builder.setTitle(diaglogVO.getTitle());
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(diaglogVO.getMessage());
        builder.setPositiveButton( diaglogVO.getButton1(), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra("response", diaglogVO.getButton1());

                setResult(RESULT_OK, intent);
                finish() ;
                overridePendingTransition(0, 0);
           }
        });
        builder.setNegativeButton(diaglogVO.getButton2(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra("response", diaglogVO.getButton2());
                setResult(RESULT_OK, intent);
                finish() ;
                overridePendingTransition(0, 0);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        // Get the alert dialog buttons reference
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        // Change the alert dialog buttons text and background color
        positiveButton.setTextColor(Color.parseColor("#FF0B8B42"));
        positiveButton.setBackgroundColor(Color.parseColor("#FFFFFF"));

        negativeButton.setTextColor(Color.parseColor("#FFFF0400"));
        negativeButton.setBackgroundColor(Color.parseColor("#FFFFFF"));



    }

    public void threebuttonDialog (DiaglogVO diaglogVO) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Dialog.this);
        builder.setTitle(diaglogVO.getTitle());
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(diaglogVO.getMessage());
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        // Get the alert dialog buttons reference
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        // Change the alert dialog buttons text and background color
        positiveButton.setTextColor(Color.parseColor("#FF0B8B42"));
        positiveButton.setBackgroundColor(Color.parseColor("#FFFFFF"));

        negativeButton.setTextColor(Color.parseColor("#FFFF0400"));
        negativeButton.setBackgroundColor(Color.parseColor("#FFFFFF"));

        neutralButton.setTextColor(Color.parseColor("#FF1B5AAC"));
        neutralButton.setBackgroundColor(Color.parseColor("#FFFFFF"));



    }


}
