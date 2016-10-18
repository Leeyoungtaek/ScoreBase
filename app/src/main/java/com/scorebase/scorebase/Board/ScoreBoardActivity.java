package com.scorebase.scorebase.Board;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scorebase.scorebase.Board.Listener.ScoreBoardListener;
import com.scorebase.scorebase.DataFormat.Game;
import com.scorebase.scorebase.DataFormat.History;
import com.scorebase.scorebase.DataFormat.Member;
import com.scorebase.scorebase.DataFormat.Team;
import com.scorebase.scorebase.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ScoreBoardActivity extends AppCompatActivity {

    // 팀 A (View & Data)
    private int teamAScore = 0;
    private TextView teamAScoreText;
    private CardView teamACard;
    private ScoreBoard teamAScoreBoard;

    // 팀 B (View & Data)
    private int teamBScore = 0;
    private TextView teamBScoreText;
    private CardView teamBCard;
    private ScoreBoard teamBScoreBoard;

    // View
    private Button btnReset, btnSave, btnStart;

    // FireBase
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    // Data
    private long createdAt;
    private String createdAtDate = null;
    private long endedAt;
    private String endedAtDate;
    private String sport;
    private ArrayList<Integer> scores = new ArrayList<Integer>();
    private HashMap<String, History> histories = new HashMap<String, History>();
    private ArrayList<Team> teams = new ArrayList<Team>();

    // Data:A
    private ArrayList<String> aMembers = new ArrayList<String>();

    // Data:B
    private ArrayList<String> bMembers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        setContentView(R.layout.activity_board);

        // temp
        aMembers.add("이영택");
        bMembers.add("김기황");

        // Intent
        Intent intent = getIntent();
        sport = intent.getStringExtra("sport");

        // View Reference
        btnStart = (Button) findViewById(R.id.start_button);
        btnReset = (Button) findViewById(R.id.reset_button);
        btnSave = (Button) findViewById(R.id.save_button);
        teamAScoreText = (TextView) findViewById(R.id.team_a_score);
        teamACard = (CardView) findViewById(R.id.team_a_card);
        teamBScoreText = (TextView) findViewById(R.id.team_b_score);
        teamBCard = (CardView) findViewById(R.id.team_b_card);

        // FireBase Reference
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // View Set
        teamAScoreText.setText("" + teamAScore);
        teamBScoreText.setText("" + teamAScore);

        // View Event
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createdAt = System.currentTimeMillis();
                createdAtDate = new Date(createdAt).toLocaleString();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createdAt = 0;
                createdAtDate = null;
                teamAScore = 0;
                teamBScore = 0;
                teamAScoreText.setText("" + teamAScore);
                teamBScoreText.setText("" + teamBScore);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(createdAtDate)) {
                    Toast.makeText(ScoreBoardActivity.this, "경기가 시작되지 않았습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    endedAt = System.currentTimeMillis();
                    endedAtDate = new Date(endedAt).toLocaleString();
                    scores.add(new Integer(teamAScore));
                    scores.add(new Integer(teamBScore));
                    teams.add(new Team(aMembers));
                    teams.add(new Team(bMembers));
                    databaseReference.child("games").push().setValue(new Game(createdAt, createdAtDate, endedAt, endedAtDate, histories, scores, sport, teams));
                    finish();
                }
            }
        });

        teamAScoreBoard = new ScoreBoard(teamACard, new ScoreBoardListener() {
            @Override
            public void onUpScrollEvent() {
                teamAScoreText.setText("" + ++teamAScore);
                if (!TextUtils.isEmpty(createdAtDate)) {
                    putHistory(1, 0);
                }
            }

            @Override
            public void onDownScrollEvent() {
                if (teamAScore > 0) {
                    teamAScoreText.setText("" + --teamAScore);
                    if (!TextUtils.isEmpty(createdAtDate)) {
                        putHistory(-1, 0);
                    }
                }
            }
        });

        teamBScoreBoard = new ScoreBoard(teamBCard, new ScoreBoardListener() {
            @Override
            public void onUpScrollEvent() {
                teamBScoreText.setText("" + ++teamBScore);
                if (!TextUtils.isEmpty(createdAtDate)) {
                    putHistory(1, 1);
                }
            }

            @Override
            public void onDownScrollEvent() {
                if (teamBScore > 0) {
                    teamBScoreText.setText("" + --teamBScore);
                    if (!TextUtils.isEmpty(createdAtDate)) {
                        putHistory(-1, 1);
                    }
                }
            }
        });
    }

    private void putHistory(int point, int team) {
        long time = System.currentTimeMillis();
        String date = new Date(time).toLocaleString();
        ArrayList<Integer> score = new ArrayList<Integer>();
        score.add(teamAScore);
        score.add(teamBScore);
        histories.put(String.valueOf(time), new History(date, point, score, team));
    }

    // Multi Touch Gesture 모드
    private final int NONE = 0;
    private final int DRAG = 1;
    private final int ZOOM = 2;
    private int mode = NONE;

    // 손가락 거리
    private float startDistance = 1f;
    private float newDistance = 1f;
    private float onEventDistance = 150f;

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                newDistance = spacing(event);
                startDistance = spacing(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ZOOM) {
                    newDistance = spacing(event);
                    // 정해진 거리 만큼 거리가 좁혀졌으면 액티비티를 끝낸다.
                    if (startDistance - newDistance > onEventDistance) {
                        finish();
                        mode = NONE;
                    }
                }
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
}