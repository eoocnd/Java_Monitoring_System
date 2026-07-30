import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

public class AbstractClient {
    private Socket socket;      
    private Student student;
    private PrintWriter socketWriter;   
    private BufferedReader socketReader;    
    private BufferedReader keyboardReader;  
    private StudentSessionManager sessionManager;


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
            sessionManager = new StudentSessionManager(student);
            sessionManager.createSession();
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
        
        ClipboardMonitor clipboardMonitor = new ClipboardMonitor();
        
        AutoCaptureManager autoCaptureManager = new AutoCaptureManager(this);
        Thread autoCaptureThread = new Thread(autoCaptureManager);
        
        clipboardMonitor.start();

        try{
            ActiveWindowMonitor activeWindowMonitor = new ActiveWindowMonitor(student);
            activeWindowMonitor.start();
        } catch (Throwable e){
            System.out.println("ActiveWindowMonitor 시작 실패");
        }
        
        autoCaptureThread.start();      // implements Runnable로 일만 정의하기 때문에 실제로 일하는 건 new Thread이다. 
        
        
        readThread.start();     
        writeThread.start();
        heartbeatThread.start();
        //  메인 스레드 대기 처리
        try{
            readThread.join();  
            writeThread.join();
            heartbeatThread.join();
            autoCaptureThread.join();
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

    private Thread createHeartbeatThread() {        // 하트비트 스레드 생성
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

    private void sendLoginMessage(){            // 로그인 성공 메세지
        String studentId = student.getStudentId();
        String name = student.getName(); 

        String loginMessage = "Login|" + student.getStudentId()  + "|" + student.getName();
        socketWriter.println(loginMessage);     
    }

    public void sendEvent(EventType eventType){     // 이벤트 
        String message = 
                "Event|" +
                student.getStudentId() +
                "|" +
                eventType;

        socketWriter.println(message);

        saveCapture(eventType); 

        }

    public void saveCapture(EventType eventType){
            // 이미지
            CaptureManager captureManager = new CaptureManager();
            File image = captureManager.capture(sessionManager.createCaptureFile(eventType));
            
            if (image == null){
                return;
            }

            // json
            String jsonName = image.getName();
            jsonName = jsonName.replace(".png",".json");
            File jsonFile = new File(image.getParent(), jsonName);
            
            CaptureMetadata metadata = new CaptureMetadata(student.getStudentId(), student.getName(), LocalDateTime.now().toString(), 
            eventType.name(), ActiveWindowMonitor.getCurrentWindow(), ClipboardMonitor.getCurrentClipboard(), image.getName());
            
            JsonManager jsonManager = new JsonManager();
            jsonManager.save(metadata, jsonFile);

            // // 업로드
            // if (image != null){
            //     UploadManager uploadManager = new UploadManager();
            //     uploadManager.upload(student, image);
            // }
        }
}
