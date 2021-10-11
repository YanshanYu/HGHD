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


import com.yu.zehnit.ChartActivity;
import com.yu.zehnit.R;
import com.yu.zehnit.SessionActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class SessionsFragment extends Fragment {
    private SessionsViewModel sessionsViewModel;
    private SessionsListAdapter mlaSessionListAdapter;
    private LinearLayout mbtDelete;
    private LinearLayout mbtNew;
    private LinearLayout mbtReperform;
    private ImageButton mbtChart;
    private ImageView ivDelete,ivReperform;
    private TextView tvDelete,tvReperform;
    private ArrayList<Session> sessionList = new ArrayList<>();
    private Boolean mReperform=false;

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
        mlaSessionListAdapter = new SessionsListAdapter(getActivity(),SessionDataManager.getSessions());
        Collections.sort(SessionDataManager.getSessions(), new Comparator<Session>() {
            @Override
            public int compare(Session s1, Session s2) {
                Date date1=s1.getDate();
                Date date2=s2.getDate();
                if(date1.before(date2)) return 1;
                return -1;
            }
        });
        lvsessions.setAdapter(mlaSessionListAdapter);
        lvsessions.setSelection(-1);
        SessionDataManager.setSelectedSessionIndex(-1);
        lvsessions.setOnItemClickListener((adapterView, view, i, l) -> {
            SessionDataManager.setSelectedSessionIndex(i);
            mlaSessionListAdapter.notifyDataSetChanged();
            setButtonsEnable();
        });
        mbtNew = root.findViewById(R.id.btn_add);
        mbtDelete = root.findViewById(R.id.btn_delete);
        mbtReperform = root.findViewById(R.id.btn_reperform);
        mbtChart=root.findViewById(R.id.btn_chart);
        ivDelete=root.findViewById(R.id.ivdelete);
        tvDelete=root.findViewById(R.id.tvdelete);
        ivReperform=root.findViewById(R.id.ivreperform);
        tvReperform=root.findViewById(R.id.tvreperform);

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
        mbtReperform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReperform=true;
                Intent i = new Intent(getActivity(), SessionActivity.class);
                i.putExtra("NO", SessionDataManager.getSelectedSessionIndex());
                startActivityForResult(i, 15);
            }
        });

        return root;
    }

    private void setButtonsEnable() {
        boolean selected = (SessionDataManager.getSelectedSessionIndex() >= 0);
        if(selected) {
            mbtDelete.setEnabled(true);
            mbtReperform.setEnabled(true);
            ivDelete.setImageResource(R.drawable.icon_delete_click);
            tvDelete.setTextColor(getResources().getColor(R.color.colorDarkGray));
            ivReperform.setImageResource(R.drawable.icon_reperform_checked);
            tvReperform.setTextColor(getResources().getColor(R.color.colorDarkGray));
        }else{
            mbtDelete.setEnabled(false);
            mbtReperform.setEnabled(false);
            ivDelete.setImageResource(R.drawable.icon_delete);
            tvDelete.setTextColor(getResources().getColor(R.color.colorBlueGray));
            ivReperform.setImageResource(R.drawable.icon_reperform_uncheck);
            tvReperform.setTextColor(getResources().getColor(R.color.colorBlueGray));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode) return;
        if (15 == requestCode) {
            Session session = (Session) data.getSerializableExtra(SessionActivity.SESSION);
            if(session.getTotalScore()>0){
                if(mReperform) {
                    SessionDataManager.updateSession(SessionDataManager.getSelectedSessionIndex(), session);
                    mlaSessionListAdapter.notifyDataSetChanged();
                }else{
                    SessionDataManager.addSession(session);
                    mlaSessionListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

}