package com.duckcatchandfit.datacollector.utils;

import android.os.Build;

public class Device {

    public static String getDeviceId() {
        return Hashing.getMD5Hash(Build.MANUFACTURER + Build.MODEL + Build.FINGERPRINT);
    }
}
