<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
    <head>
        <title>セッション情報</title>
    </head>
    <body>
        <h2>セッション情報を表示するJSP</h2>

        <p>sessionID : <%=session.getId()%></p>

        <p>count ：<%=session.getAttribute("count")%>
        <p><a href="./counter">RELOAD</a>
    </body>
</html>