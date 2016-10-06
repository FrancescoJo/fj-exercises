/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.util.logging;

import com.example.hwan.myapplication._base.AndroidTestBase;

import org.junit.After;
import org.junit.Test;

import rx.functions.Func1;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Sep - 2016
 */
public class LogFactoryTest extends AndroidTestBase {
    @Test
    public void testDefaultInstance() {
        Func1<String, Logger> defaultInstance = LogFactory.getLoggerFactory();

        assertEquals("Default instance must be DEFAULT_ANDROID_LOGGER", defaultInstance,
                     LogFactory.DEFAULT_ANDROID_LOGGER);
    }

    @Test
    public void testLoggerReused() {
        assertEquals("Other tests must not affect single test cases", 0, LogFactory.LOGGERS.size());

        LogFactory.getLogger("test");
        assertEquals("New logger must be created if logger pool is empty", 1,
                     LogFactory.LOGGERS.size());

        LogFactory.getLogger("test");
        assertEquals("Requesting a logger with same name must not increase logger pool size", 1,
                     LogFactory.LOGGERS.size());

        LogFactory.getLogger("newTest");
        assertEquals("Logger with another new name will increase logger pool size", 2,
                     LogFactory.LOGGERS.size());
    }

    @Test
    public void testLoggerFactoryInjection() {
        final Logger mockLogger = mock(Logger.class);
        Func1<String, Logger> testInstanceFactory = new Func1<String, Logger>() {
            @Override
            public Logger call(String s) {
                return mockLogger;
            }
        };
        LogFactory.setLoggerFactory(testInstanceFactory);

        Logger actual = LogFactory.getLogger("test");
        assertEquals("Logger instance must be newly injected instanceFactory's.", mockLogger,
                     actual);
    }

    @After
    public void tearDown() {
        LogFactory.setLoggerFactory(LogFactory.DEFAULT_ANDROID_LOGGER);
        LogFactory.LOGGERS.evictAll();
    }
}
