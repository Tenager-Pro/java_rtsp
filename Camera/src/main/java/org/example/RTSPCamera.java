package org.example;

import java.io.*;
import java.net.*;
import java.util.*;

public class RTSPCamera {
    private static final int RTP_PORT = 5000; // порт RTP
    private static final int BUFFER_SIZE = 1024; // размер буфера

    public static void main(String[] args) {
        try {
            // Создаем сокет для отправки RTP-пакетов
            DatagramSocket socket = new DatagramSocket();

            // Создаем адрес клиента и порт
            InetAddress clientAddress = InetAddress.getByName("127.0.0.1");
            int clientPort = RTP_PORT;

            // Открываем видео файл для чтения
            FileInputStream videoFile = new FileInputStream("D:\\videoplayback.mp4");

            // Создаем буфер для чтения видео данных
            byte[] buffer = new byte[BUFFER_SIZE];

            // Читаем видео данные из файла и отправляем их клиенту
            int bytesRead;
            while ((bytesRead = videoFile.read(buffer)) != -1) {

                // Создаем RTP-пакет с видео данными
                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, clientAddress, clientPort);

                // Отправляем RTP-пакет клиенту
                socket.send(packet);

                // Задержка для симуляции реального времени
                Thread.sleep(40);
            }

            // Закрываем видео файл и сокет
            videoFile.close();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
