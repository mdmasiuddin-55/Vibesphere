<%@ page import="javax.servlet.http.*,java.sql.*,com.vibesphere.util.DBUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String err = request.getParameter("error");
    String msg = request.getParameter("msg");
%>
<!doctype html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>VibeSphere — login</title>
  <link rel="stylesheet" href="assets/css/style.css"/>
</head>
<body class="page-auth">
  <div class="hero">
    <div class="left">
      <h1>VibeSphere</h1>
      <p class="tag">Post your vibes, sip some ☕ mocha, and ride the vibefeed ✨</p>
      <form method="post" action="login" class="card login-card">
        <h3>Welcome back</h3>
        <input name="username" placeholder="username or email" required/>
        <input name="password" placeholder="password" type="password" required/>
        <button type="submit" class="btn">Sign in</button>
        <p class="small">New? <a href="register.jsp">Create an account</a></p>
        <div class="notice">
          <% if ("login".equals(err)) { %>
            <p class="error">Invalid credentials</p>
          <% } else if (msg!=null) { %>
            <p class="ok">Registered. Please login!</p>
          <% } %>
        </div>
      </form>
    </div>
    <div class="right">
      <img src="assets/hero-genz.png" alt="GenZ"/>
    </div>
  </div>
</body>
</html>
