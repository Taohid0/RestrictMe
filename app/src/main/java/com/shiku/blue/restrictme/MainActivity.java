package com.shiku.blue.restrictme;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;

import android.app.AlertDialog;
import android.app.PendingIntent;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import android.content.Context;

import com.Taohid.blue.restrictme.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    PendingIntent alarmIntent;
    ArrayList<Integer>startTimeArrayList=  new ArrayList<>();
    ArrayList<Integer>endTimeArrayList = new ArrayList<>();

    ArrayList<String> workItem = new ArrayList<String>();
    ArrayAdapter<String> workItemAdapter;
    int itemIndex = 0;
  int c1 = 0,c2 = 0,s = 0,len;
    String arr[] = new String[1000];
    int indx = 0;
    boolean test = true;
    MyModifiedDataBase dataBase;
    DatabaseForSelectApp databaseForSelectApp;
    Vector<String> vector = new Vector<String>();
    String colorCodes[] = {//"#C5CAE9","#9FA8DA","#7986CB","#5C6BC0","#3F51B5","#3949AB","#303F9F",
            "#3F51B5", "#032612", "#42A5F5", "#2196F3", "#1E88E5", "#1976D2",
            "#0277BD", "#0288D1", "#039BE5", "#03A9F4", "#29B6F6", "#4FC3F7", "#81D4FA", "#B3E5FC",
            "#80DEEA", "#4DD0E1", "#26C6DA", "#00BCD4", "#00ACC1", "#0097A7", "#E0F7FA"

    };
    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(flag) {
            flag =false;
            Intent intenstSplash = new Intent(getApplicationContext(), SplashScreen.class);
            startActivity(intenstSplash);

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Button button =(Button) findViewById(R.id.button2);

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP) {

            button.setVisibility(View.GONE);
        }

        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent kitkat = new Intent(getApplicationContext(), KitkatSettings.class);
                startActivity(kitkat);
            }
        });

          Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
          setSupportActionBar(toolbar);

        ListView workList = (ListView) findViewById(R.id.listView1);
        registerForContextMenu(workList);
        workItemAdapter = new ArrayAdapter<String>(this, R.layout.list_item, workItem) {


            public View getView(int position, View convertView, ViewGroup parent) {

                TextView textView = (TextView) super.getView(position, convertView, parent);

                try {
                    textView.setBackgroundColor(Color.parseColor(colorCodes[position % 1]));

                } catch (Exception e) {
                    textView.setBackgroundColor(Color.RED);
                }
                return textView;
            }
        };


        workList.setAdapter(workItemAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        dataBase = new MyModifiedDataBase(this, null, null, 1);
        databaseForSelectApp= new DatabaseForSelectApp(this, null, null, 1);
        addItemsToList();

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent myIntent = new Intent(view.getContext(), InputActivity.class);
                startActivityForResult(myIntent, 2);

            }


        });


        //dataBase.resetTables();
       launchService();
        alarmStart();

        String testToast = databaseForSelectApp.databaseToString();

        if(testToast.length()<3)
        {
            Toast.makeText(getApplicationContext(),"Please, read HELP to know how to use it",Toast.LENGTH_SHORT).show();
        }

        Calendar calendar = null;

        calendar = Calendar.getInstance();

        c1 = calendar.get(Calendar.HOUR_OF_DAY);
        c2 = calendar.get(Calendar.MINUTE);
        s = c1*60+c2;
        getTime();


        len = (int)startTimeArrayList.size();

      /*  for (int k = 0; k <len; k++) {
          String selectAppString = databaseForSelectApp.databaseToString();
            if (startTimeArrayList.get(k)<=s && endTimeArrayList.get(k)>=s && selectAppString.length()>2) {

                Intent i = new Intent(this, LockScreenActivity.class);
                startActivity(i);
            }}*/
        capp();

         currentApp();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && (int)vector.size()<1)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Ops!");
            alertDialog.setMessage("Please go to settings and allow access");
            alertDialog.show();
        }

    }
       public void capp() {
           for (int i = 0; i < 5; i++) {
               ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);
               String packageName = am.getRunningTasks(1).get(0).topActivity.getPackageName();

               //Toast.makeText(getApplicationContext(), packageName, Toast.LENGTH_SHORT).show();
           }
       }
    // Call `launchTestService()` in the activity
    // to startup the service
    public void launchService() {
        // Construct our Intent specifying the Service
        Intent i = new Intent(this, LockService.class);
        // Add extras to the bundle
        i.putExtra("foo", "bar");
        // Start the service
        startService(i);
    }

    public void addItemsToList() {
        String dbString = dataBase.databaseToString();


        int first = 0;
        String addString;
        for (int I = 0; I < dbString.length(); I++) {

            if (dbString.charAt(I) == '|') {
                addString = dbString.substring(first, I);
                first = I + 1;
                workItem.add(addString);
                workItemAdapter.notifyDataSetChanged();
            }

        }


    }


    void splitString(String s) {
        int l = (int) s.length();
        int first = 0;
        for (int i = 0; i < l; i++) {
            if (s.charAt(i) == '|') {
                arr[indx++] = s.substring(first, i - 1);
                first = i + 1;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        test = true;
        String message = "";
        if (resultCode == 2) {
            if (requestCode == 2) {
                message = data.getStringExtra("message");
                String m = String.valueOf(message);


                int stringIndex = 0;
                boolean b1 = true, b2 = true;
                String workName = "", fromTime = "", toTime = "";


                for (int i = 0; i < message.length(); i++) {
                    char c = (char) message.charAt(i);
                    if (c == ':' && b1) {
                        workName = message.substring(stringIndex, i);
                        // workName+=":";
                        stringIndex = i + 1;
                        b1 = false;


                    } else if (c == ':' && b2) {
                        fromTime = message.substring(stringIndex, i);
                        //fromTime+=":";
                        stringIndex = i + 1;
                        b2 = false;
                    } else if (c == ':') {
                        toTime = message.substring(stringIndex, i);

                        break;
                    }
                }
                int sg = workName.length();
                for (int i =0;i<sg;i++)
                {
                    if(workName.charAt(i)=='|' || workName.charAt(i)=='('|| workName.charAt(i)==')'||workName.charAt(i)=='.' )
                    {
                        wrongCharacter();
                        test =false;
                        break;
                    }
                }
                if(test) {
                    String inpt = workName + ":" + fromTime + ":" + toTime + ":";
                    workName = workName + " (" + fromTime + " - " + toTime + ")";
                    workItem.add(workName);
                    workItemAdapter.notifyDataSetChanged();
                    inpt += "|";
                    // Toast.makeText(this,workName,Toast.LENGTH_SHORT).show();
                    String dbString = dataBase.databaseToString();


                    Works works = new Works(workName);
                    dataBase.addWorks(works);


                    // addItemsToList();
                }

            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
             //return true;
        }
       else if (id == R.id.selectApp) {
         /*   boolean flag=false;
            Calendar calendar = null;

            calendar = Calendar.getInstance();

            c1 = calendar.get(Calendar.HOUR_OF_DAY);
            c2 = calendar.get(Calendar.MINUTE);
            s = c1*60+c2;
            getTime();


            len = (int)startTimeArrayList.size();
            for (int k = 0; k <len; k++) {
                String selectAppString = databaseForSelectApp.databaseToString();
                if (startTimeArrayList.get(k)<=s && endTimeArrayList.get(k)>=s && selectAppString.length()>2) {

                    flag=true;
                }}
            if(flag)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Oh No!");
                alertDialog.setMessage("You cannot access your locked apps during lock time");
                alertDialog.show();
            }
            else {*/
                Toast toast = Toast.makeText(getApplicationContext(), "loading your apps", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                startSubActivity();
                // Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_SHORT).show();
            //}
        }
     //   else if(id==R.id.credit)
      //  {
      //      creditActivity();
        //}

        else if(id==R.id.helpMenu)
        {
          helpActivity();
        }
        else if(id==R.id.CreditMenu)
        {
            Intent creditIntent = new Intent(getApplicationContext(),CreditClass.class);
            startActivity(creditIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSubActivity() {

        Intent i = new Intent(this, AppData.class);
        startActivity(i);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listView1) {

            MenuInflater inflater = getMenuInflater();
           inflater.inflate(R.menu.delete_menu, menu);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_menu:
                boolean flag=false;
                Calendar calendar = null;

                calendar = Calendar.getInstance();

                c1 = calendar.get(Calendar.HOUR_OF_DAY);
                c2 = calendar.get(Calendar.MINUTE);
                s = c1*60+c2;
                getTime();


                len = (int)startTimeArrayList.size();
                for (int k = 0; k <len; k++) {
                    String selectAppString = databaseForSelectApp.databaseToString();
                    if (startTimeArrayList.get(k)<=s && endTimeArrayList.get(k)>=s) {

                        flag=true;
                    }}
                if(flag)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Oh No!");
                    alertDialog.setMessage("You cannot delete entries during your lock time");
                    alertDialog.show();
                }
                else{
                int indx = info.position;
                ListView l = (ListView) findViewById(R.id.listView1);
                String itemName = l.getItemAtPosition(indx).toString();
                String toDelete = "";
                for (int i = 0; i < itemName.length(); i++) {
                    if (itemName.charAt(i) == '(') {
                        toDelete = itemName.substring(0, i - 1);
                        break;
                    }
                }

                //  Works works = new Works(toDelete);
                dataBase.deleteWorks(itemName);
                Toast.makeText(this, "successfully removed " + toDelete , Toast.LENGTH_SHORT).show();
                workItemAdapter.clear();
                addItemsToList();

                return true;
                }

            default:
              return super.onContextItemSelected(item);
        }
    }

    public  void alarmStart()
    {
        Calendar calendar = null;
        calendar = Calendar.getInstance();

        //Toast.makeText(this,"alarm start" +calendar.get(calendar.HOUR_OF_DAY) + " " +
               // calendar.get(calendar.MINUTE),Toast.LENGTH_SHORT).show();
        Context context = getApplicationContext();



            calendar.setTimeInMillis(System.currentTimeMillis());

            calendar.set(calendar.HOUR_OF_DAY, calendar.get(calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.get(calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendar.get(calendar.SECOND));
            calendar.set(Calendar.MILLISECOND, calendar.get(calendar.MILLISECOND) + 100);

            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);



    }


    public void currentApp()
    {
        PackageManager packageManager= getApplicationContext().getPackageManager();


        int max = 5,count= 0;
        String appName = "";
        String topApp= "";
        String topPackageName ;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*20, time);
            // Sort the stats by the last time used
            if(stats != null) {
                SortedMap<Long,UsageStats> mySortedMap = new TreeMap<Long,UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);

                }
                if(mySortedMap != null && !mySortedMap.isEmpty()) {
                    topPackageName =  mySortedMap.get(mySortedMap.firstKey()).getPackageName();


                    int sg = mySortedMap.size();


                    for (Map.Entry<Long, UsageStats> entry : mySortedMap.entrySet()) {



                        String pkg = entry.getValue().getPackageName();


                        try {
                            appName =(String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(pkg, PackageManager.GET_META_DATA));

                        }

                        catch (Exception e){}
                        count++;
                        if(sg-count<=1){

                            topApp+= appName+" ";vector.add(appName);
                        }




                    }

                }}

        }
        else
        {
            ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

            ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);



            try{
                String foregroundTaskPackageName = foregroundTaskInfo .topActivity.getPackageName();
                PackageManager pm = this.getPackageManager();
                PackageInfo foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
                String foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();
                vector.add(foregroundTaskAppName);
            }
            catch (Exception e)
            {

            }


        }

    }


    public  void onPause()
    {
       super.onPause();

         Calendar calendar = null;
         calendar = Calendar.getInstance();

        /*
        Toast.makeText(this,"alarm start" +calendar.get(calendar.HOUR_OF_DAY) + " " +
                calendar.get(calendar.MINUTE),Toast.LENGTH_SHORT).show();*/
         Context context = getApplicationContext();


         calendar.setTimeInMillis(System.currentTimeMillis());
         calendar.set(calendar.HOUR_OF_DAY, calendar.get(calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,calendar.get(calendar.MINUTE));
         calendar.set(Calendar.SECOND,calendar.get(calendar.SECOND));
        calendar.set(Calendar.MILLISECOND,calendar.get(calendar.MILLISECOND)+100);

         alarmManager = (AlarmManager)  context.getSystemService(Context.ALARM_SERVICE);
         Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
         alarmIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);

         alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

     }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Calendar calendar = null;
        calendar = Calendar.getInstance();
/*
        Toast.makeText(this,"alarm start" +calendar.get(calendar.HOUR_OF_DAY) + " " +
                calendar.get(calendar.MINUTE),Toast.LENGTH_SHORT).show();*/
        Context context = getApplicationContext();


        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(calendar.HOUR_OF_DAY, calendar.get(calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.get(calendar.MINUTE));
       calendar.set(Calendar.SECOND,calendar.get(calendar.SECOND));
        calendar.set(Calendar.MILLISECOND,calendar.get(calendar.MILLISECOND)+100);

        alarmManager = (AlarmManager)  context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

    }
    int a1,a2,l;
    String ab1,ab2,abc1,abc2,ampm;
        public void  getTime()
    {
        String dbString = dataBase.databaseToString();


        int first = 0;
        String addString;
        for (int I = 0; I < dbString.length(); I++) {

            if (dbString.charAt(I) == '|') {
                addString = dbString.substring(first, I);

                int len2 = (int)addString.length();

                for(int j=0;j<len2;j++)
                {
                    if(addString.charAt(j)=='(')
                    {
                        a1 = j+1;
                    }
                    else if(addString.charAt(j)=='-')
                    {
                        a2 = j+2;
                    }
                }
                ab1 = addString.substring(a1,a2-3);
                ab2 = addString.substring(a2,len2);
                l = (int)ab1.length();



                for(int j =0;j<l;j++)
                {
                    if(ab1.charAt(j)=='.')
                    {
                        abc1 =ab1.substring(0, j);
                        abc2 = ab1.substring(j+1,l-3);
                        ampm = ab1.substring(l-2,l);
                    }
                }
                int a=0,b=0,c=0,d=0;

                try{
                    a =Integer.parseInt(abc1)*60;
                    b = Integer.parseInt(abc2);
                }
                catch (NumberFormatException e)
                {

                }
                int sum = a+b;



                if(ampm.equals("pm"))
                {
                    sum+=60*12;
                }
                int temp = sum;

                l = (int)ab2.length();
                for(int j =0;j<l;j++)
                {
                    if(ab2.charAt(j)=='.')
                    {
                        abc1 =ab2.substring(0,j);
                        abc2 = ab2.substring(j+1,l-4);
                        ampm = ab2.substring(l-3,l-1);

                    }
                }
                try{
                    c = Integer.parseInt(abc1)*60;
                    d =Integer.parseInt(abc2);
                }
                catch (NumberFormatException e)
                {

                }
                int sum2 =c+d;

                if (ampm.equals("pm"))
                {
                    sum2+=12*60;
                }
                if(temp>sum2)
                {
                    int st = temp;
                    int end = 23*60+60;
                    int st2 = 0;
                    int end2 = sum2;
                    startTimeArrayList.add(st);
                    endTimeArrayList.add(end);
                    startTimeArrayList.add(st2);
                    endTimeArrayList.add(end2);
                }
                else {
                    startTimeArrayList.add(temp);
                    endTimeArrayList.add(sum2);
                }

            }

        }

    }
    public void wrongCharacter()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("WorkName should not conatain | , () or .");
        alertDialog.show();
    }
    public void creditActivity()
    {
        Intent creditIntent = new Intent(this,CreditClass.class);
        startActivity(creditIntent);
    }
    public void helpActivity()
    {
        Intent helpIntent = new Intent(this,How_to_use.class);
        startActivity(helpIntent);
    }
}
