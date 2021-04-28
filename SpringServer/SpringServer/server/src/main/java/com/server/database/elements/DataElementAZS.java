package com.server.database.elements;

//**********************************
//Единица данных для таблицы AZS
//**********************************

public class DataElementAZS {
	private String Address;
	private short Station_Id;
	
	public DataElementAZS(String address, short station_Id) {
		Address = address;
		Station_Id = station_Id;
	}
	
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public short getStation_Id() {
		return Station_Id;
	}
	public void setStation_Id(short station_Id) {
		Station_Id = station_Id;
	}

}
