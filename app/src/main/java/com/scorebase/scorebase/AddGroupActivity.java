package com.scorebase.scorebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddGroupActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Sport> sports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        initializeData();

        mAdapter = new AddSportAdapter(sports);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initializeData(){
        sports = new ArrayList<>();
        sports.add(new Sport(R.drawable.baseball));
        sports.add(new Sport(R.drawable.basketball));
        sports.add(new Sport(R.drawable.boxing));
        sports.add(new Sport(R.drawable.soccer));
        sports.add(new Sport(R.drawable.tennisball));
    }

    class Sport{
        int imageId;
        Sport(int imageId){
            this.imageId = imageId;
        }
    }
}
