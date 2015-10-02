package com.myFirstApp;

import java.time.LocalDate;

public class Person {
	private String name;
	private int age = 0;
	private String username;
	private LocalDate dob;
	
	public Person() {
		
	}
	
	public Person(String name, int age, String username) {
		this.name = name;
		this.age = age;
		this.username = username;
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

	public int getAge() {
	if (age == 0) {
		return 0; // TODO calculate age from dob
	} else
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
