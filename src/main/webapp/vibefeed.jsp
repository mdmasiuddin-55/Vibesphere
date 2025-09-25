<%@ page import="java.sql.*,com.vibesphere.util.DBUtil,javax.servlet.http.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    HttpSession session = request.getSession(false);
    Long myId = null;
    if (session!=null && session.getAttribute("userId")!=null) myId = (Long)session.getAttribute("userId");
%>
<!doctype html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>Vibefeed — VibeSphere</title>
  <link rel="stylesheet" href="assets/css/style.css"/>
  <script src="assets/js/app.js" defer></script>
</head>
<body>
  <div class="topbar">
    <a class="logo" href="vibefeed.jsp">VibeSphere</a>
    <a href="upload.jsp">Post Vibe</a>
    <a href="mysphere.jsp">My Sphere</a>
    <% if (myId!=null) { %>
      <a href="logout">Logout</a>
    <% } else { %>
      <a href="index.jsp">Sign in</a>
    <% } %>
  </div>

  <div class="container">
    <div class="feed">
      <%
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(
               "SELECT v.id,v.caption,v.media_url,v.media_type,v.created_at,u.id as uid,u.username,u.display_name,u.profile_pic_url, " +
               "(SELECT COUNT(*) FROM mocha_likes m WHERE m.vibe_id=v.id) as mocha_count " +
               "FROM vibes v JOIN users u ON v.user_id=u.id ORDER BY v.created_at DESC")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
      %>
      <div class="vibe-card">
        <div class="vibe-head">
          <img class="avatar" src="<%= rs.getString("profile_pic_url") != null ? rs.getString("profile_pic_url") : "assets/default-profile.png" %>" />
          <div>
            <div class="name"><%= rs.getString("display_name") != null ? rs.getString("display_name") : rs.getString("username") %></div>
            <div class="time"><%= rs.getTimestamp("created_at") %></div>
          </div>
        </div>

        <div class="vibe-body">
          <p class="caption"><%= rs.getString("caption") %></p>
          <% String mediaUrl = rs.getString("media_url"); String mediaType = rs.getString("media_type"); %>
          <% if ("video".equals(mediaType)) { %>
            <video controls class="vibe-media"><source src="<%=mediaUrl%>" type="video/mp4">Your browser doesn't support video.</video>
          <% } else { %>
            <img class="vibe-media" src="<%=mediaUrl%>" />
          <% } %>
        </div>

        <div class="vibe-actions">
          <form method="post" action="like" style="display:inline;">
            <input type="hidden" name="vibeId" value="<%= rs.getLong("id") %>"/>
            <button class="like-btn">☕ mocha <span>(<%= rs.getInt("mocha_count") %>)</span></button>
          </form>

          <details class="comments">
            <summary>Comments</summary>
            <div class="comment-list">
              <% 
                 try (PreparedStatement ps2 = c.prepareStatement("SELECT c.text,u.display_name FROM comments c JOIN users u ON c.user_id=u.id WHERE c.vibe_id=? ORDER BY c.created_at")) {
                     ps2.setLong(1, rs.getLong("id"));
                     try (ResultSet rs2 = ps2.executeQuery()) {
                         while (rs2.next()) {
              %>
                <div class="comment"><strong><%= rs2.getString("display_name") %>:</strong> <%= rs2.getString("text") %></div>
              <%       }
                     }
                 }
              %>
              <% if (myId!=null) { %>
              <form method="post" action="comment" class="comment-form">
                <input type="hidden" name="vibeId" value="<%= rs.getLong("id") %>"/>
                <input name="text" placeholder="Add a vibe reply..." required/>
                <button>Reply</button>
              </form>
              <% } else { %>
                <div class="small">Sign in to comment</div>
              <% } %>
            </div>
          </details>
        </div>

      </div>
      <%      }
            }
        } catch (Exception e) { out.println("<pre>"+e.getMessage()+"</pre>"); }
      %>
    </div>
    <aside class="rightcol">
      <div class="card">
        <h4>What's a mocha?</h4>
        <p>☕ A mocha is our 'like' — show your love for the vibe!</p>
      </div>
    </aside>
  </div>
</body>
</html>
