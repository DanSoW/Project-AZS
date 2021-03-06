package com.server.database.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.server.database.elements.DataElementAZS;
import com.server.database.elements.DataElementCamera;
import com.server.database.elements.DataElementOil;
import com.server.database.mappers.DataElementMapperAZS;
import com.server.database.mappers.DataElementMapperCamera;
import com.server.database.mappers.DataElementMapperOil;

@Repository
public class DataElementDaoImpl implements DataElementDao {
	
	//База данных для АЗС
	public static final String NAME_TABLE_AZS = "TableAZS";
	public static final String NAME_ATTRIBUT_ADDRESS = "Address";
	public static final String NAME_ATTRIBUT_STATION_ID = "Station_ID";
	public static final String SQL_COMMAND_CREATE_AZS_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE_AZS + " ("
			+ NAME_ATTRIBUT_ADDRESS + " VARCHAR(255), " + NAME_ATTRIBUT_STATION_ID + " SMALLINT PRIMARY KEY);";
	
	public static final String NAME_TABLE_OIL = "TableOil";
	public static final String NAME_ATTRIBUT_NAME = "Name";
	public static final String NAME_ATTRIBUT_PRICE = "Price";
	public static final String NAME_ATTRIBUT_AMOUNT = "AmountOfFuel";
	public static final String NAME_ATTRIBUT_FK_STATION_ID = "fkStationId";
	
	private static final String SQL_COMMAND_CREATE_OIL_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE_OIL + " ("
			+ NAME_ATTRIBUT_NAME + " VARCHAR(10), " + NAME_ATTRIBUT_PRICE + " FLOAT, " + NAME_ATTRIBUT_AMOUNT + " INT, "
			+ NAME_ATTRIBUT_FK_STATION_ID + " SMALLINT, FOREIGN KEY (" + NAME_ATTRIBUT_FK_STATION_ID + ") REFERENCES " + NAME_TABLE_AZS + " (" + NAME_ATTRIBUT_STATION_ID + "));";
	
	private static final String SQL_COMMAND_INSERT_AZS = "INSERT INTO " + NAME_TABLE_AZS + " (" + NAME_ATTRIBUT_ADDRESS + ", " + NAME_ATTRIBUT_STATION_ID + ") VALUES ("
			+ ":" + NAME_ATTRIBUT_ADDRESS + ", " + ":" + NAME_ATTRIBUT_STATION_ID + ");";
	
	private static final String SQL_COMMAND_SELECT_AZS = "SELECT * FROM " + NAME_TABLE_AZS;
	
	private static final String SQL_COMMAND_INSERT_OIL = "INSERT INTO " + NAME_TABLE_OIL + " (" + NAME_ATTRIBUT_NAME + ", " + NAME_ATTRIBUT_PRICE + ", " + NAME_ATTRIBUT_AMOUNT + ", " + NAME_ATTRIBUT_FK_STATION_ID + ") "
			+ "VALUES (" + ":" + NAME_ATTRIBUT_NAME + ", :" + NAME_ATTRIBUT_PRICE + ", :" + NAME_ATTRIBUT_AMOUNT + ", :" + NAME_ATTRIBUT_FK_STATION_ID + ");";
	
	private static final String SQL_COMMAND_SELECT_OIL = "SELECT * FROM " + NAME_TABLE_OIL;
	
	private static final String SQL_COMMAND_SELECT_OIL_BY_ID = "SELECT * FROM " + NAME_TABLE_OIL + " WHERE " + NAME_ATTRIBUT_FK_STATION_ID + "=:" + NAME_ATTRIBUT_FK_STATION_ID + ";";
	
	private static final String SQL_COMMAND_UPDATE_OIL = "UPDATE " + NAME_TABLE_OIL + " SET " + NAME_ATTRIBUT_PRICE + "=:" + NAME_ATTRIBUT_PRICE + ", "
			+ NAME_ATTRIBUT_AMOUNT + "=:" + NAME_ATTRIBUT_AMOUNT + " WHERE " + NAME_ATTRIBUT_NAME + "=:" + NAME_ATTRIBUT_NAME + " AND " + NAME_ATTRIBUT_FK_STATION_ID + "=:" + NAME_ATTRIBUT_FK_STATION_ID + ";";
	
