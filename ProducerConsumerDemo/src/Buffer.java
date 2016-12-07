/*
 *  Buffer - a class to simulate a buffer for producers and consumers
 *
 *  Author: Paweł Rogaliński
 *  Date:   01 Oct 2009
 */

class Buffer
{
	private char contents;
	private boolean available = false;

	public synchronized int get(int cons) {
		System.out.println("Konsument #" + cons + " chce zabrać");
		while (available == false) {
			try { System.out.println("Konsument #" + cons + " oczekuje (bufor pusty)");
				  wait();
			} catch (InterruptedException e) { }
		}
		available = false;
		System.out.println("Konsument #" + cons + "      zabrał: " + contents);
		notifyAll();
		return contents;
	}

	public synchronized void put(int prod, char value){
		System.out.println("Producer #" + prod + "  chce oddać: " + value);
		while (available == true){
			try { System.out.println("Producer #" + prod + " oczekuje (bufor pełny)");
				  wait();
				} catch (InterruptedException e) { }
		}
		contents = value;
		available = true;
		System.out.println("Producer #" + prod + "       oddał: " + value);
		notifyAll();
	}
}
