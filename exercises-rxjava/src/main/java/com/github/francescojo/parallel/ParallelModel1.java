package com.github.francescojo.parallel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.francescojo.parallel.ParallelExecutionExample.doSomeBusinessJob;
import static com.github.francescojo.parallel.ParallelExecutionExample.doSomeError;
import static com.github.francescojo.parallel.ParallelExecutionExample.sleepUnConditionally;

/**
 * Simulates 3 threads running in parallel and its 'funnel' job does next work until all of them are ended.
 * Modelled using Thread#join.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 11 - Apr - 2016
 */
class ParallelModel1 implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(ParallelModel1.class);

	@Override
	public void run() {
		final Thread th1 = new Thread(() -> {
			LOG.debug("model1Job1 started");
			long t1 = System.currentTimeMillis();
			sleepUnConditionally(500);
			long delta = System.currentTimeMillis() - t1;
			LOG.debug("model1Job1 finished in " + delta + " ms");
		});

		final Thread th2 = new Thread(() -> {
			LOG.debug("model1Job2 started");
			long t1 = System.currentTimeMillis();
			sleepUnConditionally(1000);
			long delta = System.currentTimeMillis() - t1;
			LOG.debug("model1Job2 finished in " + delta + " ms");
		});

		final Thread th3 = new Thread(() -> {
			LOG.debug("model1Job3 started");
			long t1 = System.currentTimeMillis();
			sleepUnConditionally(250);
			long delta = System.currentTimeMillis() - t1;
			LOG.debug("model1Job3 finished in " + delta + " ms");
		});

		/*
		 * We must separate Thread#start and Thread#join like these, otherwise all threads will be executed
		 * in SERIAL sequences because of the nature of Thread#join
		 */
		try {
			th1.start();
		} catch (Exception e) {
			doSomeError(1, 1);
			return;
		}

		try {
			th2.start();
		} catch (Exception e) {
			doSomeError(1, 2);
			return;
		}

		try {
			th3.start();
		} catch (Exception e) {
			doSomeError(1, 3);
			return;
		}

		try {
			th1.join();
		} catch (InterruptedException e) {
			doSomeError(1, 1);
			return;
		}

		try {
			th2.join();
		} catch (InterruptedException e) {
			doSomeError(1, 2);
			return;
		}

		try {
			th3.join();
		} catch (InterruptedException e) {
			doSomeError(1, 3);
			return;
		}

		doSomeBusinessJob(1);
	}
}
