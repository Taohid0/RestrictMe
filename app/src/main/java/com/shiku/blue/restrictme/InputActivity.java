package com.shiku.blue.restrictme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.Taohid.blue.restrictme.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by BLUE on 2/29/2016.
 */
public class InputActivity extends Activity {
    int i=0,j=0;
    public TextView tvDisplayTime;
    public TimePicker timePicker1;
    public Button btnChangeTime;
    int b1=0,b2=0;
    public int hour;
    public int minute;
    public  int hourFrom,hourTo,minuteFrom,minuteTo;
    String amPm1 = "am",ampm2 = "am";


    public int restrictFromHour,restrictFromMinute,toHour,toMinute;

    static final int TIME_DIALOG_ID = 999;
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
                 super.onCreate(savedInstanceState);
                 setContentView(R.layout.new_entry_layout);



      TextView textView =(TextView)findViewById(R.id.lblTime);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);

        setCurrentTimeOnView();
        addListenerOnButton();
        addListenerOnButtonTo();



        Button btnOk  = (Button)findViewById(R.id.buttonOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                EditText workNameEditText = (EditText) findViewById(R.id.editTextWorkName);
                String workName = workNameEditText.getText().toString();





                if (hourFrom >= 12){
                    hourFrom -= 12;
                    amPm1 = "pm";
                }

                if(hourTo>=12){
                    hourTo-=12;
                    ampm2= "pm";
                }
               String message = "";

                if(minuteFrom<10 && minuteTo>9){
                    message = workName + ":" + hourFrom + ".0" + minuteFrom + " "+amPm1+ ":"
                            + hourTo + "." + minuteTo + " "+ ampm2+":";
                }

               else if(minuteFrom>9 && minuteTo<10){
                     message = workName + ":" + hourFrom + "." + minuteFrom + " "+amPm1+ ":"
                            + hourTo + ".0" + minuteTo + " "+ ampm2+":";
                }
               else if(minuteFrom<10 && minuteTo<10)
                {
                    message = workName + ":" + hourFrom + ".0" + minuteFrom + " "+amPm1+ ":"
                            + hourTo + ".0" + minuteTo + " "+ ampm2+":";
                }
               else if(minuteFrom>9 &&minuteTo>9 )
                       message = workName + ":" + hourFrom + "." + minuteFrom + " "+amPm1+ ":"
                        + hourTo + "." + minuteTo + " "+ ampm2+":";
                if(workName.equals(""))
                {
                    nullText();

                }
                else if(b1==0)
                {
                    buttonOneNotPressed();
                }
                else if(b2==0)
                {
                    buttonTwoNotPressed();
                }

                else{
                Intent intent = new Intent();
                intent.putExtra("message", message);
                setResult(2, intent);
                finish();
            }}
        });


    }

       void nullText()
       {
           AlertDialog alertDialog = new AlertDialog.Builder(this).create();
           alertDialog.setTitle("Warning");
           alertDialog.setMessage("Please enter your scheduled work name");
           alertDialog.show();
       }
      void buttonOneNotPressed()
      {
          AlertDialog alertDialog = new AlertDialog.Builder(this).create();
          alertDialog.setTitle("Warning");
          alertDialog.setMessage("Please press TIME TO START button");
          alertDialog.show();
      }
    void buttonTwoNotPressed()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Please press TIME TO END button");
        alertDialog.show();
    }
    public void setCurrentTimeOnView() {

        tvDisplayTime = (TextView) findViewById(R.id.tvTime);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);

       // final Calendar c = Calendar.getInstance();
       // hour = c.get(Calendar.HOUR_OF_DAY);
       // minute = c.get(Calendar.MINUTE);

        timePicker1.setEnabled(false);
        // set current time into textview
        tvDisplayTime.setText(
                new StringBuilder().append(pad(hour))
                        .append(":").append(pad(minute)));

        // set current time into timepicker
        //timePicker1.setCurrentHour(hour);
        //timePicker1.setCurrentMinute(minute);

    }

    public void addListenerOnButton() {

        btnChangeTime = (Button) findViewById(R.id.btnChangeTime);

        btnChangeTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID);

                timePicker1.clearFocus();
                hourFrom = timePicker1.getCurrentHour();
                minuteFrom = timePicker1.getCurrentMinute();


         b1 = 1;
                   i = 1;
            }

        });

    }
    public void addListenerOnButtonTo() {

        btnChangeTime = (Button) findViewById(R.id.buttonTo);

        btnChangeTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID);
                timePicker1.clearFocus();
                hourTo = timePicker1.getCurrentHour();
                minuteTo = timePicker1.getCurrentMinute();


                j = 1;
                b2=1;

            }

        });

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListener, hour, minute,false);

        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;


                        if(i == 1){
                            hourFrom = hour;
                            minuteFrom  = minute;

                            i = 0;
                        }
                    if(j==1){
                        hourTo = hour;
                        minuteTo = minute;

                        j = 0;
                    }

                    // set current time into textview
                    tvDisplayTime.setText(new StringBuilder().append(pad(hour))
                            .append(":").append(pad(minute)));

                    // set current time into timepicker
                  //  timePicker1.setCurrentHour(hour);
                   // timePicker1.setCurrentMinute(minute);

                }

            };

     static  String EXTRA_MESSAGE = "java/com.blue.RestrictMe/MyActivity.java";

    public  void buttonDone(View view)
    {
        Intent intent = new Intent(this,MainActivity.class);
        String message = hourFrom+  ":"+minuteFrom+":"+hourTo+":"+minuteTo;
        intent.putExtra(EXTRA_MESSAGE,message);
        setResult(2, intent);
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}
