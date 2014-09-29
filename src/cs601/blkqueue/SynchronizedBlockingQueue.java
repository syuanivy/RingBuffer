package cs601.blkqueue;

import java.util.LinkedList;

public class SynchronizedBlockingQueue<T> implements MessageQueue<T> {
	int size;
	LinkedList<T> queue;
	
	public SynchronizedBlockingQueue(int size) {
		this.size = size;
		queue = new LinkedList<T> ();
	}

	@Override
	public void put(T o) throws InterruptedException {
		synchronized (this) {
			while(true){
				if(queue.size()<size){
					queue.add(o);
					break;
				}else{
					wait();

				}
			}
			notifyAll();
		}
	}

	@Override
	public T take() throws InterruptedException {
		T retrieve;
		synchronized(this){
			while(true){
				if(queue.size()>0){
				retrieve = queue.remove();
				break;
				}else{
					wait();
				}
			}
			notifyAll();
		}
		return retrieve;

			
	}
}
