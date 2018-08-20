/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.inject.component;

import com.fj.android.mediamonkey.annotation.PersistOnConfigChange;
import com.fj.android.mediamonkey.inject.module.ActivityModule;

import dagger.Component;

/**
 * A dagger component that will live during the lifecycle of an Activity but it won't
 * be destroyed during configuration changes. (i.e. View Controllers)
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
@PersistOnConfigChange
@Component(dependencies = AppComponent.class)
public interface PersistOnConfigChangeComponent {
    ActivityComponent activityComponent(ActivityModule module);
}