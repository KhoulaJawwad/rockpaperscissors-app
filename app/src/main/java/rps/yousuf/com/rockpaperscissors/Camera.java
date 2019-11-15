package rps.yousuf.com.rockpaperscissors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;

public class Camera
{
    // Instance Variables
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
     * Taking Picture Intent
     * */
    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(context.getPackageManager()) != null)
        {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    

}
