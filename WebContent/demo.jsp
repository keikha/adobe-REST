<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="ciir.umass.edu.sum.TermExtractor"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<%
TermExtractor te=(TermExtractor)application.getAttribute("aspectModel");
if(te==null)
{
        te= new TermExtractor(getServletContext().getRealPath("parameters/aspect.json"));
        application.setAttribute("aspectModel", te);
}

%>

<title>Aspect Identifier</title>
</head>
<style type="text/css">
h1 {
	COLOR: #00BFFF;
	FONT-FAMILY: 'Times New Roman';
	TEXT-TRANSFORM: capitalize
}

a.specialeffects:hover {
	color: black;
	background-color: #B0B0B0;
}

a.m_title { #
	display: block;
	text-transform: capitalize;
}
</style>
<body>

	<h1 align="center" style="color: #00BFFF">
		<img
			src="https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQk7pLu7WUphAtZTrK81vAQrhaHDqI1n54CY3Tz6bs6T0Jzza-fWkl0XQ"
			alt="Aspect Identitifer"><img src="logoimage.gif" alt="logo"
			height="70">
	</h1>
	<form action="demo.jsp" method="get">
		<p align="center">
			<input type="radio" name="aspects" value="phrase" checked>Aspects
			<input type="radio" name="aspects" value="tag"># Tags<br>
		</p>
		<p align="center">
			<input type="text" name="entity" align="bottom" size="60"
				maxlength="60" placeholder="Enter entity or topic"> <input
				type="submit" value="AspectSearch" align="bottom">
		</p>
	</form>
	<ol>
		<%
String entity= request.getParameter("entity");
String query= request.getParameter("query");
String aspects=  request.getParameter("aspects");
try {

if (entity!=null) {
        entity = entity.replaceAll("[#,\\!,\\$,\\^,\\*,&,\\`,[0-9],@,%,(,),\\[,\\],\\?,\\.,\\,\\|,>,<]", "");
        if (entity.length() > 1) {
	    //System.out.println(entity);
	    if(aspects.equals("tag")) {
	        List<String>L;
                L=te.getResults(entity, true);
		if(L.size()==0)
		{
		%>

		<div style="float: left; width: 33.5%;">
			<b> There are no results for the given query, please try a new
				query</b>
		</div>

		<%
			
		}
		else{
		%>
		<div style="float: left; width: 33.5%;">
			<b>Hash Tags</b>
		</div>
		<div style="float: left; width: 33% .5;">
			<b><i>frequency in a month</i> </b>
		</div>
		<br>
		<br>
		<%

                for(String str:L) {
		    String count=te.getPhraseCount(str);
        	%>
		<div style="float: left; width: 33.5%;">
			<li align='left'><a class="m_title specialeffects"
				href='demo.jsp?query=<%=entity+" "+str %>'><b><%="#"+str%></b> </a>
			</li>
		</div>
		<div style="float: left; width: 33.5%;">
			<i><%=count%></i>
		</div>
		<% } 
	    }}
	    else {
	        List<String>L;
                L=te.getResults(entity, false);
		if(L.size()==0)
		{
		%>
		<div style="float: left; width: 33.5%;">
			<b> There are no results for the given query, please try a new
				query</b>
		</div>
		<%
		}
		else{
		%>
		<div style="float: left; width: 33.5%;">
			<b>Aspect Phrases</b>
		</div>
		<div style="float: left; width: 33.5%;">
			<b><i>Frequency in top retrieved tweets</i> </b>
		</div>
		<br>
		<br>
		<%
                for(String str:L) {
		    String count=te.getPhraseCount(str);
        	%>
		<div style="float: left; width: 33.5%;">
			<li align='left'><a class="m_title specialeffects"
				href='demo.jsp?query=<%=entity+" "+str %>'><b><%=str%></b> </a></li>
		</div>
		<div style="float: left; width: 33.5%;">
			<i><%=count%></i>
		</div>
		<% }
            }}
        }
        else {
            out.println("Enter a valid query");
        }
}
if(query!=null) {
        List<String>Q=te.getDocuments(query, 50, "tweet");
        for(String doc:Q) {
%>
		<li align='left'><%=doc%></li>
		<%
out.println();
        }
}
} catch (Exception e) {
	out.println(e.getMessage());
}
%>
	</ol>
</body>
</html>
