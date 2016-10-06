/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.event;

import android.text.TextUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * A simple Event bus made of Rx.
 * This class is designed to be injected. Do not instantiate it directly.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 10 - Oct - 2016
 */
@Singleton
public class RxEventBus {
    private final Subject<Event, Event> busSubject;

    @Inject
    public RxEventBus() {
        busSubject = new SerializedSubject<>(PublishSubject.<Event>create());
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

        return allEvents().filter(new Func1<Event, Boolean>() {
            @Override
            public Boolean call(final Event event) {
                return eventName.equals(event.getName());
            }
        });
    }
}
