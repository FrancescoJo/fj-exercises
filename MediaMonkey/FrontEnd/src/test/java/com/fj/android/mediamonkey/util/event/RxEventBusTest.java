/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.mediamonkey.util.event;

import com.fj.android.mediamonkey._base.AndroidTestBase;
import com.fj.android.mediamonkey.inject.objects.RxEventBus;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;

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

        TestObserver<Event> testObserver = new TestObserver<>();
        rxEventBus.allEvents().subscribe(testObserver);
        rxEventBus.post(event1);
        rxEventBus.post(event2);

        testObserver.assertValues(event1, event2);
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

        TestObserver<Event> testObserver = new TestObserver<>();
        rxEventBus.eventByType(event1.getClass()).subscribe(testObserver);
        rxEventBus.post(event1);
        rxEventBus.post(event2);

        testObserver.assertValueCount(1);
        testObserver.assertValue(event1);
    }

    @Test
    public void testEventByName() {
        Event event1 = new EventImpl("n1");
        Event event2 = new EventImpl("n2");
        Event event3 = new EventImpl("n1");

        TestObserver<Event> testObserver = new TestObserver<>();
        rxEventBus.eventByName("n1").subscribe(testObserver);
        rxEventBus.post(event1);
        rxEventBus.post(event2);
        rxEventBus.post(event3);

        testObserver.assertValueCount(2);
        testObserver.assertValues(event1, event3);
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
