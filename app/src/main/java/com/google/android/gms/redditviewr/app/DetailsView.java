package com.google.android.gms.redditviewr.app;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import Adapters.RedditCommentAdapter;
import ListData.DetailsData;
import Tasks.RedditDetailsTask;

/**
 * Created by Administrator on 7/8/2014.
 */
public class DetailsView extends Fragment {


    private ArrayList<DetailsData> data;
    private ListView commentList;
    private NetworkImageView imageView;
    final private String DEBUG_TAG = "Details View";
    private Bundle args;
    private LayoutInflater layoutInflater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         args = getArguments();

        RedditDetailsTask detailsTask = new RedditDetailsTask(null,DetailsView.this);

        View v = inflater.inflate(R.layout.details_view, container,
                false);
        this.commentList = (ListView) v.findViewById(R.id.commentList);
        this.imageView =(NetworkImageView) v.findViewById(R.id.imageLarge);
        if (args != null){
            String img = args.getString("img");
            String comments = args.getString("comments");

            detailsTask.execute(comments);
            setDrawable(img);
        }






        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }



    public static class DetailsViewHolder {
        public TextView comment, authorName, redditScore, postTime;

        public DetailsData data;
    }

    public void setTopics(ArrayList<DetailsData> data) {
        this.data = data;
        this.commentList.setAdapter(new RedditCommentAdapter(this, this.layoutInflater, this.data));

    }



    public void setDrawable(String url){

        if(url != null) {
            imageView.setDefaultImageResId(R.drawable.filler_icon);
            imageView.setImageUrl(url, ImgController.getInstance().getImageLoader());

        }else{
           imageView.setImageResource(R.drawable.filler_icon);

       }
    }

    public void alert(String msg) {
        Toast.makeText(this.getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.

                return true;
                default:
                return super.onOptionsItemSelected(item);
            case R.id.action_refresh:
                return true;

        }
    }




}
