package com.google.android.gms.redditviewr.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.RedditCommentAdapter;
import ImageLoader.LargeIconTask;
import ImageLoader.RedditIconTask;
import ListData.DetailsData;
import Tasks.RedditDetailsTask;

/**
 * Created by Administrator on 7/8/2014.
 */
public class DetailsView extends ActionBarActivity {


    private ArrayList<DetailsData> data;
    private ListView commentList;
    private LayoutInflater layoutInflater;
    private InputMethodManager inMangr;
    private LargeIconTask getImg;
    private RedditIconTask stopImg;
    private ImageView imageView;
    private Drawable draw;
    final private String DEBUG_TAG = "Details View";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view);
        this.inMangr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        this.commentList = (ListView) findViewById(R.id.commentList);
        this.layoutInflater = LayoutInflater.from(this);
        this.imageView =(ImageView)findViewById(R.id.imageLarge);
        this.getImg = new LargeIconTask(this);


        ActionBar actionBar = getActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

             RedditDetailsTask detailsTask = new RedditDetailsTask(DetailsView.this);
            Intent in = getIntent();
            Bundle bun = in.getExtras();


        if(bun!=null) {
            String j = (String) bun.get("link");
            final String url = (String)bun.get("img");
            detailsTask.execute(j);
            setDrawable(url);





        }else{
            Toast.makeText(getApplicationContext(),"comments link was null", Toast.LENGTH_LONG).show();

        }



    }

    public static class DetailsViewHolder {
        public TextView comment, authorName, redditScore, postTime;

        public DetailsData data;
    }
    public void alert (String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void setTopics(ArrayList<DetailsData> data) {
        this.data = data;
        this.commentList.setAdapter(new RedditCommentAdapter(this, this.layoutInflater, this.data));

    }



    public void setDrawable(String url){
        getImg.execImage(url);
    }
    public void setImage (Drawable draw){
        if (draw != null) {
            imageView.setImageDrawable(draw);


        } else {
            imageView.setImageResource(R.drawable.filler_icon);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
                default:
                return super.onOptionsItemSelected(item);
            case R.id.action_refresh:
                return true;

        }
    }

}
