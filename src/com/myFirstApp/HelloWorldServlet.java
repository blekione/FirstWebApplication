package com.myFirstApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpRetryException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloWorldServlet
 */
@WebServlet(
		name = "/helloWorldServlet",
		urlPatterns = "/myApp",
		loadOnStartup = 1
		)
		
public class HelloWorldServlet extends HttpServlet implements Subject {
	private static final String DEFAULT_USER = "Guest";
	private Person user = new Person();
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	
    public HelloWorldServlet() {
        super();
        SaveToFile saveToFile = new SaveToFile();
		registerObserver(saveToFile);
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		if (user.getName() == null) {
			getUserInformationForm(response);
		} else {
			displayUserInformation(request, response);
		}
	}

	private void displayUserInformation(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		updateUser(request);
		notifyObservers();
		writer.append("Hello ")
		.append(user.getName()).append(". You was born on ")
		.append(user.getDOB().toString())
		.append(" and your user name is ")
		.append(user.getUsername())
		.append(".");
		
	}

	private void updateUser(HttpServletRequest request) {
		user.setName(request.getParameter("name"));
		user.setDOB(request.getParameter("dob"));
		user.setUsername(request.getParameter("username"));
	}

	private void getUserInformationForm(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		PrintWriter writer = response.getWriter();

		user.setName(HelloWorldServlet.DEFAULT_USER);
		writer.append("<!DOCTYPE html>\r\n")
		.append("<html>\r\n")
		.append("<head>\r\n")
		.append("<title>Hello User</title>\r\n")
		.append("</head>")
		.append("<body>")
		.append("<h1>Hello ").append(user.getName()).append("!</h1>")
		.append("<form action=\"myApp\" method=\"POST\">\r\n")
		.append("Enter your name:<br/>\r\n")
		.append("<input type=\"text\" name=\"name\" /><br/>\r\n")
		.append("Enter your date of birth (yyyy-mm-dd):<br/>\r\n")
		.append("<input type=\"text\" name=\"dob\" /><br/>\r\n")
		.append("Enter your username:<br/>\r\n")
		.append("<input type=\"text\" name=\"username\" /><br/>\r\n")
		.append("<input type=\"submit\" value=\"Submit\" /><br/>\r\n")
		.append("</form>\r\n")
		.append("</body>")
		.append("</html>");		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		this.doGet(request, response);
	}

	@Override
	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		int index = observers.indexOf(observer);
		if (index >= 0)
			observers.remove(index);
	}

	@Override
	public void notifyObservers() {
		for (int i =  0; i < observers.size(); i++) {
			Observer observer = (Observer) observers.get(i);
			observer.update(user);
		}
	}
}
