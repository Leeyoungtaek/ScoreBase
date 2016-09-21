package com.scorebase.scorebase.Board;

import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by DSM_055 on 2016-07-25.
 * CardView, gamesBoardListener 이어줌
 */
public class GamesBoard extends Board{
    private GamesBoardListener gamesBoardListener;

    public GamesBoard(final CardView cardView, final GamesBoardListener gamesBoardListener){
        this.cardView = cardView;
        this.gamesBoardListener = gamesBoardListener;
        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 터치 시작시 startY set
                if (event.getAction() == MotionEvent.ACTION_DOWN && startY == -1f) {
                    startY = event.getY();
                }
                // 터치가 움직일 때 거리를 계산해서 정해진 거리보다 많이 갔으면 gameBoardListener에서 onDownScrollEvent를 부름
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
