package com.vibesphere.servlets;

import com.vibesphere.util.DBUtil;
import com.vibesphere.util.S3Util;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import java.util.Random;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

@WebServlet("/upload")
@MultipartConfig
public class UploadVibeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/upload.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("userId")==null) {
            resp.sendRedirect(req.getContextPath()+"/index.jsp?error=loginfirst");
            return;
        }
        long userId = (Long)s.getAttribute("userId");
        String caption = req.getParameter("caption");

        Part filePart = req.getPart("media");
        if (filePart == null || filePart.getSize() == 0) {
            resp.sendRedirect(req.getContextPath()+"/upload.jsp?error=nomedia");
            return;
        }

        String contentType = filePart.getContentType();
        String ext = ".dat";
        String mediaType = "image";
        if (contentType != null && contentType.startsWith("video")) {
            mediaType = "video";
            ext = ".mp4";
        } else if (contentType!=null && contentType.startsWith("image")) {
            ext = contentType.equals("image/png") ? ".png" : ".jpg";
        }

        String key = "user_" + userId + "/vibe_" + System.currentTimeMillis() + "_" + (new Random().nextInt(9999)) + ext;
        try (InputStream is = filePart.getInputStream()) {
            String url = S3Util.upload(key, is, filePart.getSize(), contentType);

            try (Connection c = DBUtil.getConnection();
                 PreparedStatement ps = c.prepareStatement("INSERT INTO vibes(user_id, caption, media_url, media_type) VALUES(?,?,?,?)")) {
                ps.setLong(1, userId);
                ps.setString(2, caption);
                ps.setString(3, url);
                ps.setString(4, mediaType);
                ps.executeUpdate();
            }
            resp.sendRedirect(req.getContextPath()+"/vibefeed.jsp?msg=posted");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath()+"/upload.jsp?error=upload");
        }
    }
}
