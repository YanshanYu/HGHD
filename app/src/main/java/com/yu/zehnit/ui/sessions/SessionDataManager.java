package com.yu.zehnit.ui.sessions;

import android.content.Context;

import com.yu.zehnit.ui.sessions.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class SessionDataManager {
    private static final String SESSIONSFILE="Session_v2.srl";
    private static String mSessionFilePath;
    private static ArrayList<Session> mSessions;
    private static int mSelectedSessionIndex;
    static public void readSessions(Context context){
        File directory=new File(context.getFilesDir().getAbsolutePath());
        mSelectedSessionIndex=-1;
        mSessionFilePath=directory+File.separator+SESSIONSFILE;
        mSessions=new ArrayList<>();
        try{
            ObjectInputStream inputStream=new ObjectInputStream(new FileInputStream(mSessionFilePath));
            mSessions=(ArrayList<Session>) inputStream.readObject();
            inputStream.close();
        }catch (StreamCorruptedException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        for(int i=0;i<mSessions.size();i++){
            if(null!=mSessions.get(i))continue;
            mSessions.remove(i);
            break;
        }
    }
    static void storeSessions(){

        try{
            ObjectOutput output=new ObjectOutputStream(new FileOutputStream(mSessionFilePath));
            output.writeObject(mSessions);
            output.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    static void addSession(Session s){
        if(null==s)return;
        mSessions.add(0,s);
      //  mSelectedSessionIndex=mSessions.size()-1;
        storeSessions();
    }
    static void updateSession(int index,Session s){
        if(null==s)return;
        if(index>mSessions.size())return;
        mSessions.set(index,s);
        storeSessions();

    }
    static void removeSession(int index){
        if(index<0||index>=mSessions.size())return;
        mSessions.remove(index);
     //   mSelectedSessionIndex=-1;
        storeSessions();
    }
    static void removeSelectedPatient(){
        removeSession(mSelectedSessionIndex);
    }
    static public int getSize(){
        return mSessions.size();
    }
    static public Session getSession(int index){
        if(index<0||index>mSessions.size())return null;
        return mSessions.get(index);
    }
    static int getSelectedSessionIndex(){return mSelectedSessionIndex;}

    static void setSelectedSessionIndex(int index){
        if(index>=mSessions.size())return;
        mSelectedSessionIndex=index;
    }
    static public ArrayList<Session>getSessions(){
        return mSessions;
    }
}
