package com.example.xiaotiange.activity_trans;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class NextActivity extends AppCompatActivity {

    DrawView drawView;
//    UpdateThread updateThread;

    private static String TAG = "next activity";

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_next);

        drawView = (DrawView)findViewById(R.id.draw_View);

        ServerThread thread = new ServerThread(drawView);
//
//        updateThread = new UpdateThread(infoText);
//        updateThread.start();
//
        thread.start();
//
//        drawView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int x = (int)event.getX();
//                int y = (int)event.getY();
//                for(int i = 0;i<drawView.getNode_Vec().size();i++){
//                    for(int j = 0;j<drawView.getNode_Vec().get(i).size();j++){
//                        Node node = drawView.getNode_Vec().get(i).get(j);
//                        if(Math.abs(node.getInner_X()-x)<30&&Math.abs(node.getInner_Y()-y)<30){
//                            updateThread.setInner_SpecialNode(node);
//                        }
//                    }
//                }
//
//                return false;
//            }
//        });

        spinner = (Spinner)findViewById(R.id.vehicle_Spinner);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,drawView.getInner_NumList());
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSelection(0,true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "clicked" );
                String clickStr = (String)spinner.getItemAtPosition(position);
                Log.e(TAG, clickStr );
                for (String str:drawView.getInner_NumList()){
                    if(clickStr.equals(str)){
                        Log.e(TAG, "selected" );
                        drawView.setInner_Specialvehicle(str);
                        return;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
//
//    private class UpdateThread extends Thread{
//
//        private Node inner_SpecialNode = null;
//        private EditText info_Text;
//
//        public UpdateThread(EditText infoText) {
//            this.info_Text = infoText;
//        }
//
//        @Override
//        public void run() {
//            while(true){
//                if (inner_SpecialNode !=null){
//                    info_Text.setText("坐标："+String.valueOf((int)inner_SpecialNode.getInner_X())+" "+String.valueOf((int)inner_SpecialNode.getInner_Y())+
//                            " 剩余时间："+inner_SpecialNode.getNodeLastTime()+" 交通方向："+inner_SpecialNode.getCurrentDirection());
//                }
//                try {
//                    sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        public void setInner_SpecialNode(Node inner_SpecialNode) {
//            Log.e(TAG, "setInner_SpecialNode: ");
//            this.inner_SpecialNode = inner_SpecialNode;
//        }
}
