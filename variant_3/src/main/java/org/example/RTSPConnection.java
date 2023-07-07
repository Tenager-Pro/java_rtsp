package org.example;

import java.io.*;
import java.net.Socket;

public class RTSPConnection {
    private static String URL = "127.0.0.1";
    private static String RTSP_URL = "rtsp://localhost:5000";
    private static int RTSP_PORT = 8554;
    private static String RTP_ADDRESS ="";
    private static int RTP_CLIENT_PORT = 5004;
    private static int RTP_SERVER_PORT = 0;

    public RTSPConnection(){
    }
    public RTSPConnection(String URL, String RTSP_URL, int RTSP_PORT){
        this.URL = URL;
        this.RTSP_URL = RTSP_URL;
        this.RTSP_PORT = RTSP_PORT;
    }
    public void setRTSP_URL(String RTSP_URL) {
        this.RTSP_URL = RTSP_URL;
    }

    public String getRTSP_URL() {
        return RTSP_URL;
    }
    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }
    public void setRTSP_PORT(int RTSP_PORT) {
        this.RTSP_PORT = RTSP_PORT;
    }

    public int getRTSP_PORT() {
        return RTSP_PORT;
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
    public void connect(){
        try {
            Socket rtspSocket = new Socket(URL, RTSP_PORT);
            OutputStream outputStream = rtspSocket.getOutputStream();
            InputStream inputStream = rtspSocket.getInputStream();

            String request = "SETUP " + RTSP_URL + "/trackID=0 RTSP/1.0\r\nCSeq: 1\r\nTransport: RTP/AVP;unicast;client_port=" + RTP_CLIENT_PORT + "-" + (RTP_CLIENT_PORT + 1) + "\r\n\r\n";
            outputStream.write(request.getBytes());

            // Читаем ответ от RTSP сервера
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String response;
            while ((response = reader.readLine()) != null) {
                // Обработка ответа от RTSP сервера
                System.out.println(response);
                // Извлекаем информаци
                if (response.startsWith("Transport")) {
                    String[] transportParts = response.split(";");
                    for (String part : transportParts) {
                        if (part.trim().startsWith("destination")) {
                            String[] addressParts = part.trim().split("=");
                            RTP_ADDRESS = addressParts[1].trim();
                        }
                        else if (part.trim().startsWith("client_port")) {
                            String[] portParts = part.trim().split("=");
                            String[] portRange = portParts[1].trim().split("-");
                            RTP_CLIENT_PORT = Integer.parseInt(portRange[0].trim());
                        }
                        else if (part.trim().startsWith("server_port")) {
                            String[] portParts = part.trim().split("=");
                            String[] portRange = portParts[1].trim().split("-");
                            RTP_SERVER_PORT = Integer.parseInt(portRange[0].trim());
                        }
                    }
                    break;
                }
            }

//            request = "DESCRIBE " + RTSP_URL + " RTSP/1.0\r\nCSeq: 2\r\nAccept: application/sdp\r\n\r\n";
//            outputStream.write(request.getBytes());
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            while ((response = reader.readLine()) != null) {
//                // Обработка ответа от RTSP сервера
//                System.out.println(response);
//
//            }

            request = "PLAY " + RTSP_URL + "/trackID=0 RTSP/1.0\r\nCSeq: 3\r\nSession: 1\r\nRange: npt=0.000-\r\n\r\n";
            outputStream.write(request.getBytes());
            reader = new BufferedReader(new InputStreamReader(inputStream));

            while ((response = reader.readLine()) != null) {
                // Обработка ответа от RTSP сервера
                System.out.println(response);
                if (response.startsWith("Cseq")) {
                    break;
                }
            }

            // Закрываем сокеты
            rtspSocket.close();
            outputStream.close();
            inputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
