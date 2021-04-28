package com.server.database.requests;

import com.sun.istack.NotNull;

public class DataElementCameraRequest {
	
	@NotNull
	private String date;
	
	@NotNull
	private Boolean recognize;
	
	@NotNull
	private String number;
	
	@NotNull
	private String image;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Boolean getRecognize() {
		return recognize;
	}

	public void setRecognize(Boolean recognize) {
		this.recognize = recognize;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
