package com.fj.android.rxactivityresult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Sample code for result-making.
 * You can see no restrictions at here!!
 */
public class ResultMakerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_maker);

        Toast.makeText(this, getIntent().getStringExtra("DUMMY_KEY"), Toast.LENGTH_SHORT).show();
    }

    public void onClickFinish(View v) {
        Intent result = new Intent();
        result.putExtra("RESULT", "Result from ResultMakerActivity");
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
