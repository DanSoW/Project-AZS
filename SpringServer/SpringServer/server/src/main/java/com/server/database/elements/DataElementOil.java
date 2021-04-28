package com.server.database.elements;

//********************************************
//Информация о топливе
//********************************************

public class DataElementOil {
	private String Name;			//название топлива
	private float Price;			//цена топлива
	private int AmountOfFuel;		//количество топлива на заправке
	private int fkIdStation;
	
	public DataElementOil(String name, float price, int amountOfFuel, int fkIdStation) {
		Name = name;
		Price = price;
		AmountOfFuel = amountOfFuel;
		this.fkIdStation = fkIdStation;
	}
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public float getPrice() {
		return Price;
	}
	public void setPrice(float price) {
		Price = price;
	}
	public int getAmountOfFuel() {
		return AmountOfFuel;
	}
	public void setAmountOfFuel(int amountOfFuel) {
		AmountOfFuel = amountOfFuel;
	}
	public int getFkIdStation() {
		return fkIdStation;
	}
	public void setFkIdStation(int fkIdStation) {
		this.fkIdStation = fkIdStation;
	}
}
