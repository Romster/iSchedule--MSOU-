package ru.romanov.schedule.utils;

import java.util.ArrayList;
import java.util.Map;

public class MySubjectUpdateManager {
	public static String OK = "OK";
	public static String FAIL = "FAIL";
	public static String ERROR = "ERROR!";
	
	private ArrayList<MySubject> subjectsToAdd;
	private ArrayList <SubjectToRemove> subjectsToDelete;
	private String status;
	public MySubjectUpdateManager() {
		subjectsToAdd = new ArrayList<MySubject>();
		subjectsToDelete = new ArrayList<SubjectToRemove>();
	}
	
	public ArrayList<MySubject> getSubjectsToAdd () {
		return subjectsToAdd;
	}
	
	public ArrayList<SubjectToRemove> getSubjectsToDelete() {
		return subjectsToDelete;
	}
	
	public void addSubjectToAdd (MySubject sbj) {
		subjectsToAdd.add(sbj);
	}
	
	public void addSubjectToDelete (SubjectToRemove sbj) {
		subjectsToDelete.add(sbj);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
