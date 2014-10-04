package cs601.blkqueue;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLong;


public class RingBuffer<T> implements MessageQueue<T>{
	private final AtomicLong w = new AtomicLong(-1);	// just wrote location
	private final AtomicLong r = new AtomicLong(0);		// about to read location
	
	int size;
	T[] array;

	public RingBuffer(int n) {
		if (isPowerOfTwo(n) == false) throw new IllegalArgumentException(); // for efficiency of mod
		this.size = n;
		this.array = (T[]) new Object [n];
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
		while(true){
			while( w.get()-r.get() == size-1 ) { // a full buffer, wait for space to write
				waitForFreeSlotAt(w.get());
			}
			w.set((w.get()+1));   // when space available, increment w, absolute index
			array[w.intValue() & (size-1)] = v;  // n%k = n&(k-1)
			notifyAll();
		}
	}

	@Override
	public T take() throws InterruptedException {
			T temp;
			while(true){
				while(r.get()>w.get()){   //nothing to read, wait for data
					waitForDataAt(r.get());
				}					
				temp = array[r.intValue() & (size-1)];  // when data available
				array[r.intValue() & (size-1)] = null;   //consume data
				r.set(r.get()+1);   //increment r
				notifyAll();
				break;
			}
			return temp;
	}

	// spin wait instead of lock for low latency store
	void waitForFreeSlotAt(final long writeIndex) throws InterruptedException{
		wait();
	}

	// spin wait instead of lock for low latency pickup
	void waitForDataAt(final long readIndex) throws InterruptedException{
		wait();
	}
}
