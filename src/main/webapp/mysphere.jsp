<%@ page import="java.sql.*,com.vibesphere.util.DBUtil,javax.servlet.http.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    HttpSession s = request.getSession(false);
    if (s==null || s.getAttribute("userId")==null) {
        response.sendRedirect("index.jsp?error=loginfirst");
        return;
    }
    long uid = (Long)s.getAttribute("userId");
%>
<!doctype html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>My Sphere — VibeSphere</title>
  <link rel="stylesheet" href="assets/css/style.css"/>
</head>
<body>
  <div class="topbar">
    <a class="logo" href="vibefeed.jsp">VibeSphere</a>
    <a href="upload.jsp">Post Vibe</a>
    <a href="logout">Logout</a>
  </div>
  <div class="container">
    <div class="card profile">
      <%
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT username,display_name,profile_pic_url,banner_url FROM users WHERE id=?")) {
            ps.setLong(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
      %>
      <div class="banner" style="background-image:url('<%= rs.getString("banner_url")!=null?rs.getString("banner_url") : "assets/default-banner.png" %>');"></div>
      <img class="avatar-large" src="<%= rs.getString("profile_pic_url")!=null ? rs.getString("profile_pic_url") : "assets/default-profile.png" %>"/>
      <h2><%= rs.getString("display_name")!=null?rs.getString("display_name"):rs.getString("username") %></h2>
      <%      }
            }
        } catch (Exception e) { out.println(e.getMessage()); }
      %>
      <p class="small">Your vibes</p>
      <div class="grid">
        <% try (Connection c = DBUtil.getConnection();
             PreparedStatement ps2 = c.prepareStatement("SELECT id,media_url,media_type,caption FROM vibes WHERE user_id=? ORDER BY created_at DESC")) {
             ps2.setLong(1, uid);
             try (ResultSet rs2 = ps2.executeQuery()) {
                 while (rs2.next()) {
        %>
          <div class="tile">
            <% if ("video".equals(rs2.getString("media_type"))) { %>
              <video src="<%= rs2.getString("media_url") %>" controls></video>
            <% } else { %>
              <img src="<%= rs2.getString("media_url") %>" />
            <% } %>
            <p><%= rs2.getString("caption") %></p>
          </div>
        <%     }
             }
        } catch (Exception e) { out.println(e.getMessage()); } %>
      </div>
    </div>
  </div>
</body>
</html>