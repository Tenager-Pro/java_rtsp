package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.Arrays;

public class RTPJpegConverter {
    private static String URL = "127.0.0.1";
    private static String RTSP_URL = "rtsp://localhost:8554/stream";
    private static int RTSP_PORT = 8554;



    private static String RTP_ADDRESS = "RTP_SERVER_ADDRESS";
    private static int RTP_PORT = 5004; // Порт RTP сервера
    private static final String OUTPUT_PATH = "D:\\image\\"; // Путь к директории вывода



    public void connectionRTSP(){

    }












    private byte[] unpackRTPData(byte[] rtpData, int dataLength) {
        // Пропускаем заголовок RTP (12 байт) и получаем данные JPEG
        byte[] jpegData = Arrays.copyOfRange(rtpData, 12, dataLength);

        // Возвращаем данные JPEG
        return jpegData;
    }

    // Метод для декодирования JPEG изображения
    private BufferedImage decodeJPEG(byte[] jpegData) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jpegData);
        return ImageIO.read(byteArrayInputStream);
    }

    // Метод для сохранения JPEG изображения в виде массива байт
    private byte[] saveJPEGToArray(BufferedImage image, int frameNumber) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "JPEG", byteArrayOutputStream);

        // Сохранение JPEG изображения на диск
        String outputFileName = String.format("%s/frame_%05d.jpg", OUTPUT_PATH, frameNumber);
        File outputFile = new File(outputFileName);
        ImageIO.write(image, "jpeg", outputFile);

        return byteArrayOutputStream.toByteArray();
    }


}

