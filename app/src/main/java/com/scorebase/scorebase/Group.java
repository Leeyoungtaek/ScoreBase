package com.scorebase.scorebase;

import java.util.ArrayList;

/**
 * Created by DSM_055 on 2016-09-01.
 * Group Class
 */
public class Group {
    private String Name;
    private String AccessScope;
    private ArrayList<String> Sports;
    private ArrayList<Member> Members;

    Group(String Name, String AccesssScope, boolean[] states){
        Sports = new ArrayList<String>();
        Members = new ArrayList<Member>();
        this.Name = Name;
        this.AccessScope = AccesssScope;

        String[] sportNames = new String[]{
                "baseball",
                "basketball",
                "boxing",
                "soccer",
                "tennisball"
        };
        for (int i = 0; i<states.length; i++){
            if(states[i]){
                Sports.add(sportNames[i]);
            }
        }
    }

    public void addMember(Member member){
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
    class Member{
        private String Name;
        private String Position;
        Member(String Name, String Position){
            this.Name = Name;
            this.Position = Position;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getPosition() {
            return Position;
        }

        public void setPosition(String position) {
            Position = position;
        }
    }
}
