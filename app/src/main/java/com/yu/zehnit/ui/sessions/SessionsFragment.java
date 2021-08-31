package com.yu.zehnit.ui.sessions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.yu.zehnit.ChartActivity;
import com.yu.zehnit.R;
import com.yu.zehnit.SessionActivity;
import com.yu.zehnit.SettingActivity;
import com.yu.zehnit.tools.OnRecycleViewItemClickListener;

import java.util.ArrayList;

public class SessionsFragment extends Fragment {
    private SessionsViewModel sessionsViewModel;
    private SessionsListAdapter mlaSessionListAdapter;
    private LinearLayout mbtDelete;
    private LinearLayout mbtNew;
    private LinearLayout mbtChart;
    private ImageView ivDelete;
    private TextView tvDelete;
    private ArrayList<Session> sessionList = new ArrayList<>();

    public SessionsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_sessions, container, false);
        ListView lvsessions = root.findViewById(R.id.lvsessions);
        mlaSessionListAdapter = new SessionsListAdapter(getActivity());
        lvsessions.setAdapter(mlaSessionListAdapter);
        lvsessions.setSelection(-1);
        lvsessions.setOnItemClickListener((adapterView, view, i, l) -> {
            SessionDataManager.setSelectedSessionIndex(i);
            mlaSessionListAdapter.notifyDataSetChanged();
            setButtonsEnable();
        });
        mbtNew = root.findViewById(R.id.btn_add);
        mbtDelete = root.findViewById(R.id.btn_delete);
        mbtChart = root.findViewById(R.id.btn_chart);
        ivDelete=root.findViewById(R.id.ivdelete);
        tvDelete=root.findViewById(R.id.tvdelete);

        mbtNew.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), SessionActivity.class);
            i.putExtra("NO", SessionDataManager.getSize());
            startActivityForResult(i, 15);
        });
        mbtDelete.setOnClickListener(view -> {
            SessionDataManager.removeSelectedPatient();
            setButtonsEnable();
            mlaSessionListAdapter.notifyDataSetChanged();
        });

        mbtChart.setOnClickListener(view -> {
            int amount= SessionDataManager.getSize();
            if(amount==0){
                Toast.makeText(getContext(), getResources().getString(R.string.alert_add_data), Toast.LENGTH_SHORT).show();
            }
            else{
                Intent i = new Intent(getActivity(), ChartActivity.class);
                startActivity(i);
            }

        });
        return root;
    }

    private void setButtonsEnable() {
        boolean selected = (SessionDataManager.getSelectedSessionIndex() >= 0);
        if(selected) {
            mbtDelete.setEnabled(true);
            ivDelete.setImageResource(R.drawable.icon_delete_click);
            tvDelete.setTextColor(getResources().getColor(R.color.colorDarkGray));
        }else{
            mbtDelete.setEnabled(false);
            ivDelete.setImageResource(R.drawable.icon_delete);
            tvDelete.setTextColor(getResources().getColor(R.color.colorBlueGray));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode) return;
        if (15 == requestCode) {
            Session session = (Session) data.getSerializableExtra(SessionActivity.SESSION);
            if (session.getTotalScore() > 0) {
                SessionDataManager.addSession(session);
                mlaSessionListAdapter.notifyDataSetChanged();
                setButtonsEnable();
            }
        }
    }

}