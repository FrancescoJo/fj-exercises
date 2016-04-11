package com.github.francescojo.parallel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

import static com.github.francescojo.parallel.ParallelExecutionExample.doSomeBusinessJob;
import static com.github.francescojo.parallel.ParallelExecutionExample.sleepUnConditionally;

/**
 * Simulates 3 threads running in parallel and its 'funnel' job does next work until all of them are ended.
 * Modelled using ReactiveX. All jobs are run under 'new thread' scheduler of Rx.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 11 - Apr - 2016
 */
class ParallelModel3 implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(ParallelModel3.class);

	@Override
	public void run() {
		Observable.merge(
				Observable.create(subscriber -> {
					LOG.debug("model3Job1 started");
					long t1 = System.currentTimeMillis();
					sleepUnConditionally(500);
					long delta = System.currentTimeMillis() - t1;
					LOG.debug("model3Job1 finished in " + delta + " ms");
					if (!subscriber.isUnsubscribed()) {
						subscriber.onNext(Observable.<Void>empty());
						subscriber.onCompleted();
					}
				}).subscribeOn(Schedulers.newThread()),

				Observable.create(subscriber -> {
					LOG.debug("model3Job2 started");
					long t1 = System.currentTimeMillis();
					sleepUnConditionally(1000);
					long delta = System.currentTimeMillis() - t1;
					LOG.debug("model3Job2 finished in " + delta + " ms");
					if (!subscriber.isUnsubscribed()) {
						subscriber.onNext(Observable.<Void>empty());
						subscriber.onCompleted();
					}
				}).subscribeOn(Schedulers.newThread()),

				Observable.create(subscriber -> {
					LOG.debug("model3Job3 started");
					long t1 = System.currentTimeMillis();
					sleepUnConditionally(250);
					long delta = System.currentTimeMillis() - t1;
					LOG.debug("model3Job3 finished in " + delta + " ms");
					if (!subscriber.isUnsubscribed()) {
						subscriber.onNext(Observable.<Void>empty());
						subscriber.onCompleted();
					}
				}).subscribeOn(Schedulers.newThread())
		)
		.last()
		.toBlocking()
		.subscribe(emptyObservable -> {
			doSomeBusinessJob(3);
		});
	}
}
