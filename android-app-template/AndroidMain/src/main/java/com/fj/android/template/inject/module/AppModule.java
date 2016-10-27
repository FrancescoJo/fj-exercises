/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.inject.module;

import com.fj.android.template.event.RxEventBus;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

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
