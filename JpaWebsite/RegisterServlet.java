package my;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;

public class RegisterServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email    = req.getParameter("email");

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            User existing = em.find(User.class, username);
            if (existing != null) {
                tx.rollback();
                res.setContentType("text/html");
                PrintWriter out = res.getWriter();
                out.println("<html><body>");
                out.println("<h3>Username already exists. Please choose another.</h3>");
                out.println("<a href='register.html'>Back</a>");
                out.println("</body></html>");
                return;
            }

            User u = new User(username, password, email);
            em.persist(u);
            tx.commit();

            Email.sendRegistrationEmail(email, username);
            res.sendRedirect("login?registered=true&username=" + username);

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<html><body>");
            out.println("<h3>Registration failed: " + e.getMessage() + "</h3>");
            out.println("<a href='register.html'>Back</a>");
            out.println("</body></html>");
        } finally {
            em.close();
        }
    }
}