	//База данных для Камеры
	public static final String NAME_TABLE_CAMERA = "TableCamera";
	public static final String NAME_ATTRIBUT_DATE = "Date";
	public static final String NAME_ATTRIBUT_RECOGNIZE = "Recognize";
	public static final String NAME_ATTRIBUT_NUMBER = "Number";
	public static final String NAME_ATTRIBUT_IMAGE = "Image";
	
	private static final String SQL_COMMAND_CREATE_CAMERA_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE_CAMERA + " ("
			+ NAME_ATTRIBUT_DATE + " DATE, " + NAME_ATTRIBUT_RECOGNIZE + " BOOL, " + NAME_ATTRIBUT_NUMBER + " NVARCHAR(20) PRIMARY KEY, "
			+ NAME_ATTRIBUT_IMAGE + " VARCHAR(255));";
	
	private static final String SQL_COMMAND_INSERT_CAMERA = "INSERT INTO " + NAME_TABLE_CAMERA + " (" +
	NAME_ATTRIBUT_DATE + ", " + NAME_ATTRIBUT_RECOGNIZE + ", " + NAME_ATTRIBUT_NUMBER + ", " + NAME_ATTRIBUT_IMAGE + ") VALUES ("
	+ ":" + NAME_ATTRIBUT_DATE + ", " + ":" + NAME_ATTRIBUT_RECOGNIZE + ", :" + NAME_ATTRIBUT_NUMBER + ", :" + NAME_ATTRIBUT_IMAGE + ");";
	
	private static final String SQL_COMMAND_UPDATE_CAMERA = "UPDATE " + NAME_TABLE_CAMERA + " SET " + NAME_ATTRIBUT_DATE + "=:" + NAME_ATTRIBUT_DATE + ", "
			+ NAME_ATTRIBUT_RECOGNIZE + "=:" + NAME_ATTRIBUT_RECOGNIZE + ", " + NAME_ATTRIBUT_IMAGE + "=:" + NAME_ATTRIBUT_IMAGE + " WHERE " +
			NAME_ATTRIBUT_NUMBER + "=:" + NAME_ATTRIBUT_NUMBER + ";";
	
	private static final String SQL_COMMAND_SELECT_CAMERA = "SELECT * FROM " + NAME_TABLE_CAMERA;
	
	private final DataElementMapperAZS dataMapperAZS;
	private final DataElementMapperOil dataMapperOil;
	private final DataElementMapperCamera dataMapperCamera;
	private final NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public DataElementDaoImpl(
			DataElementMapperAZS dataMapperAZS,
			DataElementMapperOil dataMapperOil,
			DataElementMapperCamera dataMapperCamera,
			NamedParameterJdbcTemplate jTempl
			) {

		this.dataMapperAZS = dataMapperAZS;
		this.dataMapperOil = dataMapperOil;
		this.dataMapperCamera = dataMapperCamera;
		this.jdbcTemplate = jTempl;
		
		//Создание таблицы для хранения уникальной информации о заправочной станции
		this.jdbcTemplate.execute(SQL_COMMAND_CREATE_AZS_TABLE, new PreparedStatementCallback<Object>() {
			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.execute();
				return null;
			}
		});

