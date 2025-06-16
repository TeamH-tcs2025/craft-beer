<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head><title>Welcome</title></head>
<body>
    <h2>ようこそ！</h2>
    <p>
        ユーザー名：<%= session.getAttribute("username") %>
    </p>
    <form action="logout" method="post">
        <input type="submit" value="ログアウト">
    </form>
</body>
</html>
