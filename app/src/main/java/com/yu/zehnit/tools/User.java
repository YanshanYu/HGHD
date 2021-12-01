package com.yu.zehnit.tools;

public class User {
    private String user_num;
    //private String user_password;

    public User(String user_num){
        super();
        this.user_num=user_num;
       // this.user_password=user_password;
    }

    public String getUser_num() {
        return user_num;
    }

    public void setUser_num(String user_num) {
        this.user_num = user_num;
    }
/*
    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

 */
}
