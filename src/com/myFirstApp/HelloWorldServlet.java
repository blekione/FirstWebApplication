package com.myFirstApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpRetryException;

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
		
public class HelloWorldServlet extends HttpServlet {
	private static final String DEFAULT_USER = "Guest";
	private Person user = new Person(); 
	
    public HelloWorldServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
			
		user.setName(request.getParameter("name"));
		if (user.getName() == null) {
			getUserInformationForm(response);
		} else {
			displayUserInformation(request, response);
		}
	}

	private void displayUserInformation(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		
		writer.append("Hello ")
		.append(user.getName()).append(". You are ")
		.append(request.getParameter("age"))
		.append(" years old and your user name is ")
		.append(request.getParameter("username"))
		.append(".");
		
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
		.append("Enter your age:<br/>\r\n")
		.append("<input type=\"text\" name=\"age\" /><br/>\r\n")
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
}
