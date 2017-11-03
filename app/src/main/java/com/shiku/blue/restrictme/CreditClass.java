package com.shiku.blue.restrictme;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.Taohid.blue.restrictme.R;


/**
 * Created by BLUE on 5/3/2016.
 */
public class CreditClass extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        super.onCreate(savedInstanceState);
        TextView textView = (TextView)findViewById(R.id.creditText);

        // if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP){
        setContentView(R.layout.credit_layout);
        // }

    }
}