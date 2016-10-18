package com.scorebase.scorebase.Board;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

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
