package com.scorebase.scorebase.Main.Fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scorebase.scorebase.DataFormat.Group;
import com.scorebase.scorebase.Main.Group.GroupActivity;
import com.scorebase.scorebase.R;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DSM_055 on 2016-09-08.
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {

    private List<Group> groups;
    private Context context;

    GroupListAdapter(Context context, List<Group> groups){
        this.context = context;
        this.groups = groups;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_groups, parent, false);
        GroupViewHolder groupViewHolder = new GroupViewHolder(v);
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        holder.groupName.setText(groups.get(position).getName());
        holder.accessScope.setText(groups.get(position).getAccessScope());
        final Group group = groups.get(position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GroupActivity.class);
                intent.putExtra("group", (Serializable) group);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{

        private TextView groupName;
        private TextView accessScope;
        private CardView cardView;

        public GroupViewHolder(View itemView) {
            super(itemView);
            groupName = (TextView)itemView.findViewById(R.id.text_view_group_name);
            accessScope = (TextView)itemView.findViewById(R.id.text_view_access_scope);
            cardView = (CardView)itemView.findViewById(R.id.card_view_group_card);
        }
    }
}
