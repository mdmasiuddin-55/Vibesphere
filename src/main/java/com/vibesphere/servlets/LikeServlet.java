package com.vibesphere.servlets;

import com.vibesphere.util.DBUtil;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.sql.*;

@WebServlet("/like")
public class LikeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("userId")==null) {
            resp.sendError(401);
            return;
        }
        long userId = (Long) s.getAttribute("userId");
        String vibeIdStr = req.getParameter("vibeId");
        if (vibeIdStr == null) { resp.sendError(400); return; }
        long vibeId = Long.parseLong(vibeIdStr);

        try (Connection c = DBUtil.getConnection()) {
            // try insert; if duplicate -> remove (toggle)
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO mocha_likes(vibe_id, user_id) VALUES(?,?)")) {
                ps.setLong(1, vibeId);
                ps.setLong(2, userId);
                ps.executeUpdate();
            } catch (SQLException ex) {
                // already liked -> remove
                try (PreparedStatement ps2 = c.prepareStatement("DELETE FROM mocha_likes WHERE vibe_id=? AND user_id=?")) {
                    ps2.setLong(1, vibeId);
                    ps2.setLong(2, userId);
                    ps2.executeUpdate();
                }
            }
            resp.sendRedirect(req.getContextPath()+"/vibefeed.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(500);
        }
    }
}
