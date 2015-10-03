package com.myFirstApp;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class Person {
	private String name;
	private String username;
	private LocalDate dob;
	
	public Person() {
		
	}
		
	public Person(String name, LocalDate dob, String username) {
		this.name = name;
		this.dob = dob;
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		int years, months, days;
			years = (int)ChronoUnit.YEARS.between(dob, LocalDate.now());
			if (years > 0)
				return years + " years";
			else {
				months = (int)ChronoUnit.MONTHS.between(dob, LocalDate.now());
				if (months > 0)
					return months + " months";
				else {
					days = (int)ChronoUnit.DAYS.between(dob, LocalDate.now());
					return days + " days";
				}
			}
	}	 
		
	public void setDOB(String dob) throws DateTimeParseException {
		this.dob = LocalDate.parse(dob);
		System.out.println(this.dob); // TODO delete it as only purpose is to check if dob is set
	}
	
	public LocalDate getDOB() {
		return this.dob;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
