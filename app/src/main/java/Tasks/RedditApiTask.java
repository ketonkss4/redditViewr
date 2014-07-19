package Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.redditviewr.app.Host;
import com.google.android.gms.redditviewr.app.MainFragment;
import com.google.android.gms.redditviewr.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ListData.ListData;
import ListData.DetailsData;

/**
 * Created by Administrator on 7/6/2014.
 */
public class RedditApiTask extends AsyncTask<String, Integer, String>
{
    private ProgressDialog progDialog;
    private Context context;
    private MainFragment fragment;
    private static final String DEBUG_TAG = "RedditAPITask";

    /**
     * Construct a task
     * @param activity
     */


    public RedditApiTask(MainFragment fragment) {
        super();
        this.fragment = fragment;
        this.context = this.fragment.getActivity();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDialog = ProgressDialog.show(this.context, "Search", this.context.getResources().getString(R.string.looking_for_topics) , true);


    }

    @Override
    protected String doInBackground(String... params) {
        try {
            //Runs HTTPGet request in the background and returns response
            Log.v(DEBUG_TAG, "Background:" + Thread.currentThread().getName());
            String result = RedditApiHelper.downloadFromServer(params);
            return result;
        } catch (Exception e) {
            return new String();
        }
    }


    @Override
    protected void onPostExecute(String result)
    {

        ArrayList<ListData> topicdata = new ArrayList<ListData>();

        progDialog.dismiss();
        if (result.length() == 0) {
            this.fragment.alert ("Unable to find reddit data. Try again later.");
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
        fragment.setTopics(topicdata);

    }

}
