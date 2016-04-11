/*
 * @(#)ParallelModel2.java $Version 11 - Apr - 2016
 *
 * Distributed under no licences and no warranty.
 */
package com.github.francescojo.parallel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

import static com.github.francescojo.parallel.ParallelExecutionExample.doSomeBusinessJob;
import static com.github.francescojo.parallel.ParallelExecutionExample.doSomeError;
import static com.github.francescojo.parallel.ParallelExecutionExample.sleepUnConditionally;

/**
 * Simulates 3 threads running in parallel and its 'funnel' job does next work until all of them are ended.
 * Modelled using CountDownLatch.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 11 - Apr - 2016
 */
class ParallelModel2 implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(ParallelModel2.class);

	@Override
	public void run() {
		final CountDownLatch cdl = new CountDownLatch(3);
		final boolean[] successFlags = new boolean[] { false, false, false };

		final Thread th1 = new Thread(() -> {
			LOG.debug("model2Job1 started");
			long t1 = System.currentTimeMillis();
			sleepUnConditionally(500);
			long delta = System.currentTimeMillis() - t1;
			LOG.debug("model2Job1 finished in " + delta + " ms");
			cdl.countDown();
			successFlags[0] = true;
		});

		final Thread th2 = new Thread(() -> {
			LOG.debug("model2Job2 started");
			long t1 = System.currentTimeMillis();
			sleepUnConditionally(1000);
			long delta = System.currentTimeMillis() - t1;
			LOG.debug("model2Job2 finished in " + delta + " ms");
			cdl.countDown();
			successFlags[1] = true;
		});

		final Thread th3 = new Thread(() -> {
			LOG.debug("model2Job3 started");
			long t1 = System.currentTimeMillis();
			sleepUnConditionally(250);
			long delta = System.currentTimeMillis() - t1;
			LOG.debug("model2Job3 finished in " + delta + " ms");
			cdl.countDown();
			successFlags[2] = true;
		});

		th1.start();
		th2.start();
		th3.start();
		try {
			cdl.await();
		} catch (InterruptedException e) {
			/*
			 * what if.. there're more than single 'false' flags??
			 */
			for (int i = 0, limit = successFlags.length; i < limit; i++) {
				if (!successFlags[i]) {
					doSomeError(2, (i + 1));
					return;
				}
			}
		}
		doSomeBusinessJob(2);
	}
}
