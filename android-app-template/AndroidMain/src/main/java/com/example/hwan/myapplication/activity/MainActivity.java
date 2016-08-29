/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.hwan.myapplication.MyActivity;
import com.example.hwan.myapplication.MyApplication;
import com.example.hwan.myapplication.R;
import com.example.hwan.myapplication.api.ApiService;
import com.example.hwan.myapplication.dto.CounterDto;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public class MainActivity extends MyActivity {
    @Inject
    public ApiService apiService;
    @BindView(R.id.textView)
    /*default*/ TextView textView;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getInstance().getDiComponent().inject(this);
        this.unbinder = ButterKnife.bind(this);

        textView.setText(R.string.hello);
        apiService.getCounter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(getObservableLifecycle(LifeCycle.ON_DESTROY))
                .subscribe(new Action1<CounterDto>() {
                    @Override
                    public void call(CounterDto counterDto) {
                        System.out.println("onNext");
                        System.out.println("Counter   : " + counterDto.getCounter());
                        System.out.println("LastAccess: " + counterDto.getLastAccessed());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable exception) {
                        System.out.println("onError");
                        exception.printStackTrace();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        System.out.println("onCompleted");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
