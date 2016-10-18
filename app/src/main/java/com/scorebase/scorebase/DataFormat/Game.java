package com.scorebase.scorebase.DataFormat;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lee young teak on 2016-10-18.
 */

public class Game {
    public long createdAt;
    public String createdAtDate;
    public long endedAt;
    public String endedAtDate;
    public HashMap<String, History> histories;
    public ArrayList<Integer> scores;
    public String sport;
    public ArrayList<Team> teams;

    public Game(long createdAt, String createdAtDate, long endedAt, String endedAtDate, HashMap<String, History> histories, ArrayList<Integer> scores, String sport, ArrayList<Team> teams){
        this.createdAt = createdAt;
        this.createdAtDate = createdAtDate;
        this.endedAt = endedAt;
        this.endedAtDate = endedAtDate;
        this.histories = histories;
        this.scores = scores;
        this.sport = sport;
        this.teams = teams;
    }
}
