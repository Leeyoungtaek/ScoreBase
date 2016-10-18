package com.scorebase.scorebase.DataFormat;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by DSM_055 on 2016-09-08.
 */
@IgnoreExtraProperties
public class Member {
    private String name;
    private int position;
    private int gameNumber;
    private int winNumber;

    Member(){
    }

    public Member(String Name, int Position, int gameNumber, int winNumber) {
        this.name = Name;
        setPosition(Position);
        this.gameNumber = gameNumber;
        this.winNumber = winNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        if (position >= 6 || position <= 0)
            return;
        this.position = position;
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    public int getWinNumber() {
        return winNumber;
    }

    public void setWinNumber(int winNumber) {
        this.winNumber = winNumber;
    }
}
