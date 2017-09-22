package com.example.xiaotiange.activity_trans;

import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Created by xiaotiange on 2017/9/2.
 */

public class NodeTransferManager {

    private Left_2_Right_Edge inner_LeftEdge,inner_RightEdge;

    private Up_2_Down_Edge inner_UpEdge,inner_DownEdge;

    private int inner_Time = 0;

    private static String TAG = "NodeManager";

    public NodeTransferManager(){
        //
    }

    /*
    设置车辆可以通过的时间
     */
    public void setTime(int time){
        this.inner_Time = time;
    }


    /*
    设置水平进入的边
     */
    public void setHorizontalEdge(Left_2_Right_Edge left_Edge,Left_2_Right_Edge right_Edge){
        this.inner_LeftEdge = left_Edge;
        this.inner_RightEdge = right_Edge;
    }

    /*
    设置竖直进入的边
     */
    public void setVerticalEdge(Up_2_Down_Edge up_Edge,Up_2_Down_Edge down_Edge){
        this.inner_UpEdge = up_Edge;
        this.inner_DownEdge = down_Edge;
    }

    /*
    管理水平边车辆进入竖直边中
     */
    public void manageHorizontal(){
        Vehicle vehicle;
        Random r = new Random();

        while(inner_Time>0){
            vehicle = inner_RightEdge.removeVehicleFromLeftWaitQueue();
            if (vehicle != null){
                float randomNumber = r.nextFloat();
                if(randomNumber>0.5){
                    inner_LeftEdge.addVehicleToRightRunningQueue(vehicle);
                    vehicle.setBelongEdge(inner_LeftEdge);
                }
                else if(randomNumber<0.25){
                    inner_UpEdge.addVehicleToDownRunningQueue(vehicle);
                    vehicle.setBelongEdge(inner_UpEdge);
                }
                else{
                    inner_DownEdge.addVehicleToUpRunningQueue(vehicle);
                    vehicle.setBelongEdge(inner_DownEdge);
                }
            }

            vehicle = inner_LeftEdge.removeVehicleFromRightWaitQueue();
            if (vehicle != null){
                float randomNumber = r.nextFloat();
                if(randomNumber>0.5){
                    inner_RightEdge.addVehicleToLeftRunningQueue(vehicle);
                    vehicle.setBelongEdge(inner_RightEdge);
                }
                else if(randomNumber<0.25){
                    inner_UpEdge.addVehicleToDownRunningQueue(vehicle);
                    vehicle.setBelongEdge(inner_UpEdge);
                }
                else{
                    inner_DownEdge.addVehicleToUpRunningQueue(vehicle);
                    vehicle.setBelongEdge(inner_DownEdge);
                }
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            inner_Time--;
        }
    }

    /*
    管理竖直边车辆进入水平边中
     */
    public void manageVertical(){
        Vehicle vehicle;
        Random r = new Random();

        while(inner_Time>0){
            vehicle = inner_UpEdge.removeVehicleFromDownWaitQueue();
            if (vehicle != null){
                float randomNumber = r.nextFloat();
                if(randomNumber>0.5){
                    inner_DownEdge.addVehicleToUpRunningQueue(vehicle);
                    vehicle.setBelongEdge(inner_DownEdge);
                }
                else if(randomNumber<0.25){
                    inner_LeftEdge.addVehicleToRightRunningQueue(vehicle);
                    vehicle.setBelongEdge(inner_LeftEdge);
                }
                else{
                    inner_RightEdge.addVehicleToLeftRunningQueue(vehicle);
                    vehicle.setBelongEdge(inner_RightEdge);
                }
            }

            vehicle = inner_DownEdge.removeVehicleFromUpWaitQueue();
            if (vehicle != null){
                float randomNumber = r.nextFloat();
                if(randomNumber>0.5){
                    inner_UpEdge.addVehicleToDownRunningQueue(vehicle);
                    vehicle.setBelongEdge(inner_UpEdge);
                }
                else if(randomNumber<0.25){
                    inner_LeftEdge.addVehicleToRightRunningQueue(vehicle);
                    vehicle.setBelongEdge(inner_LeftEdge);
                }
                else{
                    inner_RightEdge.addVehicleToLeftRunningQueue(vehicle);
                    vehicle.setBelongEdge(inner_RightEdge);
                }
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            inner_Time--;
        }
    }

    public int getLastTime(){
        return this.inner_Time;
    }
}
