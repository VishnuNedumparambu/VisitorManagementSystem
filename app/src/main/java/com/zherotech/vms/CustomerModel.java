package com.zherotech.vms;

public class CustomerModel {
    String name="";
    String phone="";
    String address="";
    String purpose="";
    String time="";
    String date="";

    public CustomerModel(String name, String phone, String address, String purpose, String time, String date) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.purpose = purpose;
        this.time = time;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
