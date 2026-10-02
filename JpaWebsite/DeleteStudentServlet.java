package my;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;

public class DeleteStudentServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            int id = Integer.parseInt(req.getParameter("id"));

            EntityManager em = JPAUtil.getEntityManager();
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                Student s = em.find(Student.class, id);
                if (s != null) {
                    em.remove(s);
                    tx.commit();
                    out.println("<h3>Student deleted successfully!</h3>");
                } else {
                    tx.rollback();
                    out.println("<h3>No student found with the given ID.</h3>");
                }
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                out.println("<h3>Error: " + e.getMessage() + "</h3>");
            } finally {
                em.close();
            }

        } catch (NumberFormatException e) {
            out.println("<h3>Error: Invalid ID.</h3>");
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }

        out.println("<a href='deleteStudent.html'>Delete Another</a> | ");
        out.println("<a href='index.html'>Home</a>");
        out.close();
    }
}