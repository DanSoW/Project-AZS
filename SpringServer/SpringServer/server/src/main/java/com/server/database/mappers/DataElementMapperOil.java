package com.server.database.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.server.database.dao.DataElementDaoImpl;
import com.server.database.elements.DataElementOil;

//**********************************
//Считыватель единицы данных для таблицы TableOil
//**********************************

@Component
public class DataElementMapperOil implements RowMapper<DataElementOil>{

	@Override
	public DataElementOil mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new DataElementOil(
				rs.getString(DataElementDaoImpl.NAME_ATTRIBUT_NAME),
				rs.getFloat(DataElementDaoImpl.NAME_ATTRIBUT_PRICE),
				rs.getInt(DataElementDaoImpl.NAME_ATTRIBUT_AMOUNT),
				rs.getInt(DataElementDaoImpl.NAME_ATTRIBUT_FK_STATION_ID));	
	}
}
