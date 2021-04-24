package com.yu.zehnit.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.AddEquipmentActivity;
import com.yu.zehnit.R;
import com.yu.zehnit.ui.control.ControlFragment;
import com.yu.zehnit.ui.home.HomeFragment;

import java.util.List;

public class EqpAdapter extends RecyclerView.Adapter<EqpAdapter.ViewHolder> {
    private List<Equipment> eqpList;
    private OnRecycleViewItemClickListener listener;

    public EqpAdapter(List<Equipment> eqpList) {
        this.eqpList = eqpList;
    }

    public void setListener(OnRecycleViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eqp_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
//        holder.eqpView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Equipment eqp = eqpList.get(position);
//                if (eqp.getCategory() == 1) {
//                    Intent intent = new Intent(v.getContext(), AddEquipmentActivity.class);
//                    v.getContext().startActivity(intent);
//                } else {
//                    // 跳转到fragment_control
//                    Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_navigation_control);
//                }
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Equipment eqp = eqpList.get(position);
        if (eqp.getCategory() == 0) {
            holder.eqpImg.setImageResource(eqp.getImgId());
            holder.goImg.setImageResource(R.drawable.go);
            holder.eqpName.setText(eqp.getName());
            holder.text.setText(eqp.getText());
        } else {
            holder.addImg.setImageResource(R.drawable.ic_add);
            holder.text.setText(R.string.add_new_device);
        }
        holder.eqpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eqpList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View eqpView;

        ImageView eqpImg;
        ImageView goImg;
        ImageView addImg;

        TextView eqpName;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eqpView = itemView;
            eqpImg = itemView.findViewById(R.id.img_eqp);
            goImg = itemView.findViewById(R.id.img_go_ctrl);
            addImg = itemView.findViewById(R.id.img_add);
            eqpName = itemView.findViewById(R.id.img_name);
            text = itemView.findViewById(R.id.msg);
        }
    }

}
