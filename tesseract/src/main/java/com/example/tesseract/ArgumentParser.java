package com.example.tesseract;

import com.example.core.util.StringUtil;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;

/**
 * 인자(arguments) 옵션 처리
 *
 * @author gunha
 * @version 1.0
 * @since 2024-07-04 오전 9:42
 */
@Component
public class ArgumentParser implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 옵션 처리
        try {
            processOptions(args);
        } catch (Exception e) {
            System.err.println("Error while processing options: " + e.getMessage());
        }

        // 정상 종료
        System.exit(0);
    }

    /**
     * 옵션 처리
     *
     * @param args 인자들
     * @throws IllegalArgumentException 이미지 파일 경로가 유효하지 않을 경우
     */
    private void processOptions(ApplicationArguments args) throws IllegalArgumentException, TesseractException {

        //
        // 옵션 f. 이미지 파일경로
        String imgFilePath = "";

        if (args.containsOption("f")) {

            try {
                Optional<String> imgFilePathOpt = args.getOptionValues("f") != null && !args.getOptionValues("f").isEmpty()
                        ? Optional.of(args.getOptionValues("f").get(0))
                        : Optional.empty();

                if (!imgFilePathOpt.isPresent()) {
                    throw new IllegalArgumentException("ARGS: Missing Image File");
                }

                imgFilePath = imgFilePathOpt.get();
            } catch (Exception e) {
                throw new IllegalArgumentException("Error processing command line arguments " + e.getMessage());
            }
        }

        // Text Detection 수행
        try {
            detectText(imgFilePath);
        } catch (TesseractException e) {
            throw e;
        }
    }

    /**
     * Text Detection 수행
     *
     * @param imgFilePath detecting 할 이미지경로
     */
    private void detectText(String imgFilePath) throws TesseractException {

        // 예외처리
        if (StringUtil.isEmpty(imgFilePath)) {
            throw new IllegalArgumentException("imgFilePath is empty");
        }

        Tesseract tesseract = getTesseract();

        File file = new File(imgFilePath);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("Image File does not exist or is not a valid File: " + imgFilePath);
        }

        String result = "";
        try {
            result = tesseract.doOCR(file);
            System.out.println(result);
        } catch (TesseractException e) {
            throw e;
        }
    }

    private static Tesseract getTesseract() {

        Tesseract instance = new Tesseract();

        instance.setLanguage("kor");
        //instance.setLanguage("eng");
        instance.setOcrEngineMode(1);
        instance.setDatapath("E:/3.Study/OCR/tesseract/tessdata_best-main");

        return instance;
    }
}
