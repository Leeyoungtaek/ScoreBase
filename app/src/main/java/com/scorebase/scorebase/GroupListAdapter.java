package com.scorebase.scorebase;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DSM_055 on 2016-09-08.
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {

    private List<Group> groups;

    GroupListAdapter(List<Group> groups){
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
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{

        private TextView groupName;
        private TextView accessScope;
        private CardView cardView;

        public GroupViewHolder(View itemView) {
            super(itemView);
            groupName = (TextView)itemView.findViewById(R.id.text_view_group_name);
            accessScope = (TextView)itemView.findViewById(R.id.text_view_access_scope);
        }
    }
}
