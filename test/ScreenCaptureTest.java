package test;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ScreenCaptureTest {
    public static void main(String[] args) {
        try {
            Robot robot = new Robot();

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Toolkit -> 운영체제 정보 얻기 -> 모니터 크기 반환
            Rectangle rectangle = new Rectangle(screenSize);    // (0,0) 화면 끝까지
            
            BufferedImage image = robot.createScreenCapture(rectangle); // Robot -> Rectangle 영역 촬영 -> 사진 생성

            File file = new File("captures/capture.png");
            ImageIO.write(image, "png", file);

            System.out.println("캡처 완료");
            
        } catch (Exception e) {
            System.out.println("화면 캡처 실패");
            e.printStackTrace();

        }
    }
}
