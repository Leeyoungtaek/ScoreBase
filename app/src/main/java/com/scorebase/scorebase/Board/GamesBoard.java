package com.scorebase.scorebase.Board;

import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;

import com.scorebase.scorebase.Board.Listener.GamesBoardListener;

public class GamesBoard extends Board{
    private GamesBoardListener gamesBoardListener;

    public GamesBoard(final CardView cardView, final GamesBoardListener gamesBoardListener){
        this.cardView = cardView;
        this.gamesBoardListener = gamesBoardListener;

        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && startY == -1f) {
                    startY = event.getY();
                }
                else if (event.getAction() == MotionEvent.ACTION_MOVE && startY != -1f) {
                    if ( event.getY() - startY > onEventDistance ) {
                        gamesBoardListener.onDownScrollEvent();
                        startY = -1f;
                    }
                }
                return true;
            }
        });
    }
}
