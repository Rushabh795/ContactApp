package com.rushabh.contactapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Contact implements Parcelable {
    // creating variables for our different fields.
    @Exclude
    private int id;
    private String strFirstName;
    private String strLastName;
    private String strMobileNum;
    private String strEmail;
    private String strAdd;

    // creating getter and setter methods.
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    // creating an empty constructor.

    protected Contact(Parcel in) {
        id = in.readInt();
        strFirstName = in.readString();
        strLastName = in.readString();
        strMobileNum = in.readString();
        strEmail = in.readString();
        strAdd = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public Contact(String strFirstName,
                        String strLastName,
                        String strMobileNum,
                        String strEmail,
                        String strAdd) {
        this.id = id;
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
        this.strMobileNum = strMobileNum;
        this.strEmail = strEmail;
        this.strAdd = strAdd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(strFirstName);
        parcel.writeString(strLastName);
        parcel.writeString(strMobileNum);
        parcel.writeString(strEmail);
        parcel.writeString(strAdd);
    }
}
