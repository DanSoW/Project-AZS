package com.server.database.elements;

public class DataElementCamera {
	private String date;
	private boolean recognize;
	private String number;
	private String image;
	
	public DataElementCamera(String date, boolean recognize, String number, String image) {
		this.date = date;
		this.recognize = recognize;
		this.number = number;
		this.image = image;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public boolean isRecognize() {
		return recognize;
	}
	public void setRecognize(boolean recognize) {
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
