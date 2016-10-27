/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.ui.main;

import com.fj.android.template._base.AndroidTestBase;
import com.fj.android.template.api.ApiService;
import com.fj.android.template.dto.CounterDto;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
public class MainActivityPresenterTest extends AndroidTestBase {
    @Mock
    private ApiService mockApiService;
    @Mock
    private MainActivityView mockView;
    private MainActivityPresenter vc;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setMockApiService(mockApiService);
        this.vc = new MainActivityPresenter();
        vc.attachView(mockView);
    }

    @Test
    public void testGetCounterSuccess() {
        final CounterDto expected = new CounterDto();
        Observable<CounterDto> mockObservable = Observable.create(new ObservableOnSubscribe<CounterDto>() {
            @Override
            public void subscribe(ObservableEmitter<CounterDto> emitter) throws Exception {
                emitter.onNext(expected);
                emitter.onComplete();
            }
        });
        when(mockApiService.getCounter()).thenReturn(mockObservable);

        vc.displayCounter();

        verify(mockView, times(1)).showCounterResult(expected);
    }

    @Test
    public void testGetCounterFail() {
        final IOException ex = new IOException();
        Observable<CounterDto> mockObservable = Observable.create(new ObservableOnSubscribe<CounterDto>() {
            @Override
            public void subscribe(ObservableEmitter<CounterDto> emitter) throws Exception {
                throw ex;
            }
        });
        when(mockApiService.getCounter()).thenReturn(mockObservable);

        vc.displayCounter();

        verify(mockView, times(1)).showCounterError(ex);
    }

    @After
    public void tearDown() throws Exception {
        setMockApiService(null);
        vc.detachView(false);
        super.tearDown();
    }
}
