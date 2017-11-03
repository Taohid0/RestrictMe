package com.shiku.blue.restrictme;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.Taohid.blue.restrictme.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

/**
 * Created by BLUE on 4/3/2016.
 */

public class LockScreenActivity extends Activity {
    Vector<Integer> startTimeVector;
    Vector<Integer> endTimeVector;
    ArrayList<Integer> arrayList = new ArrayList<>();
    AlarmManager alarmManager;
    PendingIntent alarmIntent;
    Vector<String> vector = new Vector<>();

    DatabaseForSelectApp databaseForSelectApp;
    Vector<String> selectedApps = new Vector<>();
    String temp = "";
    ArrayList<Integer> startTimeArrayList = new ArrayList<>();
    ArrayList<Integer> endTimeArrayList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        startTimeVector = new Vector<>();
        endTimeVector = new Vector<>();

        dataBase = new MyModifiedDataBase(this, null, null, 1);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_screen_layout);




        alarmItself();

        databaseForSelectApp = new DatabaseForSelectApp(this, null, null, 1);

        String blockedApp = databaseForSelectApp.databaseToString();

        if (selectedApps.isEmpty()) {
            temp = databaseForSelectApp.databaseToString();
            int initial = 0;
            for (int i = 0; i < (int) blockedApp.length(); i++) {
                if (blockedApp.charAt(i) == '|') {
                    String subString = blockedApp.substring(initial, i);
                    initial = i + 1;
                    selectedApps.add(subString);
                    temp += subString;
                    //Toast.makeText(getApplicationContext(),"."+subString+".",Toast.LENGTH_SHORT).show();
                }
            }
        }
        //currentApp();// Toast.makeText(getApplicationContext(),blockedApp,Toast.LENGTH_SHORT).show();
        for (int i = 0; i < vector.size(); i++) {
            // Toast.makeText(getApplicationContext(),">"+vector.get(i)+">",Toast.LENGTH_SHORT).show();

        }
        /*try{
            wait(10000);
        }
        catch (Exception e){

        }*/
        String sh = "";

        for (int j = 0; j < (int) vector.size(); j++) {
            sh += vector.get(j);

        }
        //  Toast.makeText(getApplicationContext(),sh,Toast.LENGTH_SHORT).show();

       /* Context context = getApplicationContext();
        Intent myIntent=new Intent(context,LockService.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(myIntent);*/

        //continuousAlarm();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(10000 * 6);
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    ;
                }
            }).start();
        } else
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    ;
                }
            }).start();
        //getTime();
        //getTime2();
    }

  /*  public  void selectedAppData()
    {
        temp = databaseForSelectApp.databaseToString();
    }*/
   /* public void currentApp() {
        PackageManager packageManager= getApplicationContext().getPackageManager();

        Intent dialogIntent = new Intent(this, LockScreenActivity.class);

        //dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //this.startActivity(dialogIntent);

       int sg=0;
        int max = 5,count= 0;
        String appName = "";
        String topApp= "";
        String topPackageName ;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*2, time);
            // Sort the stats by the last time used
            if(stats != null) {
                SortedMap<Long,UsageStats> mySortedMap = new TreeMap<Long,UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);

                }
                if(mySortedMap != null && !mySortedMap.isEmpty()) {
                    topPackageName =  mySortedMap.get(mySortedMap.firstKey()).getPackageName();

                   sg = mySortedMap.size();
                    // Toast.makeText(getApplicationContext(),topApp,Toast.LENGTH_SHORT).show();
                    // Log.e("TopPackage Name", topPackageName);

                    for (Map.Entry<Long, UsageStats> entry : mySortedMap.entrySet()) {
                        //System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
                        // topApp+=entry.getValue().getPackageName()+ " ";


                        String pkg = entry.getValue().getPackageName();

                        try {
                            appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(pkg, PackageManager.GET_META_DATA));
                           // Toast.makeText(getApplicationContext(),appName,Toast.LENGTH_SHORT).show();
                          // vector.add(appName);
                        }
                        catch (Exception e) {
                        }
                        count++;
                     if (sg - count <= 3) {
                            topApp += appName + " ";
                         vector.add(appName);
                        }
                        // Toast.makeText(getApplicationContext(),appName,Toast.LENGTH_SHORT).show();


                    }
                }//Toast.makeText(getApplicationContext(),topApp+sg,Toast.LENGTH_SHORT).show();
                 }


        }
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        Calendar calendar = null;
        calendar = Calendar.getInstance();
/*
        Toast.makeText(this,"alarm start" +calendar.get(calendar.HOUR_OF_DAY) + " " +
                calendar.get(calendar.MINUTE),Toast.LENGTH_SHORT).show();*/
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

    public void alarmItself() {
        Calendar calendar = null;
        calendar = Calendar.getInstance();
/*
        Toast.makeText(this,"alarm start" +calendar.get(calendar.HOUR_OF_DAY) + " " +
                calendar.get(calendar.MINUTE),Toast.LENGTH_SHORT).show();*/
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
   /* @Override
    public void onBackPressed() {


    }*/

    @Override
    protected void onDestroy() {
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
        calendar.set(Calendar.SECOND, calendar.get(calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.get(calendar.MILLISECOND) + 100);

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

    }

    public void continuousAlarm() {
        for (int i = 1; i <= 10; i++) {
            Calendar calendar = null;
            calendar = Calendar.getInstance();
/*
        Toast.makeText(this,"alarm start" +calendar.get(calendar.HOUR_OF_DAY) + " " +
                calendar.get(calendar.MINUTE),Toast.LENGTH_SHORT).show();*/
            Context context = getApplicationContext();


            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(calendar.HOUR_OF_DAY, calendar.get(calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.get(calendar.MINUTE) + i);
            calendar.set(Calendar.SECOND, calendar.get(calendar.SECOND));
            calendar.set(Calendar.MILLISECOND, calendar.get(calendar.MILLISECOND) + 100);

            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

        }
    }

    MyModifiedDataBase dataBase;

    int strt = 0, end = 0;
    String timeString = "", startTime1 = "", endTime1 = "", ampm1 = "";

    public static Vector<String> v = new Vector<>();

   /* void getTime() {
        startTimeVector = new Vector<>();
        endTimeVector = new Vector<>();
        String dbString = dataBase.databaseToString();


        int first = 0;
        String addString;
        for (int I = 0; I < dbString.length(); I++) {

            if (dbString.charAt(I) == '|') {
                addString = dbString.substring(first, I);

                int len2 = (int) addString.length();
                for (int i = 0; i < len2; i++) {
                    if (addString.charAt(i) == '(') {
                        strt = i + 1;
                    } else if (addString.charAt(i) == ')') {
                        end = i;
                    }
                }

                timeString = addString.substring(strt, end);

                int l = timeString.length();
                for (int j = 0; j < l; j++) {
                    if (timeString.charAt(j) == '.') {
                        strt = j;

                    } else if (timeString.charAt(j) == '-') {
                        end = j - 1;
                        break;
                    }
                }
                startTime1 = timeString.substring(0, strt);
                endTime1 = timeString.substring(strt + 1, end - 3);
                ampm1 = timeString.substring(end - 2, end);


                int a = 0, b = 0, s = 0;
                try {
                    a = Integer.parseInt(startTime1);
                    b = Integer.parseInt(endTime1);
                } catch (NumberFormatException e) {

                }
                s = a * 60 + b;

                if (ampm1.equals("pm")) {
                    s += 12 * 60;
                }
                // startTimeVector.add(10);
                arrayList.add(s);
                Toast.makeText(getApplication(), "." + startTime1 + " " + endTime1 + " " + ampm1 + ".", Toast.LENGTH_SHORT).show();
                first = I + 1;
            }
        }
        for (int i = 0; i < startTimeVector.size(); i++) {
            Toast.makeText(getApplicationContext(), startTimeVector.get(i), Toast.LENGTH_SHORT).show();
        }

    }*/

    int a1, a2, l;
    String ab1, ab2, abc1, abc2, ampm;
/*
    void getTime2() {
        String dbString = dataBase.databaseToString();


        int first = 0;
        String addString;
        for (int I = 0; I < dbString.length(); I++) {

            if (dbString.charAt(I) == '|') {
                addString = dbString.substring(first, I);

                int len2 = (int) addString.length();

                for (int j = 0; j < len2; j++) {
                    if (addString.charAt(j) == '(') {
                        a1 = j + 1;
                    } else if (addString.charAt(j) == '-') {
                        a2 = j + 2;
                    }
                }
                ab1 = addString.substring(a1, a2 - 3);
                ab2 = addString.substring(a2, len2);
                l = (int) ab1.length();


                for (int j = 0; j < l; j++) {
                    if (ab1.charAt(j) == '.') {
                        abc1 = ab1.substring(0, j);
                        abc2 = ab1.substring(j + 1, l - 3);
                        ampm = ab1.substring(l - 2, l);
                    }
                }
                int a = 0, b = 0, c = 0, d = 0;

                try {
                    a = Integer.parseInt(abc1);
                    b = Integer.parseInt(abc2);
                } catch (NumberFormatException e) {

                }
                int sum = a*60 + b;


                if (ampm.equals("pm")) {
                    sum += 60 * 12;
                }
               // Toast.makeText(getApplicationContext(),"."+ampm+".",Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),a+ " "+b+ " "+sum,Toast.LENGTH_SHORT).show();
                int temp = sum;

                l = (int) ab2.length();
                for (int j = 0; j < l; j++) {
                    if (ab2.charAt(j) == '.') {
                        abc1 = ab2.substring(0, j);
                        abc2 = ab2.substring(j + 1, l - 4);
                        ampm = ab2.substring(l - 3, l - 1);

                    }
                }
                try {
                    c = Integer.parseInt(abc1) * 60;
                    d = Integer.parseInt(abc2);
                } catch (NumberFormatException e) {

                }
                int sum2 = c + d;

                if (ampm.equals("pm")) {
                    sum2 += 12 * 60;
                }
                if (temp > sum2) {
                    int st = temp;
                    int end = 23 * 60 + 60;
                    int st2 = 0;
                    int end2 = sum2;
                    startTimeArrayList.add(st);
                    endTimeArrayList.add(end);
                    startTimeArrayList.add(st2);
                    endTimeArrayList.add(end2);
                } else {
                    startTimeArrayList.add(temp);
                    endTimeArrayList.add(sum2);
                }

            }

        }
        for(int i = 0;i<startTimeArrayList.size();i++)
        {
            Calendar calendar = null;

            calendar = Calendar.getInstance();

           int c1 = calendar.get(Calendar.HOUR_OF_DAY)*60;
           int c2 = calendar.get(Calendar.MINUTE);
           int s = c1+c2;
            Toast.makeText(getApplicationContext(),startTimeArrayList.get(i) +  " "+
                        endTimeArrayList.get(i)+" "+s,Toast.LENGTH_SHORT).show();
        }

    }*/
    @Override
    public void onBackPressed() {
    }
}