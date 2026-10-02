package my;


import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;

public class UpdateStudentServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            double math    = Double.parseDouble(req.getParameter("math"));
            double science = Double.parseDouble(req.getParameter("science"));
            double english = Double.parseDouble(req.getParameter("english"));
            double hindi   = Double.parseDouble(req.getParameter("hindi"));
            double sst     = Double.parseDouble(req.getParameter("sst"));

            if (math < 0 || math > 100 || science < 0 || science > 100 ||
                english < 0 || english > 100 || hindi < 0 || hindi > 100 ||
                sst < 0 || sst > 100) {
                out.println("<h3>Error: Marks must be between 0 and 100.</h3>");
                out.println("<a href='updateStudent.html'>Try Again</a>");
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
                Student s = em.find(Student.class, id);
                if (s != null) {
                    s.setMathMarks(math);
                    s.setScienceMarks(science);
                    s.setEnglishMarks(english);
                    s.setHindiMarks(hindi);
                    s.setSstMarks(sst);
                    s.setTotalMarks(total);
                    s.setPercentage(percentage);
                    s.setGrade(grade);
                    tx.commit();
                    out.println("<h3>Student marks updated successfully!</h3>");
                    out.println("New Total: " + total + "<br>");
                    out.println("New Percentage: " + percentage + "%<br>");
                    out.println("New Grade: " + grade + "<br>");
                } else {
                    tx.rollback();
                    out.println("<h3>No student found with ID: " + id + "</h3>");
                }
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                out.println("<h3>Error: " + e.getMessage() + "</h3>");
            } finally {
                em.close();
            }

        } catch (NumberFormatException e) {
            out.println("<h3>Error: Invalid numeric input.</h3>");
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }

        out.println("<a href='updateStudent.html'>Update Another</a> | ");
        out.println("<a href='index.html'>Home</a>");
        out.close();
    }
}