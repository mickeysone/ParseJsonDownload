package com.bluewindsolution.kare.parsejsondownload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * Created by Pruxasin on 4/3/2559.
 */
public class DisplayFileAdapter extends RecyclerView.Adapter<DisplayFileAdapter.DataObjectHolder> {
    private Context context;

    private ArrayList<DisplayFileObject> allPaths = new ArrayList<DisplayFileObject>();

    private static OnItemClickListener listener;

    public DisplayFileAdapter (ArrayList<DisplayFileObject> allPaths, Context context) {
        this.context = context;
        this.allPaths = allPaths;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comp_display_file, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
/*        Bitmap bitmap = BitmapFactory.decodeFile(imgPaths.get(position));
        holder.imvImg.setImageBitmap(bitmap);*/
        if (allPaths.get(position).getFileType().equals("IMAGE")) {
            Glide.with(context).load(allPaths.get(position).getFilePath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imvImg);
        } else if (allPaths.get(position).getFileType().equals("SOUND")) {
            holder.imvImg.setImageResource(R.mipmap.ic_sound_file);
        } else if (allPaths.get(position).getFileType().equals("VIDEO")) {
            holder.imvImg.setImageResource(R.mipmap.ic_video_file);
        }

        holder.txtFileName.setText(allPaths.get(position).getFileName());
        holder.txtFilePath.setText(allPaths.get(position).getFilePath());
    }

    @Override
    public int getItemCount() {
        return allPaths.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        ImageView imvImg;
        TextView txtFileName, txtFilePath;

        public DataObjectHolder(View itemView) {
            super(itemView);
            //imgMissionImage = (ImageView) itemView.findViewById(R.id.img_mission);
            txtFileName = (TextView) itemView.findViewById(R.id.display_file_txt_file_name);
            txtFilePath = (TextView) itemView.findViewById(R.id.display_file_txt_file_path);
            imvImg = (ImageView) itemView.findViewById(R.id.display_file_txt_file_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(txtFilePath.getText().toString());
                }
            });

        }
    }

    public interface OnItemClickListener {
        public void OnItemClick (String path);
    }

    public void SetOnItemClickListener (OnItemClickListener listener) {
        this.listener = listener;
    }
}
