<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>VibeSphere — register</title>
  <link rel="stylesheet" href="assets/css/style.css"/>
</head>
<body class="page-auth">
  <div class="center-card">
    <form action="register" method="post" class="card">
      <h2>Create your VibeSphere</h2>
      <input name="username" placeholder="username" required/>
      <input name="displayName" placeholder="Display name"/>
      <input name="email" type="email" placeholder="email" required/>
      <input name="password" type="password" placeholder="password" required/>
      <button class="btn" type="submit">Sign up</button>
      <p class="small">Already? <a href="index.jsp">Sign in</a></p>
      <div class="notice">
        <% if ("exists".equals(request.getParameter("error"))) { %>
          <p class="error">User/email already exists</p>
        <% } %>
      </div>
    </form>
  </div>
</body>
</html>
