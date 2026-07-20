import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AbstractClient {
    private Socket socket;      
    private Student student;
    private PrintWriter socketWriter;   
    private BufferedReader socketReader;    
    private BufferedReader keyboardReader;  


    public AbstractClient(Student student){     
        this.student = student;
    }

    // 외부에서 나의 멤버 변수에 참조변수를 주입 할 수 있도록 setter 메서드 설계
    protected void setSocket(Socket socket){        
        this.socket = socket;
    }

    public final void run(){        
        try{
            connectToServer();      
            setupStreams();         
            sendLoginMessage();
            startService();     
        } catch (IOException e){
            System.out.println(">>> 접속 종료 <<<");        
        } finally {
            cleanup();     
        }
    }

    protected void connectToServer() throws IOException {
    }   

    private void setupStreams() throws IOException{     
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
        socketWriter = new PrintWriter(socket.getOutputStream(), true);                    
        keyboardReader = new BufferedReader(new InputStreamReader(System.in));            
    }

    private void startService() throws IOException {        
        Thread readThread = createReadThread();     // 읽기 쓰레드   
        Thread writeThread = createWriteThread();   // 쓰기 쓰레드
        Thread heartbeatThread = createHeartbeatThread();   // Heartbeat 쓰레드

        readThread.start();     
        writeThread.start();
        heartbeatThread.start();
        //  메인 스레드 대기 처리
        try{
            readThread.join();  
            writeThread.join();
            heartbeatThread.join();
        } catch (InterruptedException e){

        }

    }

    private Thread createWriteThread() {        
        return new Thread(() -> {        
            try {
                String msg;     
                while ((msg = keyboardReader.readLine()) != null) {     
                    socketWriter.println("[" + student.getName() + "] : " + msg);       
                }
            } catch (IOException e){
                e.printStackTrace();       
            }
        });
    }

    private Thread createReadThread() {     
        return new Thread(() -> {       
            try {
                String msg;    
                while ((msg = socketReader.readLine()) != null){       
                    System.out.println(msg);    
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    private Thread createHeartbeatThread() {
        return new Thread(() -> {

            try {
                while (true) {

                    socketWriter.println(
                        "HEARTBEAT|" + student.getStudentId()
                    );

                    Thread.sleep(5000);

                }
            } catch (Exception e) {

            }

        });
    }

    private void cleanup(){     
        if (socket != null){
            try {
                socket.close();     
            } catch (IOException e){
                e.printStackTrace();       
            }
        }
    }

    private void sendLoginMessage(){
        String studentId = student.getStudentId();
        String name = student.getName(); 

        String loginMessage = "Login|" + student.getStudentId()  + "|" + student.getName();
        socketWriter.println(loginMessage);     
    }

    private void sendLoginMesssage(){
        socketWriter.println(("Login|" + student.getStudentId() + "|" + student.getName()));
    }

    private void sendEvent(String event){
        socketWriter.println(
            "Event|"
            + student.getStudentId()
            + "|"
            + event
        );
    }

}
