/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.ui.main;

import android.os.Bundle;
import android.widget.TextView;

import com.example.hwan.myapplication.R;
import com.example.hwan.myapplication.dto.CounterDto;
import com.example.hwan.myapplication.ui.base.AbstractBaseActivity;
import com.example.hwan.myapplication.ui.base.BaseViewController;
import com.example.hwan.myapplication.ui.base.LifecycleObservableView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public class MainActivity extends AbstractBaseActivity implements MainActivityView {
    @BindView(R.id.textView)
    /*default*/ TextView textView;

    @Inject
    /*default*/ MainActivityViewController controller;

    @Override
    protected List<? extends BaseViewController<? extends LifecycleObservableView>> getControllers() {
        return Collections.singletonList(controller);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        controller.attachView(this);
        controller.getCounter();
        textView.setText(R.string.hello);
    }

    @Override
    public void showCounterResult(final CounterDto counter) {
        System.out.println("Counter   : " + counter.getCounter());
        System.out.println("LastAccess: " + counter.getLastAccessed());
    }

    @Override
    public void showCounterError(final Throwable exception) {
        System.out.println("onError");
        exception.printStackTrace();
    }
}
