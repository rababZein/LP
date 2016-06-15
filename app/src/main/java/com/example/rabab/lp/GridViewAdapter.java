package com.example.rabab.lp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rabab on 12/06/16.
 */
public class GridViewAdapter  extends ArrayAdapter<People> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<People> objects = new ArrayList<People>();
    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<People> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.objects = objects;
    }

    /**
     * Updates grid data and refresh grid items.
     * @param objects
     */
    public void setGridData(ArrayList<People> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        People item = objects.get(position);
        if(item.getName() !=null)
            holder.titleTextView.setText(Html.fromHtml(item.getName()));

       // Picasso.with(context).load(item.getImage()).into(holder.imageView);
       // byte[] data = Base64.decode(item.getPassword(), Base64.DEFAULT);
       // String text = new String(data, "UTF-8");
        //Picasso.with(context).load(String.valueOf(data)).into(holder.imageView);
        Log.e("GV--getPassword", String.valueOf(item.getPassword().length()));
        byte[] decodedString = Base64.decode(item.getPassword(), Base64.URL_SAFE);
        Log.e("bytes decode length ", String.valueOf(decodedString.length));

        Log.e("GV--decodedString",decodedString.toString());

        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        if (decodedByte.toString() != null)
//            Log.e("GV--decodedByte",decodedByte.toString());
//        else
//            Log.e("GV--decodedByte", "is null");


       holder.imageView.setImageBitmap(decodedByte);
//
//        holder.imageView.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, holder.imageView.getWidth(),
//                holder.imageView.getHeight(), false));


        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}
