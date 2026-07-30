public class AutoCaptureManager implements Runnable {
    private AbstractClient client;      // 생성자에서 받은 걸 저장

    public AutoCaptureManager(AbstractClient client){   // 받은 client 저장
        this.client = client;       // 이미 연결되어 있는 AbstractClient 사용해야 해서 this를 넘김
    }

    @Override
    public void run(){
        while(true){
            try {
                client.saveCapture(EventType.SCREENSHOT);
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
