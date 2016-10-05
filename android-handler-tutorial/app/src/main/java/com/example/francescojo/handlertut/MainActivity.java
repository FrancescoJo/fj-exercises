package com.example.francescojo.handlertut;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickTutorial1(View button) {
        startActivity(new Intent(this, Tutorial1Activity.class));
    }

    public void onClickTutorial2(View button) {
        startActivity(new Intent(this, Tutorial2Activity.class));
    }

    public void onClickTutorial3(View button) {
        startActivity(new Intent(this, Tutorial3Activity.class));
    }

    public void onClickTutorial4(View button) {
        startActivity(new Intent(this, Tutorial4Activity.class));
    }
}
