package com.scorebase.scorebase.Board;

import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by DSM_055 on 2016-07-19.
 * CardView, ScoreBoardListener 이어줌
 */
public class ScoreBoard extends Board{
    private ScoreBoardListener scoreBoardListener;

    public ScoreBoard(final CardView cardView, final ScoreBoardListener scoreBoardListener) {
        this.cardView = cardView;
        this.scoreBoardListener = scoreBoardListener;
        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 터치 시작시 startY set
                if (event.getAction() == MotionEvent.ACTION_DOWN && startY == -1f) {
                    startY = event.getY();
                }
                // 터치가 움직일 때
                else if (event.getAction() == MotionEvent.ACTION_MOVE && startY != -1f) {
                    // 정해진 거리 만큼 위로 올라갔으면 onUpScrollEvent 를 열어주고
                    if ( event.getY() - startY < -onEventDistance ) {
                        scoreBoardListener.onUpScrollEvent();
                        startY = -1f;
                    }
                    // 정해진 거리 만큼 아래로 내려갔으면 onDownScrollEvent 를 열어준다.
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
