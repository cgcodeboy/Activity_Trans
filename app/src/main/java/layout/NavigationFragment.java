package layout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.xiaotiange.activity_trans.DrawView;
import com.example.xiaotiange.activity_trans.Messager;
import com.example.xiaotiange.activity_trans.R;

import java.util.ArrayList;
import java.util.List;

public class NavigationFragment extends Fragment {

    private Messager inner_Messager;

    private static String TAG = "fragment";

    private DrawView drawview;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private Handler updateHandle;

    private Runnable updateRunable;

    private List<String> dataList;

    private Context context;

    private View root;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "hello1");

        updateHandle = new Handler();

        context = this.getContext();

        root = inflater.inflate(R.layout.fragment_navigation, container, false);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        listView = (ListView)getActivity().findViewById(R.id.data_Listview);

        dataList = new ArrayList<String>();

        updateRunable = new Runnable() {
            @Override
            public void run() {
                if (dataList!=null){
                    adapter = new ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1,dataList);
                    listView.setAdapter(adapter);
                }
            }
        };

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(true){
                    dataList = inner_Messager.getMessage();
                    updateHandle.post(updateRunable);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void setMessager(Messager messager){
        this.inner_Messager = messager;
    }
}
