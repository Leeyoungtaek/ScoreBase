package com.scorebase.scorebase.Main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scorebase.scorebase.DataFormat.Account;
import com.scorebase.scorebase.DataFormat.Group;
import com.scorebase.scorebase.DataFormat.Member;
import com.scorebase.scorebase.R;

import java.util.ArrayList;
import java.util.List;

public class AddGroupActivity extends AppCompatActivity {

    // View
    private EditText inputGroupName;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnAdd;

    // View:RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // Data
    private List<Sport> sports;
    private Group groupData;
    private Member memberData;
    private String[] sportNames = new String[]{
            "baseball",
            "basketball",
            "boxing",
            "soccer",
            "tennisball"
    };
    private boolean[] sportStates = new boolean[]{
            false,
            false,
            false,
            false,
            false
    };

    // FireBase
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        // View Reference
        inputGroupName = (EditText) findViewById(R.id.edit_text_group_name);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioButton = (RadioButton) findViewById(R.id.button_public);
        btnAdd = (Button) findViewById(R.id.button_group_add);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // FireBase Reference
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // View:RecyclerView Set
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initializeData();
        mAdapter = new AddSportAdapter(sports, new SelectGameListener() {
            @Override
            public void selectGameEvent(int position) {
                sportStates[position] = !sportStates[position];
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        // View Set
        radioButton.setChecked(true);

        // View Event
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.child("accounts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = null;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Account account = child.getValue(Account.class);
                            if (account.getUid().equals(user.getUid())) {
                                name = account.getDisplayName();
                                break;
                            }
                        }
                        if (TextUtils.isEmpty(name)) { finish(); }

                        String groupName = inputGroupName.getText().toString().trim();

                        int id = radioGroup.getCheckedRadioButtonId();
                        RadioButton checkedRadioButton = (RadioButton) findViewById(id);
                        String accessScope = checkedRadioButton.getText().toString();

                        if (TextUtils.isEmpty(groupName) || !isChecked(sportStates)) {
                            Toast.makeText(getApplicationContext(), "입력을 완료해주세요!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        memberData = new Member(name, 5, 0, 0);
                        groupData = new Group(groupName, accessScope, sportStates, memberData);
                        databaseReference.child("groups").child(groupName).setValue(groupData);
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(AddGroupActivity.this, "그룹을 추가하지 못했습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    // RecyclerView Check
    private boolean isChecked(boolean[] states) {
        for (int i = 0; i < states.length; i++) {
            if (states[i] == true) {
                return true;
            }
        }
        return false;
    }

    private void initializeData() {
        sports = new ArrayList<>();
        sports.add(new Sport(R.drawable.baseball, sportNames[0]));
        sports.add(new Sport(R.drawable.basketball, sportNames[1]));
        sports.add(new Sport(R.drawable.boxing, sportNames[2]));
        sports.add(new Sport(R.drawable.soccer, sportNames[3]));
        sports.add(new Sport(R.drawable.tennisball, sportNames[4]));
    }

    class Sport {
        int imageId;
        String name;

        Sport(int imageId, String name) {
            this.imageId = imageId;
            this.name = name;
        }
    }
}
