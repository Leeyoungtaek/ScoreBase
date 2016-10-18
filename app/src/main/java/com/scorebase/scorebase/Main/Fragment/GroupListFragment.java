package com.scorebase.scorebase.Main.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.scorebase.scorebase.DataFormat.Account;
import com.scorebase.scorebase.DataFormat.Member;
import com.scorebase.scorebase.Main.AddGroupActivity;
import com.scorebase.scorebase.DataFormat.Group;
import com.scorebase.scorebase.Main.MainActivity;
import com.scorebase.scorebase.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee young teak on 2016-08-25.
 */
public class GroupListFragment extends Fragment {

    // Const
    public final static int ADD_GROUP_REQUEST = 1001;

    // View
    private FloatingActionButton btnAddGroup;

    // View:RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Group> groups;

    // FireBase
    private FirebaseAuth auth;
    private FirebaseUser user;
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

        // FireBase Reference
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // View Reference
        btnAddGroup = (FloatingActionButton) view.findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_groups);

        // View Set
        btnAddGroup.setColorNormalResId(R.color.colorAccent);
        btnAddGroup.setColorPressedResId(R.color.colorFloatingActionButton);

        // View:RecyclerView Set
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initializeData();

        // View Event
        btnAddGroup.setOnClickListener(new View.OnClickListener() {
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
        databaseReference.child("accounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = null;
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Account account = child.getValue(Account.class);
                    if (account.getUid().equals(user.getUid())) {
                        name = account.getDisplayName();
                        break;
                    }
                }
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getContext(), "그룹을 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }

                final String finalName = name;
                databaseReference.child("groups").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()){
                            Group group = child.getValue(Group.class);
                            for(int i = 0; i<group.members.size(); i++){
                                Member member = group.members.get(i);
                                if(member.getName()!=null && member.getName().equals(finalName)){
                                    groups.add(group);
                                    break;
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_GROUP_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                Fragment frg = frg = getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + ((MainActivity) getActivity()).viewPager.getId() + ":" + 0);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        }
    }
}
