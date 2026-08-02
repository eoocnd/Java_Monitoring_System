import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyboardMonitor implements NativeKeyListener{
    private AbstractClient client;
    private boolean ctrlPressed = false;

    private boolean copyPressed = false;
    private boolean pastePressed = false;

    public KeyboardMonitor(AbstractClient client){
        this.client = client;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e){
        if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL){
            ctrlPressed = true;
        }

        if (ctrlPressed && e.getKeyCode() == NativeKeyEvent.VC_C && !copyPressed){
            copyPressed = true;

            System.out.println("Ctrl + C");
            client.sendEvent(EventType.COPY);
        }

        if (ctrlPressed && e.getKeyCode() == NativeKeyEvent.VC_V && !pastePressed){
            pastePressed = true;
            
            System.out.println("Ctrl + V");
            client.sendEvent(EventType.PASTE);
        }
    }
        @Override
        public void nativeKeyReleased(NativeKeyEvent e){
            if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL){
                ctrlPressed = false;

                copyPressed = false;
                pastePressed = false;
            }
        }

        @Override
        public void nativeKeyTyped(NativeKeyEvent e){

        }

        public void start(){
            // 로그 지우기
            java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(java.util.logging.Level.OFF);
            
            try{
                if (!GlobalScreen.isNativeHookRegistered()){
                    GlobalScreen.registerNativeHook();
                }
                GlobalScreen.addNativeKeyListener(this);
            } catch (NativeHookException e){
                e.printStackTrace();
            }
        }
}