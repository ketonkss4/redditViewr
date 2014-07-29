package com.google.android.gms.redditviewr.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.Window;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ListData.DetailsData;
import ListData.ListData;
import Tasks.RedditApiHelper;


/**
 * Created by Administrator on 7/14/2014.
 */
public class SplashScreen extends ActionBarActivity {
    final private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        AsyncTask<String, Integer, String > asyncTask = new AsyncTask<String, Integer, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    //Runs HTTPGet request in the background and returns response
                    String result  = RedditApiHelper.downloadFromServer("hot");
                    return result;
                } catch (Exception e) {
                    return new String();
                }
            }


            @Override
            protected void onPostExecute(String result)
            {

                ArrayList<ListData> topicdata = new ArrayList<ListData>();

                if (result.length() == 0) {
                    return;
                }
                //Parse-Logic for Reddit JSON document
                try {
                    JSONObject response = new JSONObject(result);
                    JSONObject data = response.getJSONObject("data");
                    JSONArray hotTopics = data.getJSONArray("children");
                    for(int i=0; i<25; i++) {
                        JSONObject topic = hotTopics.getJSONObject(i).getJSONObject("data");
                        ListData item = new ListData();
                        DetailsData img = new DetailsData();
                        item.setTitle(topic.getString("title"));
                        item.setAuthor(topic.getString("author"));
                        item.setImageUrl(topic.getString("url"));
                        img.setLargeImg(topic.getString("url"));
                        item.setPostTime(topic.getLong("created_utc"));
                        item.setrScore(topic.getString("score"));
                        item.setComments(topic.getString("permalink"));




                        topicdata.add(item);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Sets data for adapter in main activity.
                MainFragment main = new MainFragment();
               main.setTopics(topicdata);

            }
        };
//



        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, Host.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
