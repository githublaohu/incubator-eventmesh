package org.apache.eventmesh.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ConvertEntity extends ConvertSuperEntity{

	private int intValue;
	
	private long longValue;
	
	private byte byteValue;
	
	private short shortValue;
	
	private char charValue;
	
	private float floatValue;
	
	private double doubleValue;
	
	private String stringValue;
	
	private List<String> listValue;
	
	private Map<String,String> mapValue;
	
	private ConverEnume converEnume;
	
	private Date dataValue;
	
	private LocalDate localDataValue;
	
	private LocalDateTime localDataTimeValue;
	
	//private ConvertEntity converEntiy;
}
