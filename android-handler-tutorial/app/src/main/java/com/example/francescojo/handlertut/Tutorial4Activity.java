package com.example.francescojo.handlertut;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class Tutorial4Activity extends Activity {
    private TextView textView;
    private Subscription countdownSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial4);

        this.textView = (TextView) findViewById(R.id.timerTextView4);

        /*
           Another approach using ReactiveX
           Subscribe to stream and define proper task.

           see also: https://realm.io/kr/news/rxandroid/
         */
        this.countdownSubscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                System.out.println("--- Loop started ---");
                for(int count = 10; count >= 0; count--) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(count);
                        System.out.println("Thread is running: " + count);
                        SystemClock.sleep(1000L);
                    }
                }
                System.out.println("--- Loop stopped ---");
                subscriber.onCompleted();
            }
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer count) {
                    textView.setText("Current count: " + count);
                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countdownSubscription.unsubscribe();
    }
}
