package com.yu.zehnit.tools;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private View mView;

    public interface OnItemClickListener{
        void onItemClick(View v,int pos);
    }

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
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyCtrl ctrl = ctrlList.get(position);
        holder.ctrlImage.setImageResource(ctrl.getImgId());
        holder.ctrlName.setText(ctrl.getName());
        holder.ctrlName.setTextColor(ctrl.getTextColor());
        holder.switchImage.setImageResource(ctrl.getSwitchImgId());
        holder.ctrlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ctrlList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        View ctrlView;
        ImageView ctrlImage;
        TextView ctrlName;
        ImageView switchImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ctrlView = itemView;
            ctrlImage = itemView.findViewById(R.id.img_ctrl);
            ctrlName = itemView.findViewById(R.id.text_ctrl);
            switchImage = itemView.findViewById(R.id.img_switch);
        }
    }

}
