/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.dagger2.component;

import com.example.hwan.myapplication.activity.MainActivity;
import com.example.hwan.myapplication.dagger2.module.NetworkApiModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
@Singleton
@Component(modules = {NetworkApiModule.class})
public interface DiComponent {
    void inject(MainActivity activity);
}
