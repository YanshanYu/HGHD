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

    public InfoAdapter(List<Info> infoList) {
        this.infoList = infoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Intent intent;
                switch (position) {
                    case 0:
//                        intent = new Intent(v.getContext(), SettingActivity.class);
//                        break;
                    case 1:
//                        intent = new Intent(v.getContext(), SettingActivity.class);
//                        break;
                    case 2:
//                        intent = new Intent(v.getContext(), SettingActivity.class);
                        break;
                    case 3:
                        intent = new Intent(v.getContext(), SettingActivity.class);
                        v.getContext().startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Info info = infoList.get(position);
        holder.iconImg.setImageResource(info.getImgId());
        holder.text.setText(info.getName());
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }


}
