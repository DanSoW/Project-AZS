package com.server.database.elements;

import java.util.List;

public class DataElementAZSFuel {

	private String address;					//адрес местонахождения заправочной станции
	
	private Short station_Id;				//уникальный идентификатор заправочной станции (от 1 до 99)	

	private List<DataElementOil> data;		//данные о заправочной станции

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Short getStation_Id() {
		return station_Id;
	}

	public void setStation_Id(Short station_Id) {
		this.station_Id = station_Id;
	}

	public List<DataElementOil> getData() {
		return data;
	}

	public void setData(List<DataElementOil> data) {
		this.data = data;
	}

	
}
