package org.example;

public class Decode {
    public void decode_mpeg4_generic(){
        System.out.println("Привет");
    }

    public void decode_JPEG(byte[] payload){
        JPEGHeader jpegHeader = new JPEGHeader();
        jpegHeader.parse(payload);

        System.out.println(jpegHeader.getHeight());
        System.out.println(jpegHeader.getWidth());
    }
    public String getPath() {
        return(getClass().getName());
    }
}
