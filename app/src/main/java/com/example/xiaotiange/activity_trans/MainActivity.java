package com.example.xiaotiange.activity_trans;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.ThemeOverlay_AppCompat_Dark);
        setContentView(R.layout.activity_main);

        Button login_Bt = (Button)findViewById(R.id.login_BT);

        login_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name_Edit = (EditText)findViewById(R.id.name_Edit);
                EditText password_Edit = (EditText)findViewById(R.id.password_Edit);
                String name_Str = name_Edit.getEditableText().toString();
                String password_Str = password_Edit.getEditableText().toString();

                Log.e(TAG, name_Str +" "+password_Str);

                if (name_Str.equals("manager") && password_Str.equals("123456")){
                    Log.e(TAG, "onClick");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,NavigationActivity.class);
                    MainActivity.this.startActivity(intent);
                }
            }
        });
    }
}
