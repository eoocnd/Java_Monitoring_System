import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class MultiClientServer {
    public static final int Port = 5000;    // 정적 정수 변수인 Port에 5000 값을 저장
    // 하나의 변수에 자원을 통으로 관리하기 기법 --> 자료구조
    // 자료구조 --> 코드 단일, 멀티 --> 멀티 스레드 --> 자료구조 ??
    // 객체 배열 <--  Vector<> : 멀티 스레드에 안정적
    private static Vector<PrintWriter> clientWriters = new Vector();    // 외부접근 불가능한 정적 벡터? clientWriters에 새로운 벡터 객체를 생성

    public static void main(String[] args){
        System.out.println("Server started ... ");      // 시작 메시지 출력 
        try (ServerSocket serverSocket = new ServerSocket(Port)){   // 5000번 포트에 새로운 서버소켓을 생성
            while(true){        // 무한반복
                // 1. serverSocket.accept() 호출하면 블록킹 상태가 된다. 멈춤
                // 2. 클라이언트가 연결 요청하면  새로운 소켓 객체가 생성이 된다.
                // 3. 새로운 스레드를 만들어서 처리.. (클라이언특라 데이터를 주고 받기 위한 스레드)
                // 4. 새로운 클라이언트가 접속 하기 까지 다시 대기(반복)
                Socket socket = serverSocket.accept();      // 소켓이 서버소켓에 접속될 때 까지 시도?

                // 새로운 클라이언트가 연결되면 새로운 쓰레드가 생성된다.
                new ClientHandler(socket).start();      // 새로운 ClientHandler 소켓을 시작?
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   // end of main

    // 정적 내부 클래스 설계
    private static class ClientHandler extends Thread{      // 스레드?
        private Socket socket;      // 소켓
        private PrintWriter out;    // 작성하는 변수? out
        private BufferedReader in;  // 읽어들이는 변수? in
        public ClientHandler(Socket socket){    //   ClientHandler 생성자 소켓을 받아들여서 만듦
            this.socket = socket;               // 소켓은 입력받은 소켓으로
        }

        public void run(){      // 시작 함수
            try{
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));        // 소켓에서 인풋스트림을 읽어와서 in에 저장
                out = new PrintWriter(socket.getOutputStream(), true);                          // true이면? 소켓에서 아웃풋스트림을 작성해서 저장 아마 내보내는거 같은데

                // 중요!! - 서버가 관리하는 자료구조에 자원 저장(클라이언트와 연결된 소켓 -> outStream)
                clientWriters.add(out);     // 위의  정보를 보냄?
                String msg;
                while ((msg = in.readLine()) != null){          // in을 통해 한줄씩 읽어들인 값을 msg에 저장하고 null값이 아니면 반복
                    System.out.println("Recevied : " + msg);        // msg를 출력
                    // 받은 데이터를 서버측과 연결된 데이터를 전달하자
                    broadcastMessage(msg);      //  msg를 전파?
                }
            } catch (Exception e){
                // e.printStackTrace();
            } finally {
                try{
                    socket.close();     // 종료
                    System.out.println("연결 해제 ... ");
                } catch (Exception e2) {
                    // e2.printStackTrace();
                }
            }
        }
    }   // end of ClientHandler

    // 모든 클라이언트에게 메시지 보내기 - 브로드 캐스트
    private static void broadcastMessage(String message){   // message를 입력 받아서
        for (PrintWriter writer : clientWriters){       // 반복문인데 잘 기억이 안나고 clientWriters를 
            writer.println(message);        // message를 출력?
        }
    }
}
