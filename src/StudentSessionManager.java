import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StudentSessionManager {
    private Student student;
    private File sessionFolder;

    public StudentSessionManager(Student student){
        this.student = student;
    }

    public void createSession(){    // 세션(폴더) 만들기
        String folderName = "captures/" + student.getStudentId() + "_" + student.getName(); // 문자열 폴더 이름

        sessionFolder = new File(folderName);   // 실제 폴더x, "이 위치가 가리키는 객체"만 만들어짐

        if (!sessionFolder.exists()){       // 폴더 이름의 폴더가 존재하지 않으면
            sessionFolder.mkdirs();         // 폴더 생성 
        }

        System.out.println("세션 생성 : " + sessionFolder.getPath());
    }

    public File getSessionFolder(){ // 세션(폴더) 이름 반환자 
        return sessionFolder;
    }

    public File createCaptureFile(EventType eventType){     // 현재 시간 가져오기, 시간 문자열 변환, 시간_이벤트.png 만들기, File 객체 반환
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

        String time = now.format(formatter);

        String fileName = time + "_" + eventType.name() + ".jpg";

        return new File(sessionFolder, fileName);
    }
}
