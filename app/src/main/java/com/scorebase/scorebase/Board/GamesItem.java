package com.scorebase.scorebase.Board;

/**
 * Created by DSM_055 on 2016-07-21.
 * 게임들 내용 (관련 이미지, 게임 이름)
 */
public class GamesItem {
    private int image;
    private String title;

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    GamesItem(int image, String title){
        this.image = image;
        this.title = title;
    }
}
