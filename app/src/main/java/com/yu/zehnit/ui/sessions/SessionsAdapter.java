package com.yu.zehnit.ui.sessions;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.R;
import com.yu.zehnit.tools.Info;
import com.yu.zehnit.tools.OnRecycleViewItemClickListener;

import java.text.DateFormat;
import java.util.List;


public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.ViewHolder> {
    private List<Session> sessionList;

    private OnRecycleViewItemClickListener listener;

    public SessionsAdapter(List<Session> sessionList) {
        this.sessionList = sessionList;
    }

    public void setListener(OnRecycleViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SessionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.session_row,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SessionsAdapter.ViewHolder holder, int position) {
        Session session = sessionList.get(position);
        holder.noText.setText(session.getNo());
        holder.dateText.setText(session.getDate().toString());
        holder.scoreText.setText(session.getTotalScore());
        holder.sessionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        View sessionView;
        TextView noText;
        TextView dateText;
        TextView scoreText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionView = itemView;
            noText = itemView.findViewById(R.id.rno);
            dateText = itemView.findViewById(R.id.rdate);
            scoreText=itemView.findViewById(R.id.rscore);
        }
    }

  /*
    private final int mColSelected;
    private final Context mContext;
  public SessionsAdapter(Context context){
        super();
        mContext=context;
        mColSelected= ContextCompat.getColor(mContext,R.color.colorPrimary);
    }

    @Override
    public int getCount() {
        return SessionDataManager.getSize();
    }

    @Override
    public Object getItem(int position) {
        return SessionDataManager.getSession(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private static class ViewHolder{
        TextView mNo;
        TextView mDate;
        TextView mScores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            convertView=LayoutInflater.from(mContext).inflate(R.layout.session_row,parent,false);
            holder=new ViewHolder();
            holder.mNo=convertView.findViewById(R.id.rno);
            holder.mDate=convertView.findViewById(R.id.rdate);
            holder.mScores=convertView.findViewById(R.id.rscore);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        Session s=SessionDataManager.getSession(position);
        if(null==s) return null;
        holder.mNo.setText(Integer.toString(position+1));
        DateFormat df = android.text.format.DateFormat.getDateFormat(mContext);
        holder.mDate.setText(df.format(s.getDate()));
        holder.mScores.setText(Integer.toString(s.getTotalScore()));
        convertView.setBackgroundColor(position==SessionDataManager.getSelectedSessionIndex()?mColSelected: Color.TRANSPARENT);

        return convertView;
    }
*/

}
