import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClipboardMonitor {
    private String previousText = null;   // 이전 클립보드 내용 저장 
    private ScheduledExecutorService scheduler; // 일정 시간이 되면 어떤 작업을 해주는 관리자
    private static String currentClipboard = "";

    public void checkClipboard(){       // 현재 저장된 클립보드 값을 가져옴
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); // 지역변수 : 이 메서드에서만 사용하기 때문에 메모리 차원에서 이득
    Transferable contents = clipboard.getContents(null);

    if (contents != null &&
        contents.isDataFlavorSupported(DataFlavor.stringFlavor)){
        
        String text;
        try {
            text = (String) contents.getTransferData(DataFlavor.stringFlavor);

            if (!text.equals(previousText)) {       // 이전 클립보드랑 같으면 변경 감지, 갱신, 출력
                System.out.println("[클립보드 변경]");
                System.out.println(text);

                previousText = text;
                currentClipboard = text;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        }
    }

    public void start(){
        scheduler = Executors.newSingleThreadScheduledExecutor();   // 감시 담당 스레드 하나 만듬

        scheduler.scheduleAtFixedRate(  // 일정 간격으로 실행
            () -> checkClipboard(),     // 람다식 checkClipboard();를 실행하라는 뜻 
             0,           // 프로그램 시작하자마자 바로 실행
             500,               // 500ms마다 반복
             TimeUnit.MILLISECONDS      // 500의 단위가 밀리초라는 뜻 
            );
    }

    public static String getCurrentClipboard(){
        return currentClipboard;
    }
}
