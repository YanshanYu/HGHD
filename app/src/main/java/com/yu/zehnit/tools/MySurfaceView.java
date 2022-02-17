package com.yu.zehnit.tools;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MySurfaceView extends GLSurfaceView {
    private SceneRenderer mRenderer;
    private float mPreviousY;
    private float mPreviousX;
    private float mPreviousZ;
    public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3);
        mRenderer =new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        mPreviousX=Data.getXdu();
        mPreviousY=Data.getYdu();
        mPreviousZ=Data.getZdu();
        new Thread(new Runnable() {
            float fx,fy,fz;
            @Override
            public void run() {
                while (true){
                    try{
                        Thread.sleep(10);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                  //  fx=Data.getXdu();
                    fy=Data.getYdu();
                 //   fz=Data.getZdu();
                   // mRenderer.xAngle+=(fx-mPreviousX);
                    mRenderer.yAngle+=fy-mPreviousY;
                    mRenderer.xAngle=0;
                    mRenderer.zAngle=0;
                   // mRenderer.zAngle+=fz-mPreviousZ;
                  //  Data.setXzero(mRenderer.xAngle);
                 //   Data.setYzero(mRenderer.yAngle);
                 //   Data.setZzero(mRenderer.zAngle);
                   // mPreviousX=fx;
                    mPreviousY=fy;
                  //  mPreviousZ=fz;
                    requestRender();
                }
            }
        }).start();

    }
    public void Zero(){
       // mRenderer.xAngle=0;
        mRenderer.yAngle=0;
     //   mRenderer.zAngle=0;
    //    Data.setXzero(mRenderer.xAngle);
        Data.setYzero(mRenderer.yAngle);
    //    Data.setZzero(mRenderer.zAngle);
        requestRender();
    }

    private class SceneRenderer implements GLSurfaceView.Renderer{
        float yAngle,xAngle,zAngle;
        LoadedObjectVertexNormal lovo;
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            MatrixState.setInitStack();
            MatrixState.setLightLocation(0,10,0);
            lovo=LoadUtil.loadFromFile("head2.obj",MySurfaceView.this.getResources(),MySurfaceView.this);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES30.glViewport(0,0,width,height);
            float ratio=(float) width/height;
            MatrixState.setProjectFrustum(-ratio,ratio,-1,1,2,100);
         //   MatrixState.setCamera(0f,0f,0f,0f,0f,-1f,0f,1f,0.0f);
            MatrixState.setCamera(1.5f,0.5f,1f,0f,0f,-1f,0f,1.0f,0.0f);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            MatrixState.pushMatrix();
            MatrixState.translate(0,0,-14f);
            MatrixState.rotate(yAngle,0,1,0);
            MatrixState.rotate(0,1,0,0);
            MatrixState.rotate(0,0,0,1);

            if(lovo!=null){
                lovo.drawSelf();
            }
            MatrixState.popMatrix();
        }
    }
}
