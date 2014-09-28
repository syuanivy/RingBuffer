package cs601.blkqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class MessageQueueAdaptor<T> implements MessageQueue<T> {
	int size;
	ArrayBlockingQueue<T> arrayblkq;
	MessageQueueAdaptor ( int size ) { 
		size = size;
		arrayblkq = new ArrayBlockingQueue<T> (size);
	}

	@Override
	public void put(T o) throws InterruptedException {
		arrayblkq.put(o);
	}

	@Override
	public T take() throws InterruptedException {
		return arrayblkq.take();
	}
}
