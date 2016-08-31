package com.scorebase.scorebase;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by DSM_055 on 2016-08-31.
 */
public class AddSportAdapter extends RecyclerView.Adapter<AddSportAdapter.SportViewHolder> {

    List<AddGroupActivity.Sport> sports;

    AddSportAdapter(List<AddGroupActivity.Sport> sports){
        this.sports = sports;
    }

    @Override
    public SportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_sport, parent, false);
        SportViewHolder sportViewHolder = new SportViewHolder(v);
        return sportViewHolder;
    }

    @Override
    public void onBindViewHolder(final SportViewHolder holder, final int position) {
        holder.sportImage.setImageResource(sports.get(position).imageId);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    public static class SportViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView sportImage;

        SportViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            sportImage = (ImageView)itemView.findViewById(R.id.add_sport_image);
        }
    }

}
