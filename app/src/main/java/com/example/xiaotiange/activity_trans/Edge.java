package com.example.xiaotiange.activity_trans;

import android.util.Log;

/**
 * Created by xiaotiange on 2017/9/1.
 */

enum Direction{
    Up_2_Down,Left_2_Right
}

class Edge extends Thread {
    private static String TAG = "father edge";

    private Direction innder_Direction;

    private int inner_HorizontalIndex;
    private int inner_VerticalIndex;

    public Edge(int horizontal_Index,int vertical_Index,Direction direction){
        this.innder_Direction = direction;
        this.inner_HorizontalIndex = horizontal_Index;
        this.inner_VerticalIndex = vertical_Index;
    }

    public void addVehicleToLeftRunningQueue(Vehicle vehicle){}

    public Vehicle removeVehicleFromLeftRunningQueue(){return null;}

    public void addVehicleToRightRunningQueue(Vehicle vehicle){}

    public Vehicle removeVehicleFromRightRunningQueue(){
        return null;
    }

    public boolean addVehicleToRightWaitQueue(Vehicle vehicle){return true;};

    public Vehicle removeVehicleFromRightWaitQueue(){
        return null;
    }

    public boolean addVehicleToLeftWaitQueue(Vehicle vehicle){return true;}

    public Vehicle removeVehicleFromLeftWaitQueue(){
        return null;
    }

    public void addVehicleToUpRunningQueue(Vehicle vehicle){}

    public Vehicle removeVehicleFromUpRunningQueue(){return null;}

    public void addVehicleToDownRunningQueue(Vehicle vehicle){}

    public Vehicle removeVehicleFromDownRunningQueue(){
        return null;
    }

    public boolean addVehicleToUpWaitQueue(Vehicle vehicle){
        return true;
    }

    public Vehicle removeVehicleFromUpWaitQueue(){
        return null;
    }

    public boolean addVehicleToDownWaitQueue(Vehicle vehicle){return true;}

    public Vehicle removeVehicleFromDownWaitQueue(){
        return null;
    }

    public void changeVehicleBelongQueue(Vehicle vehicle,MoveDirection direction){
    }
}
