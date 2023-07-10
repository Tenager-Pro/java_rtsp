package org.example;

import java.io.IOException;
import java.net.*;

public class RTPReceiver {
    private DatagramSocket socket;
    private static final int BUFFER_SIZE = 65536; // Размер буфера для приема RTP пакетов
    private static String RTP_ADDRESS = "";
    private static int RTP_CLIENT_PORT = 5004;
    private static int RTP_SERVER_PORT = 0;

    public RTPReceiver() {
    }

    public RTPReceiver(int RTP_CLIENT_PORT) {
        this.RTP_CLIENT_PORT = RTP_CLIENT_PORT;
    }

    public void setRTP_CLIENT_PORT(int RTP_CLIENT_PORT) {
        this.RTP_CLIENT_PORT = RTP_CLIENT_PORT;
    }

    public int getRTP_CLIENT_PORT() {
        return RTP_CLIENT_PORT;
    }

    public void setRTP_SERVER_PORT(int RTP_SERVER_PORT) {
        this.RTP_SERVER_PORT = RTP_SERVER_PORT;
    }

    public int getRTP_SERVER_PORT() {
        return RTP_SERVER_PORT;
    }
//    Variant 2
//    public void start() {
//        try {
//            // Создаем сокет для приема RTP-пакетов
//            socket = new DatagramSocket();
//
//            // Отправляем запрос на сервер RTP
//            sendRtpRequest();
//
//            // Получаем RTP-пакеты от сервера
//            receiveRtpPackets();
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void stop() {
//        if (socket != null) {
//            socket.close();
//        }
//    }
//
//    private void sendRtpRequest() {
//        try {
//            // Создаем адрес сервера и порт
//            InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
//            int serverPort = 8554;
//
//            // Создаем RTSP-запрос
//            String rtspRequest = "DESCRIBE rtsp://127.0.0.1:8554/stream RTSP/1.0\r\n" +
//                    "CSeq: 2\r\n\r\n";
//
//            // Создаем буфер для отправки данных
//            byte[] buffer = rtspRequest.getBytes();
//            // Создаем пакет с данными и отправляем его на сервер
//            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
//            socket.send(packet);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void receiveRtpPackets() {
//        try {
//            // Создаем буфер для приема данных
//            byte[] buffer = new byte[1024];
//            // Создаем пакет для приема данных
//            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//
//            // Получаем RTP-пакеты от сервера
//            while (true) {
//                socket.receive(packet);
//                System.out.println("Получил");
//                // Обрабатываем полученные данные
//                processRtpPacket(packet);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void processRtpPacket(DatagramPacket packet) {
//        // Обработка RTP-пакета
//        byte[] data = packet.getData();
//        int length = packet.getLength();
//
//        // Ваш код для обработки RTP-пакета
//    }

    public void reception() {
        try {
            DatagramSocket socket = new DatagramSocket(RTP_CLIENT_PORT);
            System.out.println(RTP_CLIENT_PORT);
            socket.setSoTimeout(5000); // Установка таймаута в 5 секунд
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                // Принимаем пакеты RTP через оба сокета
                try {
                    socket.receive(packet);
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