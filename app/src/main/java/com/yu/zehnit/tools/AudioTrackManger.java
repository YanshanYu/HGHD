package com.yu.zehnit.tools;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.provider.MediaStore;

public class AudioTrackManger {
    private Thread mRecordThread;
    AudioTrack mAudioTrack;
    private volatile static AudioTrackManger mInstance;
    public static AudioTrackManger getInstance(){
        if(mInstance==null){
            synchronized (AudioTrackManger.class){
                if(mInstance==null){
                    mInstance=new AudioTrackManger();
                }
            }
        }
        return mInstance;
    }
    public void playBeep(int duration,int frequency){
        destroyThread();
        final int sampleRate=8000;
        final int numSamples=duration*sampleRate;
        final double[] samples=new double[numSamples];
        final short[] buffer=new short[numSamples];
        for(int i=0;i<numSamples;i++){
            samples[i]=Math.sin(2*Math.PI*i/(sampleRate/frequency));
            buffer[i]=(short) (samples[i]*Short.MAX_VALUE);
        }
        mAudioTrack=new AudioTrack(AudioManager.STREAM_MUSIC,sampleRate, AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT,buffer.length, AudioTrack.MODE_STATIC);
        Runnable recordRunable=new Runnable() {
            @Override
            public void run() {
                mAudioTrack.write(buffer,0,buffer.length);
                mAudioTrack.play();
            }
        };
        mRecordThread=new Thread(recordRunable);
        mRecordThread.start();
    }
    private void destroyThread(){
        try{
            mAudioTrack.stop();
            mRecordThread.interrupt();
            mAudioTrack=null;
            mRecordThread=null;
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            mRecordThread=null;
            mAudioTrack=null;
        }
    }
}
