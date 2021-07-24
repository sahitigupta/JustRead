package com.example.justread.Datamodels;

public class UserModel {
    private String username, useremail, userpassword;
    private long userphone;

    private int userid;

    public UserModel(String username, String useremail, String userpassword, long userphone, int userid) {
        this.username = username;
        this.useremail = useremail;
        this.userpassword = userpassword;
        this.userphone = userphone;
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public long getUserphone() {
        return userphone;
    }

    public void setUserphone(long userphone) {
        this.userphone = userphone;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
