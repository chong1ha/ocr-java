package com.example.paddleocr;

import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.paddlepaddle.zoo.cv.objectdetection.PpWordDetectionTranslator;
import ai.djl.paddlepaddle.zoo.cv.wordrecognition.PpWordRecognitionTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import ai.djl.translate.TranslatorContext;
import com.example.core.util.StringUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gunha
 * @version 1.0
 * @since 2024-07-04 오후 2:06
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
    private void processOptions(ApplicationArguments args) throws IllegalArgumentException, IOException, TranslateException, ModelNotFoundException, MalformedModelException {

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

        // 확장자 체크
        if (!imgFilePath.endsWith(".png") && !imgFilePath.endsWith(".jpg") && !imgFilePath.endsWith(".jpeg")) {
            throw new IllegalArgumentException("The File must have a .png, .jpg, or .jpeg extension");
        }

        // Text Detection 수행
        try {
            detectText(imgFilePath);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Text Detection 수행
     *
     * @param imgFilePath detecting 할 이미지경로
     */
    private void detectText(String imgFilePath) throws IOException, ModelNotFoundException, MalformedModelException, TranslateException {

        // 예외처리
        if (StringUtil.isEmpty(imgFilePath)) {
            throw new IllegalArgumentException("imgFilePath is empty");
        }

        // 이미지 로드
        Image img = ImageFactory.getInstance().fromFile(Paths.get(imgFilePath));

        // 모델 로드
        Predictor<Image, DetectedObjects> dbPredictor = getDBPredictor();
        Predictor<Image, String> recognizer = getRecognizer();

        DetectedObjects objects = dbPredictor.predict(img);

        List<DetectedObjects.DetectedObject> boxes = objects.items();
        for (DetectedObjects.DetectedObject box : boxes) {
            Image subImg = getSubImage(img, box.getBoundingBox());
            String name = recognizer.predict(subImg);
            System.out.println(name);
        }
    }

    private static Image getSubImage(Image img, BoundingBox box) {

        Rectangle rect = box.getBounds();

        double[] extended = extendRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

        int width = img.getWidth();
        int height = img.getHeight();
        int[] recovered = {
                (int) (extended[0] * width),
                (int) (extended[1] * height),
                (int) (extended[2] * width),
                (int) (extended[3] * height)
        };

        return img.getSubImage(recovered[0], recovered[1], recovered[2], recovered[3]);
    }

    private static double[] extendRect(double xmin, double ymin, double width, double height) {

        double centerx = xmin + width / 2;
        double centery = ymin + height / 2;

        if (width > height) {
            width += height * 2.0;
            height *= 3.0;
        } else {
            height += width * 2.0;
            width *= 3.0;
        }

        double newX = centerx - width / 2 < 0 ? 0 : centerx - width / 2;
        double newY = centery - height / 2 < 0 ? 0 : centery - height / 2;
        double newWidth = newX + width > 1 ? 1 - newX : width;
        double newHeight = newY + height > 1 ? 1 - newY : height;

        return new double[] {newX, newY, newWidth, newHeight};
    }

    private static Predictor<Image, DetectedObjects> getDBPredictor()  throws ModelNotFoundException, MalformedModelException, IOException {

        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelPath(Paths.get("C:/Users/gunha/Downloads/converted/converted/db.onnx"))
                        .optEngine("OnnxRuntime")
                        .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<>()))
                        .build();

        return criteria.loadModel().newPredictor();
    }

    private static Predictor<Image, String> getRecognizer() throws MalformedModelException, ModelNotFoundException, IOException {

        Criteria<Image, String> criteria =
                Criteria.builder()
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("C:/Users/gunha/Downloads/converted/converted/rec.onnx"))
                        .optTranslator(new OnnxWordRecognitionTranslator())
                        .optEngine("OnnxRuntime")
                        .build();

        ZooModel<Image, String> model = criteria.loadModel();

        return model.newPredictor();
    }

    private static class OnnxWordRecognitionTranslator extends PpWordRecognitionTranslator {

        @Override
        public NDList processInput(TranslatorContext ctx, Image input) {

            NDArray img = input.toNDArray(ctx.getNDManager());

            img = NDImageUtils.resize(img, 100, 32);
            img = NDImageUtils.toTensor(img).sub(0.5f).div(0.5f);
            img = img.expandDims(0);

            return new NDList(img);
        }
    }
}
