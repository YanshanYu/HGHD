package com.yu.zehnit.tools;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CtrlAdapter extends RecyclerView.Adapter<CtrlAdapter.ViewHolder> {

    private static final String TAG = "dxx";
    private List<MyCtrl> ctrlList;
    private OnRecycleViewItemClickListener listener;

    private OnItemClickListener mVideoListener;
    private OnItemClickListener mSettingListener;
    private OnItemClickListener mStartListener;
    private View mView;

    public interface OnItemClickListener{
        void onItemClick(View v,int pos);
    }
    public void setVideoClickListener(OnItemClickListener listener){mVideoListener=listener;}
    public void setSettingClickListener(OnItemClickListener listener){mSettingListener=listener;}
    public void setStartClickListener(OnItemClickListener listener){mStartListener=listener;}

    public CtrlAdapter(List<MyCtrl> ctrlList) {
        this.ctrlList = ctrlList;
    }

    public void setListener(OnRecycleViewItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.control_item, parent, false);
        ViewHolder holder = new ViewHolder(view,mVideoListener,mSettingListener,mStartListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyCtrl ctrl = ctrlList.get(position);
        holder.ctrlImage.setImageResource(ctrl.getImgId());
        holder.ctrlName.setText(ctrl.getName());
        holder.ctrlName.setTextColor(ctrl.getTextColor());
        holder.ctrlName.setBackgroundResource(ctrl.getTextBackground());
        holder.ctrlVideo.setImageResource(ctrl.getVideoId());
        holder.ctrlVideoText.setTextColor(ctrl.getVideoColor());
        holder.ctrlSetting.setImageResource(ctrl.getSettingId());
        holder.ctrlSettingText.setTextColor(ctrl.getSettingColor());
        holder.ctrlPlay.setImageResource(ctrl.getSwitchImgId());
       /* holder.ctrlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, position);
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return ctrlList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        View ctrlView;
        ImageView ctrlImage;
        TextView ctrlName;
        ImageView ctrlVideo;
        TextView ctrlVideoText;
        ImageView ctrlSetting;
        TextView ctrlSettingText;
        ImageView ctrlPlay;

        public ViewHolder(@NonNull View itemView, OnItemClickListener videoListener, OnItemClickListener settingListener, OnItemClickListener startListener) {
            super(itemView);

            ctrlView = itemView;
            ctrlImage = itemView.findViewById(R.id.img_ctrl);
            ctrlName = itemView.findViewById(R.id.text_ctrl);
            ctrlVideo=itemView.findViewById(R.id.ibVideo);
            ctrlVideoText=itemView.findViewById(R.id.tvVideo);
            ctrlSetting=itemView.findViewById(R.id.ibSetting);
            ctrlSettingText=itemView.findViewById(R.id.tvSetting);
            ctrlPlay = itemView.findViewById(R.id.img_switch);
            ctrlVideo.setOnClickListener(view -> {
            if (videoListener != null) {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    videoListener.onItemClick(view, pos);
                }
            }
            });
            ctrlSetting.setOnClickListener(view -> {
                if(settingListener!=null){
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        settingListener.onItemClick(view,pos);
                    }
                }
            });
            ctrlPlay.setOnClickListener(view -> {
                if(startListener!=null){
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        startListener.onItemClick(view,pos);
                    }
                }
            });
        }
    }

}
