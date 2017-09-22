package com.example.xiaotiange.activity_trans;

import android.util.Log;

import java.util.List;

/**
 * Created by xiaotiange on 2017/9/10.
 */

public class Messager {

    private static String TAG = "messager";

    private List<String> inner_Message;

    public void setMessage(List<String> message){
        Log.e(TAG, "setMessage: ");
        this.inner_Message = message;
    }

    public List<String> getMessage(){
        Log.e(TAG, "getMessage: ");
        return this.inner_Message;
    }
}
