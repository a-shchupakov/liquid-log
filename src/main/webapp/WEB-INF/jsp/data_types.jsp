<%@ page import="ru.naumen.sd40.log.parser.parseMods.DataType" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.naumen.sd40.log.parser.parseMods.ParsingUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: PK
  Date: 06.12.2018
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>SD40 Performance indicator</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/css/bootstrap.min.css"
          integrity="sha384-AysaV+vQoT3kOAXZkl02PThvDr8HYKPZhNT5h/CXfBThSRXQ6jW5DO2ekP5ViFdi" crossorigin="anonymous"/>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/js/bootstrap.min.js"
            integrity="sha384-BLiI7JTZm+JWlgKa0M0kGRpJbF2J8q+qreVrKBC47e3K6BW78kGLrCkeRX6I9RoK"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/css/style.css"/>
</head>
<body>
<%
    //Prepare links
    String path="";
    String custom = "";
    if(request.getAttribute("custom") == null){
        Object year = request.getAttribute("year");
        Object month = request.getAttribute("month");
        Object day = request.getAttribute("day");

        String countParam = (String)request.getParameter("count");

        String params = "";
        String datePath = "";

        StringBuilder sb = new StringBuilder();


        if(countParam != null){
            params = sb.append("?count=").append(countParam).toString();
        }else{
            sb.append('/').append(year).append('/').append(month);
            if(!day.toString().equals("0")){
                sb.append('/').append(day);
            }
            datePath = sb.toString();
        }
        path = datePath + params;
    }
    else{
        custom = "/custom";
        Object from = request.getAttribute("from");
        Object to = request.getAttribute("to");
        Object maxResults = request.getAttribute("maxResults");

        StringBuilder sb = new StringBuilder();
        path = sb.append("?from=").append(from).append("&to=").append(to).append("&maxResults=").append(maxResults).toString();
    }


%>

<div class="container">
    <br>
    <h1>Performance data for <%=request.getAttribute("client")%></h1>
    <h3><a class="btn btn-success btn-lg" href="/">Client list</a></h3>
    <p>
        Feel free to hide/show specific percentile by clicking on chart's legend
    </p>
    <ul id="view-nav" class="nav nav-pills">
        <% for(DataType dataType: ((List<DataType>)request.getAttribute("dataTypes"))) { %>
        <li class="nav-item">
            <a class="btn btn-outline-primary" href="/graphics/${client}<%=custom%>/<%=dataType.toString()%><%=path%>">
                <%=ParsingUtils.stringifyDataType(dataType)%>
            </a>
        </li>
        <% } %>
    </ul>
</div>
</body>
</html>
