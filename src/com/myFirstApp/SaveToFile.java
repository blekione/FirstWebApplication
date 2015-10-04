package com.myFirstApp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class SaveToFile implements Observer {
	
	private final String FIELD_SEPARATOR = ";";

	@Override
	public <T, K> void update(Map<T, K> mapOfUsers) {
		System.out.println("inside SaveToFile object");
		Person person = (Person)mapOfUsers.get(1);
		try(FileWriter fwriter = new FileWriter("test.txt")) {
			fwriter.write(person.getName() + FIELD_SEPARATOR);
			fwriter.write(person.getDOB()+ FIELD_SEPARATOR);
			fwriter.write(person.getUsername() + FIELD_SEPARATOR);
		} catch (IOException e) {
			System.out.println("I/O Error: " + e);
		}
	}

}
