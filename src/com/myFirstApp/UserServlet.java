package com.myFirstApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet (
		name = "/userServlet",
		urlPatterns = "/user",
		loadOnStartup = 1
		)

public class UserServlet extends HttpServlet implements Subject {
	
	private volatile int USER_ID_SEQUENCE = 1;
	private Map<Integer, Person> usersDatabase = new LinkedHashMap<>();// users database
	private Person tempUser = new Person();
	private String errorMsg;
	private List<Observer> observers = new ArrayList<Observer>();
	
	public UserServlet() {
		super();
        SaveToFile saveToFile = new SaveToFile();
		registerObserver(saveToFile);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		if(action == null)
			action = "listUsers";
		switch(action){
		case "add":
			this.showForm(req, resp);
			break;
		case "view":
			this.viewUser(req,resp);
			break;
		case "list":
		default:
			this.listUsers(resp);
		}
		// setting tempUser to null to avoid filling form with values when adding new user
		this.tempUser = null;  
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		if(action == null)
			action = "list";
		switch(action) {
		case "add":
			this.addUser(req, resp);
			break;
		case "list":
		default:
			resp.sendRedirect("user");
			break;
		}
	}

	private void listUsers(HttpServletResponse resp) throws IOException {
		/** displays "home page" as list of users registered in system (from userDatabase).
		 * If none user is existing then information about it is displayed 
		 */
		this.addHtmlHeader(resp);
		PrintWriter writer = resp.getWriter();
		writer.append("<h2>Testit</h2>")
			.append("<a href=\"user?action=add\">Add User</a><br/>"); // link to add new user
		
		if(this.usersDatabase.size() == 0)
			writer.append("<p><i>There is no users registered yet in system.</i></p>\r\n");
		else {
			writer.append("<ul>\r\n");
			for (int id : this.usersDatabase.keySet()) {
				String idString = Integer.toString(id);
				Person user = this.usersDatabase.get(id);
				writer.append("<li>User #").append(idString)
					.append(": <a href=\"user?action=view&userId=").append(idString)
					.append("\">").append(user.getName()).append("</a><br/></li>\r\n");
			}
		}
		this.addHtmlFooter(resp);
	}

	private void viewUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/** displays user record with specific ID
		 *  
		 */
		this.addHtmlHeader(resp);
		PrintWriter writer = resp.getWriter();
		//getting user ID number from request parameter
		Person user = this.usersDatabase.get(Integer.parseInt(req.getParameter("userId")));
		writer.append("<p>Hello ")
		.append(user.getName()).append(". You are ")
		.append(user.getAge())
		.append(" old and your user name is ")
		.append(user.getUsername())
		.append(".</p>	");
		
		writer.append("<br/><a href=\"user\">Return to list of users</a>");
		this.addHtmlFooter(resp);
	}

	private void showForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/** displays form to add new user. Post variable action is set to call responding method */
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");

		this.addHtmlHeader(resp);
		PrintWriter writer = resp.getWriter();
		if (errorMsg != null) {
			writer.append("<div class=\"alert alert-danger\" role=\"alert\">\r\n")
			.append("   <p>").append(errorMsg).append("</p>\r\n")
			.append("</div>\r\n");
			errorMsg = null;
		}
		writer.append("<form action=\"user\" method=\"POST\">\r\n")
		.append("<input type=\"hidden\" name=\"action\" value=\"add\"/>\r\n") // post variable action ="add"
  		.append("Enter your name:<br/>\r\n")
		.append("<input type=\"text\" name=\"name\" value=\"").append(this.getHttpFormInputValue("name")).append("\"/><br/>\r\n")
		.append("Enter your date of birth (yyyy-mm-dd):<br/>\r\n")
		.append("<input type=\"text\" name=\"dob\" value=\"").append(this.getHttpFormInputValue("dob")).append("\"/><br/>\r\n")
		.append("Enter your username:<br/>\r\n")
		.append("<input type=\"text\" name=\"username\" value=\"").append(this.getHttpFormInputValue("username")).append("\"/><br/>\r\n")
		.append("<input type=\"submit\" value=\"Submit\" /><br/>\r\n")
		.append("</form>\r\n");
		this.addHtmlFooter(resp);
	}

	private CharSequence getHttpFormInputValue(String string) {
		/** return form input value depends of information stored in tempUser
		 * 
		 */
		CharSequence value = "";
		if (tempUser != null) {
			if (string.equals("name"))
				if (tempUser.getName() == null)
					return value;
				else 
					return value + tempUser.getName(); 
			if (string.equals("dob"))
				if (tempUser.getDOB() == null)
					return value;
				else 
					return value + tempUser.getDOB().toString(); 
			if (string.equals("username"))
				if (tempUser.getUsername() == null)
					return value;
				else 
					return value + tempUser.getUsername();
		}
		return value;
	}

	private void addUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/** adds user to userDatabase
		 * 
		 */
		
		this.tempUser = new Person();
		this.tempUser.setName(req.getParameter("name"));
		this.tempUser.setUsername(req.getParameter("username"));
		try{
			this.tempUser.setDOB(req.getParameter("dob"));
			int id;

			synchronized (this){
				// gives access to resources for only 1 thread at a time
				id = this.USER_ID_SEQUENCE++;	
				this.usersDatabase.put(id, this.tempUser);
				notifyObservers();
			}
			resp.sendRedirect("user?action=view&userId=" + id);
		} catch (DateTimeParseException e) {
			errorMsg = "Warning!! You enter date of birth with wrong format. Please try again.";
			resp.sendRedirect("user?action=add");
		}
	}

	private void addHtmlHeader(HttpServletResponse resp) throws IOException {
		PrintWriter writer = resp.getWriter();

		writer.append("<!DOCTYPE html>\r\n")
		.append("<html>\r\n")
		.append("   <head>\r\n")
		.append("      <title>Users database</title>\r\n")
		.append("   </head>\r\n")
		.append("   <body>\r\n");

	}

	private void addHtmlFooter(HttpServletResponse resp) throws IOException {
		PrintWriter writer = resp.getWriter();

		writer.append("   </body>\r\n")
		.append("</html>\r\n");
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
			observer.update(this.usersDatabase);
		}
	}
}