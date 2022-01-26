package com.vthree.rentbaseapplication.ModelClass;

public class UserModel {
    String user_id;
    String user_name;
    String mobile;
    String address;
    String city;
    String taluka;
    String description;
    String prize;
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserModel() {

    }

    public UserModel(String user_id, String user_name, String address, String mobile, String city, String taluka,String token) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.address = address;
        this.mobile = mobile;
        this.city = city;
        this.taluka = taluka;
        this.token=token;
    }

    public UserModel(String user_id, String user_name, String mobile, String address, String city, String taluka, String description, String prize) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.mobile = mobile;
        this.address = address;
        this.city = city;
        this.taluka = taluka;
        this.description = description;
        this.prize = prize;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }
}
