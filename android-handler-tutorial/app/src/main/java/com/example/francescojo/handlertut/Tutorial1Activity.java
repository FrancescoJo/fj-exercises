package com.example.francescojo.handlertut;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

public class Tutorial1Activity extends Activity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial1);

        this.textView = (TextView) findViewById(R.id.timerTextView1);

        /*
           We can't observe the number changes. More worse, the screen will jitter in ten seconds,
           because we're performing sleep on UI thread. Usually, Android will shut down
           unresponsive apps like this if an app stops its UI more than 5 seconds.
           In this case, sleep only 1 second in 10 times will not cause ANR(App Not Responding).
          */
        System.out.println("--- Loop started ---");
        for (int count = 10; count >= 0; count--) {
            textView.setText("Current count: " + count);
            System.out.println("Thread is running: " + count);
            SystemClock.sleep(1000L);
        }
        System.out.println("--- Loop stopped ---");
    }
}
