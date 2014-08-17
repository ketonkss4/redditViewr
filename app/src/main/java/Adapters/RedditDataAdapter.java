package Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.redditviewr.app.MainFragment;
import com.google.android.gms.redditviewr.app.R;
import com.google.android.gms.redditviewr.app.ImgController;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ListData.ListData;

/**
 * Created by Administrator on 7/6/2014.
 */
public class RedditDataAdapter extends BaseAdapter  {
    private static final String DEBUG_TAG = "LOGTAG!!!";
    private MainFragment fragment;
    private LayoutInflater layoutInflater;
    private ArrayList<ListData> topics;
    private Context mContext;


    public RedditDataAdapter(MainFragment a, LayoutInflater l, ArrayList<ListData
            > data ) {
        this.fragment = a;
        this.layoutInflater = l;
        this.topics = data;
        this.mContext = a.getActivity();
    }


    @Override
    public int getCount() {
        return this.topics.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MainFragment.MyViewHolder holder;
        if (convertView == null) {
            // sets view layout resources
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.row, parent, false);
            holder = new MainFragment.MyViewHolder();
            setLayoutRes(convertView, holder);
            //Allows view to be recycled
            convertView.setTag(holder);
            Log.v(DEBUG_TAG, "Data Set in Reddit Adapter");
        } else {
            holder = (MainFragment.MyViewHolder) convertView.getTag();
        }
        ListData data = topics.get(position);
        try {
            //converts timestamp into a date format
            String postTime = convertTime(data);

            //sets data resources in the holder
            setData(holder, data, postTime);

        } catch (Exception e) {
            e.printStackTrace();
            Log.v(DEBUG_TAG, "Cell Not Created Due to: ", e);
        }
        //sets drawable in adapter view
        if (data.getImageUrl() != null) {
//
            try {
                setDrawableView(holder, data);
                return convertView;
            } catch (Exception e) {
                e.printStackTrace();
                Log.v(DEBUG_TAG, "no image: ", e);
                holder.thumbnail.setImageResource(R.drawable.filler_icon);
            }
        }else {
            return null;
        }
        return convertView;
    }

    private void setDrawableView(MainFragment.MyViewHolder holder, ListData data) {
        holder.thumbnail.setTag(data.getImageUrl());

        holder.thumbnail.setDefaultImageResId(R.drawable.filler_icon);
        holder.thumbnail.setImageUrl(data.getImageUrl(), ImgController.getInstance().getImageLoader());
    }

    private void setData(MainFragment.MyViewHolder holder, ListData data, String postTime) {
        holder.data = data;
        holder.listName.setText(data.getTitle());
        holder.authorName.setText(data.getAuthor());
        holder.postTime.setText(postTime);
        holder.redditScore.setText(data.getrScore());
    }

    private String convertTime(ListData data) {
        long lg = Long.valueOf(data.getPostTime()) * 1000;
        Date date = new Date(lg);
        return new SimpleDateFormat("MM dd, yyyy hh:mma").format(date);
    }

    private void setLayoutRes(View convertView, MainFragment.MyViewHolder holder) {
        holder.listName = (TextView) convertView.findViewById(R.id.title);
        holder.authorName = (TextView) convertView.findViewById(R.id.author);
        holder.thumbnail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        holder.goButton = (Button) convertView.findViewById(R.id.go_button);
        holder.postTime = (TextView) convertView.findViewById(R.id.post_date);
        holder.redditScore = (TextView) convertView.findViewById(R.id.score);
    }
}


