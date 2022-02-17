package com.yu.zehnit.tools;

import android.content.res.Resources;
import android.opengl.GLES30;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ShaderUtil {
    public static int loadShader(int shaderType, String source){
        int shader= GLES30.glCreateShader(shaderType);
        if(shader!=0){
            GLES30.glShaderSource(shader,source);
            GLES30.glCompileShader(shader);
            int[] compiled=new int[1];
            GLES30.glGetShaderiv(shader,GLES30.GL_COMPILE_STATUS,compiled,0);
            if(compiled[0]==0){
                GLES30.glDeleteShader(shader);
                shader=0;
            }
        }
        return shader;
    }
    public static int createProgram(String vertexSource, String fragmentSource){
        int vertexShader=loadShader(GLES30.GL_VERTEX_SHADER, vertexSource);
        if(vertexShader==0){
            return 0;
        }
        int pixelShader=loadShader(GLES30.GL_FRAGMENT_SHADER,fragmentSource);
        if(pixelShader==0){
            return 0;
        }
        int program=GLES30.glCreateProgram();
        if(program!=0){
            GLES30.glAttachShader(program,vertexShader);
            checkGlError("glAttachShader");
            GLES30.glAttachShader(program,pixelShader);
            checkGlError("glAttachShader");
            GLES30.glLinkProgram(program);
            int[] linkStatus=new int[1];
            GLES30.glGetProgramiv(program,GLES30.GL_LINK_STATUS,linkStatus,0);
            if(linkStatus[0]!=GLES30.GL_TRUE){
                GLES30.glDeleteProgram(program);
                program=0;
            }
        }
        return program;
    }
    public static void checkGlError(String op){
        int error;
        while((error=GLES30.glGetError())!=GLES30.GL_NO_ERROR){
            throw new RuntimeException(op+"glError"+error);
        }
    }

    public static String loadFromAssetsFile(String fname, Resources r){
        String result=null;
        try{
            InputStream in=r.getAssets().open(fname);
            int ch=0;
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            while((ch=in.read())!=-1){
                baos.write(ch);
            }
            byte[] buff=baos.toByteArray();
            baos.close();
            in.close();
            result=new String(buff,"UTF-8");
            result=result.replaceAll("\\r\\n","\n");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
