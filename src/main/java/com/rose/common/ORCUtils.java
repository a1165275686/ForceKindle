package com.rose.common;

import com.benjaminwan.ocrlibrary.OcrResult;
import com.rose.constant.ImageRectangle;
import com.rose.exception.BusinessException;
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

public class ORCUtils {
    private static final String TESS4J_DATA_PATH = "src/main/resources/tessdata";
    private static final String LANGUAGE = "eng";
    private static final int PAGE_SEGMENT_MODE = 1;
    private static final int OCR_ENGINE_MODE = 1;

    public Map<String, String> getORCBatch(String imagePath) {
        Map<String, String> dataMap = new HashMap<>();
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(TESS4J_DATA_PATH);
        tesseract.setLanguage(LANGUAGE);
        tesseract.setPageSegMode(PAGE_SEGMENT_MODE);
        tesseract.setOcrEngineMode(OCR_ENGINE_MODE);

        try {
            //File imageFile = new File("C:/Users/rose/Desktop/ceshitupian/ceshi.jpg");
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);

            // 指定要识别的区域（x, y, width, height）
            int orginImageX = 550;
            int orginImageY = 180;
            int orginImageWidth = 2000;
            int orginImageHeight = 700;

            BufferedImage croppedImage = image.getSubimage(orginImageX, orginImageY, orginImageWidth, orginImageHeight);

            int blockX = 34;
            int blockY = 28;
            int blockWidth = 606;
            int blockHeight = 197;
            for (int i = blockX; i < orginImageWidth; i += 622) {
                if (i + blockWidth >= orginImageWidth) {
                    break;
                }
                BufferedImage croppedImage2 = croppedImage.getSubimage(i, blockY, blockWidth, blockHeight);
                String result = tesseract.doOCR(croppedImage2);
                // 使用空格作为分隔符分割字符串
                String[] parts = result.split("\\s+");
                if (parts.length >= 3) {
                    dataMap.put(parts[0] + parts[1], parts[2]);
                }
            }
        } catch (TesseractException | IOException e) {
            e.getMessage();
        }
        return dataMap;
    }

    //TESS4J OCR识别
    public Map<String, String> getORCBatchByFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件夹不存在");
        }

        Map<String, String> dataMap = new HashMap<>();
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(TESS4J_DATA_PATH);
        tesseract.setLanguage(LANGUAGE);
        tesseract.setPageSegMode(PAGE_SEGMENT_MODE);
        tesseract.setOcrEngineMode(OCR_ENGINE_MODE);

        try {
            //File imageFile = new File("C:/Users/rose/Desktop/ceshitupian/ceshi.jpg");
            File[] imageFile = folder.listFiles();
            if (imageFile != null) {
                for (File file : imageFile) {
                    if (file.isFile() && isImageFile(file.getName())) {
                        try {
                            BufferedImage image = ImageIO.read(file);
                            // 指定要识别的区域（x, y, width, height）
                            int orginImageX = 550;
                            int orginImageY = 180;
                            int orginImageWidth = 2000;
                            int orginImageHeight = 700;

                            BufferedImage croppedImage = image.getSubimage(orginImageX, orginImageY, orginImageWidth, orginImageHeight);

                            int blockX = 34;
                            int blockY = 28;
                            int blockWidth = 606;
                            int blockHeight = 197;
                            for (int i = blockX; i < orginImageWidth; i += 622) {
                                if (i + blockWidth >= orginImageWidth) {
                                    break;
                                }
                                BufferedImage croppedImage2 = croppedImage.getSubimage(i, blockY, blockWidth, blockHeight);
                                String result = tesseract.doOCR(croppedImage2);
                                // 使用空格作为分隔符分割字符串
                                String[] parts = result.split("\\s+");
                                if (parts.length >= 3) {
                                    dataMap.put(parts[0] + parts[1], parts[2]);
                                }
                            }
                        } catch (TesseractException | IOException e) {
                            e.getMessage();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return dataMap;
    }

    //RapidOCR识别
    public Map<String, String> getORCBatchByRapidOCR(String imagePath) {
        Map<String, String> dataMap = new HashMap<>();

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

        try {
            File imageFile = new File(imagePath);
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
                // 使用空格作为分隔符分割字符串
                String[] parts = ocrString.split("\\s+");
                dataMap.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return dataMap;
    }

    //RapidOCR识别
    public Map<String, String> getORCBatchByRapidOCRToFolder(String folderPath) {


        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件夹不存在");
        }
        Map<String, String> dataMap = new HashMap<>();

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

        File[] imageFile = folder.listFiles();
        if (imageFile != null) {
            for (File file : imageFile) {
                if (file.isFile() && isImageFile(file.getName())) {
                    try {
                        BufferedImage image = ImageIO.read(file);
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
                            // 使用空格作为分隔符分割字符串
                            String[] parts = ocrString.split("\\s+");
                            if (parts.length >= 3) {
                                dataMap.put(parts[0] + parts[1], parts[parts.length-1]);
                            } else if(parts.length == 2){
                                dataMap.put(parts[0], parts[1]);
                            }else{
                                continue;
                            }
                        }
                    } catch (IOException e) {
                        e.getMessage();
                    }
                }
            }
        }
        return dataMap;
    }

    private static boolean isImageFile(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".png") || lowerCaseFileName.endsWith(".jpg") || lowerCaseFileName.endsWith(".jpeg");
    }
}
