package com.scorebase.scorebase.DataFormat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DSM_055 on 2016-09-01.
 * Group Class
 */
@IgnoreExtraProperties
public class Group implements Serializable, Parcelable{
    private String name;
    private String accessScope;
    public ArrayList<String> sports;
    public ArrayList<Member> members;

    private String[] sportNames = new String[]{
            "baseball",
            "basketball",
            "boxing",
            "soccer",
            "tennisball"
    };

    Group(){
    }

    public Group(String Name, String AccesssScope, boolean[] states, Member member){
        sports = new ArrayList<String>();
        members = new ArrayList<Member>();
        this.name = Name;
        this.accessScope = AccesssScope;

        for (int i = 0; i<states.length; i++){
            if(states[i]){
                sports.add(sportNames[i]);
            }
        }

        members.add(member);
    }

    protected Group(Parcel in) {
        name = in.readString();
        accessScope = in.readString();
        sports = in.createStringArrayList();
        sportNames = in.createStringArray();
    }

    public void addSport(String sport){
        if(!sports.contains(sport)){
            sports.add(sport);
        }
        return;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessScope() {
        return accessScope;
    }

    public void setAccessScope(String accessScope) {
        this.accessScope = accessScope;
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(accessScope);
        parcel.writeStringList(sports);
        parcel.writeStringArray(sportNames);
    }
}
