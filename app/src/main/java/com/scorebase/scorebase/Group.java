package com.scorebase.scorebase;

import java.util.ArrayList;

/**
 * Created by DSM_055 on 2016-09-01.
 */
public class Group {
    public String groupName;
    public String accessScope;
    public static String[] sportNames = new String[]{
            "baseball",
            "basketball",
            "boxing",
            "soccer",
            "tennisball"
    };
    public ArrayList<String> sports;
    Group(String groupName, String accessScope, boolean[] states){
        sports = new ArrayList<String>();
        this.groupName = groupName;
        this.accessScope = accessScope;
        for (int i = 0; i<states.length; i++){
            if(states[i]){
                sports.add(sportNames[i]);
            }
        }
    }
}
