package com.scorebase.scorebase.Board;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by DSM_055 on 2016-07-22.
 * Padding 값을 받아서 RecyclerView 양 옆에 Padding 값을 넣어줬다.
 */
public class CustomLayoutManager extends LinearLayoutManager {
    private int padding;

    public CustomLayoutManager(final Context context, final int orientation, final int padding) {
        super(context, orientation, false);
        this.padding = padding;
    }

    @Override
    public int getPaddingLeft() {
        return padding;
    }

    @Override
    public int getPaddingRight() {
        return getPaddingLeft();
    }
}
