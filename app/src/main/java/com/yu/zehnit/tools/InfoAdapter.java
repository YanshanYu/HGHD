package com.yu.zehnit.tools;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.R;
import com.yu.zehnit.SettingActivity;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private List<Info> infoList;
    private OnRecycleViewItemClickListener listener;

    public InfoAdapter(List<Info> infoList) {
        this.infoList = infoList;
    }

    public void setListener(OnRecycleViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Info info = infoList.get(position);
        holder.iconImg.setImageResource(info.getImgId());
        holder.text.setText(info.getName());
        holder.infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View infoView;
        ImageView iconImg;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            infoView = itemView;
            iconImg = itemView.findViewById(R.id.img_icon);
            text = itemView.findViewById(R.id.text_show);
        }
    }

}
