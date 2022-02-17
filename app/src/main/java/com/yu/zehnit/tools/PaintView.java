package com.yu.zehnit.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import android.os.Handler;
import android.view.WindowManager;

import java.util.jar.Attributes;

public class PaintView extends View {
    private int width;
    private int height;
    private float scale;
    private float px1=500;
    private float py1=500;
    private float px=300;
    private float py=300;
    private float radio=300;
    private double i=0;
    private boolean isStart = true;
    public float getPx(){return px;}
    public float getPy(){return py;}

    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    if(isStart){
                        i+=1;
                        double y=(Math.PI*i)/180;
                        if(i<=360){
                            px=radio-radio*(float)Math.sin(y);
                            py=radio*(float)Math.cos(y);
                            if(i==360) i=0;
                        }
                        invalidate();
                        handler.sendEmptyMessageDelayed(0,10);
                    }
                    break;
                default:
                    break;
            }
        };
    };
    public PaintView(Context context) {
        super(context);
        getWindow(context);
    }
    public PaintView(Context context, AttributeSet attrs){
        super(context, attrs);
        getWindow(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(px1,py1,radio,paint);
        Paint paint2=new Paint();
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(Color.WHITE);
        canvas.drawCircle(px+200,py+600,10,paint2);
    }

    private void getWindow(Context context){
        WindowManager windowManager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width=windowManager.getDefaultDisplay().getWidth();
        height=windowManager.getDefaultDisplay().getHeight();
        scale=context.getResources().getDisplayMetrics().density;
    }
    public void start(){
        isStart=true;
        handler.sendEmptyMessageDelayed(0,100);
    }
    public void stop(){
        isStart=false;
    }
}
