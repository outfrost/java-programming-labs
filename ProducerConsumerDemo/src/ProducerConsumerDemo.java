/*
 *  Producer-consumer problem
 *
 *  Author: Paweł Rogaliński
 *  Date:   01 Oct 2009
 */

public class ProducerConsumerDemo {

	public static void main(String[] args) {
		Buffer c = new Buffer();
		Producer p1 = new Producer(c, 1);
		Consumer c1 = new Consumer(c, 1);
		Producer p2 = new Producer(c, 2);
		Consumer c2 = new Consumer(c, 2);
		p1.start();
		c1.start();
		p2.start();
		c2.start();
		try {
			Thread.sleep( 5000 );
		} catch (InterruptedException e) { }
		System.exit(0);
	}
}


