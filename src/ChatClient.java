import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient extends AbstractClient {

    public ChatClient(Student student) {
        super(student);
        //TODO Auto-generated constructor stub
    }
    
    protected void connectToServer() throws IOException {
        // AbstractClient -> 부모클래스 -> 서버측과 연결된 소켓을 주입해주어야 한다.
        super.setSocket(new Socket("localhost", 5000));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("학번 : ");
        String studentId = scanner.nextLine();
        System.out.print("이름 : ");
        String name = scanner.nextLine();

        Student student = new Student(studentId, name);

        ChatClient chatClient = new ChatClient(student);
        chatClient.run();

        chatClient.sendEvent(EventType.COPY);
    }
}
