package com.example.pahlik.bean;


import android.os.Build;

import com.example.pahlik.code.SecretKeyEnum;
import com.example.pahlik.code.SharedKeyHebrewEncoderDecoder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Malfunction implements Serializable {


    private String id;

    private User user;
    private String name_malfunction;
    private MalfunctionType malfunctionType;

    private String location;
    private String description;
    private String solution;


    public String localDateTime;
    private boolean is_solved = false;
    private String contact;
    private String contact_phonenumber;
    public Malfunction() {
    }

    public Malfunction(User user, String name_malfunction, MalfunctionType malfunctionType, String location, String description, LocalDateTime localDateTime, boolean is_solved, String contact, String contact_phone_number) {
        this.user = user;
        this.name_malfunction = name_malfunction;
        this.malfunctionType = malfunctionType;
        this.location = location;
        this.description = description;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" mm:HH dd/MM/yy");
            this.localDateTime = localDateTime.format(formatter);
        }
        this.is_solved = is_solved;
        this.contact = contact;
        this.contact_phonenumber = contact_phone_number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MalfunctionType getMalfunctionType() {
        return malfunctionType;
    }

    public void setMalfunctionType(MalfunctionType malfunctionType) {
        this.malfunctionType = malfunctionType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_solved() {
        return is_solved;
    }

    public void setIs_solved(boolean is_solved) {
        this.is_solved = is_solved;
    }

    public String getName_malfunction() {
        return name_malfunction;
    }

    public void setName_malfunction(String name_malfunction) {
        this.name_malfunction = name_malfunction;
    }


    public void encode() {
        malfunctionType.encode();
        user.encode();
        SharedKeyHebrewEncoderDecoder.SECRET_KEY = SecretKeyEnum.ERROR;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                description = SharedKeyHebrewEncoderDecoder.encode(description);
                location = SharedKeyHebrewEncoderDecoder.encode(location);
                name_malfunction = SharedKeyHebrewEncoderDecoder.encode(name_malfunction);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void decode() {
        malfunctionType.decode();
        user.decode();
        SharedKeyHebrewEncoderDecoder.SECRET_KEY = SecretKeyEnum.ERROR;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                description = SharedKeyHebrewEncoderDecoder.decode(description);
                location = SharedKeyHebrewEncoderDecoder.decode(location);
                name_malfunction = SharedKeyHebrewEncoderDecoder.decode(name_malfunction);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact_phonenumber() {
        return contact_phonenumber;
    }

    public void setContact_phonenumber(String contact_phonenumber) {
        this.contact_phonenumber = contact_phonenumber;
    }
}
