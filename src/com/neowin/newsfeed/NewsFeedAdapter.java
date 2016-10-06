package com.neowin.newsfeed;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neowin.utils.ImageLoader;

public class NewsFeedAdapter extends BaseAdapter {

    List<FeedItem> flist;
    Context context;
    LayoutInflater myInflater;
    public ImageLoader imageLoader;
    //Typeface face;
    
    public NewsFeedAdapter(List<FeedItem> list, Context ctxt)
    {
        context = ctxt;
        flist = list;
        myInflater = (LayoutInflater) ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create ImageLoader object to download and show image in list
        // Call ImageLoader constructor to initialize FileCache
        imageLoader = new ImageLoader(ctxt);
        //set typeface
        //face = Typeface.createFromAsset(context.getAssets(), "Cabin-Regular.ttf");
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return flist.size();
    }

    @Override
    public FeedItem getItem(int position) {
        // TODO Auto-generated method stub
        return flist.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView title;
        public TextView stamp;
        public ImageView image;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View vi=convertView;
        ViewHolder holder;
        String[] parts = flist.get(position).getPubDate().split(" ");
        String extra = parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3];
        
        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = myInflater.inflate(R.layout.feedlistitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.title = (TextView) vi.findViewById(R.id.titletext);
            holder.stamp=(TextView)vi.findViewById(R.id.stamptext);
            holder.image=(ImageView)vi.findViewById(R.id.imageView1);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();


        holder.title.setText(flist.get(position).getTitle());
        //holder.title.setTypeface(face);
        holder.stamp.setText(flist.get(position).getAuthor() + ", " + extra);
        Log.i("get View Ret value", vi.toString());
        //DisplayImage function from ImageLoader Class
        imageLoader.DisplayImage(flist.get(position).getImage(), holder.image);

        return vi;

    }

}
