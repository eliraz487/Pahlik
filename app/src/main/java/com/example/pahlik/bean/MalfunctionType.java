package com.example.pahlik.bean;

import android.os.Build;

import com.example.pahlik.code.SecretKeyEnum;
import com.example.pahlik.code.SharedKeyHebrewEncoderDecoder;

import java.io.Serializable;

public class MalfunctionType implements Serializable {
    private String system;

    private String malfunction_type;

    public MalfunctionType() {
    }

    public MalfunctionType(String system, String malfunction_type) {
        this.system = system;
        this.malfunction_type = malfunction_type;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getMalfunction_type() {
        return malfunction_type;
    }

    public void setMalfunction_type(String malfunction_type) {
        this.malfunction_type = malfunction_type;
    }


    public  void encode(){
        SharedKeyHebrewEncoderDecoder.SECRET_KEY= SecretKeyEnum.SYSTEM;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                malfunction_type=SharedKeyHebrewEncoderDecoder.encode(malfunction_type);
                system=SharedKeyHebrewEncoderDecoder.encode(system);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public  void decode(){
        SharedKeyHebrewEncoderDecoder.SECRET_KEY= SecretKeyEnum.SYSTEM;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                malfunction_type=SharedKeyHebrewEncoderDecoder.decode(malfunction_type);
                system=SharedKeyHebrewEncoderDecoder.decode(system);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "MalfunctionType{" +
                "system='" + system + '\'' +
                ", malfunction_type='" + malfunction_type + '\'' +
                '}';
    }
}
