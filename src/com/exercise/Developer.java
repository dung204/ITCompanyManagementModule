package com.exercise;

import java.util.ArrayList;

public class Developer extends Employee {
	protected String teamName;
	protected ArrayList<String> programmingLanguages;
	protected int expYear;

	public Developer(String empID, String empName, int baseSal, String teamName,
	                 ArrayList<String> programmingLanguages, int expYear) {
		super(empID, empName, baseSal);
		this.teamName = teamName;
		this.programmingLanguages = programmingLanguages;
		this.expYear = expYear;
	}

	public String getTeamName() {
		return teamName;
	}

	public ArrayList<String> getProgrammingLanguages() {
		return programmingLanguages;
	}

	public int getExpYear() {
		return expYear;
	}

	@Override
	public double getSalary() {
		if(expYear >= 5)
			return baseSal + expYear * 2000000;
		if(expYear >= 3)
			return baseSal + expYear * 1000000;
		return baseSal;
	}

	@Override
	public String toString() {
		StringBuilder languagesResult = new StringBuilder();
		for(String language : programmingLanguages) {
			languagesResult.append(language).append(",");
		}
		languagesResult.deleteCharAt(languagesResult.length() - 1);
		languagesResult.insert(0, "[");
		languagesResult.insert(languagesResult.length(), "]");
		return empID + "_" + empName + "_" + baseSal + "_" + teamName + "_" + languagesResult + "_" + expYear;
	}
}
