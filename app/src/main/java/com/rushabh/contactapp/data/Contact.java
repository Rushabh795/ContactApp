package com.rushabh.contactapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Contact  {
    // creating variables for our different fields.
    @Exclude
    private String id;
    private String strFirstName;
    private String strLastName;

    public String getStrNickName() {
        return strNickName;
    }

    public void setStrNickName(String strNickName) {
        this.strNickName = strNickName;
    }

    private String strNickName;
    private String strMobileNum;
    private String strEmail;
    private String strAdd;

    public Contact() {
    }

    // creating getter and setter methods.
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public void setStrFirstName(String strFirstName) {
        this.strFirstName = strFirstName;
    }

    public String getStrLastName() {
        return strLastName;
    }

    public void setStrLastName(String strLastName) {
        this.strLastName = strLastName;
    }

    public String getStrMobileNum() {
        return strMobileNum;
    }

    public void setStrMobileNum(String strMobileNum) {
        this.strMobileNum = strMobileNum;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getStrAdd() {
        return strAdd;
    }

    public void setStrAdd(String strAdd) {
        this.strAdd = strAdd;
    }

}
