package rps.yousuf.com.rockpaperscissors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.ImageView;

public class Camera extends Activity
{
    // Instance Variables
    static final int REQUEST_IMAGE_CAPTURE = 20001;

    Context context;
    Activity activity;

    // Constructors
    public Camera()
    {


    }

    // Getters and Setters


    // Other Methods

    /**
     * Gets the Live feed from the camera
     * */
    public void getLiveFeed()
    {

    }

    /**
     * Snaps a picture from the camera and sends it
     * */
    public void getPic()
    {


    }

    /**
     * Issues Camera Intent
     * */
    public void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(context.getPackageManager()) != null)
        {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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
