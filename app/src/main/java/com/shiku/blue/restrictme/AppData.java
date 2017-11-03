package com.shiku.blue.restrictme;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.Taohid.blue.restrictme.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.internal.request.StringParcel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Created by BLUE on 3/30/2016.
 */
public class AppData extends Activity {

    public AppData() {

    }

    ArrayList<Integer>startTimeArrayList=  new ArrayList<>();
    ArrayList<Integer>endTimeArrayList = new ArrayList<>();
    ArrayList <Drawable> imageID = new ArrayList<>();
    ArrayList<String>prgmNameList=new ArrayList();

  //  public static Drawable []imageID= new Drawable[150];
   // public static String [] prgmNameList= new String[150];

    int counter=0;
    int indx = 0;
    ArrayList<String > appRow = new ArrayList<String  >();
    ArrayAdapter<String  > appRowAdapter;
    Context context;
   public static ListView lv;

    public String lockedApp[];
     int selectedPosition;
    DatabaseForSelectApp databaseForSelectApp;
    MyModifiedDataBase dataBase;
    int a[];
    boolean test =true;
    Vector<Integer>v = new Vector<>();
    Vector<String>VString = new Vector<>();
    String colorCodes[] = {

  /* "#B2EBF2","#80DEEA","#4DD0E1","#26C6DA","#00BCD4","#00ACC1","#0097A7","#00838F",
            "#00695C","#00796B","#00897B","#009688","#26A69A","#4DB6AC","#80CBC4","#B2DFDB",

    "#C8E6C9","#A5D6A7","#81C784","#66BB6A","#4CAF50","#43A047","#388E3C",

   "#689F38","#7CB342","#8BC34A","#9CCC65","#AED581","#C5E1A5","#DCEDC8",
    "#F0F4C3","#E6EE9C","#DCE775","#D4E157","#CDDC39","#C0CA33",
   "#FDD835","#FFEB3B","#FFEE58","#FFF176","#FFF59D","#FFF9C4",*/

            "#05051F","#05051F","#081E9A","#081D97","#071C93","#071B8E","#071A89","#071A84","#071A84","#061777","#051673","#05156E",
            "#05146A","#051365","#05135D","#051259","#051152","#05104D",
            "#050F47","#050E40","#040C3A","#040B36"



    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.installed_app_list);

        dataBase = new MyModifiedDataBase(this, null, null, 1);
        databaseForSelectApp= new DatabaseForSelectApp(this, null, null, 1);
        context = this;
       // lv = (ListView) findViewById(R.id.installed_app_list);
        lv=(ListView)findViewById(R.id.installedAppList) ;
        databaseForSelectApp = new DatabaseForSelectApp(this, null, null, 1);

        String resTrictedAppList   = "";
        resTrictedAppList = databaseForSelectApp.databaseToString();
         int tempIndx = 0;
        for(int i = 0;i<resTrictedAppList.length();i++)
        {
            if(resTrictedAppList.charAt(i)=='|')
            {
                String str = resTrictedAppList.substring(tempIndx,i);
                tempIndx = i+1;
                //
                // Toast.makeText(this,"."+str+".",Toast.LENGTH_SHORT).show();
                VString.add(str);
            }
        }


        appRowAdapter = new ArrayAdapter<String>(this, R.layout.installed_app_row, appRow) {


            public View getView(int position, View convertView, ViewGroup parent) {

                TextView textView = (TextView) super.getView(position, convertView, parent);

                try {

                    textView.setBackgroundColor(Color.parseColor(colorCodes[position % 1]));

                } catch (Exception e) {
                    textView.setBackgroundColor(Color.RED);
                }
               // if (v.contains(position) && test) {
                  //  textView.setBackgroundColor(Color.parseColor("#616161"));
               // }
                if (VString.contains(prgmNameList.get(position)) && test)
                {
                    textView.setBackgroundColor(Color.parseColor("#616161"));
                }
                return textView;
            }
        };



