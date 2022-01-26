package com.vthree.rentbaseapplication.ModelClass;

public class BookingModel {
    String b_id;
    String equipment_id;
    String equipment_name;
    String equipment_discription;
    String priceinhr;
    String deposit;
    String book_user_id;
    String book_date;
    String book_from_date;
    String book_to_date;
    String book_from_time;
    String book_to_time;
    String book_contact;
    String total_hour;
    String total_amont;
    String user_id;
    String user_contact;
    String user_address;
    String user_city;
    String user_taluka;
    String status;

    public BookingModel() {
    }


    public BookingModel(String b_id, String equipment_id, String equipment_name, String equipment_discription, String priceinhr, String deposit, String book_user_id, String book_date, String book_from_time, String book_to_time, String book_contact, String total_hour, String total_amont, String user_id, String user_contact, String user_address, String user_city, String user_taluka,String status) {
        this.b_id = b_id;
        this.equipment_id = equipment_id;
        this.equipment_name = equipment_name;
        this.equipment_discription = equipment_discription;
        this.priceinhr = priceinhr;
        this.deposit = deposit;
        this.book_user_id = book_user_id;
        this.book_date = book_date;
        this.book_from_time = book_from_time;
        this.book_to_time = book_to_time;
        this.book_contact = book_contact;
        this.total_hour = total_hour;
        this.total_amont = total_amont;
        this.user_id = user_id;
        this.user_contact = user_contact;
        this.user_address = user_address;
        this.user_city = user_city;
        this.user_taluka = user_taluka;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getB_id() {
        return b_id;
    }

    public void setB_id(String b_id) {
        this.b_id = b_id;
    }

    public String getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(String equipment_id) {
        this.equipment_id = equipment_id;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public String getEquipment_discription() {
        return equipment_discription;
    }

    public void setEquipment_discription(String equipment_discription) {
        this.equipment_discription = equipment_discription;
    }

    public String getPriceinhr() {
        return priceinhr;
    }

    public void setPriceinhr(String priceinhr) {
        this.priceinhr = priceinhr;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getBook_user_id() {
        return book_user_id;
    }

    public void setBook_user_id(String book_user_id) {
        this.book_user_id = book_user_id;
    }

    public String getBook_date() {
        return book_date;
    }

    public void setBook_date(String book_date) {
        this.book_date = book_date;
    }

    public String getBook_from_date() {
        return book_from_date;
    }

    public void setBook_from_date(String book_from_date) {
        this.book_from_date = book_from_date;
    }

    public String getBook_to_date() {
        return book_to_date;
    }

    public void setBook_to_date(String book_to_date) {
        this.book_to_date = book_to_date;
    }

    public String getBook_from_time() {
        return book_from_time;
    }

    public void setBook_from_time(String book_from_time) {
        this.book_from_time = book_from_time;
    }

    public String getBook_to_time() {
        return book_to_time;
    }

    public void setBook_to_time(String book_to_time) {
        this.book_to_time = book_to_time;
    }

    public String getBook_contact() {
        return book_contact;
    }

    public void setBook_contact(String book_contact) {
        this.book_contact = book_contact;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_contact() {
        return user_contact;
    }

    public void setUser_contact(String user_contact) {
        this.user_contact = user_contact;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public String getUser_taluka() {
        return user_taluka;
    }

    public void setUser_taluka(String user_taluka) {
        this.user_taluka = user_taluka;
    }

    public String getTotal_hour() {
        return total_hour;
    }

    public void setTotal_hour(String total_hour) {
        this.total_hour = total_hour;
    }

    public String getTotal_amont() {
        return total_amont;
    }

    public void setTotal_amont(String total_amont) {
        this.total_amont = total_amont;
    }
}
