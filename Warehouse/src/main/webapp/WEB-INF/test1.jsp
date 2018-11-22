
<%@page import="java.sql.*" %>
<%@ page import="java.util.*, java.io.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<html>
<head>
<title>Insert title here</title>
</head>
<body>

</body>
<form method="post">

<table border="2">
<tr>
<td>ID</td>
<td>SIZE</td>
<td>LOADABILITY</td>

</tr>
<%!
try
{
Class.forName("com.mysql.jdbc.Driver");
String url="jdbc:mysql://localhost:3306/warehouse";
String username="root";
String password="kimheehazelhasan11905";
String getLots = "select * from lots;";
Connection conn=DriverManager.getConnection(url,username,password);
Statement stmt=conn.createStatement();
ResultSet rs=stmt.executeQuery(getLots);
while(rs.next())
{%>
     <tr>
         <td><%=rs.getString("id")%></td>
         <td><%=rs.getString("size")%></td>
         <td><%=rs.getString("loadability")%></td>
     </tr>
<%}%>
    </table>
    <%
    rs.close();
    stmt.close();
    conn.close();
    }
catch(Exception e)
{
    e.printStackTrace();
    }

%>

</form>
</html>