package my;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.List;

public class ExcelServlet extends HttpServlet {
    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        boolean isSingle = (idParam != null && !idParam.trim().isEmpty());

        EntityManager em = JPAUtil.getEntityManager();
        try {
            res.setContentType("application/vnd.ms-excel");
            if (isSingle) {
                res.setHeader("Content-Disposition",
                    "attachment; filename=student_" + idParam + ".xls");
            } else {
                res.setHeader("Content-Disposition",
                    "attachment; filename=all_students.xls");
            }
            PrintWriter out = res.getWriter();
            out.println("ID\tName\tClass\tMaths\tScience\tEnglish\tHindi\tSST\tTotal\tPercentage\tGrade");

            if (isSingle) {
                int id = Integer.parseInt(idParam);
                Student s = em.find(Student.class, id);
                if (s != null) printRow(out, s);
                else out.println("\nNo student found with ID: " + idParam);
            } else {
                TypedQuery<Student> q = em.createQuery(
                    "SELECT s FROM Student s ORDER BY s.studentId", Student.class);
                List<Student> list = q.getResultList();
                for (Student s : list) printRow(out, s);
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        } finally {
            em.close();
        }
    }

    private void printRow(PrintWriter out, Student s) {
        out.print(s.getStudentId() + "\t");
        out.print(s.getName() + "\t");
        out.print(s.getClassName() + "\t");
        out.print(s.getMathMarks() + "\t");
        out.print(s.getScienceMarks() + "\t");
        out.print(s.getEnglishMarks() + "\t");
        out.print(s.getHindiMarks() + "\t");
        out.print(s.getSstMarks() + "\t");
        out.print(s.getTotalMarks() + "\t");
        out.print(s.getPercentage() + "\t");
        out.println(s.getGrade());
    }
}