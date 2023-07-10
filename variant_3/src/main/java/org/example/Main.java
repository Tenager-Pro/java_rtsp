package org.example;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        RTSPConnection rtspConnection = new RTSPConnection("127.0.0.1", "rtsp://localhost:8554/stream", 8554);
        rtspConnection.connect();
        System.out.println(rtspConnection.getRTP_CLIENT_PORT());
        RTPReceiver rtpReceiver = new RTPReceiver(rtspConnection.getRTP_CLIENT_PORT());
        rtpReceiver.setRTP_SERVER_PORT(rtspConnection.getRTP_CLIENT_PORT());
        rtpReceiver.reception();
//        RTPReceiver client = new RTPReceiver();
//        client.start();
    }
}