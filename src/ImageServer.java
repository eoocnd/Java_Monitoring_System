import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ImageServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);     // 서버 생성
            System.out.println("서버 시작");

            Socket socket = serverSocket.accept();                       // 클라이언트 접속
            System.out.println("클라이언트 접속");

            InputStream inputStream = socket.getInputStream();  // 소켓에서 데이터를 읽는다.
            FileOutputStream fileOutputStream = new FileOutputStream("captures/receive.png");    // 파일 생성
            byte[] buffer = new byte[4096];         // 4096Byte씩 쪼개 읽음
            int length;     // 읽은 크기 저장
            while((length = inputStream.read(buffer)) != -1){   // 더 이상 읽을 게 없을 때까지 반복
                fileOutputStream.write(buffer, 0, length);  // Socket -> buffer -> receive.png 계속 반복
            }
            System.out.println("이미지 수신 완료");

            fileOutputStream.close();
            inputStream.close();
            socket.close();
            serverSocket.close();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
