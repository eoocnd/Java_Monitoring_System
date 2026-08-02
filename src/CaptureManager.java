import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;

public class CaptureManager {
    public File capture(File file){
        try {
            Robot robot = new Robot();

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Toolkit -> 운영체제 정보 얻기 -> 모니터 크기 반환
            Rectangle rectangle = new Rectangle(screenSize);    // (0,0) 화면 끝까지
            
            BufferedImage original = robot.createScreenCapture(rectangle); // Robot -> Rectangle 영역 촬영 -> 사진 생성

            BufferedImage resized = resizeImage(original, 1280, 720);

            ImageIO.write(resized, "jpg", file);

            System.out.println("캡처 완료");

            return file;
            
        } catch (Exception e) {
            System.out.println("화면 캡처 실패");
            e.printStackTrace();

            return null;
        }
    }

    private BufferedImage resizeImage(BufferedImage original, int width, int height){
        Image tmp = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = resized.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2.drawImage(tmp,0,0,null);

        g2.dispose();

        return resized;
    }
}
