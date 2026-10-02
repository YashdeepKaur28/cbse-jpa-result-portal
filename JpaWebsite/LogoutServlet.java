package my;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;

public class LogoutServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        res.sendRedirect("login");
    }
}