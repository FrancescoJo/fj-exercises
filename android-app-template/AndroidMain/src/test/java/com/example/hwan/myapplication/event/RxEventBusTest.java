/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.event;

import com.example.hwan.myapplication._base.AndroidTestBase;

import org.junit.Before;
import org.junit.Test;

import rx.observers.TestSubscriber;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 10 - Oct - 2016
 */
public class RxEventBusTest extends AndroidTestBase {
    private RxEventBus rxEventBus;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.rxEventBus = new RxEventBus();
    }

    @Test
    public void testAllEvents() {
        Event event1 = new EventImpl("");
        Event event2 = new EventImpl("");

        TestSubscriber<Event> testSubscriber = new TestSubscriber<>();
        rxEventBus.allEvents().subscribe(testSubscriber);
        rxEventBus.post(event1);
        rxEventBus.post(event2);

        testSubscriber.assertValues(event1, event2);
    }

    @Test
    public void testEventByType() {
        Event event1 = new Event() {
            @Override
            public String getName() {
                return null;
            }
        };
        Event event2 = new Event() {
            @Override
            public String getName() {
                return null;
            }
        };

        TestSubscriber<Event> testSubscriber = new TestSubscriber<>();
        rxEventBus.eventByType(event1.getClass()).subscribe(testSubscriber);
        rxEventBus.post(event1);
        rxEventBus.post(event2);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(event1);
    }

    @Test
    public void testEventByName() {
        Event event1 = new EventImpl("n1");
        Event event2 = new EventImpl("n2");
        Event event3 = new EventImpl("n1");

        TestSubscriber<Event> testSubscriber = new TestSubscriber<>();
        rxEventBus.eventByName("n1").subscribe(testSubscriber);
        rxEventBus.post(event1);
        rxEventBus.post(event2);
        rxEventBus.post(event3);

        testSubscriber.assertValueCount(2);
        testSubscriber.assertValues(event1, event3);
    }

    private static class EventImpl implements Event {
        private final String name;

        EventImpl(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return (name == null || "".equals(name)) ? this.getClass().getName() : name;
        }
    }
}
