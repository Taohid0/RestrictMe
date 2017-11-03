package com.shiku.blue.restrictme;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Taohid.blue.restrictme.R;
import com.shiku.blue.restrictme.AppData;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter{
  //  String [] result;
    ArrayList<String>result = new ArrayList<>();
    ArrayList<Drawable>imageId = new ArrayList<>();
    Context context;
    // Drawable [] imageId;
     static LayoutInflater inflater=null;
    public CustomAdapter(AppData appData, ArrayList<String> prgmNameList, ArrayList<Drawable>prgmImages) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=appData;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
        CheckBox checkBoxClass;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.program_list, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textViewProgram);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.tv.setText(result.get(position));
        holder.img.setImageDrawable(imageId.get(position));
       // holder.checkBoxClass = (CheckBox) rowView.findViewById(R.id.checkbox);
        //holder.checkBoxClass.setChecked(false);

        return rowView;
    }

} 