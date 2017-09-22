package com.example.xiaotiange.activity_trans;

import android.content.ContentValues;
import android.util.Log;

import java.util.Random;
import java.util.Timer;
import java.util.Vector;

import static android.content.ContentValues.TAG;

/**
 * Created by xiaotiange on 2017/9/1.
 */

public class Node extends Thread {

    private static final String TAG = "node";

    private enum Direction{
        UP_2_DOWN,RIGHT_LEFT
    }

    Manager inner_Manager;

    private Direction inner_direction;

    private float inner_X,inner_Y;

    private Up_2_Down_Edge inner_UpEdge,inner_DownEdge;
    private Left_2_Right_Edge inner_LeftEdge,inner_RightEdge;

    private Node inner_UpNode,inner_DownNode,inner_LeftNode,inner_RightNode;

    private int updown_Time,leftRight_Time;

    private int inner_Vertical,inner_Horizontal;

//    private Timer updownTimer,leftright_Timer;

    private  NodeTransferManager nodeManager;

    /*
    构造函数
     */
    public Node(float x,float y,int index_Horizontal,int index_vertical){
        this.inner_X = x;
        this.inner_Y = y;
        Random r = new Random();
        if(r.nextFloat()>0.5){
            this.inner_direction = Direction.RIGHT_LEFT;
            this.leftRight_Time = r.nextInt()%30;
        }
        else{
            this.inner_direction = Direction.UP_2_DOWN;
            this.updown_Time = r.nextInt()%30;
        }


        this.inner_Horizontal = index_Horizontal;
        this.inner_Vertical = index_vertical;

        nodeManager = new NodeTransferManager();
    }


    public float getInner_X() {
        return inner_X;
    }

    public float getInner_Y() {
        return inner_Y;
    }

    public int getInner_Horizontal() {
        return inner_Horizontal;
    }

    public int getInner_Vertical() {
        return inner_Vertical;
    }

    /*
    得到当前剩余将要变换红绿灯时间
     */
    public int getNodeLastTime(){
        return nodeManager.getLastTime();
    }

    /*
    得到当前交通方向
     */
    public String getCurrentDirection(){
        if(inner_direction == Direction.UP_2_DOWN){
            return String.valueOf("纵向");
        }
        else{
            return String.valueOf("横向");
        }
    }

    /*
    设置全局管理优化的manager
     */
    public void setManager(Manager manager){
        this.inner_Manager = manager;
    }

    /*
    设置节点的四个输入边
     */
    public void setInputEdge(Up_2_Down_Edge up,Up_2_Down_Edge down,Left_2_Right_Edge left,Left_2_Right_Edge right){
        this.inner_UpEdge = up;
        this.inner_DownEdge = down;
        this.inner_LeftEdge = left;
        this.inner_RightEdge = right;
        nodeManager.setVerticalEdge(this.inner_UpEdge,this.inner_DownEdge);
        nodeManager.setHorizontalEdge(this.inner_LeftEdge,this.inner_RightEdge);
    }

    /*
    设置相邻节点
     */
    public void setAdjoinNode(Node up,Node down,Node left,Node right){
        this.inner_UpNode = up;
        this.inner_DownNode= down;
        this.inner_LeftNode = left;
        this.inner_RightNode= right;
    }

    /*
    改变红绿灯，也就是车辆允许行驶的方向
    如果是从上到下，则改变方向，然后计算出从左到右的优化后的时间，然后开始计时，直到再次触发。
    如果是从左到右，则改变方向，然后计算出从上到下的优化后的时间，然后开始计时，直到再次触发。
     */
    public void changeDirection()
    {
        if(inner_direction == Direction.RIGHT_LEFT){
            inner_direction = Direction.UP_2_DOWN;
            updown_Time = inner_Manager.optimalTime(this);
            nodeManager.setTime(updown_Time);
            nodeManager.manageVertical();
        }
        else{
            inner_direction = Direction.RIGHT_LEFT;
            leftRight_Time =  60-inner_Manager.optimalTime(this);
            nodeManager.setTime(leftRight_Time);
            nodeManager.manageHorizontal();
        }
    }

    /*
    运行函数，让节点不断在一个方向时间到了之后改变方向继续让另一个方向车辆通过
     */
    @Override
    public void run() {
        Random r = new Random();
        int second = r.nextInt()%30;
        for (int i = 0;i<second * 1000;i++){
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (true) {
            changeDirection();
        }
    }
    /*
    得到在左侧等待的车辆数量
     */
    public int getLeftWaitVehicle(){
        return inner_LeftEdge.getRightWaitSize();
    }

    /*
    得到在右侧等待的车辆数量
     */
    public int getRightWaitVehicle(){
        return inner_RightEdge.getLeftWaitSize();
    }

    /*
    得到在上侧等待的车辆数量
     */
    public int getUpWaitVehicle(){
        if (inner_UpEdge!=null){
            return inner_UpEdge.getDownWaitSize();
        }
        return  0;
    }

    /*
    得到在下侧等待的车辆数量
     */
    public int getDownWaitVehicle(){
        return inner_DownEdge.getUpWaitSize();
    }
}
