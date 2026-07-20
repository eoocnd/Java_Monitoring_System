import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class MultiClientServer {
    private static StudentManager studentManager = new StudentManager();
    public static final int Port = 5000;    // 정적 정수 변수인 Port에 5000 값을 저장
    // 하나의 변수에 자원을 통으로 관리하기 기법 --> 자료구조
    // 자료구조 --> 코드 단일, 멀티 --> 멀티 스레드 --> 자료구조 ??
    // 객체 배열 <--  Vector<> : 멀티 스레드에 안정적
    private static Vector<PrintWriter> clientWriters = new Vector();    

    public static void main(String[] args){
        System.out.println("Server started ... ");     
        try (ServerSocket serverSocket = new ServerSocket(Port)){   
            while(true){        
                // 1. serverSocket.accept() 호출하면 블록킹 상태가 된다. 멈춤
                // 2. 클라이언트가 연결 요청하면  새로운 소켓 객체가 생성이 된다.
                // 3. 새로운 스레드를 만들어서 처리.. (클라이언특라 데이터를 주고 받기 위한 스레드)
                // 4. 새로운 클라이언트가 접속 하기 까지 다시 대기(반복)
                Socket socket = serverSocket.accept();      

                // 새로운 클라이언트가 연결되면 새로운 쓰레드가 생성된다.
                new ClientHandler(socket).start();      
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   // end of main

    private static class ClientHandler extends Thread{      
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private Student student;
        public ClientHandler(Socket socket){    
            this.socket = socket;
        }

        public void run(){      
            try{
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));        
                out = new PrintWriter(socket.getOutputStream(), true);                          

                // 중요!! - 서버가 관리하는 자료구조에 자원 저장(클라이언트와 연결된 소켓 -> outStream)
                clientWriters.add(out);     
                String msg;
                while ((msg = in.readLine()) != null){          
                    if (msg.startsWith("Login|")){
                        handleLogin(msg);
                        continue;
                    }

                    if (msg.startsWith("Heatbeat|")){
                        handleHeartbeat(msg);
                        continue;
                    }
                    System.out.println(msg);        
                    // 받은 데이터를 서버측과 연결된 데이터를 전달하자
                    broadcastMessage(msg);     
                }
            } catch (Exception e){
                // e.printStackTrace();
            } finally {
                try{
                    socket.close();     // 종료
                    if (student != null){
                        studentManager.removeStudent(student);
                        System.out.println(student.getName() + "퇴장");
                    }
                    System.out.println("연결 해제 ... ");

                    studentManager.printStudents();

                } catch (Exception e2) {
                    // e2.printStackTrace();
                }
            }
        }

        // 접속 관리
        private void handleLogin(String msg){
            String[] data = msg.split("\\|");
            String studentId = data[1];
            String name = data[2];
            student = new Student(studentId, name);
            studentManager.addStudent(student);
    
            studentManager.printStudents();
        }

        // 하트비트 관리
        private void handleHeartbeat(String msg){
            System.out.println(msg);

        }

        // 이벤트 관리
        private void handleEvent(String msg){
            String[] data = msg.split("\\|");
            String studentId = data[1];
            String eventType = data[2];

            System.out.println(studentId + " : " + eventType);
        }
    
    }   // end of ClientHandler

    // 모든 클라이언트에게 메시지 보내기 - 브로드 캐스트
    private static void broadcastMessage(String message){   
        for (PrintWriter writer : clientWriters){      
            writer.println(message);        
        }
    }


}
