/*
 *  Buffer - a class to simulate a buffer for producers and consumers
 *
 *  Author: Paweł Rogaliński, Iwo Bujkiewicz
 *  Date:   07 Dec 2016
 */

class Buffer {
	
	private char[] contents;
	private int availableItemsCount;
	private int currentHead;
	private StringHandler outputHandler;
	
	protected Buffer(int length, StringHandler outputHandler) {
		this.contents = new char[length];
		this.availableItemsCount = 0;
		this.currentHead = 0;
		this.outputHandler = outputHandler;
	}

	public synchronized int get(int cons) {
		outputHandler.accept("Konsument #" + cons + " chce zabrać");
		while (availableItemsCount < 1) {
			try { outputHandler.accept("Konsument #" + cons + " oczekuje (bufor pusty)");
				  wait();
			} catch (InterruptedException e) { }
		}
		availableItemsCount--;
		char taken = contents[currentHead];
		currentHead = (currentHead + 1) % contents.length;
		outputHandler.accept("Konsument #" + cons + "      zabrał: " + taken);
		notifyAll();
		return taken;
	}

	public synchronized void put(int prod, char value) {
		outputHandler.accept("Producer #" + prod + "  chce oddać: " + value);
		while (availableItemsCount >= contents.length){
			try { outputHandler.accept("Producer #" + prod + " oczekuje (bufor pełny)");
				  wait();
				} catch (InterruptedException e) { }
		}
		contents[(currentHead + availableItemsCount) % contents.length] = value;
		availableItemsCount++;
		outputHandler.accept("Producer #" + prod + "       oddał: " + value);
		notifyAll();
	}
	
	public int getLength() { return contents.length; }
	public StringHandler getOutputHandler() { return outputHandler; }
}
