package com.example.xiaotiange.activity_trans;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xiaotiange on 2017/9/7.
 *
 * 服务端线程，当新来一个客户端请求，就对新的客户端请求建立一个线程进行通信
 */

public class ServerThread extends Thread {

    private static String TAG = "Server thread";

    private DrawView inner_View;

    public ServerThread(DrawView view){
        this.inner_View = view;
    }

    @Override
    public void run() {
        super.run();

        try {
            ServerSocket serverSocket = new ServerSocket(6666);

            if (serverSocket != null){
                Log.e(TAG, "start ...");
            }

            Socket  clientSocket = null;

            boolean isRun = true;

            while(isRun){

                clientSocket = serverSocket.accept();

                Log.e(TAG, "新的连接建立");

                new ClientThread(clientSocket,this).start();

            }

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public DrawView getDrawView(){
        if(inner_View != null){
            return inner_View;
        }
        return null;
    }
}
