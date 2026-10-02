package my;

public class GradeUtil {

    public static double calculateTotal(double math, double science,
                                        double english, double hindi, double sst) {
        return math + science + english + hindi + sst;
    }

    public static double calculatePercentage(double total) {
        return total / 5.0;
    }

    public static String getGrade(double percentage) {
        if (percentage >= 91) return "A1";
        else if (percentage >= 81) return "A2";
        else if (percentage >= 71) return "B1";
        else if (percentage >= 61) return "B2";
        else if (percentage >= 51) return "C1";
        else if (percentage >= 41) return "C2";
        else if (percentage >= 33) return "D";
        else return "E";
    }

    public static Object[] calculateFullResult(double math, double science,
                                               double english, double hindi,
                                               double sst) {
        double total = calculateTotal(math, science, english, hindi, sst);
        double percentage = calculatePercentage(total);
        String grade = getGrade(percentage);
        return new Object[]{total, percentage, grade};
    }
}