      //  lv.setAdapter(appRowAdapter);
     CustomAdapter customAdapter = new CustomAdapter(this, prgmNameList,imageID)
     {

         public View getView(int position, View convertView, ViewGroup parent) {


             Holder holder=new Holder();
             View rowView;
             rowView = inflater.inflate(R.layout.program_list, null);
             holder.tv=(TextView) rowView.findViewById(R.id.textViewProgram);
             holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
             holder.tv.setText(result.get(position));
             holder.img.setImageDrawable(imageId.get(position));
           //  holder.checkBoxClass = (CheckBox) rowView.findViewById(R.id.checkbox);
             //holder.checkBoxClass.setChecked(false);



             try {
                 rowView.setBackgroundColor(Color.parseColor("#05051F"));


             } catch (Exception e) {
                 rowView.setBackgroundColor(Color.BLACK);
             }

             if ( VString.contains(prgmNameList.get(position)) && test)
             {
                 //Toast.makeText(getApplicationContext(),"p"+VString.get(position)+"p",Toast.LENGTH_SHORT).show();
               rowView.setBackgroundColor(Color.parseColor("#616161"));



             }

             return rowView;
         }

     };
        lv.setAdapter(customAdapter);
        getInstalledApps();


        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String selectedFromList =  lv.getItemAtPosition(position)+toString();

                if(VString.contains(prgmNameList.get(position)))
                {
                    Calendar calendar = null;

                    boolean flag =false;
                    calendar = Calendar.getInstance();

                    int c1 = calendar.get(Calendar.HOUR_OF_DAY);
                    int c2 = calendar.get(Calendar.MINUTE);
                    int s = c1*60+c2;
                    getTime();


                   int  len = (int)startTimeArrayList.size();
                    for (int k = 0; k <len; k++) {
                       // String selectAppString = databaseForSelectApp.databaseToString();
                        if (startTimeArrayList.get(k)<=s && endTimeArrayList.get(k)>=s) {

                            flag=true;
                        }}

                    if(flag)
                    {

                       AlertDialog alertDialog = new AlertDialog.Builder(AppData.this).create();
                        alertDialog.setTitle("Oh No!");
                        alertDialog.setMessage("You cannot remove blocked apps during lock time");
                        alertDialog.show();
                    }
                    else {
                        databaseForSelectApp.deleteWorks(prgmNameList.get(position));
                        VString.remove(prgmNameList.get(position));

                        view.setBackgroundColor(Color.parseColor("#05051F"));

                        Toast.makeText(getApplicationContext(), "removed " + prgmNameList.get(position) + " from restricted list", Toast.LENGTH_SHORT).show();
                    }

                }
             else {
                view.setBackgroundColor(Color.parseColor("#616161"));

               // Toast.makeText(getApplicationContext(), selectedFromList, Toast.LENGTH_SHORT).show();

                SelectAppClass selectAppClass = new SelectAppClass(prgmNameList.get(position));
                databaseForSelectApp.addWorks(selectAppClass);
                VString.add(prgmNameList.get(position));
               // v.add(position);
                   // int in = Integer.parseInt(selectedFromList);
               Toast.makeText(getApplicationContext(),"added "+prgmNameList.get(position)+" to restricted list",Toast.LENGTH_SHORT).show();
                }
            }


        });

    }

 ArrayList<String>sortedAppName = new ArrayList<String >();
    public void getInstalledApps()
    {
        List<PackageInfo> PackList = getPackageManager().getInstalledPackages(0);
        for (int i=0; i < PackList.size(); i++)
        {
            PackageInfo PackInfo = PackList.get(i);
            if(((PackInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) != true)
            {
                String AppName = PackInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Drawable icon = PackInfo.applicationInfo.loadIcon(getPackageManager());
               // appName[i] = AppName;
                 sortedAppName.add(AppName);
                 prgmNameList.add(AppName);
                 imageID.add(icon);
                counter++;
              //  appRow.add(AppName);
               //appRowAdapter.notifyDataSetChanged();

                //Toast.makeText(this,AppName,Toast.LENGTH_SHORT).show();
            }indx = i;
        }
              for(int i=0;i<prgmNameList.size();i++)
              {
                  for(int j=0;j<prgmNameList.size();j++)
                  {
                      if(prgmNameList.get(i).compareTo(prgmNameList.get(j))<0)
                      {
                          String temp = prgmNameList.get(i);
                          Drawable temp2=imageID.get(i);

                          prgmNameList.set(i,prgmNameList.get(j));
                          prgmNameList.set(j,temp);

                          imageID.set(i,imageID.get(j));
                          imageID.set(j,temp2);

                      }
                  }
              }
        Collections.sort(sortedAppName);

        for(String counter: sortedAppName){
                 appRow.add(counter);
            appRowAdapter.notifyDataSetChanged();

        }


 }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_menu) {

        }


        return super.onOptionsItemSelected(item);
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
  }