package com.example.administrator.myapplication;

<<<<<<< HEAD
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SelectCity extends Activity implements View.OnClickListener {
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        backBtn = findViewById(R.id.title_selectCity_back);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_selectCity_back:
                finish();
                break;
            default:
                break;
        }
    }
=======
public class SelectCity {
>>>>>>> eae34a39e93af0633c2e455b94741b99433fa9f1
}
