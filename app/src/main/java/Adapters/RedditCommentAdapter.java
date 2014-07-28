package Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.redditviewr.app.DetailsView;
import com.google.android.gms.redditviewr.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ListData.DetailsData;

/**
* Created by Administrator on 7/8/2014.
*/
public class RedditCommentAdapter extends BaseAdapter  {
    private static final String DEBUG_TAG = "LOGTAG!!!";
    private DetailsView dfragment;
    private LayoutInflater layoutInflater;
    private ArrayList<DetailsData> topics;
    private Context mContext;



    public RedditCommentAdapter(DetailsView a,  LayoutInflater l, ArrayList<DetailsData
            > data ) {
        this.dfragment = a;
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
        DetailsView.DetailsViewHolder holder;
        if (convertView == null){
    // sets view layout resources
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();

            convertView = inflater.inflate(R.layout.comment_row, parent,false);
            holder = new DetailsView.DetailsViewHolder();
            holder.comment = (TextView)convertView.findViewById(R.id.title);
            holder.authorName = (TextView)convertView.findViewById(R.id.author);
            holder.postTime = (TextView)convertView.findViewById(R.id.post_date);
            holder.redditScore = (TextView)convertView.findViewById(R.id.score);
    //Allows view to be recycled
            convertView.setTag(holder);


        }else {
            holder = (DetailsView.DetailsViewHolder) convertView.getTag();
        }


        DetailsData data = topics.get(position);

        try {
        //converts timestamp into a date format
            long lg = Long.valueOf(data.getPostTime())*1000;
            Date date = new  Date(lg);
            String postTime = new SimpleDateFormat("MM dd, yyyy hh:mma").format(date);
        //sets data resources in the holder
            holder.data = data;
            holder.comment.setText(data.getComment());
            holder.authorName.setText(data.getAuthor());
            holder.postTime.setText(postTime);
            holder.redditScore.setText(data.getScore());


        }catch (Exception e){
            e.printStackTrace();
            Log.v(DEBUG_TAG,"Cell Not Created Due to: ",e);
        }






        return convertView;
    }


}
