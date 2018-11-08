package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (CheckNet.getNetStatus(this) == CheckNet.NET_NONE) {
            Log.d("My Application","无网络连接!");
            Toast.makeText(MainActivity.this,"无网络连接!",Toast.LENGTH_LONG).show();
        } else {
            Log.d("My Application","网络连通");
            Toast.makeText(MainActivity.this,"网络连通。",Toast.LENGTH_LONG).show();
        }
    }
}
