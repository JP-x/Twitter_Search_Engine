<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.io.*,java.util.*, java.util.regex.*, com.mycompany.*" %>
<%@page session="true"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title> Twitter34 </title>
<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico" />
<style type="text/css">
.cursorHand{  cursor: pointer;   cursor: hand; }	
#container {    margin:0 auto;    width: 50%;	float: center;	text-align: center;	postion: relative;}
#contents {    width: 100%;	text-align: left;	float: center;}	
#masthead {width: 100%;height: 60px;text-align: center;background-color: #0099cc;float: center;margin:0 auto;}
#results_area {margin-top: 30px; font-family: "Arial Black", Gadget, sans-serif; }
#form {	float: center;margin-top: 30px;}
</style>


<script type ="text/javascript">
function empty() {
    var x;
    x = document.getElementById("queryString").value;
    if (x == "") {
        alert("Enter a non empty string.");
        return false;
    };
}
</script>





</head>
<body topmargin="0" leftmargin="10" rightmargin="10" bottommargin="0" style="color:white">
        <body background="twitterBG.png">  
<% 
String queryString = "";
Integer answerSize = 10;
%>
<%
	if (request.getParameter("search_submit") != null) {
		queryString = request.getParameter("queryString");			
	}
        
%>
<div id="page">
	
		<text color ="white">
                    <center><h1><a href="index.jsp"><img src="twitter34lettering.png" height="78" width="400"></a></h1></center>
	
	<div id="container">
		<div id="form">
			<form action="results" method="POST">
				<br />
				<input type="text" name="queryString" value="<% out.println(queryString); %>" size="40" /> 
				<input type="submit" value="Search" onClick="return empty()" />
			</form>
		</div>
		<div id="contents">		
			<div id="results_area">
			</div>
		</div>
	</div>
</div>
</body>
</html>
