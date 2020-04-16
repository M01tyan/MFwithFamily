package Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet(name="HelloServlet", urlPatterns = { "/HelloServlet"})
public class HelloServlet extends HttpServlet implements ServletContextAttributeListener {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
        out.println("<HTML>");
        out.println("<BODY>");
        out.println("<H3>Hello World!</H3>");
        out.println("</BODY>");
        out.println("</HTML>");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = (String)request.getParameter("name");
		System.out.println("name: " + name);
	}
}
