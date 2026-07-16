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
        Robot robot = new Robot();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Toolkit -> 운영체제 정보 얻기 -> 모니터 크기 반환
        Rectangle rectangle = new Rectangle(screenSize);    // (0,0) 화면 끝까지
        BufferedImage image = robot.createScreenCapture(rectangle); // Robot -> Rectangle 영역 촬영 -> 사진 생성
        ImageIO.write(image, "png", new File("capture.png"));   // 사진 -> PNG -> capture.png
        System.out.println("캡처 완료");
    }
}
