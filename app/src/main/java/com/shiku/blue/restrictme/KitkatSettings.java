package com.shiku.blue.restrictme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.Taohid.blue.restrictme.R;

/**
 * Created by BLUE on 4/30/2016.
 */
public class KitkatSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.kitkat_settings);



        Button buttonTwo = (Button)findViewById(R.id.buttonSelectApps);

        buttonTwo.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "loading your apps", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                startSubActivity();
            }
        });

        Button buttonThree = (Button)findViewById(R.id.buttonHelp);

        buttonThree.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                helpActivity();
            }
        });


        Button buttonCredit = (Button)findViewById(R.id.buttonCredit);

        buttonCredit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
               creditActivity();
           }
        });
    }
    private void startSubActivity() {

        Intent i = new Intent(this, AppData.class);
        startActivity(i);
    }
    public void helpActivity()
    {
        Intent helpIntent = new Intent(this,How_to_use.class);
        startActivity(helpIntent);
    }
    public void creditActivity()
    {
        Intent helpIntent = new Intent(this,CreditClass.class);
        startActivity(helpIntent);
    }
}
