/*
 *  MiniWebStoreItem - serializable class for sellable item management
 *
 *  Author: Iwo Bujkiewicz
 *  Date: 22 Oct 2016
 */

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.Serializable;

public class MiniWebStoreItem implements Serializable {
	private String name;
	private long unitPrice;
	private long stock;
	
	public MiniWebStoreItem(String name, long unitPrice) {
		this.name = name;
		this.unitPrice = unitPrice;
		this.stock = 0L;
	}
	
	public String getName() { return name; }
	public long getUnitPrice() { return unitPrice; }
	public long getStock() { return stock; }
	
	public void modifyStock(long value) throws InvalidArgumentException {
		if (stock + value >= 0L)
			stock += value;
		else
			throw new InvalidArgumentException(new String[] { "Warehouse stock cannot be negative." });
	}
	
	public void setUnitPrice(long newPrice) throws InvalidArgumentException {
		if (newPrice >= 0L)
			unitPrice = newPrice;
		else
			throw new InvalidArgumentException(new String[] { "Unit price cannot be negative." });
	}
	
	@Override
	public String toString() {
		return getName() + "\t" + getStock() + "\t" + getUnitPrice() / 100L + "." + getUnitPrice() % 100L;
	}
}
