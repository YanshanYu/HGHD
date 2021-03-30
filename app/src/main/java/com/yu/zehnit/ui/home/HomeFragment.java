package com.yu.zehnit.ui.home;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.yu.zehnit.AddEquipmentActivity;
import com.yu.zehnit.R;
import com.yu.zehnit.tools.CardUtils;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button addEquipBtn;
    private CardView cardView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        cardView = root.findViewById(R.id.card_view_home);
//        CardUtils.setCardShadowColor(cardView, getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.white));


        addEquipBtn = root.findViewById(R.id.button_add_experiment);
        addEquipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 切换到添加设备页面
                Intent intent = new Intent(getActivity(), AddEquipmentActivity.class);
                startActivity(intent);

            }
        });

        return root;
    }
}