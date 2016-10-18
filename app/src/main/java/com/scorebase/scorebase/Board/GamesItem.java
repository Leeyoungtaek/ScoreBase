package com.scorebase.scorebase.Board;

public class GamesItem {
    private int image;
    private String title;

    GamesItem(int image, String title){
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}
