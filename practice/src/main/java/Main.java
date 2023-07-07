

public class Main {
    public static void main(String[] args) {
        RTSPViewer myRTSP = new RTSPViewer("rtsp://localhost:8554/stream", 800, 600);
        myRTSP.openWindow();
    }
}
