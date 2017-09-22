package layout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiaotiange.activity_trans.DrawView;
import com.example.xiaotiange.activity_trans.Messager;
import com.example.xiaotiange.activity_trans.Node;
import com.example.xiaotiange.activity_trans.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ViewFragment extends Fragment {

    private Messager inner_Messager;

    private DrawView inner_View;

    private Runnable updateRunable;

    private Handler inner_Handler;

    private View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inner_Handler = new Handler();

        root = inflater.inflate(R.layout.fragment_view, container, false);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        inner_View = (DrawView)getActivity().findViewById(R.id.draw_View);

        updateRunable = new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<String>();

                if (inner_View != null) {
                    for (Vector<Node> nodeVec : inner_View.getNode_Vec()) {
                        for (Node node : nodeVec) {
                            if (node!=null){
                                list.add("上侧车辆：" + String.valueOf(node.getUpWaitVehicle() +
                                        " 下侧车辆：" + String.valueOf(node.getDownWaitVehicle()) +
                                        " 左侧车辆：" + String.valueOf(node.getLeftWaitVehicle()) +
                                        " 右侧车辆：" + String.valueOf(node.getRightWaitVehicle()) +
                                        " 当前行车方向：" + node.getCurrentDirection() +
                                        " 剩余绿灯时间：" + node.getNodeLastTime()));
                            }
                        }
                    }
                    inner_Messager.setMessage(list);
                }
            }
        };

        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
                while(true){

                    boolean ok = inner_Handler.post(updateRunable);
                    Log.e("hello", String.valueOf(ok));
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void setMessager(Messager messager) {
        this.inner_Messager = messager;
    }
}
