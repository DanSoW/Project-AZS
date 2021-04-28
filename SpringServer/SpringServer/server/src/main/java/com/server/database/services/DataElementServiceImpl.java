package com.server.database.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.server.database.dao.DataElementDao;
import com.server.database.elements.DataElementAZS;
import com.server.database.elements.DataElementCamera;
import com.server.database.elements.DataElementOil;

@Primary
@Service
public class DataElementServiceImpl implements DataElementService {

	private final DataElementDao dataDao;
	
	@Autowired
	public DataElementServiceImpl(DataElementDao dataDao) {
		this.dataDao = dataDao;
	}

	@Override
	public DataElementAZS getDataElementAZSById(short stationId) {
		return dataDao.getDataElementAZSById(stationId);
	}

	@Override
	public void insertDataAZS(DataElementAZS dataElementAZS) {
		dataDao.insertDataAZS(dataElementAZS);
	}

	@Override
	public List<DataElementAZS> getDataElementAZSALL() {
		return dataDao.getDataElementAZSALL();
	}

	@Override
	public DataElementOil getDataElementOilByID(short stationId) {
		return dataDao.getDataElementOilByID(stationId);
	}

	@Override
	public void insertDataOil(DataElementOil dataElementOil) {
		dataDao.insertDataOil(dataElementOil);
	}

	@Override
	public List<DataElementOil> getDataElementOilAll() {
		return dataDao.getDataElementOilAll();
	}

	@Override
	public List<DataElementOil> getDataElementOilAllById(short id) {
		return dataDao.getDataElementOilAllById(id);
	}

	@Override
	public void updateDataOil(DataElementOil data) {
		dataDao.updateDataOil(data);
	}

	@Override
	public DataElementCamera getDataElementCameraByNumber(String number) {
		return dataDao.getDataElementCameraByNumber(number);
	}

	@Override
	public List<DataElementCamera> getDataElementCameraAll() {
		return dataDao.getDataElementCameraAll();
	}

	@Override
	public void insertDataElementCamera(DataElementCamera data) {
		dataDao.insertDataElementCamera(data);
	}

	@Override
	public void updateDataElementCamera(DataElementCamera data) {
		dataDao.updateDataElementCamera(data);
	}
	
}
