package ru.romanov.schedule.utils;

import java.util.HashMap;
import java.util.Map;

public class SubjectToRemove {
	
	private String ID = "id";
	@SuppressWarnings("unused")
	private String UPDATE_DATE = "update-dt"; 
	
	private Map <String, String> fields;
	
	public SubjectToRemove() {
		fields = new HashMap<String, String>(3);
	}
	
	public void setId (String _id) {
		
	}
	
	public String getId() {
		return fields.get(ID);
	}
	
}
