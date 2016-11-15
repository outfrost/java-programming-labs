/*
 * Room - a demonstration data model class for the CompareRoomCollections application
 *
 * Author: Iwo Bujkiewicz
 * Date: 14 Nov 2016
 */

class Room implements Comparable<Room> {
	
	private String building;
	private String roomNumber;
	private String description;
	
	Room(String building, String roomNumber, String description) {
		this.building = building;
		this.roomNumber = roomNumber;
		this.description = description;
	}
	
	String getBuilding() {
		return building;
	}
	
	String getRoomNumber() {
		return roomNumber;
	}
	
	String getDescription() {
		return description;
	}
	
	void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return building + ":" + roomNumber + " (" + description + ")";
	}
	
	@Override
	public int hashCode() {
		return building.hashCode() - roomNumber.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (building.equals(((Room) obj).getBuilding())) && (roomNumber.equals(((Room) obj).getRoomNumber()));
	}
	
	@Override
	public int compareTo(Room o) {
		return (this.getBuilding().compareTo(o.getBuilding()) != 0) ? this.getBuilding().compareTo(o.getBuilding()) : this.getRoomNumber().compareTo(o.getRoomNumber());
	}
}
