import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;  // HWND = Window Handle 창 번호
import com.sun.jna.win32.StdCallLibrary;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ActiveWindowMonitor {
    private Student student;
    private AbstractClient client;
    private String previousTitle = "";
    private ScheduledExecutorService scheduler;
    private static String currentWindow = "";

    public ActiveWindowMonitor(Student student, AbstractClient client){
        this.student = student;
        this.client = client;
    }

    public interface User32 extends StdCallLibrary{
        User32 INSTANCE =
                Native.load("user32", User32.class);    // Windows -> user32.dll -> 불러오기

        HWND GetForegroundWindow(); // 현재 활성 창 번호 반환 

        int GetWindowTextA(HWND hwnd, byte[] lpString, int nMaxCount);  // 창 제목 읽기
    }

    public void checkAtciveWindow(){
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();  // 현재 사용자가 보고 있는 창 -> 번호 얻기
        byte[] windowText = new byte[512];  // 버퍼 생성 ? Windows가 문자열을 넣어주기 떄문에

        User32.INSTANCE.GetWindowTextA(hwnd, windowText, 512);

        String title = Native.toString(windowText);
        if (!title.equals(previousTitle)){
            System.out.println("[활성 창 변경]");
            System.out.println(title);

            // 창이 바뀌면 ALT + TAB 이벤트 젖방
            client.sendEvent(EventType.ALT_TAB);
            
            // Ai 사이트 감지
            if (title.contains("ChatGPT") 
                || title.contains("Gemini") 
                || title.contains("Claude")){
                System.out.println("AI 사이트 감지");}

            client.sendEvent(EventType.PROCESS);    // PROCESS 이벤트 저장
        }
        previousTitle = title;
        currentWindow = title;
    }

    public void start(){
        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(
            () -> checkAtciveWindow(), 
            0, 
            500, 
            TimeUnit.MILLISECONDS);   
    }

    public static String getCurrentWindow(){
        return currentWindow;
    }
}
