package com.vibesphere.servlets;

import com.vibesphere.util.DBUtil;
import at.favre.lib.crypto.bcrypt.BCrypt;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.sql.*;

@WebServlet("/register")
@MultipartConfig
public class RegisterServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String display = req.getParameter("displayName");
        String email = req.getParameter("email");
        String pass = req.getParameter("password");

        if (username==null || pass==null || email==null) {
            resp.sendRedirect(req.getContextPath() + "/register.jsp?error=Missing");
            return;
        }

        String hash = BCrypt.withDefaults().hashToString(12, pass.toCharArray());

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO users(username, email, password_hash, display_name) VALUES(?,?,?,?)")) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, hash);
            ps.setString(4, display);
            ps.executeUpdate();
            resp.sendRedirect(req.getContextPath() + "/index.jsp?msg=registered");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/register.jsp?error=exists");
        }
    }
}
