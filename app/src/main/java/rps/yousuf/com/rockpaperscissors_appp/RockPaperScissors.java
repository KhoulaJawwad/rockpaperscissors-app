package rps.yousuf.com.rockpaperscissors_appp;

// Important imports for the timer part.
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// Important imports for the camera part.
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.provider.MediaStore.Files.FileColumns;
import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class RockPaperScissors extends Activity
{
    private TextView countdownText;
    private Button countdownButton;    private CountDownTimer countDownTimer;
    private long timeLeftinMilliseconds = 6000; //4 seconds
    private boolean timerRunning;

    private static final int CAMERA_REQUEST = 1888;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    //ImageView imageView;
    FrameLayout frameLayout;
    Camera camera;
    ShowCamera showCamera;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rock_paper_scissors);

        // Create ImageView and button
        //imageView = (ImageView) this.findViewById(R.id.imageView);
        Button Imagebutton = (Button) this.findViewById(R.id.button);

        frameLayout = (FrameLayout)findViewById(R.id.frame);
        imageView = (ImageView) this.findViewById(R.id.imageView);

        // First disable the image view
        imageView.setVisibility(View.GONE);

        countdownText = findViewById(R.id.countdown_text);
        countdownButton = findViewById(R.id.countdown_button);

        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                turnOnCamera();

                startStop();
            }
        });
    }
    public void startStop() {
        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }
    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftinMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftinMilliseconds = l;
                updateTimer();            }            @Override
            public void onFinish() {            }
        }.start();
        countdownButton.setText("PAUSE");
        timerRunning = true;
    }
    public void stopTimer()
    {
        countDownTimer.cancel();
        countdownButton.setText("START");
        timerRunning = false;
    }
    public void updateTimer()
    {
        int seconds = (int) timeLeftinMilliseconds % 60000 / 1000;
        String timeLeftText = "" + seconds;
        if (seconds==4) {
            timeLeftText = "Rock";
            countdownText.setText(timeLeftText);
        }
        else if (seconds==3) {
            timeLeftText = "Paper";
            countdownText.setText(timeLeftText);
        }
        else if (seconds==2) {
            timeLeftText = "Scissors";
            countdownText.setText(timeLeftText);
        }
        else if (seconds==1) {
            timeLeftText = "Shoot";
            countdownText.setText(timeLeftText);

            // Now take a picture.
            takePicture();
        }

    }

    /**
     * Take a picture from the camera, which has already been enabled.
     *
     * Requires: The camera has already been enabled and open.
     * */
    public void takePicture()
    {
        if(camera != null)
        {
            camera.takePicture(null, null, mPictureCallBack);

            // Disable the frame and enable the imageview.
            frameLayout.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);

        }
    }

    public void turnOnCamera()
    {
        // Open the camera
        camera = Camera.open(0); // Camera.open(1) for the front camera view

        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);
    }
    Camera.PictureCallback mPictureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {



            // Save to a file-path
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }

            // Display the image on the imageView
            String filePath = pictureFile.getPath();
            Bitmap DisplayImage = BitmapFactory.decodeFile(filePath);
            DisplayImage = rotateImage(DisplayImage, 90);
            imageView.setImageBitmap(DisplayImage);


        }
    };


    public void CaptureImage(View v)
    {
        if(camera != null)
        {
            camera.takePicture(null, null, mPictureCallBack);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }


    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * Rotate Bitmap
     * from https://stackoverflow.com/questions/10332783/rotate-a-saved-bitmap-in-android
     * */
    public static Bitmap rotateImage(Bitmap src, float degree)
    {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bmp;
    }

}