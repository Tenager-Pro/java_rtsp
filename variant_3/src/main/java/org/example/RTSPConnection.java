package org.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class RTSPConnection {

    private static Socket rtspSocket;
    private static OutputStream outputStream;
    private static InputStream inputStream;
    private static String URL = "127.0.0.1";
    private static String RTSP_URL = "rtsp://localhost:8554";
    private static int RTSP_PORT = 8554;
    private static String RTP_ADDRESS ="";

    private static String PATH_DECODE_FILE;

    private static RTPReceiver rtpReceiver;
    private static int RTP_CLIENT_PORT = 5004;
    private static int RTP_SERVER_PORT = 0;

    public RTSPConnection(){
    }
    public RTSPConnection(String URL, String RTSP_URL, int RTSP_PORT){
        this.URL = URL;
        this.RTSP_URL = RTSP_URL;
        this.RTSP_PORT = RTSP_PORT;
        Decode decode = new Decode();
        this.PATH_DECODE_FILE = decode.getPath();
    }

    public RTSPConnection(String URL, String RTSP_URL, int RTSP_PORT, String PATH_DECODE_FILE){
        this.URL = URL;
        this.RTSP_URL = RTSP_URL;
        this.RTSP_PORT = RTSP_PORT;
        this.PATH_DECODE_FILE = PATH_DECODE_FILE;
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
            rtspSocket = new Socket(URL, RTSP_PORT);
            outputStream = rtspSocket.getOutputStream();
            inputStream = rtspSocket.getInputStream();

            String request = "DESCRIBE " + RTSP_URL + " RTSP/1.0\r\nCSeq: 1\r\nAccept: application/sdp\r\n\r\n";
            outputStream.write(request.getBytes());
            outputStream.flush();

            // Читаем ответ от RTSP сервера
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            int cnt = 0;
            String response;
            ArrayList<String> uri_list = new ArrayList<>();
            ArrayList<String> codec_list = new ArrayList<>();
            String uri;
            String codec;
            int contentLength = 0;

            response = reader.readLine();
            while (response != null && cnt < 50) {
                // Обработка ответа от RTSP сервера
                cnt++;// Счетчик полученных пакетов
                System.out.println(response);
                // Нахождение длины тела запроса
                if (response.startsWith("Content-Length: ")){
                    contentLength = Integer.parseInt(response.replace("Content-Length: ",""));
                }

                // Конец считывания заголовка запроса
                if (response.isEmpty()){
                    break;
                }

                response = reader.readLine();
            }

            //Парсер тела запроса
            char[] buffer = new char[contentLength];
            reader.read(buffer, 0, contentLength);
            String responseBody = new String(buffer);
            String[] parts = responseBody.split("\n");
            for (String part : parts){
                System.out.println(part);
                if (part.startsWith("a=control:")) {
                    uri = part.replace("a=control:","");
                    uri_list.add(uri.replace("\r", ""));
                }
                if (part.startsWith("a=rtpmap:")){
                    codec = part.replace("a=rtpmap:","");
                    codec_list.add(codec.replace("\r", ""));
                }
            }


            request = "SETUP " + uri_list.get(1) + " RTSP/1.0\r\nCSeq: 2\r\nTransport: RTP/AVP;unicast;client_port=" + RTP_CLIENT_PORT + "-" + (RTP_CLIENT_PORT + 1) + "\r\n\r\n";
            outputStream.write(request.getBytes());
            outputStream.flush();

            // Читаем ответ от RTSP сервера
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String session = "1";
            boolean is_session = false;
            boolean is_transport = false;
            while ((response = reader.readLine()) != null) {
                System.out.println(response);
                // Обработка ответа от RTSP сервера
                // Извлекаем информаци
                if(response.startsWith("Session")){
                    String[] sessions = response.split(":");
                    session = sessions[1].replace(";timeout=60","");
                    is_session = true;
                }
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
                    is_transport = true;
                }
                if (is_transport && is_session){
                    break;
                }
            }

            request = "PLAY " + RTSP_URL + " RTSP/1.0\r\nCSeq: 3\r\nSession: " + session + "\r\nRange: npt=0.000-\r\n\r\n";

            outputStream.write(request.getBytes());
            outputStream.flush();



            rtpReceiver = new RTPReceiver(RTP_CLIENT_PORT, codec_list, PATH_DECODE_FILE);
            rtpReceiver.reception();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            rtspSocket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
