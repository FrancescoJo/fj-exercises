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
import android.support.v4.util.Pair;

import io.reactivex.Observable;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Mar - 2017
 */
public final class RxActivity {
    /**
     * Starts an activity and transforms its result as reactive form.
     *
     * @param context A caller context to perform given intent.
     * @param param   An activity invocation intent which contains necessary parameters.
     * @return a {@link android.support.v4.util.Pair} of <code>resultCode</code> and <code>data</code>
     * which can be obtained after performing given intent.
     * @see android.app.Activity#onActivityResult(int, int, Intent)
     */
    public static Observable<Pair<Integer, Intent>> startActivityForResult(final Context context, final Intent param) {
        return ResultInterceptorActivity.subscribe(context, param);
    }

    /**
     * Starts an dialogue which requests user to grant given permissions or not,
     * and transforms its result as reactive form.
     *
     * @param activity       A caller activity to display the permission asking dialogue.
     * @param permissions    The requested permissions. Must be non-null and not empty.
     * @return a {@link android.support.v4.util.Pair} of requested permissions and grant results
     * after the permission asking dialog finishes.
     * @see android.app.Activity#requestPermissions(String[], int)
     */
    public static Observable<Pair<String[], int[]>> requestPermissions(final Activity activity, final String[] permissions) {
        return PermissionInterceptorActivity.subscribe(activity, permissions);
    }

    private RxActivity() {}
}
