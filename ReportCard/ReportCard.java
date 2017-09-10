
public class ReportCard {
    private int StudentID;
    private String StudentName;
    private String Course;
    private int Grade;
    private String Semester;

    public ReportCard(int studentID, String studentName, String course, int grade, String semester) {
        StudentID = studentID;
        StudentName = studentName;
        Course = course;
        Grade = grade;
        Semester = semester;
    }

    public int getStudentID() {
        return StudentID;
    }
    public void setStudentID(int studId){
        StudentID=studId;}

    public String getStudentName() {
        return StudentName;
    }
    public void setStudentName(String studName){
        StudentName=studName;}

    public String getCourse() {
        return Course;
    }
    public void setCourse(String studGrade){
        Course=studGrade;}

    public int getGrade() {
        return Grade;
    }
    public void setGrade(int studGrade){
        Grade=studGrade;}

    public String getSemester() {
        return Semester;
    }
    public void setSemester(String sem){
        Semester=sem;}

    public String getLetterGrade(int grade){

        String Grade;

        if(grade >= 79 && grade <=100){
            Grade = "A";
        } else if (grade >= 69 && grade <= 50){
            Grade = "B";
        } else if (grade >= 59 && grade <= 70){
            Grade = "C";
        }  else if (grade >= 49 && grade <= 60){
            Grade = "D";
        } else {
            Grade = "F";
        }
        return Grade;
    }

    @Override
    public String toString() {
        return "ReportCard{" +"studentID=" + StudentID + ", studentName='" + StudentName + '\'' + ", course='" + Course + '\'' + ", grade=" + Grade + ", semester='" + Semester + '\'' + '}';
    }
}
