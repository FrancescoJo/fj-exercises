package com.example.francescojo.handlertut;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.widget.TextView;

public class Tutorial3Activity extends Activity {
    private TextView textView;
    private Handler eventHandler;
    private Thread countDownThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial3);

        this.textView = (TextView) findViewById(R.id.timerTextView3);
        this.eventHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int count = (int) msg.obj;
                textView.setText("Current count: " + count);
            }
        };

        /*
           Low-level (UI draw) message handling
           Works fine, but both Handler and message should know its context in
           java.lang.Object form and predefined 'what' integer field.
           This model will be confusing if your app grows larger.
           (Suppose that there's a multiple data retrieving Activity.)

           See also: android.os.AsyncTask
         */
        this.countDownThread = new Thread() {
            @Override
            public void run() {
                System.out.println("--- Loop started ---");
                for (int count = 10; count >= 0; count--) {
//                    Message msg = Message.obtain();
//                    msg.setTarget(eventHandler);
                    Message msg = eventHandler.obtainMessage();
                    msg.obj = count;

//                    eventHandler.sendMessage(msg);
                    msg.sendToTarget();
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
