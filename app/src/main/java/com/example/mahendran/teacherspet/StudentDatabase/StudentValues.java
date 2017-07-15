package com.example.mahendran.teacherspet.StudentDatabase;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mahendran on 03-06-2017.
 */

public class StudentValues implements Serializable {

    private String studentName;
    private String emailID;
    private String studentph;
    private String parentph;
    private String address;
    private String id;
    public Map<String, String> map = new HashMap<String, String>();

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParentph() {
        return parentph;
    }

    public void setParentph(String parentph) {
        this.parentph = parentph;
    }

    public String getStudentph() {
        return studentph;
    }

    public void setStudentph(String studentph) {
        this.studentph = studentph;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }
}
