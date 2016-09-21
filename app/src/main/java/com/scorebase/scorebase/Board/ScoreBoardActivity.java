package com.scorebase.scorebase.Board;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scorebase.scorebase.R;

public class ScoreBoardActivity extends AppCompatActivity {

    // 팀 A (Score, Text, Card, Board)
    private int teamAScore = 0;
    private TextView teamAScoreText;
    private CardView teamACard;
    private ScoreBoard teamAScoreBoard;

    // 팀 B (Score, Text, Card, Board)
    private int teamBScore = 0;
    private TextView teamBScoreText;
    private CardView teamBCard;
    private ScoreBoard teamBScoreBoard;

    // 리셋 버튼
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        setContentView(R.layout.activity_board);

        resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamAScore = 0;
                teamBScore = 0;
                teamAScoreText.setText("" + teamAScore);
                teamBScoreText.setText("" + teamBScore);
            }
        });

        // 팀 A 참조 및 Listener 설정
        teamAScoreText = (TextView) findViewById(R.id.team_a_score);
        teamAScoreText.setText("" + teamAScore);
        teamACard = (CardView) findViewById(R.id.team_a_card);
        teamAScoreBoard = new ScoreBoard(teamACard, new ScoreBoardListener() {
            @Override
            public void onUpScrollEvent() {
                teamAScoreText.setText("" + ++teamAScore);
            }

            @Override
            public void onDownScrollEvent() {
                if (teamAScore > 0)
                    teamAScoreText.setText("" + --teamAScore);
            }
        });

        // 팀 B 참조 및 Listener 설정
        teamBScoreText = (TextView) findViewById(R.id.team_b_score);
        teamBScoreText.setText("" + teamAScore);
        teamBCard = (CardView) findViewById(R.id.team_b_card);
        teamBScoreBoard = new ScoreBoard(teamBCard, new ScoreBoardListener() {
            @Override
            public void onUpScrollEvent() {
                teamBScoreText.setText("" + ++teamBScore);
            }

            @Override
            public void onDownScrollEvent() {
                if (teamBScore > 0)
                    teamBScoreText.setText("" + --teamBScore);
            }
        });
    }

    // Multi Touch Gesture 모드
    private final int NONE = 0;
    private final int DRAG = 1;
    private final int ZOOM = 2;
    private int mode = NONE;

    // 두 손가락 거리들
    private float startDistance = 1f;
    private float newDistance = 1f;
    private float onEventDistance = 150f;

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            // 첫 번째 손가락 클릭시
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;
                break;
            // 두 번째 손가락 클릭시
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                // 두 손가락의 거리를 구한다.
                newDistance = spacing(event);
                startDistance = spacing(event);
                break;
            // 손가락이 움직일 시
            case MotionEvent.ACTION_MOVE:
                // 두 손가락이 터치되어 있으면
                if (mode == ZOOM) {
                    // 새로운 거리를 구한다.
                    newDistance = spacing(event);
                    // 정해진 거리 만큼 거리가 좁혀졌으면 새로운 액티비티를 띄운다.
                    if (startDistance - newDistance > onEventDistance) {
                        finish();
                        mode = NONE;
                    }
                }
                break;
            // 손가락이 떨어지면 모드는 NONE이 된다.
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
    // 두 손가락 거리 구하는 메소드
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
}