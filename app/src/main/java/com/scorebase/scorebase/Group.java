package com.scorebase.scorebase;

import java.util.ArrayList;

/**
 * Created by DSM_055 on 2016-09-01.
 * Group Class
 */
public class Group {
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
}
