/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.util.logging;

import com.fj.android.template._base.AndroidTestBase;
import io.reactivex.functions.Function;
import org.junit.After;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Sep - 2016
 */
public class LogFactoryTest extends AndroidTestBase {
    @Test
    public void testDefaultInstance() {
        Function<String, Logger> defaultInstance = LogFactory.getLoggerFactory();

        assertEquals("Default instance must be DEFAULT_ANDROID_LOGGER", defaultInstance, LogFactory
                .DEFAULT_ANDROID_LOGGER);
    }

    @Test
    public void testLoggerReused() {
        assertEquals("Other tests must not affect single test cases", 0, LogFactory.LOGGERS.size());

        LogFactory.getLogger("test");
        assertEquals("New logger must be created if logger pool is empty", 1, LogFactory.LOGGERS.size());

        LogFactory.getLogger("test");
        assertEquals("Requesting a logger with same name must not increase logger pool size", 1, LogFactory.LOGGERS
                .size());

        LogFactory.getLogger("newTest");
        assertEquals("Logger with another new name will increase logger pool size", 2, LogFactory.LOGGERS.size());
    }

    @Test
    public void testLoggerFactoryInjection() {
        final Logger mockLogger = mock(Logger.class);
        Function<String, Logger> testInstanceFactory = new Function<String, Logger>() {
            @Override
            public Logger apply(String s) throws Exception {
                return mockLogger;
            }
        };
        LogFactory.setLoggerFactory(testInstanceFactory);

        Logger actual = LogFactory.getLogger("test");
        assertEquals("Logger instance must be newly injected instanceFactory's.", mockLogger, actual);
    }

    @After
    public void tearDown() {
        LogFactory.setLoggerFactory(LogFactory.DEFAULT_ANDROID_LOGGER);
        LogFactory.LOGGERS.evictAll();
    }
}
