package com.shiku.blue.restrictme;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by BLUE on 4/8/2016.
 */
public class LockService extends IntentService {

   ArrayList<Integer>startTimeArrayList=  new ArrayList<>();
    ArrayList<Integer>endTimeArrayList = new ArrayList<>();

     public static Vector<String>v = new Vector<>();

      Vector<String>vector = new Vector<String>();
     Vector<String>selectedApps = new Vector<>();
    MyModifiedDataBase dataBase;
    DatabaseForSelectApp databaseForSelectApp;
      String topApp = "ok";
    AlarmManager alarmManager;
    PendingIntent alarmIntent;
   String activityname = "",activityName = "";
    Calendar c = null;
    int c1 =0;
    int c2 = 0,s=0;
    public LockService() {
        super("lock_service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        dataBase = new MyModifiedDataBase(this, null, null, 1);
        setAlarm();
        try {
            wait(1000);
        } catch (Exception e) {

        }
        databaseForSelectApp = new DatabaseForSelectApp(this, null, null, 1);
        Intent dialogIntent = new Intent(this, LockScreenActivity.class);
        //dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //this.startActivity(dialogIntent);

        ;
        // Toast.makeText(getApplicationContext(),"lockService",Toast.LENGTH_SHORT).show();
        // Toast.makeText(getApplicationContext(), "lockService" + calendar.get(Calendar.HOUR_OF_DAY) + " " + calendar.get(calendar.MINUTE), Toast.LENGTH_SHORT).show();

        Calendar calendar = null;

        calendar = Calendar.getInstance();

       c1 = calendar.get(Calendar.HOUR_OF_DAY)*60;
        c2 = calendar.get(Calendar.MINUTE);
        s = c1+c2;
         getTime();


        int len = (int)startTimeArrayList.size();

       for (int k = 0; k <len; k++) {

        if (startTimeArrayList.get(k)<=s && endTimeArrayList.get(k)>=s ) {

            if (selectedApps.isEmpty()) {
                    String blocedApps = databaseForSelectApp.databaseToString();
                    int initial = 0;
                    for (int i = 0; i < (int) blocedApps.length(); i++) {
                        if (blocedApps.charAt(i) == '|') {
                            String subString = blocedApps.substring(initial, i);
                            initial = i + 1;
                            selectedApps.add(subString);
                            // Toast.makeText(getApplicationContext(),subString,Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                currentApp();
                for (int i = 0; i < (int) selectedApps.size(); i++) {
                    if (vector.contains(selectedApps.get(i))) {
                        dialogIntent = new Intent(getApplicationContext(), LockScreenActivity.class);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        this.startActivity(dialogIntent);

                    }

                }
                if (vector.contains("Settings")) {
                    dialogIntent = new Intent(getApplicationContext(), LockScreenActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(dialogIntent);
                }

            }


      }
       kitkat();
  }
    public void kitkat()
    {
        PackageManager packageManager= getApplicationContext().getPackageManager();
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        String pkg = services.get(0).topActivity.getPackageName().toString();
            try
            {
                 String appName =(String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(pkg, PackageManager.GET_META_DATA));
                vector.add(appName);
            }
            catch (Exception e){}
        }


    public void currentApp()
    {
        PackageManager packageManager= getApplicationContext().getPackageManager();

       // Intent dialogIntent = new Intent(this, LockScreenActivity.class);

        //dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //this.startActivity(dialogIntent);


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
                    // Toast.makeText(getApplicationContext(),topApp,Toast.LENGTH_SHORT).show();
                    // Log.e("TopPackage Name", topPackageName);

                    for (Map.Entry<Long, UsageStats> entry : mySortedMap.entrySet()) {
                        //System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
                        // topApp+=entry.getValue().getPackageName()+ " ";


                        String pkg = entry.getValue().getPackageName();


                        try {
                            appName =(String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(pkg, PackageManager.GET_META_DATA));

                        }

                        catch (Exception e){}
                        count++;
                    if(sg-count<=1){
                       /* if (vector.contains("CppDroid")) {
                            dialogIntent = new Intent(getApplicationContext(), LockScreenActivity.class);
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            this.startActivity(dialogIntent);

                        }*/
                        topApp+= appName+" ";vector.add(appName);
                      }
                           // Toast.makeText(getApplicationContext(),appName,Toast.LENGTH_SHORT).show();






                    }
                  // Toast.makeText(getApplicationContext(),topApp,Toast.LENGTH_SHORT).show();

                   /*if(vector.contains("cppDroid")) {
                        dialogIntent = new Intent(this, LockScreenActivity.class);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        this.startActivity(dialogIntent);
                    }*/

            }}//dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           // this.startActivity(dialogIntent);
          //  Toast.makeText(getApplicationContext(),topApp,Toast.LENGTH_SHORT).show();

      }
       else
        {
            ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
           // The first in the list of RunningTasks is always the foreground task.
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
            for(int j = 0;j<10000;j++)
            {
                Calendar calendar = null;
                calendar = Calendar.getInstance();

                // Toast.makeText(this,"alarm start" +calendar.get(calendar.HOUR_OF_DAY) + " " +
                // calendar.get(calendar.MINUTE),Toast.LENGTH_SHORT).show();
                Context context = getApplicationContext();


                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(calendar.HOUR_OF_DAY, calendar.get(calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE,calendar.get(calendar.MINUTE));
                calendar.set(Calendar.SECOND, calendar.get(calendar.SECOND)*j*10);
                calendar.set(Calendar.MILLISECOND,calendar.get(calendar.MILLISECOND)+200);


                alarmManager = (AlarmManager)  context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

            }

            }

            }





    @Override
    public void onDestroy() {
        super.onDestroy();

        Calendar calendar = null;
        calendar = Calendar.getInstance();

       // Toast.makeText(this,"alarm start" +calendar.get(calendar.HOUR_OF_DAY) + " " +
               // calendar.get(calendar.MINUTE),Toast.LENGTH_SHORT).show();
        Context context = getApplicationContext();


        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(calendar.HOUR_OF_DAY, calendar.get(calendar.HOUR_OF_DAY));
       calendar.set(Calendar.MINUTE,calendar.get(calendar.MINUTE));
       calendar.set(Calendar.SECOND, calendar.get(calendar.SECOND));
       calendar.set(Calendar.MILLISECOND,calendar.get(calendar.MILLISECOND)+200);


        alarmManager = (AlarmManager)  context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

    }
    public void setAlarm() {
        Calendar calendar = null;
        calendar = Calendar.getInstance();

        // Toast.makeText(this,"alarm start" +calendar.get(calendar.HOUR_OF_DAY) + " " +
        //  calendar.get(calendar.MINUTE),Toast.LENGTH_SHORT).show();
        Context context = getApplicationContext();

        for (int i = 0; i < 10; i++) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(calendar.HOUR_OF_DAY, calendar.get(calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.get(calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendar.get(calendar.SECOND)+i*2);
            calendar.set(Calendar.MILLISECOND, calendar.get(calendar.MILLISECOND) + 300);
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

        }
    }
     int a1,a2,l;
     String ab1,ab2,abc1,abc2,ampm;

    void  getTime()
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


    }




/* String dbString = dataBase.databaseToString();

        // dbString = "sdfsdfsdfsad";
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
               // startTimeVector.add(sum);
                l = (int)ab2.length();
                for(int j =0;j<0;j++)
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
                sum =c+d;

                if (ampm.equals("pm"))
                {
                    sum+=12*60;
                }
               // endTimeVector.add(sum);

            }
            first=I+1;

        }

*/