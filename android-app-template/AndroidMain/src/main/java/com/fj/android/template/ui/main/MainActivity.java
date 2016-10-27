/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.ui.main;

import android.os.Bundle;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.fj.android.template.R;
import com.fj.android.template.dto.CounterDto;
import com.fj.android.template.ui.base.AbstractBaseActivity;
import com.fj.android.template.ui.base.BasePresenter;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public class MainActivity extends AbstractBaseActivity implements MainActivityView {
    @BindView(R.id.textView)
    /*default*/ TextView textView;
    @BindView(R.id.textView2)
    /*default*/ TextView textView2;

    @Inject
    /*default*/ MainActivityPresenter presenter;

    @Override
    protected List<? extends BasePresenter> getPresenters() {
        return Collections.singletonList(presenter);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.attachView(this);
        presenter.displayCounter();
        textView.setText(R.string.hello);
    }

    @Override
    public void showCounterResult(final CounterDto counter) {
        textView2.setText("Number of visitors: " + counter.getCounter());
    }

    @Override
    public void showCounterError(final Throwable exception) {
        textView2.setText("Exception occurred: " + exception.getMessage());
        exception.printStackTrace();
    }
}
