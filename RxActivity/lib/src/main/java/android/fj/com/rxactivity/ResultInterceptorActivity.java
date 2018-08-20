/*
 * Copyright 2017 Francesco Jo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.fj.com.rxactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.Window;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * This, invisible screen captures delivered result(s) from target Activity
 * via callback method {@link android.app.Activity#onActivityResult(int, int, Intent)}
 * and transforms that result as reactive form.
 * <p>
 * This class is designed for internal use.
 * Do not invoke this screen by explicit intent in your logic directly.
 * </p>
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Mar - 2017
 */
public final class ResultInterceptorActivity extends Activity {
    /*
     * static referencing To prevent double-marshalling process of original
     * intent and bundles. However, these are bad practice and must not be
     * leaked to outside world, and must be cleaned up as soon as possible!
     *
     * Default accessors to prevent synthetic accessor creation.
     */
    @SuppressWarnings("WeakerAccess")
    /*default*/ static Intent                                   param;
    @SuppressWarnings("WeakerAccess")
    /*default*/ static ObservableEmitter<Pair<Integer, Intent>> resultEmitter;

    private static int    resultCode = RESULT_CANCELED;
    private static Intent data;

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        startActivityForResult(param, Short.MAX_VALUE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        ResultInterceptorActivity.resultCode = resultCode;
        ResultInterceptorActivity.data       = data;
        finish();
    }

    @Override
    protected void onDestroy() {
        resultEmitter.onNext(new Pair<>(resultCode, data));
        resultEmitter.onComplete();
        ResultInterceptorActivity.resultCode    = RESULT_CANCELED;
        ResultInterceptorActivity.data          = null;
        ResultInterceptorActivity.param         = null;
        ResultInterceptorActivity.resultEmitter = null;

        super.onDestroy();
    }

    static Observable<Pair<Integer, Intent>> subscribe(final Context context, final Intent param) {
        return Observable.create(new ObservableOnSubscribe<Pair<Integer, Intent>>() {
            @Override
            public void subscribe(final ObservableEmitter<Pair<Integer, Intent>> e) throws Exception {
                ResultInterceptorActivity.param         = param;
                ResultInterceptorActivity.resultEmitter = e;

                context.startActivity(new Intent(context, ResultInterceptorActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
    }
}
