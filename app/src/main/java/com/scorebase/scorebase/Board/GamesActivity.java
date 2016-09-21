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

/**
 * Created by DSM_055 on 2016-07-22.
 * 무슨 게임의 카운트를 셀지 고르는 Activity 다.
 */

public class GamesActivity extends AppCompatActivity {

    // RecyclerView related
    private RecyclerView recyclerView;
    private CustomLayoutManager linearLayoutManager;
    private RecyclerAdapter recyclerAdapter;

    // CardView Item related
    private List<GamesItem> items = new ArrayList<>();
    private GamesItem[] item;

    // Display related
    private Point size;
    private Display display;

    // width related
    private float padding;
    private float itemWidth;

    // Data
    private ArrayList<String> sports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        setContentView(R.layout.activity_games);

        // get Intent
        Intent intent = getIntent();
        sports = intent.getStringArrayListExtra("sport");

        // get padding, itemWidth
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        itemWidth = getResources().getDimension(R.dimen.width_card);
        padding = (size.x - itemWidth) / 2;

        // GamesItem 내용물
        item = new GamesItem[sports.size()];
        for (int i = 0; i < sports.size(); i++) {
            switch (sports.get(i)){
                case "baseball":
                    item[i] = new GamesItem(R.drawable.baseball, "Baseball");
                    break;
                case "basketball":
                    item[i] = new GamesItem(R.drawable.basketball, "Basketball");
                    break;
                case "boxing":
                    item[i] = new GamesItem(R.drawable.boxing, "Boxing");
                    break;
                case "soccer":
                    item[i] = new GamesItem(R.drawable.soccer, "Soccer");
                    break;
                case "tennisball":
                    item[i] = new GamesItem(R.drawable.tennisball, "Tennisball");
                    break;
                default :
                    break;
            }
        }

        // items 리스트 만들기
        for (int i = 0; i < sports.size(); i++)
            items.add(item[i]);

        // RecyclerAdapter, LayoutManager 인스턴스화
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), items);
        linearLayoutManager = new CustomLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, (int) padding);

        // RecyclerView 설정
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
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
        // Adapter, LayoutManager set
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
