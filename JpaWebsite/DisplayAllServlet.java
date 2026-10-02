package my;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.List;

public class DisplayAllServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>All Students</title>");
        out.println("<style>table, th, td { border: 1px solid black; border-collapse: collapse; padding: 8px; }</style>");
        out.println("</head><body>");
        out.println("<h2>All Students - CBSE Result</h2>");
       out.println("<a href='excel'>Download all results</a><br><br>");

        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Student> q = em.createQuery(
                "SELECT s FROM Student s ORDER BY s.studentId", Student.class);
            List<Student> list = q.getResultList();

            out.println("<table>");
            out.println("<tr><th>ID</th><th>Name</th><th>Class</th>");
            out.println("<th>Maths</th><th>Science</th><th>English</th><th>Hindi</th><th>SST</th>");
            out.println("<th>Total</th><th>%</th><th>Grade</th></tr>");

            for (Student s : list) {
                out.println("<tr>");
                out.println("<td>" + s.getStudentId() + "</td>");
                out.println("<td>" + s.getName() + "</td>");
                out.println("<td>" + s.getClassName() + "</td>");
                out.println("<td>" + s.getMathMarks() + "</td>");
                out.println("<td>" + s.getScienceMarks() + "</td>");
                out.println("<td>" + s.getEnglishMarks() + "</td>");
                out.println("<td>" + s.getHindiMarks() + "</td>");
                out.println("<td>" + s.getSstMarks() + "</td>");
                out.println("<td>" + s.getTotalMarks() + "</td>");
                out.println("<td>" + s.getPercentage() + "%</td>");
                out.println("<td>" + s.getGrade() + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            if (list.isEmpty()) out.println("<p>No students found.</p>");

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        } finally {
            em.close();
        }

        out.println("<br><a href='index.html'>Back to Home</a>");
        out.println("</body></html>");
        out.close();
    }
}