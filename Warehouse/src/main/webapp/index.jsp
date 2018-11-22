<%@ page import="model.WarehouseDAO" %>
<%@page import="java.sql.*" %>
<%@ page import="java.util.*, java.io.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%! WarehouseDAO wrh = new WarehouseDAO();%>

<html>
<body>

<%=wrh.getInformationForAllDatabase() %>


</body>
</html>
