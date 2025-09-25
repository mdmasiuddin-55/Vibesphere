<%@ page import="javax.servlet.http.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    HttpSession s = request.getSession(false);
    if (s==null || s.getAttribute("userId")==null) {
        response.sendRedirect("index.jsp?error=loginfirst");
        return;
    }
%>
<!doctype html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>Post a Vibe — VibeSphere</title>
  <link rel="stylesheet" href="assets/css/style.css"/>
</head>
<body>
  <div class="topbar">
    <a class="logo" href="vibefeed.jsp">VibeSphere</a>
    <a href="vibefeed.jsp">Vibefeed</a>
    <a href="mysphere.jsp">My Sphere</a>
    <a href="logout">Logout</a>
  </div>

  <div class="container">
    <div class="card">
      <h3>Post a Vibe</h3>
      <form action="upload" method="post" enctype="multipart/form-data">
        <textarea name="caption" placeholder="Write a caption..."></textarea>
        <input type="file" name="media" accept="image/*,video/*" required/>
        <button class="btn">Post Vibe</button>
      </form>
    </div>
  </div>
</body>
</html>
