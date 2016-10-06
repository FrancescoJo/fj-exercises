/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.inject.module;

import com.example.hwan.myapplication.event.RxEventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
@Module
public final class AppModule {
    @Provides
    @Singleton
    RxEventBus providesEventBus() {
        return new RxEventBus();
    }
}
