package com.yusun.cartracker.api;

import java.util.ArrayList;

import android.telephony.SmsManager;

public class SendMsg {
    String message;
    String phone;

    public SendMsg(String message, String phone) {
        this.message = message;
        this.phone = phone;
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> list = smsManager.divideMessage(message);
        for (String text:list) {
            smsManager.sendTextMessage(phone, null, text, null, null);
        }
    }
}