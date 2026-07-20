import java.io.BufferedReader;
import java.io.File;
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
                    if (msg.equals("/copy")){
                        sendEvent(EventType.COPY);
                        continue;
                    }   

                    socketWriter.println("[" + student.getName() + "] : " + msg );
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
                        "Heartbeat|" + student.getStudentId()
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

    public void sendEvent(EventType eventType){
        String message = 
                "Event|" +
                student.getStudentId() +
                "|" +
                eventType;

        socketWriter.println(message);

        // COPY 이벤트면 즉시 캡처 후 업로드
        if (eventType == EventType.COPY){
            CaptureManager captureManager = new CaptureManager();
            File image = captureManager.capture();
            if (image != null){
                UploadManager uploadManager = new UploadManager();
                uploadManager.upload(student, image);
            }
        }
    }

    public enum EventType{
        COPY,
        AI_SITE,
        SCREEN_CAPTURE
    }

}
