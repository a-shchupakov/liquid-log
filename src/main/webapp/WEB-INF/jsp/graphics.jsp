<%@ page import="ru.naumen.sd40.log.parser.parseMods.DataType" %>
<%@ page import="java.util.List" %>
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


<div class="container">
    <br>
    <h1>Performance data for "client"</h1>
    <h3><a class="btn btn-success btn-lg" href="/">Client list</a></h3>
    <h4 id="date_range"></h4>
    <p>
        Feel free to hide/show specific percentile by clicking on chart's legend
    </p>
    <ul id="view-nav" class="nav nav-pills">
        <% for(DataType dataType: (List<DataType>)request.getAttribute("dataTypes")) { %>
        <li class="nav-item"><a class="btn btn-outline-primary" onclick="setActive(event)"><%=dataType.toString()%></a></li>
        <% } %>
    </ul>
</div>

<div class="container" id="response-chart-container" style="height:600px;">
</div>

<script>

    function setActive(event) {
        var element = document.getElementById("view-nav");
        for (var i = 0; i < element.children.length; i++) {
            element.children[i].children[0].classList.add("btn", "btn-outline-primary");
            element.children[i].children[0].classList.remove("active", "nav-link");
        }
        var target = event.target;
        target.classList.add("nav-link", "active");
    }

</script>



</body>
</html>
