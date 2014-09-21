package cs601.blkqueue;

import java.util.concurrent.atomic.AtomicLong;

public class RingBuffer<T> implements MessageQueue<T> {
	private final AtomicLong w = new AtomicLong(-1);	// just wrote location
	private final AtomicLong r = new AtomicLong(0);		// about to read location

	public RingBuffer(int n) {
	}

	// http://graphics.stanford.edu/~seander/bithacks.html#CountBitsSetParallel
	static boolean isPowerOfTwo(int v) {
		if (v<0) return false;
		v = v - ((v >> 1) & 0x55555555);                    // reuse input as temporary
		v = (v & 0x33333333) + ((v >> 2) & 0x33333333);     // temp
		int onbits = ((v + (v >> 4) & 0xF0F0F0F) * 0x1010101) >> 24; // count
		// if number of on bits is 1, it's power of two, except for sign bit
		return onbits==1;
	}

	@Override
	public void put(T v) throws InterruptedException {
	}

	@Override
	public T take() throws InterruptedException {
		return null;
	}

	// spin wait instead of lock for low latency store
	void waitForFreeSlotAt(final long writeIndex) {
	}

	// spin wait instead of lock for low latency pickup
	void waitForDataAt(final long readIndex) {
	}
}
