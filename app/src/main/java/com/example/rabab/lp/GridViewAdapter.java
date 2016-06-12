package com.example.rabab.lp;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(holder.imageView);
        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}
