package com.server.database.requests;

import com.sun.istack.NotNull;

public class DataRequest {
	@NotNull
	private String Address;

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}	
}
