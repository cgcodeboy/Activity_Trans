package com.example.xiaotiange.activity_trans;

import android.graphics.PointF;
import android.util.Log;

import java.util.Random;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * Created by xiaotiange on 2017/9/1.
 */

public class Up_2_Down_Edge extends Edge implements Runnable {

    private Node inner_UpNode,inner_DownNode;

    private static String TAG = "UD edge";

//    private int inner_Distance;

    private int inner_HorizontalIndex;
    private int inner_VerticalIndex;

    private BlockingQueue<Vehicle> upWait_Queue;
    private BlockingQueue<Vehicle> upRunning_Queue;
    private BlockingQueue<Vehicle> downWait_Queue;
    private BlockingQueue<Vehicle> downRunning_Queue;

    public Up_2_Down_Edge(int horizontal_Index,int vertical_Index,Node up_Node,Node down_Node){
        super(horizontal_Index,vertical_Index,Direction.Left_2_Right);
        this.inner_HorizontalIndex = horizontal_Index;
        this.inner_VerticalIndex = vertical_Index;

        this.inner_UpNode = up_Node;
        this.inner_DownNode = down_Node;

        upWait_Queue     = new LinkedBlockingQueue<Vehicle>();
        upRunning_Queue  = new LinkedBlockingQueue<Vehicle>();
        downWait_Queue      =  new LinkedBlockingQueue<Vehicle>();
        downRunning_Queue   =  new LinkedBlockingQueue<Vehicle>();

//        computeDistance();

        initQueue();
    }

    /*
    初始化等待队列
     */
    private void initQueue(){
        Random r = new Random();
        if(inner_UpNode!=null){
            for(int i = 0;i<2;i++){
                Vehicle vehicle = new Vehicle(inner_UpNode.getInner_X(),inner_UpNode.getInner_Y(),this);
                vehicle.setInner_Speed(10);
                vehicle.setInner_Number("No" +String.valueOf(r.nextInt(1000)));
                upWait_Queue.add(vehicle);
            }
        }
        if(inner_DownNode!=null){
            for(int i = 0;i<2;i++){
                Vehicle vehicle = new Vehicle((int)inner_DownNode.getInner_X(),(int)inner_DownNode.getInner_Y(),this);
                vehicle.setInner_Speed(10);
                vehicle.setInner_Number("No" +String.valueOf(r.nextInt(1000)));
                downWait_Queue.add(vehicle);
            }
        }
    }


    public void addVehicleToUpRunningQueue(Vehicle vehicle){
        if(this.inner_DownNode == null){
            return;
        }
        vehicle.setMoveDirection(MoveDirection.DOWN);
        vehicle.setMovePath(inner_UpNode,inner_DownNode);
        upRunning_Queue.add(vehicle);
    }

    public Vehicle removeVehicleFromUpRunningQueue(){
        Vehicle vehivle = null;
        try {
            vehivle = upRunning_Queue.poll(100, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }

        return vehivle;
    }

    public void addVehicleToDownRunningQueue(Vehicle vehicle){
        if(this.inner_UpNode == null){
            return;
        }
        vehicle.setMoveDirection(MoveDirection.UP);
        vehicle.setMovePath(inner_DownNode,inner_UpNode);
        downRunning_Queue.add(vehicle);
    }

    public Vehicle removeVehicleFromDownRunningQueue(){
        Vehicle vehivle = null;
        try {
            vehivle = downRunning_Queue.poll(100, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }

        return vehivle;
    }

    public boolean addVehicleToUpWaitQueue(Vehicle vehicle){
        return upWait_Queue.add(vehicle);
    }

    public Vehicle removeVehicleFromUpWaitQueue(){
        Vehicle vehivle = null;
        try {
            vehivle = upWait_Queue.poll(100, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }

        return vehivle;
    }

    public boolean addVehicleToDownWaitQueue(Vehicle vehicle){
        return downWait_Queue.add(vehicle);
    }

    public Vehicle removeVehicleFromDownWaitQueue(){
        Vehicle vehivle = null;
        try {
            vehivle = downWait_Queue.poll(100, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }

        return vehivle;
    }

    /*
    计算边的长度，用于边内的车辆行驶计算
     */
//    protected void computeDistance(){
//        float x1 = this.inner_UpNode.getInner_X();
//        float y1 = this.inner_UpNode.getInner_Y();
//        float x2 = this.inner_DownNode.getInner_X();
//        float y2 = this.inner_DownNode.getInner_Y();
//
//        this.inner_Distance = (int)Math.sqrt((double)(x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
//    }

    /*
    当边内部的runningQueue不为空时，让queue内部的车辆运行
    每辆车在运行到边的终点时就会调用边内部的函数将自己调到waitQueue中
     */
    @Override
    public void run() {
        int t = 0;
        for (int i = 0;i<1000;i++){
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(true) {
            Random r = new Random();
            for (Vehicle vehicle:upRunning_Queue) {
                vehicle.beginMove();
            }
            for (Vehicle vehicle:downRunning_Queue) {
                vehicle.beginMove();
            }
            if(t%4 == 0){
                if (inner_UpNode == null){
                    Vehicle vehicle = new Vehicle((int)inner_DownNode.getInner_X(),(int)inner_DownNode.getInner_Y(),this);
                    vehicle.setInner_Speed(10);
                    vehicle.setInner_Number("No" +String.valueOf(r.nextInt(1000)));
                    downWait_Queue.add(vehicle);
                }

                if (inner_DownNode == null){
                    Vehicle vehicle = new Vehicle(inner_UpNode.getInner_X(),inner_UpNode.getInner_Y(),this);
                    vehicle.setInner_Speed(10);
                    vehicle.setInner_Number("No" +String.valueOf(r.nextInt(1000)));
                    upWait_Queue.add(vehicle);
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

    @Override
    public void changeVehicleBelongQueue(Vehicle vehicle, MoveDirection direction) {
        super.changeVehicleBelongQueue(vehicle, direction);
        if (direction == MoveDirection.UP){
            this.removeVehicleFromDownRunningQueue();
            this.addVehicleToUpWaitQueue(vehicle);
        }
        else if(direction == MoveDirection.DOWN){
            this.removeVehicleFromUpRunningQueue();
            this.addVehicleToDownWaitQueue(vehicle);
        }
    }

    /*
    上侧等待车辆的数量
     */
    public int getUpWaitSize(){
        return upWait_Queue.size();
    }

    /*
    下侧等待车辆的数量
     */
    public int getDownWaitSize(){
        return downWait_Queue.size();
    }

    /*
    得到所有车辆
     */
    public Vector<Vehicle> getVehicles(){
        Vector<Vehicle> point_Vec = new Vector<Vehicle>();
        for (Vehicle vehicle:upRunning_Queue) {
            point_Vec.add(vehicle);
        }
        for (Vehicle vehicle:upWait_Queue) {
            point_Vec.add(vehicle);
        }
        for (Vehicle vehicle:downRunning_Queue) {
            point_Vec.add(vehicle);
        }
        for (Vehicle vehicle:downWait_Queue) {
            point_Vec.add(vehicle);
        }
        return point_Vec;
    }
}
