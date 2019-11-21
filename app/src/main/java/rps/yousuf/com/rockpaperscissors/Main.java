package rps.yousuf.com.rockpaperscissors;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class Main extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 20001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Camera camera = new Camera();
        //camera.dispatchTakePictureIntent();

        dispatchTakePictureIntent();

    }


    /**
     * Issues Camera Intent
     * */
    public void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Snaps a picture
     * */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bitmap b = (data.getExtras()).getParcelable("data");

            ImageView iv = (ImageView)findViewById(R.id.iv);
            iv.setImageBitmap(b);
        }

    }

}
