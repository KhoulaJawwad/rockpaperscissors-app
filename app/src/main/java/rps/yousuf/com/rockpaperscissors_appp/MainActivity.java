package rps.yousuf.com.rockpaperscissors_appp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity
{

    private Button button;    @Override
    protected void onCreate(Bundle savedInstanceState)
{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openRockPaperScissors();
            }
        });
    }

    public void openRockPaperScissors()
    {
        Intent intent = new Intent(this, RockPaperScissors.class);
        startActivity(intent);
    }

}



