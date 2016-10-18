package com.scorebase.scorebase.DataFormat;

import java.util.ArrayList;

/**
 * Created by Lee young teak on 2016-10-18.
 */

public class History {
    public String date;
    public int point;
    public ArrayList<Integer> score;
    public int team;

    public History(String date, int point, ArrayList<Integer> score, int team){
        this.date = date;
        this.point = point;
        this.score = score;
        this.team = team;
    }
}
