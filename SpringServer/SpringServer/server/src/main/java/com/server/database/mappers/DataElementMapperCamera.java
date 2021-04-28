package com.server.database.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.server.database.dao.DataElementDaoImpl;
import com.server.database.elements.DataElementCamera;

@Component
public class DataElementMapperCamera implements RowMapper<DataElementCamera>{

	@Override
	public DataElementCamera mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new DataElementCamera(
				rs.getString(DataElementDaoImpl.NAME_ATTRIBUT_DATE),
				rs.getBoolean(DataElementDaoImpl.NAME_ATTRIBUT_RECOGNIZE),
				rs.getString(DataElementDaoImpl.NAME_ATTRIBUT_NUMBER),
				rs.getString(DataElementDaoImpl.NAME_ATTRIBUT_IMAGE));	
	}
}