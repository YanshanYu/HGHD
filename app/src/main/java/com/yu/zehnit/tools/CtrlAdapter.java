package com.yu.zehnit.tools;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CtrlAdapter extends RecyclerView.Adapter<CtrlAdapter.ViewHolder> {

    private static final String TAG = "dxx";
    private List<MyCtrl> ctrlList;
    private RecyclerView recyclerView;

    private boolean[] clickedStatus = new boolean[5]; // 记录点击状态

    static class ViewHolder extends RecyclerView.ViewHolder {
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

    public CtrlAdapter() {
    }

    public CtrlAdapter(List<MyCtrl> ctrlList) {
        this.ctrlList = ctrlList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recyclerView = parent.findViewById(R.id.recycle_view_ctrl);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.control_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        holder.ctrlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                MyCtrl ctrl = ctrlList.get(position);

                int index = -1;
                for (int i = 0; i < clickedStatus.length; i++) {
                    if (clickedStatus[i]) {
                        index = i;
                        break;
                    }
                }

                if (index == position) {
                    // 若该项是点击状态则还原
                    initCtrl(holder, position);
                    // 还原背景颜色
                    CardView cardView = holder.itemView.findViewById(R.id.card_view);
                    cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.white));
                    // 还原文字颜色
                    TextView textView = cardView.findViewById(R.id.text_ctrl);
                    textView.setTextColor(cardView.getResources().getColor(R.color.colorGray));
                    // 更新点击状态
                    clickedStatus[position] = false;
                } else if (index == -1){
                    // 没有item处于点击状态
                    // 记录点击状态
                    clickedStatus[position] = true;
                    // 替换开关图片
                    holder.switchImage.setImageResource(R.drawable.switch_on);
                    // 替换控制图片
                    holder.ctrlImage.setImageResource(ctrl.getImgId2());
                    // 替换背景颜色
                    CardView cardView = holder.itemView.findViewById(R.id.card_view);
                    cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorPrimary));
                    // 替换文字颜色
                    TextView textView = cardView.findViewById(R.id.text_ctrl);
                    textView.setTextColor(cardView.getResources().getColor(R.color.white));
                } else {
                    // 其他item处于点击状态
                    Toast.makeText(v.getContext(), "请关闭之前的控制开关，再进行新的操作", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        initCtrl(holder, position);
    }

    @Override
    public int getItemCount() {
        return ctrlList.size();
    }


    private void initCtrl(ViewHolder holder, int position) {
        MyCtrl ctrl = ctrlList.get(position);
        holder.ctrlImage.setImageResource(ctrl.getImgId());
        holder.ctrlName.setText(ctrl.getName());
        holder.switchImage.setImageResource(ctrl.getSwitchImgId());
    }


//    private void clear() {
//        for (int i = 0; i < isClicked.length; i++) {
//            if (isClicked[i]) {
//                ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
//                initCtrl(holder, i);
//                // 还原背景颜色
//                CardView cardView = holder.itemView.findViewById(R.id.card_view);
//                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.white));
//                // 还原文字颜色
//                TextView textView = cardView.findViewById(R.id.text_ctrl);
//                textView.setTextColor(cardView.getResources().getColor(R.color.colorGray));
//            }
//        }
//    }

}
