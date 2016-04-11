package com.github.francescojo.parallel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rx parallel execution simulator.
 * This source code is demonstrating synchronisation strategy on single business logic which can be separated as
 * three parallel tasks. Assumed that the synchronisation time must not be longer than the most late job.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 11 - Apr - 2016
 */
public class ParallelExecutionExample {
	private static final Logger LOG = LoggerFactory.getLogger(ParallelExecutionExample.class);

	public static void main(String[] args) {
		LOG.debug("ParallelModel1 start");
		new ParallelModel1().run();
		LOG.debug("ParallelModel2 start");
		new ParallelModel2().run();
		LOG.debug("ParallelModel3 start");
		new ParallelModel3().run();
	}

	static void doSomeBusinessJob(int jobId) {
		// no-op
		LOG.debug("BUSINESS JOB OF #" + jobId + " HAS BEEN ENDED\n");
	}

	static void sleepUnConditionally(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
