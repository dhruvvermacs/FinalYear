/**
 * This contains to delete the message and its deatils when ever it is called by the user
 * 
 * messages are permanently deleted from the database
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
import java.util.*;
public class deletemessage extends HttpServlet//importing of data present in the httpservlet
{
	Connection con;
	ResultSet rs=null;
	Statement st;
	PrintWriter out;
	public void init(ServletConfig sc)throws ServletException/*initiliziing the servlet*/
	{
	try
	{
		super.init(sc);
		Class.forName("com.mysql.jdbc.Driver");
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/messagedb","root","ROOT");
		st=con.createStatement();/*Creating Statement object for sending SQL statements to the database.*/
	}catch(Exception e)
		{System.out.println(e.toString());}
	}
	public void service(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
	try
	{
		res.setContentType("text/html");//setting the response type as html
		out=res.getWriter();
		String una=null;
		Cookie[] c = req.getCookies();//accessing cookie from cache data i.e form sessions 
		if(c!=null)
		for(int i=0;i<c.length;i++)/*getting user cache*/
		{
			if(c[i].getName().equals("signin"))
			{
			una=c[i].getValue();
			break;
			}
		}
	//Deleting the unwanted mails
		Enumeration names = req.getParameterNames();
		StringTokenizer str; 	
         	while(names.hasMoreElements())
         	{
           	String name = (String)names.nextElement();
           	String value = req.getParameter(name);
	if(value.equals("on"))
		{
		str=new StringTokenizer(name,"|");
		while(str.hasMoreTokens())
			{
			String mfrom=str.nextToken();
			String mdat=str.nextToken();
			String del="delete from "+una+" where msgfrom='"+mfrom+"' and msgdate='"+mdat+"'";
			st.executeUpdate(del);
			con.commit();

			}
		}
	}
	
	//Remaining mails will be displayed on the left frame of the inbox
         	/* below is the syntax for displaying mails*/
				
		String mailsel="select msgfrom,subject,msgdate from "+una;
		rs=st.executeQuery(mailsel);
		out.println("<html><BODY ><form action='deletemessage' method=post><h2>Welcome <FONT COLOR=blue>"+una+" </FONT></h2>");
		out.println("<input type=submit name=t1 value=Delete><h3>Messages in inbox</h3><TABLE BORDER=0><TR><TH>Check</TH><TH>From</TH><TH>Subject</TH><TH>Date</TH></TR>");
		while(rs.next())
		 {	
			String from=rs.getString(1);
			String sub=rs.getString(2);
			String dat=rs.getString(3);
			out.println("<TR><TD><INPUT TYPE=CHECKBOX NAME="+from+"|"+dat+">");
			out.println("<TD width=200 align=center><A href=getmsg?msgf="+from+"&msgd="+dat+" target=rightf>"+from+"</A>");
			out.println("<TD width=200 align=center>"+sub+"<TD width=200 align=center>"+dat+"</TR>");
		}
		out.println("</Table><p><input type=submit name=b1 value=Delete></form></BODY>");
		}catch(Exception e)
		{out.println(e.toString());
		e.printStackTrace();}
	}
}
