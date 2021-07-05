package com.yu.zehnit.tools;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.R;

import java.util.List;


public class ParamAdapter extends RecyclerView.Adapter<ParamAdapter.ViewHolder> {

    private List<Param> paramList;

    public ParamAdapter(List<Param> paramList) {
        this.paramList = paramList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.param_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LinearLayout linearLayout = holder.itemView.findViewById(R.id.param_linearLayout);
        // 获取第二个参数的布局
        FrameLayout frameLayout = holder.itemView.findViewById(R.id.second_param_frameLayout);

        Param param = paramList.get(position);
        holder.experimentName.setText(param.getExperimentName());
        // 设置第一个参数的布局
        holder.param1Name.setText(param.getParamName1());
        if (param.getExperimentName().equals("视追踪实验") || param.getExperimentName().equals("扫视实验")) {
            holder.param1Value.setText(param.getParamValue1() + "Hz");
            holder.param1SeekBar.setProgress((int) (param.getParamValue1() * 100));
        } else {
            holder.param1SeekBar.setMax(20);
            if (param.getExperimentName().equals("校准")) {
                holder.param1SeekBar.setProgressDrawable(holder.itemView.getResources().getDrawable(R.drawable.seekbar_style_white));
                holder.param1SeekBar.setProgress((int) param.getParamValue1() + 10);
                holder.param1Value.setText(Integer.toString((int) (param.getParamValue1())));
            } else {
//                holder.param1SeekBar.setMax(20);
                holder.param1SeekBar.setProgress((int) (param.getParamValue1() * 10));
                holder.param1Value.setText(Float.toString(param.getParamValue1()));
            }
        }
        // 给第一个参数的seekBar设置监听
        holder.param1SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (param.getExperimentName().equals("视追踪实验") || param.getExperimentName().equals("扫视实验")) {
                    holder.param1Value.setText((float) progress / 100 + "Hz");
                } else if (param.getExperimentName().equals("校准")){
                    holder.param1Value.setText(Integer.toString(progress - 10));
                } else {
                    holder.param1Value.setText(Float.toString((float) progress / 10));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 保存数据
                // 保存的都是实际值，注意换算关系
                if (param.getExperimentName().equals("视追踪实验")) {
                    SharedPreferencesUtils.setParam(holder.itemView.getContext(), "pursuit_frequency", (float) seekBar.getProgress()/100);
                } else if (param.getExperimentName().equals("扫视实验")) {
                    SharedPreferencesUtils.setParam(holder.itemView.getContext(), "saccade_frequency", (float) seekBar.getProgress()/100);
                } else if (param.getExperimentName().equals("校准")) {
                    SharedPreferencesUtils.setParam(holder.itemView.getContext(), "calibration", (float) seekBar.getProgress() - 10);
                } else {
                    SharedPreferencesUtils.setParam(holder.itemView.getContext(), "gain", (float) seekBar.getProgress() / 10);
                }
            }
        });



        if (param.getCategory() == 0) {
            // 从整体布局中删除第二个参数的布局
            linearLayout.removeView(frameLayout);
        } else {
            // 设置第二个参数的布局
            holder.param2Name.setText(param.getParamName2());
            holder.param2Value.setText(param.getParamValue2() + "");
            holder.param2SeekBar.setProgress((int) param.getParamValue2());
            holder.param2SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    holder.param2Value.setText(Integer.toString(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    SharedPreferencesUtils.setParam(holder.itemView.getContext(), "pursuit_amplitude", (float) seekBar.getProgress());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return paramList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView experimentName;
        TextView param1Name;
        TextView param1Value;
        SeekBar param1SeekBar;
        TextView param2Name;
        TextView param2Value;
        SeekBar param2SeekBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            experimentName = itemView.findViewById(R.id.experiment_name);
            param1Name = itemView.findViewById(R.id.param1_name);
            param1Value = itemView.findViewById(R.id.param1_value);
            param1SeekBar = itemView.findViewById(R.id.param1_seekBar);

            param2Name = itemView.findViewById(R.id.param2_name);
            param2Value = itemView.findViewById(R.id.param2_value);
            param2SeekBar = itemView.findViewById(R.id.param2_seekBar);
        }
    }

}
