<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>CIS3368 - Final Project - View - Lejing Huang</title>

        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

        <!-- CSS Template Source: https://www.w3schools.com/bootstrap/bootstrap_ref_css_tables.asp -->
        <!--===============================================================================================-->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <!--===============================================================================================-->
        <script src="https://canvasjs.com/assets/script/canvasjs.min.js" type="text/javascript"></script>
        <!--===============================================================================================-->
        <script type="text/javascript">
            window.onload = function() {
                var dps1 = [[]];
                var chart1 = new CanvasJS.Chart("chartContainer1", {
	                animationEnabled: true,
	                theme: "light2",
                    title: {
                        text: "COVID-19 Total Cases vs. Total Deaths"
                    },
                    subtitles: [{
                        text: "Source: John Hopkins University"
                    }],
                    axisY: {
                        title: "Cases"
                    },
                    data: [{
                        type: "column",
                        yValueFormatString: "###,###,000",
                        dataPoints: dps1[0]
                    }]
                });

                var yValue1;
                var label1;

                <c:forEach items="${dataList1}" var="dataPoints" varStatus="loop">
                    <c:forEach items="${dataPoints}" var="dataPoint">
                        yValue1 = parseInt("${dataPoint.y}");
                        label1 = "${dataPoint.label}";
                        dps1[parseInt("${loop.index}")].push({
                            label : label1,
                            y : yValue1,
                        });
                    </c:forEach>
                </c:forEach>

                chart1.render();

                var dps2 = [[]];
                var chart2 = new CanvasJS.Chart("chartContainer2", {
	                animationEnabled: true,
	                theme: "light2",
                    title: {
                        text: "COVID-19 New Cases vs. New Deaths"
                    },
                    subtitles: [{
                        text: "Source: John Hopkins University"
                    }],
                    axisY: {
                        title: "Cases"
                    },
                    data: [{
                        type: "column",
                        yValueFormatString: "###,###,000",
                        dataPoints: dps2[0]
                    }]
                });

                var yValue2;
                var label2;

                <c:forEach items="${dataList2}" var="dataPoints" varStatus="loop">
                    <c:forEach items="${dataPoints}" var="dataPoint">
                        yValue2 = parseInt("${dataPoint.y}");
                        label2 = "${dataPoint.label}";
                        dps2[parseInt("${loop.index}")].push({
                        label : label2,
                        y : yValue2,
                        });
                    </c:forEach>
                </c:forEach>

                chart2.render();
            }
        </script>
        <!--===============================================================================================-->

    </head>

    <body>
        <div class="container">

            <h2>COVID-19 Cases Graph</h2><hr>

            <div id="chartContainer1" style="height: 370px; width: 100%;"></div><br><br><br><br>
            <div id="chartContainer2" style="height: 370px; width: 100%;"></div><br><br><br><br>

            <h2>COVID-19 Cases Detail</h2>
            <div class="table-responsive">

                <!-- Create a Country Table at the View Page -->
                <table class="table">

                    <!-- COUNTY -->
                    <tr>
                        <th>Country</th>
                        <td>${selectedCountry.getCountryName()}</td>
                    </tr>

                    <!-- DATE -->
                    <tr>
                        <th>Date</th>
                        <td>${selectedCountry.getDate()}</td>
                    </tr>

                    <!-- TOTAL CASES -->
                    <tr>
                        <th>Total Cases</th>
                        <td>${selectedCountry.getTotalCases()}</td>
                    </tr>

                    <!-- TOTAL DEATHS -->
                    <tr>
                        <th>Total Deaths</th>
                        <td>${selectedCountry.getTotalDeaths()}</td>
                    </tr>

                    <!-- New CASES -->
                    <tr>
                        <th>New Cases</th>
                        <td>${selectedCountry.getNewCases()}</td>
                    </tr>

                    <!-- New DEATHS -->
                    <tr>
                        <th>New Deaths</th>
                        <td>${selectedCountry.getNewDeaths()}</td>
                    </tr>

                    <!-- ADMIN USERNAME -->
                    <tr>
                        <th>Save By</th>
                        <td>${selectedUser.getUsername()}</td>
                    </tr>

                </table>
            </div>

            <a href="/home">Back to Homepage</a><br><br>

        </div>

    </body>
</html>
