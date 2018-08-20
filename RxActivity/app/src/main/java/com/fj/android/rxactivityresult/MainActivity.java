package com.fj.android.rxactivityresult;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.fj.com.rxactivity.RxActivity;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickResults(View v) {
        Intent intent = new Intent(this, ResultMakerActivity.class);
        intent.putExtra("DUMMY_KEY", "Hello from MainActivity");
        RxActivity.startActivityForResult(this, intent)
                .subscribe(new Consumer<Pair<Integer, Intent>>() {
                    @Override
                    public void accept(@NonNull Pair<Integer, Intent> result) throws Exception {
                        if (Activity.RESULT_OK == result.first) {
                            Toast.makeText(MainActivity.this,
                                    result.first + ":" + result.second.getStringExtra("RESULT"),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "Cancelled",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @TargetApi(23)
    public void onClickPermission(View v) {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        RxActivity.requestPermissions(this, permissions)
                .subscribe(new Consumer<Pair<String[], int[]>>() {
                    @Override
                    public void accept(@NonNull Pair<String[], int[]> grantedPermissions) throws Exception {
                        for (int i = 0; i < grantedPermissions.first.length; i++) {
                            String permission = grantedPermissions.first[i];
                            int grantStatus = grantedPermissions.second[i];
                            Toast.makeText(MainActivity.this,
                                    permission + " : " + grantStatus,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /* We don't need to shit around with this method anymore!! */
    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        // no-op
    }

    /* We don't need to shit around with this method anymore!! */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // no-op
    }
}
