package rps.yousuf.com.rockpaperscissors;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.model.Model;

import java.nio.MappedByteBuffer;
import java.util.List;

public class RockPaperScissorsActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 888 ;
    public Interpreter interpreter ;
    private static final float IMAGE_MEAN = 255.0f;
    private static final float IMAGE_STD = 1.0f;
    private static final float PROBABILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 1.0f;
    MappedByteBuffer tfliteModel ;
    Classifier classifier ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rock_paper_scissors);

        Button playButton = findViewById(R.id.PlayButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
            }
        });
        Model.Device device = Model.Device.CPU ;

        try {
        classifier = new Classifier(this, device) {
            @Override
            protected TensorOperator getPreprocessNormalizeOp() {
                return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
            }

            @Override
            protected TensorOperator getPostprocessNormalizeOp() {
                return new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD);
            }

            @Override
            protected String getModelPath() {
                String model_path = "model.tflite";
                return model_path;
            }

            @Override
            protected String getLabelPath() {
                String label_path = "labels.txt";
                return label_path;
            }
        } ;
        }
        catch (Exception e) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bitmap b = (Bitmap)data.getExtras().get("data");

            ImageView iv = (ImageView)findViewById(R.id.iv);
            List<String> results = classifier.recognizeImage(b);
            iv.setImageBitmap(b);
        }

    }
}
