package com.yu.zehnit.tools;



import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LoadUtil {
    // 求两个向量的叉积
    public static float[] getCrossPRoduct(float x1, float y1, float z1, float x2, float y2, float z2){
        float A=y1*z2-y2*z1;
        float B=z1*x2-z2*x1;
        float C=x1*y2-x2*y1;
        return new float[]{A,B,C};
    }

    //向量规格化
    public static float[] vectorNormal(float[] vector){
        //求向量的模
        float module=(float)Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]);
        return new float[]{vector[0]/module,vector[1]/module,vector[2]/module};
    }
    public static LoadedObjectVertexNormal loadFromFile(String fname, Resources r, MySurfaceView mv){
        LoadedObjectVertexNormal lo=null;
        ArrayList<Float> alv=new ArrayList<Float>();
        ArrayList<Integer> alFaceIndex=new ArrayList<Integer>();
        ArrayList<Float> alvResult=new ArrayList<Float>();
        HashMap<Integer, HashSet<Normal>> hmn=new HashMap<Integer,HashSet<Normal>>();
        try{
            InputStream in =r.getAssets().open(fname);
            InputStreamReader isr=new InputStreamReader(in);
            BufferedReader br=new BufferedReader(isr);
            String temps=null;
            while((temps=br.readLine())!=null){
                String[] tempsa=temps.split("[ ]+");
                if(tempsa[0].trim().equals("v")){
                    alv.add(Float.parseFloat(tempsa[1]));
                    alv.add(Float.parseFloat(tempsa[2]));
                    alv.add(Float.parseFloat(tempsa[3]));
                }
                else if(tempsa[0].trim().equals("f")){

                    int[] index=new int[3];  //此面中3个顶点编号值的数组

                    //计算第0个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[0]=Integer.parseInt(tempsa[1].split("/")[0])-1;
                    float x0=alv.get(3*index[0]);
                    float y0=alv.get(3*index[0]+1);
                    float z0=alv.get(3*index[0]+2);
                    alvResult.add(x0);
                    alvResult.add(y0);
                    alvResult.add(z0);

                    //计算第1个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[1]=Integer.parseInt(tempsa[2].split("/")[0])-1;
                    float x1=alv.get(3*index[1]);
                    float y1=alv.get(3*index[1]+1);
                    float z1=alv.get(3*index[1]+2);
                    alvResult.add(x1);
                    alvResult.add(y1);
                    alvResult.add(z1);

                    //计算第2个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
                    float x2=alv.get(3*index[2]);
                    float y2=alv.get(3*index[2]+1);
                    float z2=alv.get(3*index[2]+2);
                    alvResult.add(x2);
                    alvResult.add(y2);
                    alvResult.add(z2);

                    alFaceIndex.add(index[0]);
                    alFaceIndex.add(index[1]);
                    alFaceIndex.add(index[2]);

                    //通过三角形面两个边向量0-1，0-2求叉积得到此面的法向量
                    //求三角形中第一个点到第二个点的向量
                    float vxa=x1-x0;
                    float vya=y1-y0;
                    float vza=z1-z0;
                    //求三角形中第一个点到第三个点的向量
                    float vxb=x2-x0;
                    float vyb=y2-y0;
                    float vzb=z2-z0;
                    //通过求两个向量的叉积计算出此三角形面的法向量
                    float[] vNormal=vectorNormal(getCrossPRoduct(vxa,vya,vza,vxb,vyb,vzb));
                    for(int tempIndex:index){
                        HashSet<Normal> hsn=hmn.get(tempIndex);
                        if(hsn==null){
                            hsn=new HashSet<Normal>();
                        }
                        hsn.add(new Normal(vNormal[0],vNormal[1],vNormal[2]));
                        hmn.put(tempIndex,hsn);
                    }
                }
            }
            //生成顶点数组
            int size=alvResult.size();
            float[] vXYZ=new float[size];
            for(int i=0;i<size;i++){
                vXYZ[i]=alvResult.get(i);
            }
            //生成法向量数组
            float[] nXYZ=new float[alFaceIndex.size()*3];
            int c=0;
            for(Integer i: alFaceIndex){
                HashSet<Normal> hsn=hmn.get(i);
                float[] tn=Normal.getAverage(hsn);
                nXYZ[c++]=tn[0];
                nXYZ[c++]=tn[1];
                nXYZ[c++]=tn[2];
            }
            lo=new LoadedObjectVertexNormal(mv,vXYZ,nXYZ);

        }catch (Exception e){
            e.printStackTrace();
        }
        return lo;
    }

}
