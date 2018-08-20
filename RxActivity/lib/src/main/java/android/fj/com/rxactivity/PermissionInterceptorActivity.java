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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.Window;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * This, invisible screen captures delivered result(s) from target Activity
 * via callback method {@link Activity#onActivityResult(int, int, Intent)}
 * and transforms that result as reactive form.
 * <p>
 * This class is designed for internal use.
 * Do not invoke this screen by explicit intent in your logic directly.
 * </p>
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Mar - 2017
 */
public final class PermissionInterceptorActivity extends Activity {
    /*
     * Default accessors to prevent synthetic accessor creation.
     */
    @SuppressWarnings("WeakerAccess")
    /*default*/ static String[]                                 param;
    @SuppressWarnings("WeakerAccess")
    /*default*/ static ObservableEmitter<Pair<String[], int[]>> resultEmitter;

    private static String[] permissions;
    private static int[]    grantResults;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestPermissions(param, Short.MAX_VALUE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionInterceptorActivity.permissions  = permissions;
        PermissionInterceptorActivity.grantResults = grantResults;
        finish();
    }

    @Override
    protected void onDestroy() {
        resultEmitter.onNext(new Pair<>(permissions, grantResults));
        resultEmitter.onComplete();
        PermissionInterceptorActivity.permissions   = null;
        PermissionInterceptorActivity.grantResults  = null;
        PermissionInterceptorActivity.param         = null;
        PermissionInterceptorActivity.resultEmitter = null;

        super.onDestroy();
    }

    static Observable<Pair<String[], int[]>> subscribe(final Activity activity, final String[] permissions) {
        return Observable.create(new ObservableOnSubscribe<Pair<String[], int[]>>() {
            @Override
            public void subscribe(final ObservableEmitter<Pair<String[], int[]>> e) throws Exception {
                PermissionInterceptorActivity.param         = permissions;
                PermissionInterceptorActivity.resultEmitter = e;

                activity.startActivity(new Intent(activity, PermissionInterceptorActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
    }
}
