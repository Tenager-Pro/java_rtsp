package org.example;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        RTSPConnection rtspConnection = new RTSPConnection("172.16.99.7", "rtsp://172.16.99.6/ISAPI/Streaming/Channels/103", 554);
        rtspConnection.connect();
        System.out.println(rtspConnection.getRTP_CLIENT_PORT());
        RTPReceiver rtpReceiver = new RTPReceiver(rtspConnection.getRTP_CLIENT_PORT());
        rtpReceiver.setRTP_SERVER_PORT(rtspConnection.getRTP_CLIENT_PORT());
        rtpReceiver.reception();
//        RTPReceiver client = new RTPReceiver();
//        client.start();
    }
}