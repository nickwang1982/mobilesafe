package com.example.course.mobilesafe.utils;

import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encode text string using Md5.
 */
public class Md5Encoder {
    public static String encode(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            byte[] result = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            // Convert each byte data into HEX
            for (int i= 0 ; i<result.length; i++) {
                int number = result[i] & 0xff;
                String str = Integer.toHexString(number);
                if(str.length() == 1) {
                    sb.append("0");
                    sb.append(str);
                } else {
                    sb.append(str);
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
