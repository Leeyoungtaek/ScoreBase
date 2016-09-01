package com.scorebase.scorebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

/**
 * Created by Lee young teak on 2016-08-25.
 */
public class GroupFragment extends Fragment {

    // Views
    private FloatingActionButton floatingActionButton;

    public GroupFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        // View Reference
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);

        // View Setting
        floatingActionButton.setColorNormalResId(R.color.colorAccent);
        floatingActionButton.setColorPressedResId(R.color.colorFloatingActionButton);

        // Go to AddGroupActivity
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddGroupActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
