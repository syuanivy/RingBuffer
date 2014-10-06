package cs601.blkqueue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

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
	
	protected Thread threadToMonitor; 
	protected boolean monitor = true;
	public static long frequency; //sampling frequency, in nano seconds

	public ThreadObserver(Thread threadToMonitor, long periodInNanoSeconds) {
		this.threadToMonitor = threadToMonitor;
	    frequency = periodInNanoSeconds;
	}

	@Override
	public void run() {
		while(monitor){
			numEvents++;
			switch (threadToMonitor.getState()){
 			    case BLOCKED: blocked++; break; //count each state
			    case WAITING: waiting++; break;
			    case TIMED_WAITING: sleeping++; break; //parking is timed-waiting
			}
			StackTraceElement stackTrace[] = threadToMonitor.getStackTrace();
			if(stackTrace.length > 0){
				StackTraceElement  first = stackTrace[0];
				String key = first.getMethodName() + "." + first.getClassName();
				if (histogram.containsKey(key)){
					long value = histogram.get(key);
					histogram.put(key, ++value);   // put key-value pairs
				}else{
					histogram.put(key, (long)1);
				}
			}
			LockSupport.parkNanos(frequency);
		}
	}

	public Map<String, Long> getMethodSamples() { 
		return histogram; 
	}

	public void terminate() { 
		monitor = false;
	}

	public String toString() {
		return String.format("(%d blocked + %d waiting + %d sleeping) / %d samples = %1.2f%% wasted",
							 blocked,
							 waiting,
							 sleeping,
							 numEvents,
							 100.0*(blocked + waiting + sleeping)/numEvents);
	}
}
