package com.exercise;

import java.util.ArrayList;

public class TeamLeader extends Developer {
	double bonus_rate;

	public TeamLeader(String empID, String empName, int baseSal, String teamName,
	                  ArrayList<String> programmingLanguages, int expYear, double bonus_rate) {
		super(empID, empName, baseSal, teamName, programmingLanguages, expYear);
		this.bonus_rate = bonus_rate;
	}

	public double getBonus_rate() {
		return bonus_rate;
	}

	@Override
	public double getSalary() {
		return super.getSalary() + bonus_rate * super.getSalary();
	}
}
