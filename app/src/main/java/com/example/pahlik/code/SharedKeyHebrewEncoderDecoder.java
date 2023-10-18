package com.example.pahlik.code;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SharedKeyHebrewEncoderDecoder {
    public static  SecretKeyEnum SECRET_KEY ;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encode(String input) throws Exception {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getKey().getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encodedBytes = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encodedBytes);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decode(String encodedInput) throws Exception {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getKey().getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = cipher.doFinal(Base64.getDecoder().decode(encodedInput));
        return new String(decodedBytes);
    }


}
