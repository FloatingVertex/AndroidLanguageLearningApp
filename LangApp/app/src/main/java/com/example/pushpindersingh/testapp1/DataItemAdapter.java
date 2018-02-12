package com.example.pushpindersingh.testapp1;

/**
 * Created by Pushpinder Singh on 7/17/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pushpindersingh.testapp1.models.JWord;

import java.util.List;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder> {

    private JWord[] mItems;
    private Context mContext;

    public DataItemAdapter(Context context, JWord[] items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public DataItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DataItemAdapter.ViewHolder holder, int position) {
        JWord item = mItems[position];

        holder.tvName.setText(item.romaji);
        float known = (float)item.getKnown();
        holder.progress.setMax(200);
        holder.progress.setProgress((int)(known * 100));
        //InputStream inputStream = mContext.getAssets().open(imageFile);
        //Drawable d = Drawable.createFromStream(known, null);
        //holder.progress.setImageDrawable(d);
    }

    @Override
    public int getItemCount() {
        return mItems.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ProgressBar progress;
        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.textView);
            progress = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
}