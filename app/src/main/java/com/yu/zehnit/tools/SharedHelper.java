package com.yu.zehnit.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class SharedHelper {
    private Context mcontext;
    public SharedHelper(){

    }
    public SharedHelper(Context mcontext){
        super();
        this.mcontext=mcontext;
    }
    public void save(Float pitchData,Float yawData,Float rollData){
        SharedPreferences sp=mcontext.getSharedPreferences("data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putFloat("pitch",pitchData);
        editor.putString(";"," ");
        editor.putFloat("yaw",yawData);
        editor.putString(";"," ");
        editor.putFloat("yaw",rollData);
        editor.putString("huanhang","\n");
        editor.commit();
        Toast.makeText(mcontext, "pitch,yaw,roll数据已存入！！", Toast.LENGTH_SHORT).show();
    }
}
