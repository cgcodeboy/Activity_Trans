package com.example.xiaotiange.activity_trans;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;

import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;
import java.util.jar.Attributes;

import static java.lang.Thread.sleep;


/**
 * Created by xiaotiange on 2017/9/1.
 */

public class DrawView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    SurfaceHolder holder;

    Paint m_paint;

    private boolean isRun = false;

    private static String TAG = "DrawView";

    private int WIDTH_NUM = 4,HEIGHT_NUM = 3;

    /*
    四个边上用于存储随机点的vector
    */
    Vector<PointF> hori_LeftVec = new Vector<PointF>();
    Vector<PointF> hori_RightVec = new Vector<PointF>();
    Vector<PointF> vert_TopVec = new Vector<PointF>();
    Vector<PointF> vert_BottomVec = new Vector<PointF>();

    private ArrayList<String> inner_NumList;

    private Vector<Vector<Node>> node_Vec;

    private Vector<Vector<Left_2_Right_Edge>> edge_LeftRight;

    private Vector<Vector<Up_2_Down_Edge>> edge_UpDown;

    private Canvas canvas;

    public int getScreenWidth() {
        return width;
    }

    public int getScreenHeight() {
        return height;
    }

    private int width;
    private int height;

    private Vehicle inner_Specialvehicle = null;

    private Manager manager;

    public DrawView(Context context,  AttributeSet attrs) {
        super(context,attrs);

        inner_NumList = new ArrayList<String>(10000);

        manager = new Manager();

        holder = this.getHolder();
        holder.addCallback(this);

        node_Vec = new Vector<Vector<Node>>();
        edge_LeftRight = new Vector<Vector<Left_2_Right_Edge>>();
        edge_UpDown = new Vector<Vector<Up_2_Down_Edge>>();

        m_paint = new Paint();
        m_paint.setColor(Color.RED);
        m_paint.setStrokeWidth(3);
        m_paint.setStrokeJoin(Paint.Join.ROUND);
        m_paint.setStrokeCap(Paint.Cap.ROUND);
        m_paint.setAntiAlias(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRun = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRun = false;

    }

    /*
    线程的运行函数
     */
    @Override
    public void run() {

        width = getWidth();
        height = getHeight();

        randomPointGeneration(width, height);
        computerAllNode();
        computeAllEdge();
        setAllNodeAdjoinNode();
        setAllNodeInputEdge();
        generateVehicleNumber();

        manager.setScale(WIDTH_NUM,HEIGHT_NUM);
        manager.setOptimizationNode(node_Vec);

        Random r = new Random();

        for(Vector<Node> cur:node_Vec){
            for (Node node:cur){
                node.start();
            }
        }

        for (Vector<Left_2_Right_Edge> cur :edge_LeftRight){
            for(Left_2_Right_Edge edge :cur){
                edge.start();
            }
        }

        for (Vector<Up_2_Down_Edge> cur :edge_UpDown){
            for(Up_2_Down_Edge edge :cur){
                edge.start();
            }
        }

        while(isRun){

            paint();

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    /*
    /   产生边上的随机点
     */
    private void randomPointGeneration(int width, int height) {

        Random r = new Random();

        int widthOffset = width /WIDTH_NUM;
        int heightOffset = height /HEIGHT_NUM;
        for (int i = 0; i < WIDTH_NUM; i++) {
            int random_1 = i * widthOffset + r.nextInt(widthOffset);
            vert_TopVec.add(new PointF(random_1, 0));
            int random_2 = i * widthOffset + r.nextInt(widthOffset);
            vert_BottomVec.add(new PointF(random_2, height));
        }

        m_paint.setColor(Color.argb(255, 200, 200, 20));
        for (int i = 0; i < HEIGHT_NUM; i++) {
            int random_1 = i * heightOffset + r.nextInt(heightOffset);
            hori_LeftVec.add(new PointF(0, random_1));
            int random_2 = i * heightOffset + r.nextInt(heightOffset);
            hori_RightVec.add(new PointF(width, random_2));
        }
    }

    public Vector<Vector<Node>> getNode_Vec() {
        return node_Vec;
    }

    public Vector<PointF> getHori_LeftVec() {
        return hori_LeftVec;
    }

    public Vector<PointF> getHori_RightVec() {
        return hori_RightVec;
    }

    public Vector<PointF> getVert_TopVec() {
        return vert_TopVec;
    }

    public Vector<PointF> getVert_BottomVec() {
        return vert_BottomVec;
    }

//    public Vector<Vector<Up_2_Down_Edge>> getEdge_UpDown() {
//        return edge_UpDown;
//    }
//
//    public Vector<Vector<Left_2_Right_Edge>> getEdge_LeftRight() {
//        return edge_LeftRight;
//    }

    /*
    设置特殊车辆函数
     */
    public void setInner_Specialvehicle(String specialvehicleNum) {
        for (Vector<Left_2_Right_Edge> cur_Vec :edge_LeftRight){
            for (Left_2_Right_Edge edge:cur_Vec){
                Vector<Vehicle> vehicles = edge.getVehicles();
                for (Vehicle vehicle:vehicles){
                    if (vehicle.getInner_Number().equals(specialvehicleNum)){
                        Log.e(TAG, "has a car");
                        this.inner_Specialvehicle = vehicle;
                        return;
                    }
                }
            }
        }
        for (Vector<Up_2_Down_Edge> cur_Vec :edge_UpDown){
            for (Up_2_Down_Edge edge:cur_Vec){
                Vector<Vehicle> vehicles = edge.getVehicles();
                for (Vehicle vehicle:vehicles){
                    if (vehicle.getInner_Number().equals(specialvehicleNum)){
                        Log.e(TAG, "has a car");
                        this.inner_Specialvehicle = vehicle;
                        return;
                    }
                }
            }
        }
    }

    public ArrayList<String> getInner_NumList() {
        return inner_NumList;
    }

    /*
    生成汽车的标识符
     */
    protected void generateVehicleNumber(){
        for (Vector<Left_2_Right_Edge> cur_Vec :edge_LeftRight){
            for (Left_2_Right_Edge edge:cur_Vec){
                Vector<Vehicle> vehicles = edge.getVehicles();
                for (Vehicle vehicle:vehicles){
                    inner_NumList.add(vehicle.getInner_Number());
                }
            }
        }
        for (Vector<Up_2_Down_Edge> cur_Vec :edge_UpDown){
            for (Up_2_Down_Edge edge:cur_Vec){
                Vector<Vehicle> vehicles = edge.getVehicles();
                for (Vehicle vehicle:vehicles){
                    inner_NumList.add(vehicle.getInner_Number());
                }
            }
        }

    }


    /*
    绘图函数，利用产生的边上的随机点，画出街道
     */
    private void paint(){
        canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        drawLines();
        drawNodeInfo();
        drawVehicle();
        drawSpecialVehicle();
        holder.unlockCanvasAndPost(canvas);
    }

    /*
    计算出所有的交点并生成相应的节点
     */
    protected void computerAllNode() {
        for (int i = 0; i < hori_LeftVec.size(); i++) {
            Vector<Node> curVec = new Vector<Node>();
            for (int j = 0; j < vert_BottomVec.size(); j++) {
                PointF point = computerIntersection(hori_LeftVec.get(i), hori_RightVec.get(i), vert_TopVec.get(j), vert_BottomVec.get(j));
                Node node = new Node(point.x, point.y, i, j);
                node.setManager(manager);
                curVec.add(node);
            }
            node_Vec.add(curVec);
        }
    }

    /*
    根据节点信息和边界点信息计算并生成边
     */
    protected void computeAllEdge() {
        Node preNode = null;
        //取横向的边
        for (int i = 0; i < hori_LeftVec.size(); i++) {
            Vector<Left_2_Right_Edge> curVec = new Vector<Left_2_Right_Edge>();
            for (int j = 0; j < vert_TopVec.size(); j++) {
                Node node = node_Vec.get(i).get(j);
                if (j == 0) {
                    Left_2_Right_Edge edge = new Left_2_Right_Edge(i, j, null, node);
                    curVec.add(edge);
                } else if (j == vert_TopVec.size() - 1) {
                    Left_2_Right_Edge edge = new Left_2_Right_Edge(i, j, node_Vec.get(i).get(j-1), node);
                    curVec.add(edge);
                    Left_2_Right_Edge edge_2 = new Left_2_Right_Edge(i, j + 1, node, null);
                    curVec.add(edge_2);
                } else {
                    Left_2_Right_Edge edge = new Left_2_Right_Edge(i, j, node_Vec.get(i).get(j-1), node);
                    curVec.add(edge);
                }
            }
            edge_LeftRight.add(curVec);
        }

        //取纵向的边
        for (int i = 0; i < hori_LeftVec.size(); i++) {
            Vector<Up_2_Down_Edge> curVec = new Vector<Up_2_Down_Edge>();
            for (int j = 0; j < vert_TopVec.size(); j++) {
//                for (Node node : node_Vec) {
//                    if (node.getInner_Horizontal() == i && node.getInner_Vertical() == j) {
//                        if (i == 0) {
//                            preNode = node;
//                            Up_2_Down_Edge edge = new Up_2_Down_Edge(i, j, null, node);
//                            edge_UpDown.add(edge);
//                        } else if (i == hori_LeftVec.size() - 1) {
//                            Up_2_Down_Edge edge = new Up_2_Down_Edge(i, j, preNode, node);
//                            edge_UpDown.add(edge);
//                            edge = new Up_2_Down_Edge(i + 1, j, preNode, node);
//                            edge_UpDown.add(edge);
//                        } else {
//                            Up_2_Down_Edge edge = new Up_2_Down_Edge(i, j, preNode, node);
//                            edge_UpDown.add(edge);
//                        }
//                    }
//                }
                Node node = node_Vec.get(i).get(j);
                if (i == 0) {
                    Up_2_Down_Edge edge = new Up_2_Down_Edge(i, j, null, node);
                    curVec.add(edge);
                } else if (i == hori_LeftVec.size() - 1) {
                    Up_2_Down_Edge edge = new Up_2_Down_Edge(i, j, node_Vec.get(i-1).get(j), node);
                    curVec.add(edge);
                } else {
                    Up_2_Down_Edge edge = new Up_2_Down_Edge(i, j, node_Vec.get(i-1).get(j), node);
                    curVec.add(edge);
                }
            }
            edge_UpDown.add(curVec);
        }

        Vector<Up_2_Down_Edge> curVec = new Vector<Up_2_Down_Edge>();
        for(int i = 0;i<vert_TopVec.size();i++){
            Up_2_Down_Edge edge = new Up_2_Down_Edge(hori_LeftVec.size(),i,node_Vec.get(hori_LeftVec.size()-1).get(i),null);
            curVec.add(edge);
        }
        edge_UpDown.add(curVec);
    }

    /*
    计算交点的方法
    输入两条直线的端点，计算出交点并返回
     */
    private PointF computerIntersection(PointF point1, PointF point2, PointF point3, PointF point4) {
        float x1 = point1.x;
        float x2 = point2.x;
        float x3 = point3.x;
        float x4 = point4.x;

        float y1 = point1.y;
        float y2 = point2.y;
        float y3 = point3.y;
        float y4 = point4.y;

        float x = ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1)) / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));
        float y = ((y1 - y2) * (x3 * y4 - x4 * y3) - (y3 - y4) * (x1 * y2 - x2 * y1)) / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));

        return new PointF(x, y);
    }

    /*
    画出道路的函数
     */
    protected void drawLines() {

        m_paint.setColor(Color.argb(255, 20, 200, 20));
        for (int i = 0; i < WIDTH_NUM; i++) {
            canvas.drawLine(vert_TopVec.get(i).x - 5, 0, vert_BottomVec.get(i).x - 5, height, m_paint);
            canvas.drawLine(vert_TopVec.get(i).x, 0, vert_BottomVec.get(i).x, height, m_paint);
            canvas.drawLine(vert_TopVec.get(i).x + 5, 0, vert_BottomVec.get(i).x + 5, height, m_paint);
        }

        m_paint.setColor(Color.argb(255, 200, 200, 20));
        for (int i = 0; i < HEIGHT_NUM; i++) {
            canvas.drawLine(0, hori_LeftVec.get(i).y - 5, width, hori_RightVec.get(i).y - 5, m_paint);
            canvas.drawLine(0, hori_LeftVec.get(i).y, width, hori_RightVec.get(i).y, m_paint);
            canvas.drawLine(0, hori_LeftVec.get(i).y + 5, width, hori_RightVec.get(i).y + 5, m_paint);
        }
    }

    /*
    画出节点信息的函数
     */
    protected void drawNodeInfo() {
        m_paint.setColor(Color.RED);
        m_paint.setTextSize(25);
        for (Vector<Node> curVec : node_Vec) {
            for(Node node:curVec){
                canvas.drawText(String.valueOf(node.getLeftWaitVehicle()), node.getInner_X() - 45, node.getInner_Y(), m_paint);
                canvas.drawText(String.valueOf(node.getRightWaitVehicle()), node.getInner_X() + 35, node.getInner_Y(), m_paint);
                canvas.drawText(String.valueOf(node.getUpWaitVehicle()), node.getInner_X() - 5, node.getInner_Y() - 40, m_paint);
                canvas.drawText(String.valueOf(node.getDownWaitVehicle()), node.getInner_X() - 5, node.getInner_Y() + 40, m_paint);
            }
        }

        m_paint.setColor(Color.argb(255,200,50,200));
        m_paint.setTextSize(30);
        for (Vector<Node> curVec : node_Vec) {
            for(Node node:curVec){
                canvas.drawText(String.valueOf(node.getNodeLastTime()), node.getInner_X() - 5, node.getInner_Y(), m_paint);
            }
        }
    }

    /*
    画出所有车辆位置
     */
    protected void drawVehicle(){
        m_paint.setColor(Color.BLACK);
        m_paint.setStyle(Paint.Style.FILL);

        for (Vector<Left_2_Right_Edge> cur_Vec :edge_LeftRight){
            for (Left_2_Right_Edge edge:cur_Vec){
                Vector<Vehicle> points = edge.getVehicles();
                for (Vehicle vehicle:points){
                    canvas.drawCircle(vehicle.getInner_X(),vehicle.getInner_Y(),4,m_paint);
                }
            }
        }
        for (Vector<Up_2_Down_Edge> cur_Vec :edge_UpDown){
            for (Up_2_Down_Edge edge:cur_Vec){
                Vector<Vehicle> points = edge.getVehicles();
                for (Vehicle vehicle:points){
                    canvas.drawCircle(vehicle.getInner_X(),vehicle.getInner_Y(),4,m_paint);
                }
            }
        }
    }

    /*
    画出特定节点
     */
    protected void drawSpecialVehicle(){
        m_paint.setColor(Color.RED);
        m_paint.setStyle(Paint.Style.FILL);
        if(inner_Specialvehicle != null){
            canvas.drawCircle(inner_Specialvehicle.getInner_X(),inner_Specialvehicle.getInner_Y(),20,m_paint);
        }
    }

    /*
    设置所有节点的邻接节点
     */
    protected void setAllNodeAdjoinNode() {
        for(int i = 0;i<hori_LeftVec.size();i++){
            for (int j = 0;j<vert_TopVec.size();j++){
                Node node = node_Vec.get(i).get(j);
                if(i == 0){
                    if(j == 0){
                        node.setAdjoinNode(null,node_Vec.get(i+1).get(j),null,node_Vec.get(i).get(j+1));
                    }
                    else if(j == vert_TopVec.size() - 1){
                        node.setAdjoinNode(null,node_Vec.get(i+1).get(j),node_Vec.get(i).get(j-1),null);
                    }
                    else{
                        node.setAdjoinNode(null,node_Vec.get(i+1).get(j),node_Vec.get(i).get(j-1),node_Vec.get(i).get(j+1));
                    }
                }
                else if(i == hori_LeftVec.size() -1){
                    if(j == 0){
                        node.setAdjoinNode(node_Vec.get(i-1).get(j),null,null,node_Vec.get(i).get(j+1));
                    }
                    else if(j == vert_TopVec.size() - 1){
                        node.setAdjoinNode(node_Vec.get(i-1).get(j),null,node_Vec.get(i).get(j-1),null);
                    }
                    else{
                        node.setAdjoinNode(node_Vec.get(i-1).get(j),null,node_Vec.get(i).get(j-1),node_Vec.get(i).get(j+1));
                    }
                }
                else{
                    if(j == 0){
                        node.setAdjoinNode(node_Vec.get(i-1).get(j),node_Vec.get(i+1).get(j),null,node_Vec.get(i).get(j+1));
                    }
                    else if(j == vert_TopVec.size() - 1){
                        node.setAdjoinNode(node_Vec.get(i-1).get(j),node_Vec.get(i+1).get(j),node_Vec.get(i).get(j-1),null);
                    }
                    else{
                        node.setAdjoinNode(node_Vec.get(i-1).get(j),node_Vec.get(i+1).get(j),node_Vec.get(i).get(j-1),node_Vec.get(i).get(j+1));
                    }
                }
            }
        }
    }

    /*
    设置所有节点的输入边
     */
    protected void setAllNodeInputEdge() {
        for(int i = 0;i<hori_LeftVec.size();i++){
            for(int j = 0;j<vert_TopVec.size();j++){
                node_Vec.get(i).get(j).setInputEdge(edge_UpDown.get(i).get(j),
                                                    edge_UpDown.get(i+1).get(j),
                                                    edge_LeftRight.get(i).get(j),
                                                    edge_LeftRight.get(i).get(j+1));
            }
        }
    }

    /*
    路径规划
     */
    public Vector<PointF> pathPlanning(PointF begin,PointF end){
        Vector<PointF> points = new Vector<PointF>();

        return points;
    }

    /*
    处理客户端初始化请求
     */
    public String handleInitalizaMessage(){
        String returnData = null;
        return returnData;
    }
}
