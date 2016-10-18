package com.scorebase.scorebase.Board;

import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;

import com.scorebase.scorebase.Board.Listener.ScoreBoardListener;

public class ScoreBoard extends Board{
    private ScoreBoardListener scoreBoardListener;

    public ScoreBoard(final CardView cardView, final ScoreBoardListener scoreBoardListener) {
        this.cardView = cardView;
        this.scoreBoardListener = scoreBoardListener;

        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && startY == -1f) {
                    startY = event.getY();
                }
                else if (event.getAction() == MotionEvent.ACTION_MOVE && startY != -1f) {
                    if ( event.getY() - startY < -onEventDistance ) {
                        scoreBoardListener.onUpScrollEvent();
                        startY = -1f;
                    }
                    else if ( event.getY() - startY > onEventDistance ) {
                        scoreBoardListener.onDownScrollEvent();
                        startY = -1f;
                    }
                }
                return true;
            }
        });
    }
}
