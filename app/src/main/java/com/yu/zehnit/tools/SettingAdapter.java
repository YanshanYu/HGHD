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

import com.yu.zehnit.AboutActivity;
import com.yu.zehnit.ActivityController;
import com.yu.zehnit.LoginActivity;
import com.yu.zehnit.ParamSettingActivity;
import com.yu.zehnit.R;
import com.yu.zehnit.SettingActivity;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

    private List<Setting> settingList;
    private RecyclerView recyclerView;

    public SettingAdapter(List<Setting> settingList) {
        this.settingList = settingList;
    }

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recyclerView = parent.findViewById(R.id.recycle_view_setting);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_item, parent, false);
        SettingAdapter.ViewHolder holder = new SettingAdapter.ViewHolder(view);
        holder.settingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(v.getContext(), ParamSettingActivity.class);
                        v.getContext().startActivity(intent);
                        break;
                    case 1:
                        tipDialog("是否确定解绑设备？");
                        break;
                    case 2:
                        intent = new Intent(v.getContext(), AboutActivity.class);
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
        Setting setting = settingList.get(position);
        holder.iconImg.setImageResource(setting.getImgId());
        holder.text.setText(setting.getName());
    }

    @Override
    public int getItemCount() {
        return settingList.size();
    }

    private void tipDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());
        builder.setTitle("提示");
        builder.setMessage(msg);
//        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);            //点击对话框以外的区域是否让对话框消失

        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(recyclerView.getContext(), "设备已解绑", Toast.LENGTH_LONG).show();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        dialog.show();                              //显示对话框
    }
}
