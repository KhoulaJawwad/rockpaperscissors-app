package rps.yousuf.com.rockpaperscissors;

import android.app.Activity;
import android.graphics.Bitmap;
import android.provider.ContactsContract;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.nnapi.NnApiDelegate;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Map;

public abstract class Classifier {

    MappedByteBuffer tfliteModel ;
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();
    Interpreter tflite ;
    private List<String> labels ;
    private final int imageSizeY;
    private final int imageSizeX;
    private TensorImage inputImageBuffer ;
    private final TensorBuffer outputProbabilityBuffer ;
    private final TensorProcessor probabilityProcessor;


    protected Classifier(Activity activity, Model.Device device) throws IOException {

        tfliteModel = FileUtil.loadMappedFile(activity, getModelPath());
        NnApiDelegate nnApiDelegate = new NnApiDelegate();
        tfliteOptions.addDelegate(nnApiDelegate);
        tfliteOptions.setNumThreads(1);
        tflite = new Interpreter(tfliteModel, tfliteOptions) ;

        // Load labels from the label file.
        labels = FileUtil.loadLabels(activity, getLabelPath());

        int imageTensorIndex = 0;
        int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape();
        imageSizeY = imageShape[1];
        imageSizeX = imageShape[2];
        DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();
        int probabilityTensorIndex = 0;
        int[] probabilityShape = tflite.getOutputTensor(probabilityTensorIndex).shape();
        DataType probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType();


        inputImageBuffer = new TensorImage(imageDataType);
        outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);

        probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();

    }

    public List<String> recognizeImage(final Bitmap bitmap) {
        inputImageBuffer= loadImage(bitmap) ;
        tflite.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getBuffer().rewind());

        Map<String, Float> labeledProbability =
                new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                .getMapWithFloatValue();
        return (List<String>) labeledProbability;
    }

    private TensorImage loadImage(final Bitmap bitmap) {

        inputImageBuffer.load(bitmap);

        // Creates process for the TensorImage
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        ImageProcessor imageProcessor = new ImageProcessor.Builder()
                .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.BILINEAR))
                .add(getPreprocessNormalizeOp())
                .build();

        return imageProcessor.process(inputImageBuffer);
    }

    protected abstract TensorOperator getPreprocessNormalizeOp();
    protected abstract TensorOperator getPostprocessNormalizeOp();


    protected abstract String getModelPath();
    protected abstract String getLabelPath();
}
