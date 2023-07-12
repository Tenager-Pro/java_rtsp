package org.example;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class RTPReceiver {
    private DatagramSocket socket;
    private static final int BUFFER_SIZE = 65536; // Размер буфера для приема RTP пакетов
    private static int RTP_CLIENT_PORT = 5004;

    private static String PATH_DECODE_FILE;

    private static ArrayList<String> CODEC_LIST = new ArrayList<>(); //Содержит информацию о кодировании потоков


    public RTPReceiver(int RTP_CLIENT_PORT, ArrayList<String> CODEC_LIST, String PATH_DECODE_FILE) {
        this.RTP_CLIENT_PORT = RTP_CLIENT_PORT;
        this.CODEC_LIST = CODEC_LIST;
        this.PATH_DECODE_FILE = PATH_DECODE_FILE;
    }

    public void setRTP_CLIENT_PORT(int RTP_CLIENT_PORT) {
        this.RTP_CLIENT_PORT = RTP_CLIENT_PORT;
    }

    public int getRTP_CLIENT_PORT() {
        return RTP_CLIENT_PORT;
    }

    public void reception() {
        try {
            DatagramSocket socket = new DatagramSocket(RTP_CLIENT_PORT);
            socket.setSoTimeout(5000); // Установка таймаута в 5 секунд
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            RTPJpegConverter rtpJpegConverter = new RTPJpegConverter(CODEC_LIST, PATH_DECODE_FILE);
            int frame = 0;
            while (true) {
                try {
                    frame++;
                    // Принимаем пакеты RTP
                    socket.receive(packet);
                    System.out.println(packet);

                    rtpJpegConverter.convertByteToImage(packet, frame);
                    // Очищаем буфер пакета для следующего приема
                    packet.setLength(buffer.length);
                    } catch (SocketTimeoutException e) {
                        // Обработка исключения при истечении таймаута
                        System.out.println("Таймаут истек");
                    }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}