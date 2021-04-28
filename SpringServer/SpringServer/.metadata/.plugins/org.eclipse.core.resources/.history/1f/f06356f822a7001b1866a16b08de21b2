package com.server.database.requests;

import java.util.List;

import com.server.database.elements.DataElementOil;
import com.sun.istack.NotNull;

//**************************************
//Входные данные, которые поступают на серверную часть в формате JSON
//***************************************

public class DataElementRequestAZS {

	@NotNull
	private String Address;					//адрес местонахождения заправочной станции
	
	@NotNull
	private Short Station_ID;				//уникальный идентификатор заправочной станции (от 1 до 99)	
	
	@NotNull
	private List<DataElementOil> data;		//данные о заправочной станции

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public Short getStation_ID() {
		return Station_ID;
	}

	public void setStation_ID(Short station_ID) {
		Station_ID = station_ID;
	}

	public List<DataElementOil> getData() {
		return data;
	}

	public void setData(List<DataElementOil> data) {
		this.data = data;
	}
	
}
