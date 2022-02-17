package com.yu.zehnit.tools;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class LoadedObjectVertexNormal {
    int mProgram;
    int muMVPMatrixHandle;
    int muMMatrixHandle;
    int maPositionHandle;
    int maNormalHandle;
    int maLightLocationHandle;
    int maCameraHandle;
    String mVertexShader;
    String mFragmentShader;

    FloatBuffer mVertexBuffer;
    FloatBuffer mNormalBuffer;
    int vCount=0;

    public LoadedObjectVertexNormal(MySurfaceView mv, float[] vertices, float[] normals){
        initVertexData(vertices,normals);
        initShader(mv);
    }
    public void initVertexData(float[] vertices, float[] normals){
        vCount = vertices.length/3;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer=vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        ByteBuffer cbb=ByteBuffer.allocateDirect(normals.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mNormalBuffer=cbb.asFloatBuffer();
        mNormalBuffer.put(normals);
        mNormalBuffer.position(0);

    }
    public void initShader(MySurfaceView mv){
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh",mv.getResources());
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh",mv.getResources());
        mProgram=ShaderUtil.createProgram(mVertexShader,mFragmentShader);
        maPositionHandle= GLES30.glGetAttribLocation(mProgram,"aPosition");
        maNormalHandle=GLES30.glGetAttribLocation(mProgram,"aNormal");
        muMVPMatrixHandle=GLES30.glGetUniformLocation(mProgram,"uMVPMatrix");
        muMMatrixHandle=GLES30.glGetUniformLocation(mProgram,"uMMatrix");
        maLightLocationHandle=GLES30.glGetUniformLocation(mProgram,"uLightLocation");
        maCameraHandle=GLES30.glGetUniformLocation(mProgram,"uCamera");
    }
    public void drawSelf(){
        GLES30.glUseProgram(mProgram);
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle,1,false,MatrixState.getFinalMatrix(),0);
        GLES30.glUniformMatrix4fv(muMMatrixHandle,1,false,MatrixState.getMMatrix(),0);
        GLES30.glUniform3fv(maLightLocationHandle,1,MatrixState.lightPositionFB);
        GLES30.glUniform3fv(maCameraHandle,1,MatrixState.cameraFB);;
        GLES30.glVertexAttribPointer(maPositionHandle,3,GLES30.GL_FLOAT,false,3*4,mVertexBuffer);
        GLES30.glVertexAttribPointer(maNormalHandle,3,GLES30.GL_FLOAT,false,3*4,mNormalBuffer);
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maNormalHandle);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,0,vCount);
    }
}
