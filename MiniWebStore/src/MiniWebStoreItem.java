/*
 *  MiniWebStoreItem - serializable class for sellable item management
 *
 *  Author: Iwo Bujkiewicz
 *  Date: 22 Oct 2016
 */

import java.io.Serializable;

/**
 * Provides an object representation of a single item in a store.
 *
 * Contains information about the item's name, unit price, and number of units in stock.
 *
 * Class is serializable.
 *
 * @author  Iwo Bujkiewicz
 * @version 20161101
 */
public class MiniWebStoreItem implements Serializable {
	private String name;
	private long unitPrice;
	private long stock;
	
	/**
	 * Creates an item object and initializes fields from provided data.
	 * Number of units in stock is set to 0.
	 * @param name      Name of the item
	 * @param unitPrice Unit price of the item
	 * @throws IllegalArgumentException If the provided unit price is negative
	 */
	public MiniWebStoreItem(String name, long unitPrice) throws IllegalArgumentException {
		if (unitPrice < 0L) throw new IllegalArgumentException("Unit price cannot be negative.");
		
		this.name = name;
		this.unitPrice = unitPrice;
		this.stock = 0L;
	}
	
	/** Returns the name of the item. */
	public String getName() { return name; }
	/** Returns the unit price of the item. */
	public long getUnitPrice() { return unitPrice; }
	/** Returns the number of units in stock. */
	public long getStock() { return stock; }
	
	/**
	 * Modifies the stock of the item by the provided number.
	 * Positive values increase stock, while negative values decrease it.
	 * @param value Number of units to add to the item's stock
	 * @throws IllegalArgumentException If the item's stock would become negative as a result of this operation
	 */
	public void modifyStock(long value) throws IllegalArgumentException {
		if (stock + value >= 0L)
			stock += value;
		else
			throw new IllegalArgumentException("Warehouse stock cannot be negative.");
	}
	
	/**
	 * Sets a new unit price for the item.
	 * @param newPrice  New unit price to be assigned
	 * @throws IllegalArgumentException If the provided value is negative
	 */
	public void setUnitPrice(long newPrice) throws IllegalArgumentException {
		if (newPrice >= 0L)
			unitPrice = newPrice;
		else
			throw new IllegalArgumentException("Unit price cannot be negative.");
	}
	
	@Override
	public String toString() {
		return getName() + "\t" + getStock() + "\t" + FixedPointFormat.decimal2Places(getUnitPrice());
	}
}
