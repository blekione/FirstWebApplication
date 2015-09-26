package com.myFirstApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
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

public class UserServlet extends HttpServlet {
	
	private volatile int USER_ID_SEQUENCE = 1;
	private Map<Integer, Person> usersDatabase = new LinkedHashMap<>();// users database
	
	public UserServlet() {
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
		
		PrintWriter writer = resp.getWriter();
		writer.append("<h2>Testit</h2>")
			.append("<a href=\"user?action=add\">Add User</a><br/>"); // link to add new user
		
		if(this.usersDatabase.size() == 0)
			writer.append("<p><i>There is no users registered yet in system.</i></p>");
		else {
			for (int id : this.usersDatabase.keySet()) {
				String idString = Integer.toString(id);
				Person user = this.usersDatabase.get(id);
				writer.append("User #").append(idString)
					.append(": <a href=\"user?action=view&userId=").append(idString)
					.append("\">").append(user.getName()).append("</a><br/>");
			}
		}
	}

	private void viewUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/** displays user record
		 *  
		 */
		PrintWriter writer = resp.getWriter();
		Person user = this.usersDatabase.get(Integer.parseInt(req.getParameter("userId")));
		writer.append("Hello ")
		.append(user.getName()).append(". You are ")
		.append(Integer.toString(user.getAge()))
		.append(" years old and your user name is ")
		.append(user.getUsername())
		.append(".");	
	}

	private void showForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/** displays form to add new user. Post variable action is set to call responding method */
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");

		PrintWriter writer = resp.getWriter();
		writer.append("<form action=\"user\" method=\"POST\">\r\n")
		.append("<input type=\"hidden\" name=\"action\" value=\"add\"/>\r\n") // post variable action ="add"
  		.append("Enter your name:<br/>\r\n")
		.append("<input type=\"text\" name=\"name\" /><br/>\r\n")
		.append("Enter your age:<br/>\r\n")
		.append("<input type=\"text\" name=\"age\" /><br/>\r\n")
		.append("Enter your username:<br/>\r\n")
		.append("<input type=\"text\" name=\"username\" /><br/>\r\n")
		.append("<input type=\"submit\" value=\"Submit\" /><br/>\r\n")
		.append("</form>\r\n")
		.append("</body>")
		.append("</html>");		

	}

	private void addUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/** adds user to userDatabase
		 * 
		 */
		
		System.out.println("in addUser");
		Person user = new Person();
		user.setName(req.getParameter("name"));
		user.setAge(Integer.parseInt(req.getParameter("age")));
		user.setUsername(req.getParameter("username"));
		int id;
		
		synchronized (this){
		// gives access to resources for only 1 thread at a time
			id = this.USER_ID_SEQUENCE++;	
		this.usersDatabase.put(id, user);
		}
		resp.sendRedirect("user?action=view&userId=" + id);
	}
}
