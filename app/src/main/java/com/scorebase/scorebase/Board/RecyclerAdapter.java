package com.scorebase.scorebase.Board;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scorebase.scorebase.Board.Listener.GamesBoardListener;
import com.scorebase.scorebase.R;

import java.util.List;

/**
 * Created by DSM_055 on 2016-07-21.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context context;
    private List<GamesItem> items;
    private GamesBoard gamesBoard;

    public RecyclerAdapter(Context context, List<GamesItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.games_card, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GamesItem item = items.get(position);
        Drawable drawable = context.getResources().getDrawable(item.getImage());
        holder.image.setBackground(drawable);
        holder.title.setText(item.getTitle());
        gamesBoard = new GamesBoard(holder.cardView, new GamesBoardListener() {
            @Override
            public void onDownScrollEvent() {
                Intent intent = new Intent(context, ScoreBoardActivity.class);
                intent.putExtra("sport", item.getTitle());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.games_image);
            title = (TextView)itemView.findViewById(R.id.games_text);
            cardView = (CardView)itemView.findViewById(R.id.cardview);
        }
    }
}
