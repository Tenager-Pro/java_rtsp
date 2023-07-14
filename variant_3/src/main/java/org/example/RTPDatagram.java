package org.example;

public class RTPDatagram {
    private int PayloadType;
    private int Version;
    private int Padding;
    private int Extension;
    private int CSRCCount;
    private int Marker;
    private int HeaderSize;


    public int getHeaderSize(){
        return HeaderSize;
    }

    public int getPayloadType(){
        return PayloadType;
    }

    public int getVersion(){
        return Version;
    }

    public int getPadding(){
        return Padding;
    }

    public int getExtension(){
        return Extension;
    }

    public int getCSRCCount(){
        return CSRCCount;
    }

    public int getMarker(){
        return Marker;
    }
    public void parse(byte[] rtpData){
        Version = (rtpData[0] & 0xC0); // Извлекаем версию
        Padding = (rtpData[0] & 0x20); // Извлекаем padding
        PayloadType = (rtpData[1] & 0x7F); // Извлекаем тип данных кодека из заголовка RTP
        CSRCCount = (rtpData[0] & 0x0F); // Значение поля CC указывает на количество доп. источников после заголовка rtp
        HeaderSize = 12 + (CSRCCount * 4);
    }
}
