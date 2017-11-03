package com.shiku.blue.restrictme;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.Taohid.blue.restrictme.R;

/**
 * Created by BLUE on 5/3/2016.
 */
public class How_to_use extends Activity{

    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

            setContentView(R.layout.help_layout_kitkat);

        TextView textView = (TextView)findViewById(R.id.textView23);
        textView.setMovementMethod(new ScrollingMovementMethod());



    }
}

