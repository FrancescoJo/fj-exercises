/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.inject.component;

import com.example.hwan.myapplication.inject.module.ActivityModule;
import com.example.hwan.myapplication.ui.main.MainActivity;

import dagger.Subcomponent;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity activity);
}
