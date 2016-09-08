package com.scorebase.scorebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee young teak on 2016-08-25.
 */
public class GroupListFragment extends Fragment {

    // Const
    public final static int ADD_GROUP_REQUEST = 1001;

    // RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Group> groups;

    // Views
    private FloatingActionButton floatingActionButton;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public GroupListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        // Firebase Reference
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // View Reference
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_groups);

        // View Setting
        floatingActionButton.setColorNormalResId(R.color.colorAccent);
        floatingActionButton.setColorPressedResId(R.color.colorFloatingActionButton);

        // RecyclerView Setting
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initializeData();


        // Go to AddGroupActivity
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddGroupActivity.class);
                startActivityForResult(intent, ADD_GROUP_REQUEST);
            }
        });
        return view;
    }

    // Groups init
    private void initializeData() {
        groups = new ArrayList<Group>();
        databaseReference.child("Group").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Group group = child.getValue(Group.class);
                    for (int i = 0; i < group.Members.size(); i++) {
                        if (group.Members.get(i).getName().equals("이영택")) {
                            groups.add(group);
                        }
                    }
                }
                mAdapter = new GroupListAdapter(getContext(), groups);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_GROUP_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + ((MainActivity) getActivity()).viewPager.getId() + ":" + 0);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        }
    }
}
