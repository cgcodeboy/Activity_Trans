package com.example.xiaotiange.activity_trans;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import layout.NavigationFragment;
import layout.ViewFragment;

public class NavigationActivity extends AppCompatActivity {
    private static String TAG = "Navigation";

    private android.support.v4.app.FragmentManager manager;
    private android.support.v4.app.FragmentTransaction transaction;

    private ViewFragment viewFragment;
    private NavigationFragment navigationFragment;

    private Messager inner_Messager;

    @Override
    public void onStart() {
        super.onStart();

        inner_Messager = new Messager();

        viewFragment = new ViewFragment();
        navigationFragment = new NavigationFragment();
        viewFragment.setMessager(inner_Messager);
        navigationFragment.setMessager(inner_Messager);


        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_Layout,viewFragment);
        transaction.add(R.id.fragment_Layout,navigationFragment);
//        transaction.replace(R.id.fragment_Layout,navigationFragment);
//        transaction.replace(R.id.fragment_Layout,viewFragment);
        transaction.commitNow();
        boolean ok = manager.executePendingTransactions();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    manager = getSupportFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.hide(navigationFragment);
                    transaction.show(viewFragment);
                    transaction.commitNow();
                    return true;
                case R.id.navigation_dashboard:
                    manager = getSupportFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.hide(viewFragment);
                    transaction.show(navigationFragment);
                    transaction.commitNow();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
