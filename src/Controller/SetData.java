package Controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet implementation class SetData
 */
//@WebServlet("/SetData")
public class SetData implements Filter {

	public void init(FilterConfig config) throws ServletException {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
		System.out.println(((HttpServletRequest) request).getRequestURL() + ": " + new java.util.Date());
		chain.doFilter(request, response);
	}

	public void destroy() {}

}
