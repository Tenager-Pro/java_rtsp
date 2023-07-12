package org.example;


public class Main {
    public static void main(String[] args) {

        RTSPConnection rtspConnection = new RTSPConnection("127.0.0.1", "rtsp://localhost:8554/stream", 8554);
        rtspConnection.connect();
    }
}