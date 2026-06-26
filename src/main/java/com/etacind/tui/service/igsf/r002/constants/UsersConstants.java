package com.etacind.tui.service.igsf.r002.constants;

public enum UsersConstants {
    
    USERS_TABLE("users");

    private String value;

    UsersConstants(final String value){
        this.value = value;
    }

    public String value(){
        return this.value;
    }

}
