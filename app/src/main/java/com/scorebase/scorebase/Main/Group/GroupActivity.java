package com.scorebase.scorebase.Main.Group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.scorebase.scorebase.Board.GamesActivity;
import com.scorebase.scorebase.DataFormat.Group;
import com.scorebase.scorebase.R;

public class GroupActivity extends AppCompatActivity {

    // View
    private FloatingActionButton btnGoToGames;
    private TextView groupName, accessScope;

    // Data
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        // Intent
        Intent intent = getIntent();
        group = (Group) intent.getSerializableExtra("group");

        // View Reference
        btnGoToGames = (FloatingActionButton) findViewById(R.id.fab);
        groupName = (TextView)findViewById(R.id.text_view_group_name);
        accessScope = (TextView)findViewById(R.id.text_view_access_scope);

        // View Set
        btnGoToGames.setColorNormalResId(R.color.colorAccent);
        btnGoToGames.setColorPressedResId(R.color.colorFloatingActionButton);
        groupName.setText(group.getName());
        accessScope.setText(group.getAccessScope());

        // View Event
        btnGoToGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GamesActivity.class);
                intent.putStringArrayListExtra("sport", group.sports);
                startActivity(intent);
            }
        });
    }
}
