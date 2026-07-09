import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AbstractClient {
    private String name;        // 문자열 name 
    private Socket socket;      // 소켓 소켓
    private PrintWriter socketWriter;   // 작성하는거? socketWriter
    private BufferedReader socketReader;    // 버퍼리더? socketReader
    private BufferedReader keyboardReader;  // 버퍼리더? keyboardReader

    public AbstractClient(String name){     // 생성자 name을 받아와서 생성
        this.name = name;
    }

    // 외부에서 나의 멤버 변수에 참조변수를 주입 할 수 있도록 setter 메서드 설계
    protected void setSocket(Socket socket){        // 소켓을 설정 하는건데 이게 setter 메서드구나.. 
        this.socket = socket;
    }

    public final void run(){        // 런 메서드
        try{
            connectToServer();      // 서버에 연결
            setupStreams();         // 셋업 스트림?
            startService();     // join() 걸어 둔 상태  서버 시작
        } catch (IOException e){
            System.out.println(">>> 접속 종료 <<<");        // 실패하면 접속종료를 띄움
        } finally {
            cleanup();      // 커널창을 clean 하게 만들겠지?
        }
    }

    protected void connectToServer() throws IOException {
    }   // 서버 접속 실패하면 예외처리는 버리라는건가?

    private void setupStreams() throws IOException{     // 모르겠음
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // socketReader 변수에 소켓에서 inputStream값을 저장
        socketWriter = new PrintWriter(socket.getOutputStream(), true);                     // socketWriter 변수에 true면 아웃풋스트림?을 저장
        keyboardReader = new BufferedReader(new InputStreamReader(System.in));             // keyboardReader 변수에 시스템 입력을 저장
    }

    private void startService() throws IOException {        // 서비스가 시작할때 예외처리는 버려라
        Thread readThread = createReadThread();     // 쓰레드? readThread 읽어라 읽는 스레드를 만듦?
        Thread writeThread = createWriteThread();       // writeThread 쓰는 스레드를 만듦?

        // 스레드 시작
        readThread.start();     // 위에서 만든 읽는 쓰레드 시작
        writeThread.start();    // 쓰는 쓰레드 시작
        //  메인 스레드 대기 처리
        try{
            readThread.join();  // 읽는 쓰레드 합쳐라?
            writeThread.join(); // 쓰는 쓰레드 합쳐라?  
        } catch (InterruptedException e){

        }

    }

    private Thread createWriteThread() {        // 쓰는 쓰레드 만들기
        return new Thread(() -> {           // 새로운 쓰레드를 반환
            try {
                String msg;     // msg 문자열
                while ((msg = keyboardReader.readLine()) != null) {     // 키보드에서 한줄 씩 읽은게 null이 아니면 반복
                    socketWriter.println("[" + name + "] : " + msg);        // socketWriter에게 [name] : 읽어들인 값 형식으로 출력?
                }
            } catch (IOException e){
                e.printStackTrace();        // 몰라
            }
        });
    }

    private Thread createReadThread() {     // 읽는 쓰레드 만들기
        return new Thread(() -> {       // 새로운 쓰레드 반환
            try {
                String msg;     // msg 문자열
                while ((msg = socketReader.readLine()) != null){        // socketReader가 읽은 값이 null이 아니면 반복
                    System.out.println("방송 옴 : " + msg);     // 방송옴 : 읽은 값 형태로 출력
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    private void cleanup(){     // 클린업 함수
        if (socket != null){
            try {
                socket.close();     // 소켓 닫어 
            } catch (IOException e){
                e.printStackTrace();        // 몰라
            }
        }
    }
}
