/*
*this page includes the code to change the password of the user
 * 
 */
package com.msg.pkg;

/**
 * @author Pagidi Maneesh
 *
 */
import javax.servlet.*;
 import javax.servlet.http.*;
 import java.io.*;
 import java.sql.*;
 public class Setnewpassword extends HttpServlet//importing of data present in the httpservlet
 {
 	Connection con;
 	Statement st;
 	PrintWriter out;
 public void init(ServletConfig sc)throws ServletException/*initiliziing the servlet*/
 {
 	try
 	{
 	super.init(sc);
 	Class.forName("com.mysql.jdbc.Driver");
 	con=DriverManager.getConnection("jdbc:mysql://localhost:3306/messagedb","root","ROOT");
 	st=con.createStatement();
 	}catch(Exception e)
 		{System.out.println(e.toString());}
 }
 public void service(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
 {
 	try
 	{
 	res.setContentType("text/plain");//setting the response type as html
 	out=res.getWriter();
 	String oldpwd=req.getParameter("oldp");
 	String newpwd=req.getParameter("newp");
 	String conpwd=req.getParameter("conp");
 	String una=null;
 	Cookie[] c = req.getCookies();//requesting the cache data from the browser
 		if(c!=null)
 		for(int i=0;i<c.length;i++)
 		{
 			if(c[i].getName().equals("signin"))
 			{
 				una=c[i].getValue();
 				break;
 			}
 		}
 	ResultSet rs=st.executeQuery("select password from mailusers where username='"+una+"'");
 	while(rs.next())
 	{
 		if(rs.getString("password").equalsIgnoreCase(oldpwd))//comapring the ld passwoed with new
 			
 		{/*if password satisfies the required fields then it will be changed */
 			st.executeUpdate("update mailusers set password='"+newpwd+"' where password='"+oldpwd+"'");
 			out.println("<h3>Your New Pasword was set successfully wants to go for <a href=inbox.html target=rightf><i> Inbox </i></a></h3>");
 			break;
 		}
 		else
 			out.println("Invalid password");
 	}
 	}catch(Exception e)
 		{out.println(e.toString());
 		e.printStackTrace();}
 }
 }