package com.vibesphere.servlets;

import com.vibesphere.util.DBUtil;
import at.favre.lib.crypto.bcrypt.BCrypt;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String usernameOrEmail = req.getParameter("username");
        String pw = req.getParameter("password");

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id, username, email, password_hash, display_name, profile_pic_url, banner_url FROM users WHERE username=? OR email=?")) {
            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("password_hash");
                    BCrypt.Result result = BCrypt.verifyer().verify(pw.toCharArray(), hash);
                    if (result.verified) {
                        HttpSession s = req.getSession(true);
                        s.setAttribute("userId", rs.getLong("id"));
                        s.setAttribute("username", rs.getString("username"));
                        s.setAttribute("displayName", rs.getString("display_name"));
                        s.setAttribute("profilePic", rs.getString("profile_pic_url"));
                        resp.sendRedirect(req.getContextPath() + "/vibefeed.jsp");
                        return;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        resp.sendRedirect(req.getContextPath() + "/index.jsp?error=login");
    }
}
