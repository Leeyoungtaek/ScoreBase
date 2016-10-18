package com.scorebase.scorebase.Board;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;

import com.scorebase.scorebase.R;

import java.util.ArrayList;
import java.util.List;

public class GamesActivity extends AppCompatActivity {

    // View:RecyclerView
    private RecyclerView recyclerView;
    private CustomLayoutManager linearLayoutManager;
    private RecyclerAdapter recyclerAdapter;

    // Data
    private List<GamesItem> items = new ArrayList<>();
    private ArrayList<String> sports;

    // Data:Display
    private Point size;
    private Display display;

    // Data:Width
    private float padding;
    private float itemWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        setContentView(R.layout.activity_games);

        // Intent & Init
        Intent intent = getIntent();
        sports = intent.getStringArrayListExtra("sport");

        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        itemWidth = getResources().getDimension(R.dimen.width_card);
        padding = (size.x - itemWidth) / 2;

        // View Reference
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        // View Set
        for (int i = 0; i < sports.size(); i++) {
            switch (sports.get(i)){
                case "baseball":
                    items.add(new GamesItem(R.drawable.baseball, "Baseball"));
                    break;
                case "basketball":
                    items.add(new GamesItem(R.drawable.basketball, "Basketball"));
                    break;
                case "boxing":
                    items.add(new GamesItem(R.drawable.boxing, "Boxing"));
                    break;
                case "soccer":
                    items.add(new GamesItem(R.drawable.soccer, "Soccer"));
                    break;
                case "tennisball":
                    items.add(new GamesItem(R.drawable.tennisball, "Tennisball"));
                    break;
                default :
                    break;
            }
        }
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), items);
        linearLayoutManager = new CustomLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, (int) padding);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        // View Event
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean isComputed = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // 아래 변수는 현재 RecyclerView 의 위치이다. (Padding 포함)
                int offset = recyclerView.computeHorizontalScrollOffset();
                // 움직이지 않고 있으면 실행
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isComputed) {
                        recyclerView.stopScroll();
                        // 어디에 마추어야 하나 계산
                        int value = (int) ((offset) / itemWidth);
                        recyclerView.smoothScrollToPosition(value);
                        isComputed = true;
                    } else if (isComputed) {
                        isComputed = false;
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
}
