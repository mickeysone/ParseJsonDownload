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

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Pruxasin on 16/2/2559.
 */
public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.DataObjectHolder>{

    Context context;

    ArrayList<MissionData> missionDataset;

    private static OnItemClickListener listener;

    public MissionAdapter(ArrayList<MissionData> missionDataset, Context context) {
        this.missionDataset = missionDataset;
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //นำ viewHolder แต่ละอันมาใส่ position ให้มัน
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mission_item, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.txtMissionId.setText(missionDataset.get(position).id);
        holder.txtMissionId.setVisibility(View.GONE);
        holder.txtMissionName.setText(missionDataset.get(position).name);
        Glide.with(context).load("http://genetic-plus.org/presite/kare/" + missionDataset.get(position).img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgMissionImage);
    }

    @Override
    public int getItemCount()  {
        //รีเทิร์นค่าจำนวนไอเท็ม
        return missionDataset.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        //ตัวแทนของ Layout mission_item.xml

        ImageView imgMissionImage;
        TextView txtMissionName, txtMissionId;

        public DataObjectHolder(View itemView) {
            super(itemView);
            //imgMissionImage = (ImageView) itemView.findViewById(R.id.img_mission);
            txtMissionId = (TextView) itemView.findViewById(R.id.txt_mission_id);
            txtMissionName = (TextView) itemView.findViewById(R.id.txt_mission_name);
            imgMissionImage = (ImageView) itemView.findViewById(R.id.ivImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.OnItemClick(txtMissionId.getText().toString(), txtMissionName.getText().toString(), v);
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        public void OnItemClick (String appId, String appName, View view);
    }

    public void SetOnItemClickListener (OnItemClickListener listener) {
        this.listener = listener;
    }

}