		//Создание таблицы для хранения данных о топливе и принадлежности данного топлива определённой АЗС
		this.jdbcTemplate.execute(SQL_COMMAND_CREATE_OIL_TABLE, new PreparedStatementCallback<Object>() {
		@Override
		public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.execute();
				return null;
			}
		});
		
		//Создание таблицы для хранения данных о зарегистрированных автомобилях
		this.jdbcTemplate.execute(SQL_COMMAND_CREATE_CAMERA_TABLE, new PreparedStatementCallback<Object>() {
			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
					ps.execute();
					return null;
				}
		});
	}

	@Override
	public void insertDataAZS(DataElementAZS dataElementAZS) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(NAME_ATTRIBUT_ADDRESS, dataElementAZS.getAddress());
		params.addValue(NAME_ATTRIBUT_STATION_ID, dataElementAZS.getStation_Id());
		
		jdbcTemplate.update(SQL_COMMAND_INSERT_AZS, params);
	}
	
	@Override
	public DataElementAZS getDataElementAZSById(short stationId) {
		List<DataElementAZS> list = jdbcTemplate.query(SQL_COMMAND_SELECT_AZS, dataMapperAZS);
		for(DataElementAZS i : list)
			if(i.getStation_Id() == stationId) {
				return i;
			}
		return null;
	}

	@Override
	public List<DataElementAZS> getDataElementAZSALL() {
		return jdbcTemplate.query(SQL_COMMAND_SELECT_AZS, dataMapperAZS);
	}

	@Override
	public DataElementOil getDataElementOilByID(short stationId) {
		List<DataElementOil> list = jdbcTemplate.query(SQL_COMMAND_SELECT_OIL, dataMapperOil);
		for(DataElementOil i : list)
			if(i.getFkIdStation() == stationId) {
				return i;
			}
		return null;
	}

	@Override
	public void insertDataOil(DataElementOil dataElementOil) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(NAME_ATTRIBUT_NAME, dataElementOil.getName());
		params.addValue(NAME_ATTRIBUT_PRICE, dataElementOil.getPrice());
		params.addValue(NAME_ATTRIBUT_AMOUNT, dataElementOil.getAmountOfFuel());
		params.addValue(NAME_ATTRIBUT_FK_STATION_ID, dataElementOil.getFkIdStation());
		
		jdbcTemplate.update(SQL_COMMAND_INSERT_OIL, params);
	}

	@Override
	public List<DataElementOil> getDataElementOilAll() {
		return jdbcTemplate.query(SQL_COMMAND_SELECT_OIL, dataMapperOil);
	}

	@Override
	public List<DataElementOil> getDataElementOilAllById(short id) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(NAME_ATTRIBUT_FK_STATION_ID, id);
		return jdbcTemplate.query(SQL_COMMAND_SELECT_OIL_BY_ID, params, dataMapperOil);
	}

	@Override
	public void updateDataOil(DataElementOil dataElementOil) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(NAME_ATTRIBUT_NAME, dataElementOil.getName());
		params.addValue(NAME_ATTRIBUT_PRICE, dataElementOil.getPrice());
		params.addValue(NAME_ATTRIBUT_AMOUNT, dataElementOil.getAmountOfFuel());
		params.addValue(NAME_ATTRIBUT_FK_STATION_ID, dataElementOil.getFkIdStation());
		
		jdbcTemplate.update(SQL_COMMAND_UPDATE_OIL, params);
	}

	@Override
	public DataElementCamera getDataElementCameraByNumber(String number) {
		List<DataElementCamera> list = jdbcTemplate.query(SQL_COMMAND_SELECT_CAMERA, dataMapperCamera);
		for(DataElementCamera i : list) {
			if(i.getNumber().equals(number)) {
				return i;
			}
		}
		return null;
	}

	@Override
	public List<DataElementCamera> getDataElementCameraAll() {
		return jdbcTemplate.query(SQL_COMMAND_SELECT_CAMERA, dataMapperCamera);
	}

	@Override
	public void insertDataElementCamera(DataElementCamera data) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(NAME_ATTRIBUT_DATE, data.getDate());
		params.addValue(NAME_ATTRIBUT_RECOGNIZE, data.isRecognize());
		params.addValue(NAME_ATTRIBUT_NUMBER, data.getNumber());
		params.addValue(NAME_ATTRIBUT_IMAGE, data.getImage());
		
		jdbcTemplate.update(SQL_COMMAND_INSERT_CAMERA, params);
	}

	@Override
	public void updateDataElementCamera(DataElementCamera data){
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(NAME_ATTRIBUT_DATE, data.getDate());
		params.addValue(NAME_ATTRIBUT_RECOGNIZE, data.isRecognize());
		params.addValue(NAME_ATTRIBUT_NUMBER, data.getNumber());
		params.addValue(NAME_ATTRIBUT_IMAGE, data.getImage());
		
		jdbcTemplate.update(SQL_COMMAND_UPDATE_CAMERA, params);
	}
}
