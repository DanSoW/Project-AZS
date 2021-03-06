package com.server.database.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.server.database.dao.DataElementDaoImpl;
import com.server.database.elements.DataElementAZS;

@Component
public class DataElementMapperAZS implements RowMapper<DataElementAZS>{

	@Override
	public DataElementAZS mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new DataElementAZS(
				rs.getString(DataElementDaoImpl.NAME_ATTRIBUT_ADDRESS),
				rs.getShort(DataElementDaoImpl.NAME_ATTRIBUT_STATION_ID));
	}

}
