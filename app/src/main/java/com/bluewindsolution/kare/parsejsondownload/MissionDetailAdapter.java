package com.bluewindsolution.kare.parsejsondownload;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by Pruxasin on 23/2/2559.
 */
public class MissionDetailAdapter extends RecyclerView.Adapter<MissionDetailAdapter.DataObjectHolder> {

    ArrayList<String> detailDataset;
    Context context;

    public MissionDetailAdapter(ArrayList<String> detailDataset, Context context) {
        this.detailDataset = detailDataset;
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_detail_images_item, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Glide.with(context).load("http://genetic-plus.org/presite/kare/" + detailDataset.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgExGameDetail);
    }

    @Override
    public int getItemCount() {
        return detailDataset.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        //ตัวแทนของ Layout mission_item.xml
        ImageView imgExGameDetail;
        public DataObjectHolder(View itemView) {
            super(itemView);
            imgExGameDetail = (ImageView) itemView.findViewById(R.id.imv_game_detail);
        }
    }
}
