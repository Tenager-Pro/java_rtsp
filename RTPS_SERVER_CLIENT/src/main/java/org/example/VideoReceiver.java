package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class VideoReceiver {
    public static void main(String[] args) throws SocketException, IOException {
        // Создание RTSP-клиента
        DatagramSocket rtspClientSocket = new DatagramSocket();

        // Создание RTP-сервера
        DatagramSocket rtpServerSocket = new DatagramSocket(8557);

        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        // Отправка RTSP-запроса на сервер
        String rtspRequest = "PLAY";
        InetAddress serverAddress = InetAddress.getByName("localhost");
        int serverPort = 8556;
        packet = new DatagramPacket(rtspRequest.getBytes(), rtspRequest.length(), serverAddress, serverPort);
        rtspClientSocket.send(packet);

        // Получение RTP-пакетов от сервера
        while (true) {
            rtpServerSocket.receive(packet);
            byte[] videoData = packet.getData();
            int videoLength = packet.getLength();

            // Обработка видео данных
            processVideoData(videoData, videoLength);
        }
    }

    private static void processVideoData(byte[] videoData, int videoLength) {
        // Реализуйте этот метод для обработки видео данных
    }
}