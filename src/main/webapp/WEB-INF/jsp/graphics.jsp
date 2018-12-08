<%@ page import="java.util.Map" %>
<%@ page import="java.util.Arrays" %>
<%--
  Created by IntelliJ IDEA.
  User: PK
  Date: 08.12.2018
  Time: 13:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Graphics</title>
</head>
<body>

<%
    Map<String, Number[]> map = (Map<String, Number[]>)request.getAttribute("data");
    for (Map.Entry<String, Number[]> entry: map.entrySet())
    {%>
        <p><span>KEY: <%=entry.getKey()%></span>: <span><%=Arrays.toString(entry.getValue())%></span></p>
    <%}%>
</body>
</html>
