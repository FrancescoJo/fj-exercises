/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.inject.component;

import com.fj.android.template.annotation.PersistOnConfigChange;
import com.fj.android.template.inject.module.ActivityModule;
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