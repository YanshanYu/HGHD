package com.yu.zehnit.tools;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.R;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

    private List<Setting> settingList;
    private OnRecycleViewItemClickListener listener;

    public SettingAdapter(List<Setting> settingList) {
        this.settingList = settingList;
    }

    /**
     * 设置监听
     * @param listener
     */
    public void setListener(OnRecycleViewItemClickListener listener) {
        this.listener = listener;
    }


    /**
     * 创建ViewHolder实例
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载setting_item布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_item, parent, false);
        SettingAdapter.ViewHolder holder = new SettingAdapter.ViewHolder(view);
        return holder;
    }

    /**
     * 对ViewHolder实例赋值
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Setting setting = settingList.get(position);
        holder.iconImg.setImageResource(setting.getImgId());
        holder.text.setText(setting.getName());
        holder.settingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return settingList.size();
    }

    /**
     * 静态内部类，用于获得布局中的控件实例
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        View settingView;
        ImageView iconImg;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            settingView = itemView;
            iconImg = itemView.findViewById(R.id.img_ic);
            text = itemView.findViewById(R.id.text_info);
        }
    }

}
