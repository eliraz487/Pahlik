package com.example.pahlik.code;
public enum SecretKeyEnum {
    RANK("JHs<DA;16l|e8'*3"),ERROR("XGkPtciGZ8414QJT"),JOB("RCX0wvhe9rSLgKgJ"), SYSTEM("Jt4x24XeQmEwcjcp");
    private String key;
    SecretKeyEnum(String key){
        this.key=key;
    }

    public String getKey() {
        return key;
    }
}