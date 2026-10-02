package my;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;

public class AddStudentServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("name");
            String cls  = req.getParameter("class");
            double math    = Double.parseDouble(req.getParameter("math"));
            double science = Double.parseDouble(req.getParameter("science"));
            double english = Double.parseDouble(req.getParameter("english"));
            double hindi   = Double.parseDouble(req.getParameter("hindi"));
            double sst     = Double.parseDouble(req.getParameter("sst"));

            if (math < 0 || math > 100 || science < 0 || science > 100 ||
                english < 0 || english > 100 || hindi < 0 || hindi > 100 ||
                sst < 0 || sst > 100) {
                out.println("<h3>Error: Marks must be between 0 and 100.</h3>");
                out.println("<a href='addStudent.html'>Try Again</a>");
                return;
            }

            Object[] r = GradeUtil.calculateFullResult(math, science, english, hindi, sst);
            double total      = (Double) r[0];
            double percentage = (Double) r[1];
            String grade      = (String) r[2];

            EntityManager em = JPAUtil.getEntityManager();
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                Student s = new Student(id, name, cls, math, science, english,
                                        hindi, sst, total, percentage, grade);
                em.persist(s);
                tx.commit();
                out.println("<h3>Student added successfully!</h3>");
           } catch (Exception e) {
    if (tx.isActive()) tx.rollback();

    Throwable root = e;
    while (root.getCause() != null) root = root.getCause();
    String msg = root.getMessage();

    
    if (msg != null && msg.contains("ORA-00001")) {
        msg = "Student ID already exists. Please use a different ID.";
    }

    out.println("<h3>Error: " + msg + "</h3>");
    out.println("<a href='addStudent.html'>Try Again</a>");
} finally {
                em.close();
            }

        } catch (NumberFormatException e) {
            out.println("<h3>Error: Invalid numeric input.</h3>");
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }

        out.println("<a href='addStudent.html'>Add Another</a> | ");
        out.println("<a href='index.html'>Home</a>");
        out.close();
    }
}