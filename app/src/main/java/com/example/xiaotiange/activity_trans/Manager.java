package com.example.xiaotiange.activity_trans;

import android.util.Log;

import org.jgap.Configuration;
import org.jgap.impl.DefaultConfiguration;

import java.util.Random;
import java.util.RandomAccess;
import java.util.Vector;

/**
 * Created by xiaotiange on 2017/9/1.
 */

class Manager {

    private static String TAG = "Manager";

    private int inner_Width,inner_Height;

    private Vector<Vector<Node>> inner_Vec;

    int array[][];

    public Manager(){

    }

    /*
    优化函数
     */
    public int optimalTime(Node node){
//        Random r = new Random();
//        return r.nextInt()%10+30;
        for (int i = 0;i<inner_Height;i++){
            for (int j = 0;j<inner_Width;j++){
                Node cur_Node = inner_Vec.get(i).get(j);
                if(cur_Node == null){
                    return 30;
                }
                array[i*inner_Width+j][0] = cur_Node.getLeftWaitVehicle() + cur_Node.getRightWaitVehicle();
                array[i*inner_Width+j][1] = cur_Node.getUpWaitVehicle() + cur_Node.getDownWaitVehicle();
            }
        }


        GeneticAlgorithm my_GA = null;

        try {
            Configuration configuration = new DefaultConfiguration();
            my_GA = new GeneticAlgorithm(inner_Width*inner_Height,node.getInner_Horizontal()*inner_Width+node.getInner_Vertical(),array,configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return my_GA.getValue();
    }

    /*
    设置规模函数
     */
    public void setScale(int width,int height){
        this.inner_Height = height;
        this.inner_Width = width;
        array = new int[inner_Width*inner_Height][2];
    }

    /*
    设置全局优化节点
     */
    public void setOptimizationNode(Vector<Vector<Node>> nodeVec){
        this.inner_Vec = nodeVec;
    }
}
