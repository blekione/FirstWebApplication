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
		try(FileWriter fwriter = new FileWriter("test.txt")) {
			for (int i = 1; i <= mapOfUsers.size(); i++) {
				Person person = (Person)mapOfUsers.get(i);

				fwriter.write(person.getName() + FIELD_SEPARATOR);
				fwriter.write(person.getDOB()+ FIELD_SEPARATOR);
				fwriter.write(person.getUsername() + FIELD_SEPARATOR);
				fwriter.write("\n");
			}
		} catch (IOException e) {
			System.out.println("I/O Error: " + e);
		}
	}

}
