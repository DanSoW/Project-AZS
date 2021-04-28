package com.server.database.requests;

import com.sun.istack.NotNull;

public class DataElementRequestOil {
	
	@NotNull
	private String Name;			//название топлива
	
	@NotNull
	private Float Price;			//цена топлива
	
	@NotNull
	private Integer AmountOfFuel;		//количество топлива на заправке

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Float getPrice() {
		return Price;
	}

	public void setPrice(Float price) {
		Price = price;
	}

	public Integer getAmountOfFuel() {
		return AmountOfFuel;
	}

	public void setAmountOfFuel(Integer amountOfFuel) {
		AmountOfFuel = amountOfFuel;
	}
}