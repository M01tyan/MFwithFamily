/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/9.0.27
 * Generated at: 2020-05-11 00:11:56 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class login_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = null;
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    if (!javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      final java.lang.String _jspx_method = request.getMethod();
      if ("OPTIONS".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        return;
      }
      if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET, POST or HEAD. Jasper also permits OPTIONS");
        return;
      }
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<style type=\"text/css\">\n");
      out.write("\n");
      out.write("body {\n");
      out.write("\ttext-align: center;\n");
      out.write("\tmargin-top: 150px;\n");
      out.write("}\n");
      out.write(".background {\n");
      out.write("}\n");
      out.write("\n");
      out.write(".cp_iptxt {\n");
      out.write("\tposition: relative;\n");
      out.write("\twidth: 50%;\n");
      out.write("\tmargin: 20px auto;\n");
      out.write("}\n");
      out.write(".cp_iptxt input[type=text], .cp_iptxt input[type=password] {\n");
      out.write("\tfont: 15px/24px sans-serif;\n");
      out.write("\tbox-sizing: border-box;\n");
      out.write("\twidth: 100%;\n");
      out.write("\tmargin: 8px 0;\n");
      out.write("\tpadding: 0.3em;\n");
      out.write("\ttransition: 0.3s;\n");
      out.write("\tborder: 1px solid #1b2538;\n");
      out.write("\tborder-radius: 4px;\n");
      out.write("\toutline: none;\n");
      out.write("}\n");
      out.write(".cp_iptxt input[type=text]:focus, .cp_iptxt input[type=password]:focus {\n");
      out.write("\tborder-color: #da3c41;\n");
      out.write("}\n");
      out.write(".cp_iptxt input[type=text], .cp_iptxt input[type=password] {\n");
      out.write("\tpadding-left: 40px;\n");
      out.write("}\n");
      out.write(".cp_iptxt i {\n");
      out.write("\tposition: absolute;\n");
      out.write("\ttop: 8px;\n");
      out.write("\tleft: 0;\n");
      out.write("\tpadding: 9px 8px;\n");
      out.write("\ttransition: 0.3s;\n");
      out.write("\tcolor: #aaaaaa;\n");
      out.write("}\n");
      out.write(".cp_iptxt input[type=text]:focus + i {\n");
      out.write("\tcolor: #da3c41;\n");
      out.write("}\n");
      out.write(".button {\n");
      out.write("\tpadding: 20px 40px;\n");
      out.write("\tfont-size: 1.2em;\n");
      out.write("\tborder-style: none;\n");
      out.write("\tborder: double 1px black;\n");
      out.write("\tcolor: orange;\n");
      out.write("\tmargin-bottom: 20px;\n");
      out.write("\tbackground-color: white;\n");
      out.write("}\n");
      out.write("input[type=\"submit\"]:hover {\n");
      out.write("\tbackground-color: orange;\n");
      out.write("\tcolor: white;\n");
      out.write("}\n");
      out.write(".new-accunt {\n");
      out.write("\tcolor: orange;\n");
      out.write("}\n");
      out.write("</style>\n");
      out.write("<meta charset=\"UTF-8\">\n");
      out.write("<title>Money Forward with Family</title>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("<div class=\"background\">\n");
      out.write("\t<h1>Money Forward <span style=\"color: orange;\">with Family</span></h1>\n");
      out.write("\t<h2>ログイン</h2>\n");
      out.write("\t");
 String message = (String)request.getAttribute("message"); 
      out.write("\n");
      out.write("\t<span style=\"color: red\">");
      out.print( message == null ? "" : message );
      out.write("</span>\n");
      out.write("\t<form name=\"loginForm\" action=\"login\" method=\"post\">\n");
      out.write("\t\t<div class=\"cp_iptxt\">\n");
      out.write("\t\t\t<input type=\"text\" placeholder=\"メールアドレス\" name=\"email\">\n");
      out.write("\t\t\t<i class=\"fa fa-user fa-lg fa-fw\" aria-hidden=\"true\"></i>\n");
      out.write("\t\t</div>\n");
      out.write("\t\t<div class=\"cp_iptxt\">\n");
      out.write("\t\t\t<input type=\"password\" placeholder=\"パスワード\" name=\"password\">\n");
      out.write("\t\t\t<i class=\"fa fa-user fa-lg fa-fw\" aria-hidden=\"true\"></i>\n");
      out.write("\t\t</div>\n");
      out.write("\t\t<div class=\"button_wrapper\"><input class=\"button\" type=\"submit\" value=\"LOGIN\"></div>\n");
      out.write("\t</form>\n");
      out.write("\n");
      out.write("\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("/signUp\" class=\"new-accunt\">新規登録</a>\n");
      out.write("</div>\n");
      out.write("</body>\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
