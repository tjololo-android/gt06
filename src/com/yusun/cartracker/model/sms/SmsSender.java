package com.yusun.cartracker.model.sms;

import java.util.ArrayList;

import android.telephony.SmsManager;

public class SmsSender {
    public static void send(String phone, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> list = smsManager.divideMessage(message);
        for (String text:list) {
            smsManager.sendTextMessage(phone, null, text, null, null);
        }
    }
}