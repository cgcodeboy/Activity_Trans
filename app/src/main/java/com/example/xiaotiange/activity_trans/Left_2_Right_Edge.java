package com.example.xiaotiange.activity_trans;

import android.graphics.PointF;
import android.util.Log;

import java.util.Random;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiaotiange on 2017/9/1.
 */

public class Left_2_Right_Edge extends Edge{

    private Node inner_LeftNode,inner_RightNode;

    private BlockingQueue<Vehicle> rightWait_Queue;
    private BlockingQueue<Vehicle> rightRunning_Queue;
    private BlockingQueue<Vehicle> leftWait_Queue;
    private BlockingQueue<Vehicle> leftRunning_Queue;

    private static String TAG = "LR edge";

//    private int inner_Distance;

    private int inner_HorizontalIndex;
    private int inner_VerticalIndex;


    public Left_2_Right_Edge(int horizontal_Index,int vertical_Index,Node left_Node,Node right_Node){
        super(horizontal_Index,vertical_Index,Direction.Left_2_Right);
        this.inner_LeftNode = left_Node;
        this.inner_RightNode = right_Node;

        this.inner_HorizontalIndex = horizontal_Index;
        this.inner_VerticalIndex = vertical_Index;

        rightWait_Queue     = new LinkedBlockingQueue<Vehicle>();
        rightRunning_Queue  = new LinkedBlockingQueue<Vehicle>();
        leftWait_Queue      = new LinkedBlockingQueue<Vehicle>();
        leftRunning_Queue   = new LinkedBlockingQueue<Vehicle>();

//        computeDistance();

        initQueue();
    }

    /*
    初始化等待队列
     */
    private void initQueue(){
        Random r = new Random();
        if(inner_LeftNode!=null){
            for(int i = 0;i<2;i++){
                Vehicle vehicle = new Vehicle((int)inner_LeftNode.getInner_X(),(int)inner_LeftNode.getInner_Y(),this);
                vehicle.setInner_Speed(10);
                vehicle.setInner_Number("No" +String.valueOf(r.nextInt(1000)));
                leftWait_Queue.add(vehicle);
            }
        }
        if(inner_RightNode!=null){
            for(int i = 0;i<2;i++){
                Vehicle vehicle = new Vehicle((int)inner_RightNode.getInner_X(),(int)inner_RightNode.getInner_Y(),this);
                vehicle.setInner_Speed(10);
                vehicle.setInner_Number("No" +String.valueOf(r.nextInt(1000)));
                rightWait_Queue.add(vehicle);
            }
        }
    }

    public void addVehicleToLeftRunningQueue(Vehicle vehicle){
        if(this.inner_RightNode == null){
            return;
        }
        vehicle.setMoveDirection(MoveDirection.RIGHT);
        vehicle.setMovePath(inner_LeftNode,inner_RightNode);
        leftRunning_Queue.add(vehicle);
    }

    public Vehicle removeVehicleFromLeftRunningQueue(){
        Vehicle vehivle = null;
        try {
            vehivle = leftRunning_Queue.poll(100, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }

        return vehivle;
    }

    public void addVehicleToRightRunningQueue(Vehicle vehicle){
        if(this.inner_LeftNode == null){
            return;
        }
        vehicle.setMoveDirection(MoveDirection.LEFT);
        vehicle.setMovePath(inner_RightNode,inner_LeftNode);
        rightRunning_Queue.add(vehicle);
    }

    public Vehicle removeVehicleFromRightRunningQueue(){
        Vehicle vehivle = null;
        try {
            vehivle = rightRunning_Queue.poll(100, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }

        return vehivle;
    }

    public boolean addVehicleToRightWaitQueue(Vehicle vehicle){
        return rightWait_Queue.add(vehicle);
    }

    public Vehicle removeVehicleFromRightWaitQueue(){
        Vehicle vehicle = null;
        try {
            vehicle = rightWait_Queue.poll(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return vehicle;
    }

    public boolean addVehicleToLeftWaitQueue(Vehicle vehicle){
        return leftWait_Queue.add(vehicle);
    }

    public Vehicle removeVehicleFromLeftWaitQueue(){
        Vehicle vehicle = null;
        try {
            vehicle = leftWait_Queue.poll(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return vehicle;
    }

    /*
    计算边的长度，用于边内的车辆行驶计算
     */
//    protected void computeDistance(){
//        float x1 = this.inner_LeftNode.getInner_X();
//        float y1 = this.inner_LeftNode.getInner_Y();
//        float x2 = this.inner_RightNode.getInner_X();
//        float y2 = this.inner_RightNode.getInner_Y();
//
//        this.inner_Distance = (int)Math.sqrt((double)(x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
//    }


    /*
    当边内部的runningQueue不为空时，让queue内部的车辆运行
    每辆车在运行到边的终点时就会调用边内部的函数将自己调到waitQueue中
     */
    @Override
    public void run() {
        Random r = new Random();
        int t = 0;
        int second = r.nextInt()%10;
        for (int i = 0;i<second *1000;i++){
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(true){
            for(Vehicle vehicle:leftRunning_Queue){
                vehicle.beginMove();
            }
            for (Vehicle vehicle:rightRunning_Queue){
                vehicle.beginMove();
            }
            if(t%4 == 0){
                if (inner_LeftNode == null){
                    Vehicle vehicle = new Vehicle((int)inner_RightNode.getInner_X(),(int)inner_RightNode.getInner_Y(),this);
                    vehicle.setInner_Speed(10);
                    vehicle.setInner_Number("No" +String.valueOf(r.nextInt(1000)));
                    rightWait_Queue.add(vehicle);
                }
                if (inner_RightNode == null){
                    Vehicle vehicle = new Vehicle((int)inner_LeftNode.getInner_X(),(int)inner_LeftNode.getInner_Y(),this);
                    vehicle.setInner_Speed(10);
                    vehicle.setInner_Number("No" +String.valueOf(r.nextInt(1000)));
                    leftWait_Queue.add(vehicle);
                }
            }
            t++;
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    当边内的车辆运行到端点时，调用此方法将车辆从运行队列变到等待队列
     */
    @Override
    public void changeVehicleBelongQueue(Vehicle vehicle,MoveDirection direction){
        //super.changeVehicleBelongQueue(vehicle, direction);
        if(direction == MoveDirection.LEFT){
            this.removeVehicleFromRightRunningQueue();
            this.addVehicleToLeftWaitQueue(vehicle);
        }
        else if(direction == MoveDirection.RIGHT){
            this.removeVehicleFromLeftRunningQueue();
            this.addVehicleToRightWaitQueue(vehicle);
        }
    }

    /*
    左侧等待车辆的数量
     */
    public int getLeftWaitSize(){
        return leftWait_Queue.size();
    }

    /*
    右侧等待车辆的数量
     */
    public int getRightWaitSize(){
        return rightWait_Queue.size();
    }

    /*
    得到所有车辆
     */
    public Vector<Vehicle> getVehicles(){
        Vector<Vehicle> point_Vec = new Vector<Vehicle>();
        for (Vehicle vehicle:leftRunning_Queue) {
            point_Vec.add(vehicle);
        }
        for (Vehicle vehicle:leftWait_Queue) {
            point_Vec.add(vehicle);
        }
        for (Vehicle vehicle:rightRunning_Queue) {
            point_Vec.add(vehicle);
        }
        for (Vehicle vehicle:rightWait_Queue) {
            point_Vec.add(vehicle);
        }
        return point_Vec;
    }
}
