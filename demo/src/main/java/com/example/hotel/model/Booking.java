package com.example.hotel.model;

public class Booking {
    private String hotelId;
    private String arrival; // Format: YYYYMMDD
    private String departure; // Format: YYYYMMDD
    private String roomType;
    private String roomRate;
    
	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	public String getArrival() {
		return arrival;
	}
	public void setArrival(String arrival) {
		this.arrival = arrival;
	}
	public String getDeparture() {
		return departure;
	}
	public void setDeparture(String departure) {
		this.departure = departure;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getRoomRate() {
		return roomRate;
	}
	public void setRoomRate(String roomRate) {
		this.roomRate = roomRate;
	}


}