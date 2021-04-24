package com.yu.zehnit.ui.control;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.R;
import com.yu.zehnit.tools.CtrlAdapter;
import com.yu.zehnit.tools.MyCtrl;
import com.yu.zehnit.tools.OnRecycleViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.wandersnail.widget.dialog.DefaultAlertDialog;
import cn.wandersnail.widget.textview.SwitchButton;

public class ControlFragment extends Fragment {

    private ControlViewModel controlViewModel;
    private static final String TAG = "dxx";

    private List<MyCtrl> ctrlList = new ArrayList<>();
    private boolean[] clickedStatus = new boolean[5]; // 记录点击状态
    private SwitchButton sbTarget;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controlViewModel =
                new ViewModelProvider(this).get(ControlViewModel.class);
        View root = inflater.inflate(R.layout.fragment_control, container, false);

        initCtrl();
        RecyclerView recyclerView = root.findViewById(R.id.recycle_view_ctrl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        CtrlAdapter adapter = new CtrlAdapter(ctrlList);
        adapter.setListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onClick(View v,int pos) {
                MyCtrl ctrl = ctrlList.get(pos);
                CardView cardView = v.findViewById(R.id.card_view);
                TextView textView = cardView.findViewById(R.id.text_ctrl);
                int index = -1; // 处于点击状态的item的索引
                for (int i = 0; i < clickedStatus.length; i++) {
                    if (clickedStatus[i]) {
                        index = i;
                        break;
                    }
                }
                if (index == pos) {
                    // 该项是已点击状态
                    // 设置未选中状态的图片
                    ctrlOff(ctrl, pos);
                    // 设置未选中状态的cardView颜色和字体颜色
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                    textView.setTextColor(getResources().getColor(R.color.colorGray));
                    // 更新点击状态
                    clickedStatus[pos] = false;
                } else if (index == -1){
                    // 没有item处于点击状态
                    // 记录点击状态
                    clickedStatus[pos] = true;
                    // 设置选中状态的图片
                    ctrlOn(ctrl, pos);

                    // 设置选中状态的cardView颜色和字体颜色
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    textView.setTextColor(getResources().getColor(R.color.white));

                } else {
                    // 其他item处于点击状态
                    Toast.makeText(getContext(), "请关闭之前的控制开关，再进行新的操作", Toast.LENGTH_SHORT).show();
                }
                // 监听数据改变
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);

        sbTarget = root.findViewById(R.id.switch_target);
        sbTarget.setBackColor(SwitchButton.generateBackColor(ContextCompat.getColor(getContext(), R.color.colorGreen)));
        sbTarget.setThumbColor(SwitchButton.getDefaultThumbColor());
        sbTarget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getContext(), "开关打开了", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "开关关闭了", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private void ctrlOff(MyCtrl ctrl, int pos){
        switch (pos) {
            case 0:
                ctrl.setImgId(R.drawable.gaze1);
                break;
            case 1:
                ctrl.setImgId(R.drawable.gaze2);
                break;
            case 2:
                ctrl.setImgId(R.drawable.gaze3);
                break;
            case 3:
                ctrl.setImgId(R.drawable.pursuit);
                break;
            case 4:
                ctrl.setImgId(R.drawable.saccade);
                break;
        }
        ctrl.setSwitchImgId(R.drawable.switch_off);
    }

    private void ctrlOn(MyCtrl ctrl, int pos) {
        switch (pos) {
            case 0:
                ctrl.setImgId(R.drawable.gaze1_2);
                break;
            case 1:
                ctrl.setImgId(R.drawable.gaze2_2);
                break;
            case 2:
                ctrl.setImgId(R.drawable.gaze3_2);
                break;
            case 3:
                ctrl.setImgId(R.drawable.pursuit_2);
                break;
            case 4:
                ctrl.setImgId(R.drawable.saccade_2);
                break;
        }
        ctrl.setSwitchImgId(R.drawable.switch_on);
    }

    private void initCtrl(){
        MyCtrl ctrlGaze1 = new MyCtrl(getString(R.string.gaze_holding1), R.drawable.gaze1, R.drawable.switch_off);
        ctrlList.add(ctrlGaze1);
        MyCtrl ctrlGaze2 = new MyCtrl(getString(R.string.gaze_holding2), R.drawable.gaze2, R.drawable.switch_off);
        ctrlList.add(ctrlGaze2);
        MyCtrl ctrlGaze3 = new MyCtrl(getString(R.string.gaze_holding3), R.drawable.gaze3, R.drawable.switch_off);
        ctrlList.add(ctrlGaze3);
        MyCtrl ctrlTrack = new MyCtrl(getString(R.string.smooth_pursuit), R.drawable.pursuit,  R.drawable.switch_off);
        ctrlList.add(ctrlTrack);
        MyCtrl ctrlSaccade = new MyCtrl(getString(R.string.saccades), R.drawable.saccade, R.drawable.switch_off);
        ctrlList.add(ctrlSaccade);
    }
}