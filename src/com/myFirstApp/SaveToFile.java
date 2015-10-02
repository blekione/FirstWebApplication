package com.myFirstApp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveToFile implements Observer {
	
	private final String FIELD_SEPARATOR = ";";

	@Override
	public void update(Person person) {
		System.out.println("inside SaveToFile object");
		try(FileWriter fwriter = new FileWriter("test.txt")) {
			fwriter.write(person.getName() + FIELD_SEPARATOR);
			fwriter.write(person.getDOB()+ FIELD_SEPARATOR);
			fwriter.write(person.getUsername() + FIELD_SEPARATOR);
		} catch (IOException e) {
			System.out.println("I/O Error: " + e);
		}
	}

}
