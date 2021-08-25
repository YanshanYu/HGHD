package com.yu.zehnit.ui.sessions;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.yu.zehnit.R;

import org.w3c.dom.Text;

import java.text.DateFormat;

class SessionsListAdapter extends BaseAdapter {
    private final int mColSelected;
    private final Context mContext;
    SessionsListAdapter(Context context){
        super();
        mContext=context;
        mColSelected= ContextCompat.getColor(mContext, R.color.colorPrimary);
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
        TextView mtvNo;
        TextView mtvDate;
        TextView mtvScore;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.session_row,parent,false);
            holder=new ViewHolder();
            holder.mtvNo=convertView.findViewById(R.id.rno);
            holder.mtvDate=convertView.findViewById(R.id.rdate);
            holder.mtvScore=convertView.findViewById(R.id.rscore);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder) convertView.getTag();
        }
        Session p=SessionDataManager.getSession(position);
        if(null==p)return null;
        holder.mtvNo.setText(Integer.toString(position+1));
        DateFormat df= android.text.format.DateFormat.getDateFormat(mContext);
        holder.mtvDate.setText(df.format(p.getDate()));
        holder.mtvScore.setText(Integer.toString(p.getTotalScore()));
        convertView.setBackgroundColor(position==SessionDataManager.getSelectedSessionIndex()?mColSelected: Color.TRANSPARENT);

        return convertView;
    }
}
