package com.scorebase.scorebase.Main;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scorebase.scorebase.R;

import java.util.List;

/**
 * Created by DSM_055 on 2016-08-31.
 */
public class AddSportAdapter extends RecyclerView.Adapter<AddSportAdapter.SportViewHolder> {

    private List<AddGroupActivity.Sport> sports;
    private SelectGameListener listener;

    AddSportAdapter(List<AddGroupActivity.Sport> sports, SelectGameListener listener) {
        this.sports = sports;
        this.listener = listener;
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
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.selectGameEvent(position);
                if (holder.state == false) {
                    holder.state = true;
                    holder.cardView.setCardBackgroundColor(Color.GRAY);
                } else {
                    holder.state = false;
                    holder.cardView.setCardBackgroundColor(Color.WHITE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    public static class SportViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView sportImage;
        private boolean state;

        SportViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            sportImage = (ImageView) itemView.findViewById(R.id.add_sport_image);
            state = false;
        }

        public boolean getState() {
            return state;
        }
    }

}
