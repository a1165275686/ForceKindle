package com.rose.common;

import com.benjaminwan.ocrlibrary.OcrResult;
import com.rose.constant.ImageRectangle;
import io.github.mymonstercat.Model;
import io.github.mymonstercat.ocr.InferenceEngine;
import io.github.mymonstercat.ocr.config.ParamConfig;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ORCUtilsDemo {



    public static String getORC() throws TesseractException, IOException {
        File imageFile = new File("C:/Users/rose/Desktop/ceshitupian/ceshi.jpg");
        BufferedImage image = ImageIO.read(imageFile);

        // 指定要识别的区域（x, y, width, height）
        int x = 550;
        int y = 180;
        int width = image.getWidth() - x;
        int height = 700;

        BufferedImage croppedImage = image.getSubimage(x, y, width, height);
        ImageIO.write(croppedImage, "jpg", new File("C:/Users/rose/Desktop/ceshitupian/ceshi1.jpg"));

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata");
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
        String result = tesseract.doOCR(croppedImage);
        return result;
    }

    static Map<String, String> dataMap = new HashMap<>();

    public static String getORCBatch() throws TesseractException, IOException {

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata");
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);


        File imageFile = new File("C:/Users/rose/Desktop/ceshitupian/ceshi.jpg");
        BufferedImage image = ImageIO.read(imageFile);

        // 指定要识别的区域（x, y, width, height）
        int orginImageX = 550;
        int orginImageY = 180;
        int orginImageWidth = 2000;
        int orginImageHeight = 700;

        BufferedImage croppedImage = image.getSubimage(orginImageX, orginImageY, orginImageWidth, orginImageHeight);
        ImageIO.write(croppedImage, "jpg", new File("C:/Users/rose/Desktop/ceshitupian/ceshi1.jpg"));

        int blockX = 33;
        int blockY = 27;
        int blockWidth = 606;
        int blockHeight = 197;
        for (int j = blockY; j < orginImageHeight; j += 202) {
            if (j + blockHeight >= orginImageHeight) {
                break;
            }
            for (int i = blockX; i < orginImageWidth; i += 622) {
                if (i + blockWidth >= orginImageWidth) {
                    break;
                }

                BufferedImage croppedImage2 = croppedImage.getSubimage(i, j, blockWidth, blockHeight);
                ImageIO.write(croppedImage2, "jpg", new File("C:/Users/rose/Desktop/ceshitupian/ceshiWW" + i + ";;;" + j + ".jpg"));
                String result = tesseract.doOCR(croppedImage2);
                // 使用空格作为分隔符分割字符串
                String[] parts = result.split("\\s+");
                dataMap.put(parts[0] + parts[1], parts[2]);
            }
        }

        return null;

    }

    public static String getORCBatchByList() throws TesseractException, IOException {

        // 预先存储六个框的位置
        List<ImageRectangle> rectanglesList = ImageRectangle.getInstance().getRectanglesList();
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata");
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);

        File imageFile = new File("C:/Users/rose/Desktop/ceshitupian/ceshu22.png");
        BufferedImage image = ImageIO.read(imageFile);

        for(int i =0;i<rectanglesList.size();i++){

                BufferedImage croppedImage2 = image.getSubimage(
                        rectanglesList.get(i).getX(),
                        rectanglesList.get(i).getY(),
                        rectanglesList.get(i).getWidth(),
                        rectanglesList.get(i).getHeight()
                        );

                ImageIO.write(croppedImage2, "jpg", new File("C:/Users/rose/Desktop/ceshitupian/ceshiWW" + i +  ".jpg"));
                String result = tesseract.doOCR(croppedImage2);
                // 使用空格作为分隔符分割字符串
                String[] parts = result.split("\\s+");
                dataMap.put(parts[0] + parts[1], parts[2]);
            }
        return null;
        }


    public static String getORCBatchByRapidOCR() throws IOException {

        ParamConfig paramConfig = ParamConfig.getDefaultConfig();
        paramConfig.setDoAngle(true);
        paramConfig.setMostAngle(true);
        InferenceEngine engine = InferenceEngine.getInstance(Model.ONNX_PPOCR_V3);
        //临时文件保存目录
        File customDirectory = new File("C:/Users/rose/Desktop/CustomTempDirectory");
        // 如果目录不存在，则创建它
        if (!customDirectory.exists()) {
            if (customDirectory.mkdirs()) {
                System.out.println("自定义临时目录创建成功");
            } else {
                System.out.println("无法创建自定义临时目录");
            }
        }


        File imageFile = new File("C:/Users/rose/Desktop/ceshitupian/ceshi.jpg");
        BufferedImage image = ImageIO.read(imageFile);

        // 指定要识别的区域（x, y, width, height）
        int orginImageX = 550;
        int orginImageY = 180;
        int orginImageWidth = 2000;
        int orginImageHeight = 700;

        BufferedImage croppedImage = image.getSubimage(orginImageX, orginImageY, orginImageWidth, orginImageHeight);
        //ImageIO.write(croppedImage, "jpg", new File("C:/Users/rose/Desktop/ceshitupian/ceshi1.jpg"));

        int blockX = 34;
        int blockY = 28;
        int blockWidth = 606;
        int blockHeight = 197;

        for (int i = blockX; i < orginImageWidth; i += 622) {
            if (i + blockWidth >= orginImageWidth) {
                break;
            }
            BufferedImage croppedImage2 = croppedImage.getSubimage(i, blockY, blockWidth, blockHeight);
            File tempFile = File.createTempFile("temp_ocr", ".jpg", customDirectory);
            ImageIO.write(croppedImage2, "jpg", tempFile);
            OcrResult ocrResult = engine.runOcr(tempFile.getAbsolutePath(), paramConfig);
            String ocrString = ocrResult.getStrRes().toString();
            return ocrString;
        }
        return null;

    }

    public static String getORCBatchByRapidOCRMap() throws IOException {

        ParamConfig paramConfig = ParamConfig.getDefaultConfig();
        paramConfig.setDoAngle(true);
        paramConfig.setMostAngle(true);
        InferenceEngine engine = InferenceEngine.getInstance(Model.ONNX_PPOCR_V3);
        //临时文件保存目录
        File customDirectory = new File("C:/Users/rose/Desktop/CustomTempDirectory");
        // 如果目录不存在，则创建它
        if (!customDirectory.exists()) {
            if (customDirectory.mkdirs()) {
                System.out.println("自定义临时目录创建成功");
            } else {
                System.out.println("无法创建自定义临时目录");
            }
        }

        List<ImageRectangle> rectanglesList = ImageRectangle.getInstance().getRectanglesList();

        File imageFile = new File("C:/Users/rose/Desktop/ceshitupian/ceshu22.png");
        BufferedImage image = ImageIO.read(imageFile);
        for(int i =0;i<rectanglesList.size();i++){

            BufferedImage croppedImage = image.getSubimage(
                    rectanglesList.get(i).getX(),
                    rectanglesList.get(i).getY(),
                    rectanglesList.get(i).getWidth(),
                    rectanglesList.get(i).getHeight()
            );
            File tempFile = File.createTempFile("temp_ocr", ".jpg", customDirectory);
            ImageIO.write(croppedImage, "jpg", tempFile);
            OcrResult ocrResult = engine.runOcr(tempFile.getAbsolutePath(), paramConfig);
            String ocrString = ocrResult.getStrRes().toString();
            System.out.println(ocrString);
            // 使用空格作为分隔符分割字符串
            String[] parts = ocrString.split("\\s+");
            dataMap.put(parts[0], parts[parts.length-1]);
        }
        return null;

    }

    public static void main(String[] args) {
        try {
            System.out.println(new ORCUtilsDemo().getORCBatchByRapidOCRMap());

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

}
