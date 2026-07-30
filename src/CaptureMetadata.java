public class CaptureMetadata {
    private String studentId;
    private String studentName;
    private String captureTime;
    private String eventType;
    private String windowTitle;
    private String clipboard;
    private String imageFileName;

    public CaptureMetadata(String studentId, String studentName, String captureTime, 
                        String eventType, String windowTitle, String clipboard, String imageFileName){
                            this.studentId = studentId;
                            this.studentName = studentName;
                            this.captureTime = captureTime;
                            this.eventType = eventType;
                            this.windowTitle = windowTitle;
                            this.clipboard = clipboard;
                            this.imageFileName = imageFileName;
                        }

    public String getStudentId(){
        return studentId;
    }

    public String getStudentName(){
        return studentName;
    }

    public String getCaptureTime(){
        return captureTime;
    }

    public String getEventType(){
        return eventType;
    }

    public String getWindowTitle(){
        return windowTitle;
    }

    public String getClipboard(){
        return clipboard;
    }

    public String getImageFileName(){
        return imageFileName;
    }
}
