package com.br.gridviewmovie;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class GridViewAdapter extends ArrayAdapter<GridItem> {
    private static final String TAG = GridViewAdapter.class.getSimpleName();
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();


    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<GridItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    // Updates grid data and refresh grid items.

    public void setGridData(ArrayList<GridItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
           // holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);//2good
           // holder.averrateTextView = (TextView) row.findViewById(R.id.grid_item_averrate);
           // holder.overviewTextView = (TextView) row.findViewById(R.id.grid_item_overview);
           // holder.releasedateTextView = (TextView) row.findViewById(R.id.grid_item_releasedate);

            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        GridItem item = mGridData.get(position);
       // holder.titleTextView.setText(item.getTitle());//2good
        //donn't want show rate and releasedate data on main UI
       // holder.averrateTextView.setText(item.getAverrate());
        //holder.overviewTextView.setText(item.getOverview());
        //holder.releasedateTextView.setText(item.getReleasedate());


       //Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
        Picasso.with(mContext)
                .load(mContext.getResources().getString(R.string.MOVIE_PATH)+item.getImage())
                .placeholder(R.mipmap.placeholder)
                .into(holder.imageView);
        return row;
    }

    static class ViewHolder {
       // TextView titleTextView;//2good
       // TextView averrateTextView;
       // TextView overviewTextView;
       // TextView releasedateTextView;

        ImageView imageView;
    }
}