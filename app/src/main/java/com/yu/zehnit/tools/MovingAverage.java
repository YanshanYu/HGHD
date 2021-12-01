package com.yu.zehnit.tools;

import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

public class MovingAverage {
    private double sum=0;
    private double squreSum=0;
    private int size;
    private int num;
    private Queue<Integer> queue;
    public MovingAverage(int size){
        this.size=size;
        queue=new LinkedList<>();
    }
    public double Average(int num){
        this.num=num;
        sum+=num;
        if(queue.size()==size){
            sum=sum- queue.peek();
            queue.poll();
        }
        queue.offer(num);
        return sum/queue.size();

    }

}
