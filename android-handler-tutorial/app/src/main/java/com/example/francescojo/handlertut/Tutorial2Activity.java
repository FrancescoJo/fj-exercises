package com.example.francescojo.handlertut;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

public class Tutorial2Activity extends Activity {
    private TextView textView;
    private Thread countDownThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial2);

        this.textView = (TextView) findViewById(R.id.timerTextView2);

        /*
         * Android threads are basically not associated with Main event looper.
         * Thus, this approach will crash our app
         *
         * see also: android.os.Looper
         */
        this.countDownThread = new Thread() {
            @Override
            public void run() {
                System.out.println("--- Loop started ---");
                for (int count = 10; count >= 0; count--) {
                    textView.setText("Current count: " + count);
                    System.out.println("Thread is running: " + count);
                    SystemClock.sleep(1000L);
                }
                System.out.println("--- Loop stopped ---");
            }
        };
        countDownThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownThread.interrupt();
    }
}
