<%@ page import="java.util.Map" %>
<%@ page import="ru.naumen.sd40.log.parser.parseMods.ParsingUtils" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/css/bootstrap.min.css"
          integrity="sha384-AysaV+vQoT3kOAXZkl02PThvDr8HYKPZhNT5h/CXfBThSRXQ6jW5DO2ekP5ViFdi" crossorigin="anonymous"/>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/js/bootstrap.min.js"
            integrity="sha384-BLiI7JTZm+JWlgKa0M0kGRpJbF2J8q+qreVrKBC47e3K6BW78kGLrCkeRX6I9RoK"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/css/style.css"/>
</head>
<body>
<script src="http://code.highcharts.com/highcharts.js"></script>

<div style="margin-left: 20px">
    <br>
    <h1>Performance data for <%=request.getAttribute("client")%></h1>
    <button type="button" class="btn btn-success btn-lg" onclick="history.back()">Go back</button>
    <h4 id="date_range"></h4>
</div>

<div id="chart-container" style="height: 600px"></div>

<script type="text/javascript">
    <%
        Map<String, Number[]> values = (Map<String, Number[]>)request.getAttribute("data");
        Map<String, String> fullNames = (Map<String, String>)request.getAttribute("fullNames");
        Map<String, String> units = (Map<String, String>)request.getAttribute("units");

        Set<String> namesSet = values.keySet();
        Number times[] = values.remove(ParsingUtils.Constants.TIME);
        String names[] = namesSet.toArray(new String[namesSet.size()]);
        List<Number[]> entries =  new ArrayList<>(values.values());
    %>

    times = [];
    dataFetched = [];
    names = [];
    units = [];

    <% for(int i=0;i<names.length;i++) {%>
    dataFetched[<%=i%>] = [];
        names[<%=i%>] = "<%=fullNames.get(names[i])%>";
        units[<%=i%>] = "<%=units.get(names[i])%>";
    <%}%>

    <% for(int i=0;i<times.length;i++) {%>
    times.push((<%=times[i]%>));
        <% for(int j = 0;j<names.length;j++) {%>
    dataFetched[<%=j%>].push([new Date(<%= times[i] %>), <%= java.lang.Math.round(entries.get(j)[i].doubleValue()) %>]);
        <%}%>
    <% } %>

    resultSeries = [];
    for (var i = 0; i < names.length; i++) {
        resultSeries.push({
            name: names[i] + units[i],
            data: dataFetched[i],
            turboThreshold: 10000
        })
    }

    console.log(resultSeries);

    var myChart = Highcharts.chart('chart-container', {
        chart: {
            zoomType: 'x,y'
        },
        title: {
            text: '<%=request.getAttribute("title")%>'
        },
        tooltip: {
            formatter: function() {
                var index = this.point.index;
                var date =  new Date(times[index]);
                return Highcharts.dateFormat('%a %d %b %H:%M:%S', date)
                    + '<br/> <b>'+this.series.name+'</b> - '+ this.y + ' times<br/>'
            }
        },
        xAxis: {
            labels:{
                formatter:function(obj){
                    return Highcharts.dateFormat('%a %d %b %H:%M:%S', new Date(times[this.value]));
                }
            },
            reversed: true
        },
        yAxis: {
            title: {
                text: '<%=request.getAttribute("dataTypeName")%>'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        plotOptions: {
            line: {
                marker: {
                    enabled: false
                }
            }
        },
        series: resultSeries
    });

    document.getElementById('date_range').innerHTML += 'From: '+new Date(times[<%=times.length%>-1])+'<br/>To:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +new Date(times[0])

</script>
</body>
</html>
