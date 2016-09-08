package com.scorebase.scorebase;

/**
 * Created by DSM_055 on 2016-09-08.
 */
public class Member {
    private String Name;
    private int Position;

    Member(String Name, int Position) {
        this.Name = Name;
        setPosition(Position);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        if (position >= 6 || position <= 0)
            return;
        Position = position;
    }
}
