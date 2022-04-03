package com.duckcatchandfit.datacollector.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {

    public static String getMD5Hash(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
            return toHexString(digest);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private static String toHexString(byte[] byteArray) {
        StringBuilder hexString = new StringBuilder();

        for (byte value : byteArray) {
            StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & value));

            while (h.length() < 2) {
                h.insert(0, "0");
            }

            hexString.append(h);
        }

        return hexString.toString();
    }
}
