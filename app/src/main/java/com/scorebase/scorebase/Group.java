package com.scorebase.scorebase;

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
    private String Name;
    private String AccessScope;
    public ArrayList<String> Sports;
    public ArrayList<Member> Members;

    private String[] sportNames = new String[]{
            "baseball",
            "basketball",
            "boxing",
            "soccer",
            "tennisball"
    };

    Group(){
    }

    Group(String Name, String AccesssScope, boolean[] states, Member member){
        Sports = new ArrayList<String>();
        Members = new ArrayList<Member>();
        this.Name = Name;
        this.AccessScope = AccesssScope;

        for (int i = 0; i<states.length; i++){
            if(states[i]){
                Sports.add(sportNames[i]);
            }
        }

        Members.add(member);
    }

    protected Group(Parcel in) {
        Name = in.readString();
        AccessScope = in.readString();
        Sports = in.createStringArrayList();
        sportNames = in.createStringArray();
    }

    public void addSport(String sport){
        if(!Sports.contains(sport)){
            Sports.add(sport);
        }
        return;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAccessScope() {
        return AccessScope;
    }

    public void setAccessScope(String accessScope) {
        AccessScope = accessScope;
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
        parcel.writeString(Name);
        parcel.writeString(AccessScope);
        parcel.writeStringList(Sports);
        parcel.writeStringArray(sportNames);
    }
}
