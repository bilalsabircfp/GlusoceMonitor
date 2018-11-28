package com.bilal.dzone.medical_glucose.Admin.All_Posts;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bilal.dzone.medical_glucose.R;
import com.bilal.dzone.medical_glucose.URL.Url;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by DzonE on 09-Oct-17.
 */

public class All_Posts_Adapter extends BaseAdapter {

    Activity con;
    String[] desc,
            title
            ,image
            ,date;

    All_Posts_Adapter(Activity contex,String[] desc,String[] title,
                       String[] date, String[] image){

        con             = contex;
        this.desc       = desc;
        this.title    = title;
        this.date   = date;
        this.image   = image;

    }

    @Override
    public int getCount()
    {
        return desc.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class Viewholder{

        TextView tv_desc,tv_title,tv_date;
        ImageView pic;
        Button delete, edit;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        Viewholder viewholder;

        LayoutInflater inflater = con.getLayoutInflater();

        if (convertView == null){

            viewholder      = new Viewholder();
            convertView     = inflater.inflate(R.layout.admin_news_feed_items,null);

            viewholder.tv_desc          = (TextView) convertView.findViewById(R.id.desc);
            viewholder.tv_title       = (TextView) convertView.findViewById(R.id.title);
            viewholder.tv_date       = (TextView) convertView.findViewById(R.id.date);
            viewholder.pic                   = (ImageView) convertView.findViewById(R.id.image);
            viewholder.delete = (Button) convertView.findViewById(R.id.delete);
            viewholder.edit = (Button) convertView.findViewById(R.id.edit);

            convertView.setTag(viewholder);

        }
        else {

            viewholder = (Viewholder) convertView.getTag();
        }


        Picasso.with( con )
                .load( "http://www." + image[position] )
                .error( R.drawable.error).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .placeholder( R.drawable.progress_animation )
                .into( viewholder.pic );

        viewholder.tv_title.setText(title[position]);
        viewholder.tv_desc.setText(desc[position]);
        viewholder.tv_date.setText(date[position]);

        viewholder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView)parent).performItemClick(v,position,1);
            }
        });


        viewholder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView)parent).performItemClick(v,position,2);
            }
        });



        return convertView;
    }
}

