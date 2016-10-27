/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.inject.component;

import com.fj.android.template.api.ApiService;
import com.fj.android.template.event.RxEventBus;
import com.fj.android.template.inject.module.AppModule;
import com.fj.android.template.inject.module.NetworkApiModule;
import dagger.Component;

import javax.inject.Singleton;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
@Singleton
@Component(modules = { AppModule.class, NetworkApiModule.class })
public interface AppComponent {
    ApiService apiService();

    RxEventBus eventBus();
}
