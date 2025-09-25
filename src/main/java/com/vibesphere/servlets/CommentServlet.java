package com.vibesphere.servlets;

import com.vibesphere.util.DBUtil;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.sql.*;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("userId")==null) {
            resp.sendRedirect(req.getContextPath()+"/index.jsp?error=loginfirst");
            return;
        }
        long userId = (Long) s.getAttribute("userId");
        String vibeIdStr = req.getParameter("vibeId");
        String text = req.getParameter("text");
        if (vibeIdStr==null || text==null) { resp.sendRedirect(req.getContextPath()+"/vibefeed.jsp"); return; }
        long vibeId = Long.parseLong(vibeIdStr);

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO comments(vibe_id, user_id, text) VALUES(?,?,?)")) {
            ps.setLong(1, vibeId);
            ps.setLong(2, userId);
            ps.setString(3, text);
            ps.executeUpdate();
            resp.sendRedirect(req.getContextPath()+"/vibefeed.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath()+"/vibefeed.jsp?error=comment");
        }
    }
}
