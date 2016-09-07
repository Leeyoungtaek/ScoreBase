package com.scorebase.scorebase;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by DSM_055 on 2016-09-06.
 */
@IgnoreExtraProperties
public class Account {

    private String createdAt;
    private String displayName;
    private String email;
    private String gender;
    private String introduction;
    private String photoURL;
    private String providerId;
    private String uid;
    private ArrayList<String> groups;

    public Account(){
    }

    public Account(String createdAt, String displayName, String email, String gender, String introduction, Uri photoURL, String providerId, String uid) {
        this.createdAt = createdAt;
        this.displayName = displayName;
        this.email = email;
        this.gender = gender;
        this.introduction = introduction;
        this.photoURL = String.valueOf(photoURL);
        this.providerId = providerId;
        this.uid = uid;
        groups = new ArrayList<String>();
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void addGroup(String group){
        groups.add(group);
    }
}
