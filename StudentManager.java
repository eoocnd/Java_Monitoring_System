import java.util.ArrayList;

public class StudentManager {
    private ArrayList<Student> students = new ArrayList<>();

    public void addStudent(Student student){
        students.add(student);
    }

    public void removeStudent(Student student){
        students.remove(student);
    }

    public void printStudents(){
        int i = 1;
        System.out.println("--- 현재 접속 학생 ---");
        for (Student s : students){
            System.out.println("[" + i + "] " + s.getStudentId() + " " + s.getName());
            i ++;
        }
        System.out.println("--------------------");
    }
}
