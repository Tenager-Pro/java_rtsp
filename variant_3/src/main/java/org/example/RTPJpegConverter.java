package org.example;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RTPJpegConverter {

    private static int headerSize = 12;

    private static String PATH_DECODE;
    private static final String OUTPUT_PATH = "D:\\image\\"; // Путь к директории вывода

    private static ArrayList<String> CODEC_LIST = new ArrayList<>(); //Содержит информацию о кодировании потоков


    public RTPJpegConverter(ArrayList<String> CODEC_LIST, String PATH_DECODE){
        this.CODEC_LIST = CODEC_LIST;
        this.PATH_DECODE = PATH_DECODE;
    }

    // Метод для декодирования JPEG изображения
    private byte[] convertImageToBytes(BufferedImage image, String format) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, format, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Функция создания JPEG-изображения из данных
    private BufferedImage createJPEGImage(byte[] data) {
        try {
            // Чтение данных в формате JPEG
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ImageReader reader = ImageIO.getImageReadersByFormatName("JPEG").next();
            reader.setInput(ImageIO.createImageInputStream(bais));

            // Извлечение изображения из данных JPEG
            return reader.read(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> getPayloadCodec(){
        String[] parts;
        ArrayList<String> payloadTypes = new ArrayList<>();
        for (String part : CODEC_LIST){
            parts = part.split(" ");
            payloadTypes.add(parts[1]);
        }
        return payloadTypes;
    }

    private String getPayloadCodecVideo(){
        String[] parts;
        ArrayList<String> payloadTypes = getPayloadCodec();
        parts = payloadTypes.get(0).split("/");
        return parts[0];
    }

    private List<Integer> getPayloadType(){
        String[] parts;
        List<Integer> payloadTypes = new ArrayList<Integer>();
        for (String part : CODEC_LIST){
            parts = part.split(" ");
            payloadTypes.add(Integer.parseInt(parts[0]));
        }
        return payloadTypes;
    }

    private int getPayloadTypeVideo(){
        List<Integer> payloadTypes = getPayloadType();
        return payloadTypes.get(0);
    }

    public void convertByteToImage(DatagramPacket packet, int frameNumber) throws IOException {
        // Преобразование полезной нагрузки в массив байт

        byte[] rtpData = new byte[packet.getLength()]; // Создаем массив для данных из ответа rtp
        System.arraycopy(packet.getData(), packet.getOffset(), rtpData, 0, packet.getLength()); // Данные из ответа rtp
        int payloadType = (rtpData[1] & 0x7F); // Извлекаем тип данных кодека из заголовка RTP

        String codec = getPayloadCodecVideo();
        codec = codec.replace("-", "_");
        codec = codec.replace(".", "_");
        // Находим длинну header
        int cc = (rtpData[0] & 0x0F); // Значение поля CC указывает на количество доп. источников после заголовка rtp
        // Размер RTP заголовка в байтах.
        int headerSize = 12 + (cc * 4); // Высчитываеться по формуле Стандартный заголовок = 12 байт + 4 байта за каждый доп. источник

        // Тело ответа
        byte[] payload = new byte[packet.getLength() - headerSize];
        System.arraycopy(packet.getData(), packet.getOffset() + headerSize, payload, 0, packet.getLength() - headerSize);

        // Имя метода, которое нужно выполнить
        String methodName = "decode_"+codec;
        // Полное имя класса, которое нужно вызвать
        String className = PATH_DECODE;

        try {
            Class<?> decodeClass = Class.forName(className); // Получение класса по имени
            Object object = decodeClass.getDeclaredConstructor().newInstance(); // Создание экземпляра класса

            Method method = decodeClass.getMethod(methodName); // Получение метода по имени
            method.invoke(object); // Вызов метода
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.reflect.InvocationTargetException e) {
            e.printStackTrace();
        }

        // Создание байт кода изображения
        ByteArrayInputStream inputStream = new ByteArrayInputStream(payload);
        BufferedImage image = ImageIO.read(inputStream);
        // Создание JPEG изображения в виде массива байт
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", baos);

        // Сохранение JPEG изображения на диск
        String outputFileName = String.format("%s/frame_%05d.jpg", OUTPUT_PATH, frameNumber);
        File outputFile = new File(outputFileName);
        ImageIO.write(image, "jpeg", outputFile);
    }

    // Методы декодерования

}

