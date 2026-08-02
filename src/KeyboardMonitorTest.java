// import com.github.kwhat.jnativehook.GlobalScreen;
// import com.github.kwhat.jnativehook.NativeHookException;
// import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
// import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

// public class KeyboardMonitorTest implements NativeKeyListener{              // 폐기!
//     @Override
//     public void nativeKeyPressed(NativeKeyEvent e) {    // 키를 누르는 순간 호출
//         System.out.println("Ket Pressed : " + NativeKeyEvent.getKeyText(e.getKeyCode()));   // 코드 문자열((누른 키 코드))
//     }

//     @Override
//     public void nativeKeyReleased(NativeKeyEvent e) {   

//     }

//     @Override
//     public void nativeKeyTyped(NativeKeyEvent e) {

//     }

//     public static void main(String[] args) {
//         try{
//             GlobalScreen.registerNativeHook();  // 운영체제의 전역 키보드 이벤트 받을 준비

//             GlobalScreen.addNativeKeyListener(new KeyboardMonitorTest());   // 키 입력 발생 -> 등록된 Listener에게 이벤트 전달 -> nativeKeyPressed() 실행

//             System.out.println("Keyboard Monitor Started");
//         } catch (com.github.kwhat.jnativehook.NativeHookException e) {
//             e.printStackTrace();
//         }
//     }
// }
