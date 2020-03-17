package com.uav.ava.MosambeeBarcode;

import android.os.Handler;
import android.os.Message;

public class HendleMessageScan {

    public static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 888:
                    SerialPortIOManage.getInstance().sendDataToDevice("00");
                    sendEmptyMessageDelayed(1000, 20);
                    break;
                case 999:
                    SerialPortIOManage.getInstance().sendDataToDevice("00");
                    sendEmptyMessageDelayed(1001, 20);
                    break;
                case 1000:
                    SerialPortIOManage.getInstance().sendDataToDevice("04 E4 04 00 FF 14");
                    break;
                case 1001:
                    SerialPortIOManage.getInstance().sendDataToDevice("07 C6 04 00 FF 8A 08 FD 9E");
                    //SerialPortIOManage.getInstance().sendDataToDevice("07 C6 04 80 14 8A 01 FE 10");
                    break;
            }
        }


    };
}
