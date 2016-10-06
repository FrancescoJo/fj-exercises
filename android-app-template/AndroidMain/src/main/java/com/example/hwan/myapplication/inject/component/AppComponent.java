/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.inject.component;

import com.example.hwan.myapplication.api.ApiService;
import com.example.hwan.myapplication.event.RxEventBus;
import com.example.hwan.myapplication.ui.main.MainActivity;
import com.example.hwan.myapplication.inject.module.AppModule;
import com.example.hwan.myapplication.inject.module.NetworkApiModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
@Singleton
@Component(modules = {AppModule.class, NetworkApiModule.class})
public interface AppComponent {
    ApiService apiService();

    RxEventBus eventBus();
}
