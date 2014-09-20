package cs601.blkqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class MessageQueueAdaptor<T> implements MessageQueue<T> {
	MessageQueueAdaptor(int size) { }

	@Override
	public void put(T o) throws InterruptedException {
	}

	@Override
	public T take() throws InterruptedException {
		return null;
	}
}
