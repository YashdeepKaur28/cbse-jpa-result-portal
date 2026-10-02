package my;

import javax.persistence.EntityManager;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;

public class SearchStudentServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            int id = Integer.parseInt(req.getParameter("id"));

            EntityManager em = JPAUtil.getEntityManager();
            try {
                Student s = em.find(Student.class, id);
                if (s != null) {
                    out.println("<h3>Student Result Card</h3>");
                    out.println("<table border='1'><tr><th>Field</th><th>Value</th></tr>");
                    out.println("<tr><td>Name: </td><td>" + s.getName() + "</td></tr>");
                    out.println("<tr><td>Class: </td><td>" + s.getClassName() + "</td></tr>");
                    out.println("<tr><td>Maths</td><td>" + s.getMathMarks() + "</td></tr>");
                    out.println("<tr><td>Science</td><td>" + s.getScienceMarks() + "</td></tr>");
                    out.println("<tr><td>English</td><td>" + s.getEnglishMarks() + "</td></tr>");
                    out.println("<tr><td>Hindi</td><td>" + s.getHindiMarks() + "</td></tr>");
                    out.println("<tr><td>Social Science</td><td>" + s.getSstMarks() + "</td></tr>");
                    out.println("<tr><td>Total: </td><td>" + s.getTotalMarks() + "</td></tr>");
                    out.println("<tr><td>Percentage: </td><td>" + s.getPercentage() + "</td></tr>");
                    out.println("<tr><td>Grade: </td><td>" + s.getGrade() + "</td></tr>");
                    out.println("</table>");
                    out.println("<a href='excel?id=" + id + "'>Download</a><br>");
                } else {
                    out.println("<h3>No student found with ID: " + id + "</h3>");
                }
            } finally {
                em.close();
            }

        } catch (NumberFormatException e) {
            out.println("<h3>Error: Invalid ID.</h3>");
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }

        out.println("<a href='searchStudent.html'>Search Again</a> | ");
        out.println("<a href='index.html'>Home</a>");
        out.close();
    }
}