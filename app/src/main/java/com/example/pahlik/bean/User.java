package com.example.pahlik.bean;

import android.os.Build;

import com.example.pahlik.code.SecretKeyEnum;
import com.example.pahlik.code.SharedKeyHebrewEncoderDecoder;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String jobTitle;
    private String rank;

    private boolean manager;

    private boolean specialist;
    public User() {
    }

    public User(String firstName, String lastName, String phoneNumber, String jobTitle, String rank) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.jobTitle = jobTitle;
        this.rank = rank;
    }

    // Getter and setter methods for each field
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", rank='" + rank + '\'' +
                '}';
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }
    public  void encode(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                SharedKeyHebrewEncoderDecoder.SECRET_KEY= SecretKeyEnum.RANK;
                rank=SharedKeyHebrewEncoderDecoder.encode(rank);
                SharedKeyHebrewEncoderDecoder.SECRET_KEY= SecretKeyEnum.JOB;
                jobTitle=SharedKeyHebrewEncoderDecoder.encode(jobTitle);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public  void decode(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                SharedKeyHebrewEncoderDecoder.SECRET_KEY= SecretKeyEnum.RANK;
                rank=SharedKeyHebrewEncoderDecoder.decode(rank);
                SharedKeyHebrewEncoderDecoder.SECRET_KEY= SecretKeyEnum.JOB;
                jobTitle=SharedKeyHebrewEncoderDecoder.decode(jobTitle);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

