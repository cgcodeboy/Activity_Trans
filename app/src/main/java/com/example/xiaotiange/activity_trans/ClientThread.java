package com.example.xiaotiange.activity_trans;

import android.graphics.PointF;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by xiaotiange on 2017/9/7.
 *
 * 该线程是用于对新来的客户端的请求进行通讯的。
 */

public class ClientThread extends Thread {

    private static String TAG = "client";

    private Socket innner_Socket;

    private ServerThread inner_FatherServerThread;

    private OutputStream outputStream;
    private InputStream inputStream;


    public ClientThread(Socket socket,ServerThread serverThread){
        this.innner_Socket = socket;
        try {
            outputStream = innner_Socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream = innner_Socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.inner_FatherServerThread = serverThread;
    }

    @Override
    public void run() {
        super.run();

        try {
            OutputStream outputStream = innner_Socket.getOutputStream();

            InputStream inputStream = innner_Socket.getInputStream();

            boolean isRun = true;

            while(isRun){

                if (inputStream.available()!=0){
                    int availabel = inputStream.available();

                    Log.e(TAG, String.valueOf(availabel));
                    byte strByte[] = new byte[availabel];

                    inputStream.read(strByte);

                    String str = new String(strByte);

                    Log.e(TAG, str );

                    if (str.equals("bye")){
                        isRun = false;
                    }

                    else if(str.equals("initialize")){
                        String returnData = "";
                        Vector<PointF> points;

                        int width = inner_FatherServerThread.getDrawView().getScreenWidth();
                        int height = inner_FatherServerThread.getDrawView().getScreenHeight();

                        points = this.inner_FatherServerThread.getDrawView().getVert_TopVec();
                        for (PointF point :points){
                            Log.e(TAG, String.valueOf(point.x) );
                            returnData+=String.valueOf(point.x/width);
                            returnData+="@";
                        }
                        returnData+="#";
                        points = this.inner_FatherServerThread.getDrawView().getVert_BottomVec();
                        for (PointF point :points){
                            returnData+=String.valueOf(point.x/width);
                            returnData+="@";
                        }
                        returnData+="#";
                        points = this.inner_FatherServerThread.getDrawView().getHori_LeftVec();
                        for (PointF point :points){
                            returnData+=String.valueOf(point.y/height);
                            returnData+="@";
                        }
                        returnData+="#";
                        points = this.inner_FatherServerThread.getDrawView().getHori_RightVec();
                        for (PointF point :points){
                            returnData+=String.valueOf(point.y/height);
                            returnData+="@";
                        }
                        returnData+="#";
                        outputStream.write(returnData.getBytes());
                    }

                    else if(str.substring(8) == "planning"){
                        int index = str.indexOf('#');
                        String useful_Str = str.substring(index);
                        String propStr[] = useful_Str.split("@");
                        float begin_Prop_X = Float.parseFloat(propStr[0]);
                        float begin_Prop_Y = Float.parseFloat(propStr[0]);
                        float end_Prop_X = Float.parseFloat(propStr[0]);
                        float end_Prop_Y = Float.parseFloat(propStr[0]);

                        String returnData = null;
                        int width = inner_FatherServerThread.getDrawView().getScreenWidth();
                        int height = inner_FatherServerThread.getDrawView().getScreenHeight();

                        Vector<PointF> points = inner_FatherServerThread.getDrawView().pathPlanning(new PointF(begin_Prop_X,begin_Prop_Y),new PointF(end_Prop_X,end_Prop_Y));
                        for (PointF point:points){
                            returnData.concat(String.valueOf((float)point.x/width));
                            returnData.concat("@");
                            returnData.concat(String.valueOf((float)point.y/height));
                            returnData.concat("@");
                            returnData.concat("#");
                        }
                        outputStream.write(returnData.getBytes());
                    }
                    else{
                        Log.e(TAG,str);
                    }
                }
            }

            innner_Socket.close();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        try {
            this.outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
//    public String getMessage(){
//        return this.inner_Data;
//    }
}
