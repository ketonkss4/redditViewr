package Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.redditviewr.app.DetailsView;
import com.google.android.gms.redditviewr.app.Host;
import com.google.android.gms.redditviewr.app.MainFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ListData.DetailsData;

/**
* Created by Administrator on 7/8/2014.
*/
public class RedditDetailsTask extends AsyncTask<String, Integer, String> {
    private ProgressDialog progDialog;
    private Context context;
    private DetailsView detailsView;
    private MainFragment mFrag;
    private static final String DEBUG_TAG = "RedditDetailsTask";
    private Host host;


    /**
     * Construct a task
     * @param activity
     */


    public RedditDetailsTask(MainFragment mainFragment, DetailsView detailsView) {
        super();
        this.mFrag = mainFragment;
        this.detailsView = detailsView;
        this.context = this.detailsView.getActivity();
        this.host = new Host();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDialog = ProgressDialog.show(this.context, "Search", "getting comments" , true);

        progDialog.setCancelable(true);

    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Log.v(DEBUG_TAG, "Background:" + Thread.currentThread().getName());
            String result = RedditCommentHelper.downloadFromServer(params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new String();
        }
    }

    @Override
    protected void onPostExecute(String result)
    {

        ArrayList<DetailsData> detaildata = new ArrayList<DetailsData>();

        progDialog.dismiss();
        if (result.length() == 0) {
            this.detailsView.alert("Unable to find reddit data. Try again later.");
            return;
        }

        //Parse-Logic for Reddit JSON document, takes in array NOT object like subreddit JSON
        try {
            JSONArray jSON = new JSONArray(result);

                JSONObject response = jSON.getJSONObject(1);

                JSONObject data = response.getJSONObject("data");
                JSONArray hotTopics = data.getJSONArray("children");

                for (int j = 0; j < hotTopics.length(); j++) {
                    JSONObject topic = hotTopics.getJSONObject(j).getJSONObject("data");
                    DetailsData item = new DetailsData();
                    item.setAuthor(topic.getString("author"));

                    item.setPostTime(topic.getLong("created_utc"));
                    item.setScore(topic.getString("score"));
                    item.setComment(topic.getString("body"));


                    detaildata.add(item);

                }


        } catch (JSONException e) {
            e.printStackTrace();
        }
            //sets data in details view
              this.detailsView.setTopics(detaildata);

    }
}
