import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class UploadManager {
    public void upload(File imageFile){
            try {
                Socket socket = new Socket("localhost", 9999);
                System.out.println("서버 연결");

                OutputStream outputStream = socket.getOutputStream();   // OutputStream 얻기
                System.out.println("이미지 전송 시작");
                
                FileInputStream fileInputStream = new FileInputStream(imageFile);    // capture.png -> 읽기 시작

                byte[] buffer = new byte[4096];
                int length;
                while((length = fileInputStream.read(buffer)) != -1){
                    outputStream.write(buffer, 0, length);              // 파일 -> Socket이다.
                }

                outputStream.flush();   // 남아있는 데이터를 전부 보내라
                System.out.println("이미지 전송 완료");

                fileInputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }

    }
}
