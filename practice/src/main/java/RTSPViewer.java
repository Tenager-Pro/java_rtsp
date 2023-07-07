
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;

public class RTSPViewer {
    private static String RTSP_URL; // IP-address cameras
    private int width; // width window
    private int height; // height window


    private boolean visible = true; // visible window
    private String title_window = "RTSP Viewer"; // name new window


    public RTSPViewer(String RTSP_URL, int width, int height) {
        this.RTSP_URL = RTSP_URL;
        this.width = width;
        this.height = height;
    }

    public void setRTSP_URL(String RTSP_URL) {
        this.RTSP_URL = RTSP_URL;
    }

    public String getRTSP_URL() {
        return RTSP_URL;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setTitleWindow(String title_window) {
        this.title_window = title_window;
    }

    public String getTitleWindow() {
        return title_window;
    }
    public boolean checkNativeDiscovery() {
        // The method performs a check for the presence of libraries
        NativeDiscovery nativeDiscovery = new NativeDiscovery();
        boolean found = nativeDiscovery.discover();

        if (!found) {
            System.out.println("VLC not found");
            return false;
        }
        return true;
    }

    public void openWindow(){
        // The method opens a window and connects via rtsp to get an image
        boolean check_native_discovery = checkNativeDiscovery();
        if (!check_native_discovery) {
            System.out.println("ERROR! VLC native discovery not found");
            return;
        }
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        frame.setContentPane(mediaPlayerComponent);

        frame.setSize(getWidth(), getHeight());
        frame.setVisible(getVisible());

        EmbeddedMediaPlayer mediaPlayer = mediaPlayerComponent.getMediaPlayer();
        mediaPlayer.playMedia(getRTSP_URL());
    }
}