public class Student {
    private String studentId;
    private String name;

    public Student(String name){
        this(name,"미입력");
    }

    public Student(String studentId, String name){
        this.studentId = studentId;
        this.name = name;
    }

    public void setStudentId(String studentId){
        this.studentId = studentId;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getStudentId(){
        return studentId;
    }
}
