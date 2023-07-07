import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.io.File;

public class RTSPJpegConverter {
    private static final String RTSP_URL = "rtsp://localhost:8554/stream";
    private static final String OUTPUT_PATH = "D:\\image\\"; // Путь к директории вывода

    public static void main(String[] args) throws FFmpegFrameGrabber.Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(RTSP_URL);
        try {
            grabber.start();

            int frameNumber = 0;
            Frame frame;
            while ((frame = grabber.grabImage()) != null) {
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage image = converter.getBufferedImage(frame);

                // Создание JPEG изображения в виде массива байт
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpeg", baos);
                byte[] jpegBytes = baos.toByteArray();

                // Сохранение JPEG изображения на диск
                String outputFileName = String.format("%s/frame_%05d.jpg", OUTPUT_PATH, frameNumber);
                File outputFile = new File(outputFileName);
                ImageIO.write(image, "jpeg", outputFile);

                frameNumber++;

                baos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            grabber.stop();
        }
    }
}