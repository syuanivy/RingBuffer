package cs601.blkqueue;

import java.util.HashMap;
import java.util.Map;

/** A runnable class that attaches to another thread and wakes up
 *  at regular intervals to determine that thread's state. The goal
 *  is to figure out how much time that thread is blocked, waiting,
 *  or sleeping.
 */
class ThreadObserver implements Runnable {
	protected final Map<String, Long> histogram = new HashMap<String, Long>();
	protected int numEvents = 0;
	protected int blocked = 0;
	protected int waiting = 0;
	protected int sleeping = 0;

	public ThreadObserver(Thread threadToMonitor, long periodInNanoSeconds) {
	}

	@Override
	public void run() {
	}

	public Map<String, Long> getMethodSamples() { return histogram; }

	public void terminate() { /* ... */ }

	public String toString() {
		return String.format("(%d blocked + %d waiting + %d sleeping) / %d samples = %1.2f%% wasted",
							 blocked,
							 waiting,
							 sleeping,
							 numEvents,
							 100.0*(blocked + waiting + sleeping)/numEvents);
	}
}
