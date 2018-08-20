/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.inject.objects;

import android.text.TextUtils;

import com.fj.android.mediamonkey.util.event.Event;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * A simple Event bus made of Rx.
 * This class is designed to be injected. Do not instantiate it directly.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 10 - Oct - 2016
 */
@Singleton
public class RxEventBus {
    private final Subject<Event> busSubject;

    @Inject
    public RxEventBus() {
        busSubject = PublishSubject.<Event>create().toSerialized();
    }

    /**
     * Posts an object (usually an Event) to the bus
     */
    public void post(final Event... event) {
        for (final Event e : event) {
            busSubject.onNext(e);
        }
    }

    /**
     * Observable that will emit everything posted to the event bus.
     */
    public Observable<Event> allEvents() {
        return busSubject;
    }

    /**
     * Observable that only emits events of a specific class.
     * Use this if you only want to subscribe to one type of events.
     */
    public <T extends Event> Observable<T> eventByType(final Class<T> eventClass) {
        return busSubject.ofType(eventClass);
    }

    /**
     * Observable that only emits events of a specific name.
     * Use this if you only want to subscribe to one name of events.
     * This method is useful when managing events in group.
     */
    public Observable<? extends Event> eventByName(final String eventName) {
        if (TextUtils.isEmpty(eventName)) {
            throw new IllegalArgumentException("Event name must not empty.");
        }

        return allEvents().filter(new Predicate<Event>() {
            @Override
            public boolean test(Event event) throws Exception {
                return eventName.equals(event.getName());
            }
        });
    }
}
