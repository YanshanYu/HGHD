package com.yu.zehnit.tools;

import android.content.Context;

import com.yu.zehnit.ui.sessions.Session;
import com.yu.zehnit.ui.sessions.SessionDataManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class writeData {


    private static SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH");

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    public static void writelsFocus(List<String> data, Context context,Date date) {

            try {

                File path = new File(context.getExternalFilesDir(null) + "/Dataset/" + "/Session-" +df.format(date.getTime()) + "/");
                //不存在就新建
                if (!path.exists()) {
                    path.mkdirs();
                }
                File file = new File(path + "/" + "Focus.csv");
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file, true);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter bw = new BufferedWriter(osw);
                for (int i = 0; i < data.size(); i++) {

                    bw.write(String.valueOf(data.get(i)));
                    bw.newLine();

                }
                bw.flush();
                bw.close();
                osw.close();
                fos.close();
            } catch (FileNotFoundException e) {
                System.out.println("没有找到指定文件");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public static void writelsPursuit(List<String> data, Context context,Date date) {
        try {

            File path = new File(context.getExternalFilesDir(null) + "/Dataset/" + "/Session-" + df.format(date.getTime()) + "/");

            //不存在就新建
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path + "/" +  "Pursuit.csv");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            for (int i = 0; i < data.size(); i++) {
                bw.write(String.valueOf(data.get(i)));
                bw.newLine();
            }
            bw.flush();
            bw.close();
            osw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("没有找到指定文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    //传递一个次数进来，然后把df.format(new Date().getTime())改成次数就可以了
    public static void writelsPursuits(List<List<String>> data, Context context) {
        try {

            File path = new File(context.getExternalFilesDir(null) + "/Dataset/" + "/Session-" + df.format(new Date().getTime()) + "/");

            //不存在就新建
            if (!path.exists()) {
                path.mkdirs();
            }

            for (int j = 0; j < data.size(); j++) {
                File file = new File(path + "/" + "text-" + j + "-Pursuit.txt");
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file, true);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter bw = new BufferedWriter(osw);
                for (int i = 0; i < data.get(j).size(); i++) {
                    bw.write(String.valueOf(data.get(j).get(i)));
                    bw.newLine();
                }
                bw.flush();
                bw.close();
                osw.close();
                fos.close();

            }

        } catch (FileNotFoundException e) {
            System.out.println("没有找到指定文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 */

    public static void writelsSaccades(List<String> data, Context context,Date date) {
        try {
            File path = new File(context.getExternalFilesDir(null) + "/Dataset/" + "/Session-" + df.format(date.getTime()) + "/");

            //不存在就新建
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path + "/" +  "Saccades.csv");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            for (int i = 0; i < data.size(); i++) {
                bw.write(String.valueOf(data.get(i)));
                bw.newLine();
            }
            bw.flush();
            bw.close();
            osw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("没有找到指定文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writelsVorsupp(List<String> data, Context context,Date date) {
        try {
            File path = new File(context.getExternalFilesDir(null) + "/Dataset/" + "/Session-" + df.format(date.getTime())+ "/");

            //不存在就新建
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path + "/" +  "Vorsupp.csv");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            for (int i = 0; i < data.size(); i++) {
                bw.write(String.valueOf(data.get(i)));
                bw.newLine();
            }
            bw.flush();
            bw.close();
            osw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("没有找到指定文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writelsVortrain(List<String> data, Context context,Date date) {
        try {
            File path = new File(context.getExternalFilesDir(null) + "/Dataset/" + "/Session-" + df.format(date.getTime()) + "/");

            //不存在就新建
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path + "/" +  "Vortrain.csv");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            for (int i = 0; i < data.size(); i++) {
                bw.write(String.valueOf(data.get(i)));
                bw.newLine();
            }
            bw.flush();
            bw.close();
            osw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("没有找到指定文件");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
