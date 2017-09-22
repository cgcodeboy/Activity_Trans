package com.example.xiaotiange.activity_trans;

import android.util.Log;

/**
 * Created by xiaotiange on 2017/9/1.
 */

enum MoveDirection{
    UP,DOWN,LEFT,RIGHT
}

public class Vehicle{

    private float inner_X,inner_Y;

    private Node inner_BeginNode,inner_EndNode;

    private float xOffset,yOffset;

    private Edge belongEdge;

    private MoveDirection inner_Direction;

    private float inner_Speed;

    private static String TAG = "vehicle";

    private String inner_Number;

    public Vehicle(float x,float y,Edge belongEdge){
        this.inner_X = x;
        this.inner_Y = y;
        this.belongEdge = belongEdge;
    }

    /*
    位置横坐标的get函数
     */
    public float getInner_X() {
        return inner_X;
    }

    /*
    位置横坐标的set函数
     */
    public void setInner_X(float inner_X) {
        this.inner_X = inner_X;
    }
    /*
    位置纵坐标的get函数
    */
    public float getInner_Y() {
        return inner_Y;
    }

    /*
    位置横坐标的set函数
     */
    public void setInner_Y(float inner_Y) {
        this.inner_Y = inner_Y;
    }

    /*
    得到号码
     */
    public String getInner_Number() {
        return inner_Number;
    }

    /*
    设置号码
    */
    public void setInner_Number(String inner_Number) {
        this.inner_Number = inner_Number;
    }

    /*
    设置速度函数
     */
    public void setInner_Speed(float inner_Speed) {
        this.inner_Speed = inner_Speed;
    }

    /*
    设置移动路径，也就是起点和终点
     */
    public void setMovePath(Node beginNode,Node endNode) {
        this.inner_X = beginNode.getInner_X();
        this.inner_Y = beginNode.getInner_Y();
        this.inner_BeginNode = beginNode;
        this.inner_EndNode = endNode;
        computeOffset();
    }

    public void setMoveDirection(MoveDirection direction){
        this.inner_Direction = direction;
    }

    /*
    当外部调用时，会沿着路径移动一个步长，当到达终点时，调用stopMove函数
     */
    public void beginMove() {
        if (Math.sqrt((inner_X - inner_EndNode.getInner_X())*(inner_X - inner_EndNode.getInner_X())+(inner_Y - inner_EndNode.getInner_Y())*(inner_Y - inner_EndNode.getInner_Y()))<30){
            inner_X += xOffset/4;
            inner_Y += yOffset/4;
        }
        else{
            inner_X += xOffset;
            inner_Y += yOffset;
        }
        if (Float.compare(Math.abs(inner_X - inner_EndNode.getInner_X()), Float.NaN) == 0 && Float.compare(Math.abs(inner_Y - inner_EndNode.getInner_Y()), Float.NaN) == 0) {
            this.stopMove();
        }
        switch (inner_Direction){
            case LEFT:
                if (inner_X<inner_EndNode.getInner_X()){
                    stopMove();
                }
                break;
            case RIGHT:
                if (inner_X>inner_EndNode.getInner_X()){
                    stopMove();
                }
                break;
            case UP:
                if (inner_Y<inner_EndNode.getInner_Y()){
                    stopMove();
                }
                break;
            case DOWN:
                if (inner_Y>inner_EndNode.getInner_Y()){
                    stopMove();
                }
                break;
            default:
                break;
        }
    }

    /*
    计算X,Y步长
     */
    public void computeOffset(){
        float xDistance = inner_EndNode.getInner_X() - inner_BeginNode.getInner_X();
        float yDistance = inner_EndNode.getInner_Y() - inner_BeginNode.getInner_Y();
        float distance = (float)Math.sqrt(xDistance*xDistance+yDistance*yDistance);
        float segmentNum = distance/inner_Speed;
        xOffset = (inner_EndNode.getInner_X() - inner_BeginNode.getInner_X())/segmentNum;
        yOffset = (inner_EndNode.getInner_Y() - inner_BeginNode.getInner_Y())/segmentNum;
    }

    /*
    停止移动的函数，内部调用所属边的函数，使从运动队列变为等待队列
     */
    protected void stopMove(){
        this.belongEdge.changeVehicleBelongQueue(this,inner_Direction);
    }

    /*
    设置所属边
     */
    public void setBelongEdge(Edge belongEdge) {
        this.belongEdge = belongEdge;
    }
}
