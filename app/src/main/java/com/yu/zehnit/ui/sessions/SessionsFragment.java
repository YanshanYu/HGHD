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

      /*  sessionsViewModel =
                new ViewModelProvider(this).get(SessionsViewModel.class);
        RecyclerView recyclerView = root.findViewById(R.id.recycle_view_sessions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mlaSessionAdapter=new SessionsAdapter(sessionList);
        sessionDataManager=new SessionDataManager();
        mlaSessionAdapter.setListener(new OnRecycleViewItemClickListener(){
            @Override
            public void onClick(int pos) {
                SessionDataManager.setSelectedSessionIndex(pos);
                mlaSessionAdapter.notifyDataSetChanged();
                setButtonEnable();
            }
        });
        recyclerView.setAdapter(mlaSessionAdapter);

        deleteBtn=root.findViewById(R.id.btdelete);
        deleteBtn.setBackgroundResource(R.drawable.icon_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionDataManager.removeSelectedPatient();
                mlaSessionAdapter.notifyDataSetChanged();
            }
        });
        chartBtn=root.findViewById(R.id.btchart);
        chartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),ChartActivity.class);
                startActivity(i);
            }
        });

        addBtn=root.findViewById(R.id.btadd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SessionActivity.class);
                intent.putExtra("NO",sessionDataManager.getSize());
                startActivityForResult(intent,15);
            }
        });


        return root;
    }
    private void setButtonEnable(){
        boolean selected=(SessionDataManager.getSelectedSessionIndex()>=0);
        mbtDelete.setEnabled(selected);
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(Activity.RESULT_OK!=resultCode)return;
        if(15==requestCode){
            Session session=(Session) data.getSerializableExtra(SessionActivity.SESSION);
            if(session.getTotalScore()>0){
                SessionDataManager.addSession(session);
                mlaSessionAdapter.notifyDataSetChanged();
            }
        }
    }
}*/

        ListView lvsessions = root.findViewById(R.id.lvsessions);
        mlaSessionListAdapter = new SessionsListAdapter(getActivity());
        lvsessions.setAdapter(mlaSessionListAdapter);
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
            mlaSessionListAdapter.notifyDataSetChanged();
        });

        mbtChart.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), ChartActivity.class);
            startActivity(i);
        });
        return root;
    }

    private void setButtonsEnable() {
        boolean selected = (SessionDataManager.getSelectedSessionIndex() >= 0);
        mbtDelete.setEnabled(selected);
        ivDelete.setImageResource(R.drawable.icon_delete_click);
        tvDelete.setTextColor(getResources().getColor(R.color.colorDarkGray));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode) return;
        if (15 == requestCode) {
            Session session = (Session) data.getSerializableExtra(SessionActivity.SESSION);
            if (session.getTotalScore() > 0) {
                SessionDataManager.addSession(session);
                mlaSessionListAdapter.notifyDataSetChanged();
            }
        }
    }

}