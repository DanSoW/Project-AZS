package com.server.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.server.database.elements.DataElementAZS;
import com.server.database.elements.DataElementAZSFuel;
import com.server.database.elements.DataElementCamera;
import com.server.database.elements.DataElementOil;
import com.server.database.requests.DataElementCameraRequest;
import com.server.database.requests.DataElementRequestAZS;
import com.server.database.requests.DataElementRequestOil;
import com.server.database.requests.DataElementRequestOilUpdate;
import com.server.database.services.DataElementServiceImpl;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class DatabaseController {

	DataElementServiceImpl dataService;
	
	@Autowired
	public DatabaseController(DataElementServiceImpl dataService) {
		this.dataService = dataService;
	}
	
	//Маршрутизация для модуля "АЗС"
	@PostMapping(value = "/setStation")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void insertDataElementAZS(@Valid @RequestBody DataElementRequestAZS request) throws Exception {
		dataService.insertDataAZS(
				new DataElementAZS(request.getAddress(), request.getStation_ID()));
		List<DataElementOil> list = request.getData();

		for(DataElementOil i : list) {
			i.setFkIdStation(request.getStation_ID());
			dataService.insertDataOil(i);
		}
	}
	
	@PostMapping(value = "/updateStation")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateDataElementAZS(@Valid @RequestBody DataElementAZSFuel request) throws Exception {
		List<DataElementOil> list = request.getData();
	
		for(DataElementOil i : list) {
			i.setFkIdStation(request.getStation_Id());
			dataService.updateDataOil(i);
		}
	}
	
	@PostMapping(value = "/setStation/all")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void insertDataElementAZSALL(@Valid @RequestBody List<DataElementRequestAZS> request) {
		for(DataElementRequestAZS req : request) {
			dataService.insertDataAZS(
					new DataElementAZS(req.getAddress(), req.getStation_ID()));
			List<DataElementOil> list = req.getData();

			for(DataElementOil i : list) {
				i.setFkIdStation(req.getStation_ID());
				dataService.insertDataOil(i);
			}
		}
	}
	
	@GetMapping(value = "/getStationInfo")
	public DataElementAZS getDataElementAZSById(@RequestParam short id) {
		return dataService.getDataElementAZSById(id);
	}
	
	private boolean validFuelType(String fuelType) {
		String[] types = new String[] {
				"92", "95", "98", "DT", "Disel Fuel"
		};
		
		for(String i : types) {
			if(i.equals(fuelType))
				return true;
		}
		
		return false;
	}
	
	@GetMapping(value = "/stations")
	public List<DataElementAZS> getListAZS(@RequestParam String fuel) throws Exception{
		if(!validFuelType(fuel))
			throw new Exception("Ошибка! Тип топлива должен быть 92, 95, 98 или DT!");
		List<DataElementAZS> listAZS = dataService.getDataElementAZSALL();
		List<DataElementOil> listOil = dataService.getDataElementOilAll();
		
		List<DataElementAZS> result = new ArrayList<DataElementAZS>();
		
		for(int i = 0; i < listOil.size(); i++) {
			if(listOil.get(i).getName().equals(fuel)) {
				for(int j = 0; j < listAZS.size(); j++) {
					if(listAZS.get(j).getStation_Id() == listOil.get(i).getFkIdStation()) {
						result.add(listAZS.get(j));
						break;
					}
				}
			}
		}
		
		return result;
	}
	
	@GetMapping(value = "/getStationInfo/all")
	public List<DataElementAZS> getDataElementAZSAll(){
		return dataService.getDataElementAZSALL();
	}
	
	@GetMapping(value = "/getFuelInfo/all")
	public List<DataElementOil> getDataElementOilALL(){
		return dataService.getDataElementOilAll();
	}
	
	@GetMapping(value = "/getFuelInfo/all/")
	public List<DataElementOil> getDataElementOilAllById(@RequestParam short id){
		return dataService.getDataElementOilAllById(id);
	}
	
	@PostMapping(value = "/updateDataOil")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateDataElementOil(@Valid @RequestBody DataElementRequestOilUpdate request) {
		dataService.updateDataOil(
				new DataElementOil(request.getName(), request.getPrice(), request.getAmountOfFuel(), request.getVariable()));
	}
	
	@PostMapping(value = "/updateDataOil/all")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateDataElementOilAll(@Valid @RequestBody List<DataElementRequestOilUpdate> request) {
		for(int i = 0; i < request.size(); i++) {
			dataService.updateDataOil(
					new DataElementOil(request.get(i).getName(), request.get(i).getPrice(), request.get(i).getAmountOfFuel(), request.get(i).getVariable()));
		}
	}
	
	
	//Маршрутизация для модуля "Камера"
	@PostMapping(value = "/addDataCamera")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void addDataElementCamera(@Valid @RequestBody DataElementCameraRequest request) {
		List<DataElementCamera> data = dataService.getDataElementCameraAll();
		for(DataElementCamera i : data) {
			if(i.getNumber().equals(request.getNumber())) {
				dataService.updateDataElementCamera(
						new DataElementCamera(request.getDate(), request.getRecognize(), request.getNumber(), request.getImage()));
				return;
			}
		}
		
		dataService.insertDataElementCamera(
				new DataElementCamera(request.getDate(), request.getRecognize(), request.getNumber(), request.getImage()));
	}
	
	@GetMapping(value = "/getDataCamera/number")
	public DataElementCamera getDataElementCameraByNumber(@RequestParam String number) {
		List<DataElementCamera> data = dataService.getDataElementCameraAll();
		for(DataElementCamera i : data) {
			if(i.getNumber().equals(number)) {
				return i;
			}
		}
		
		return null;
	}
	
	@GetMapping(value = "/getDataCamera/all")
	public List<DataElementCamera> getDataElementCameraAll() {
		return dataService.getDataElementCameraAll();
	}
}
