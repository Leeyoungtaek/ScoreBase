package com.scorebase.scorebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

public class GroupActivity extends AppCompatActivity {

    // Views
    private FloatingActionButton floatingActionButton;
    private TextView groupName, accessScope;

    // Data
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        // Get Group Data
        Intent intent = getIntent();
        group = (Group) intent.getSerializableExtra("group");

        // View Reference
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        groupName = (TextView)findViewById(R.id.text_view_group_name);
        accessScope = (TextView)findViewById(R.id.text_view_access_scope);

        // View Setting
        floatingActionButton.setColorNormalResId(R.color.colorAccent);
        floatingActionButton.setColorPressedResId(R.color.colorFloatingActionButton);
        groupName.setText(group.getName());
        accessScope.setText(group.getAccessScope());

        // View Event
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
