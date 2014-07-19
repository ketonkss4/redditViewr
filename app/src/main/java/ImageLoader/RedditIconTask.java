package ImageLoader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Administrator on 7/6/2014.
 */
public class RedditIconTask {
    private static final String debugTag = "ImageWorker";

    private HashMap<String, Drawable> imageCache;
    private static Drawable DEFAULT_ICON = null;
    private BaseAdapter adapter;
    private Boolean cancelled = false;


    public RedditIconTask (Context context)
    {
        //sets faux-image cache in form of HashMap stores drawables in memory
        imageCache = new HashMap<String, Drawable>();
    }

    public Drawable loadImage (BaseAdapter adapt, ImageView view)
    {
        //checks if image is in memory and makes a call to Reddit Icon task if imaage must be downloaded again.
        this.adapter = adapt;
        String url = (String) view.getTag();
        if (imageCache.containsKey(url))
        {
            return imageCache.get(url);
        }
        else {
            new ImageTask().execute(url);
            return DEFAULT_ICON;
        }
    }
    //receives cancel async task request from MainFragment on Pause
    public void stopImage (Boolean stop){
            cancelled=stop;
        Log.v(debugTag,"Stop AsyncTask");

    }
    //sets state of cancelled variable
    public boolean cancelled (){
        return cancelled;

    }
    public class ImageTask extends AsyncTask<String, Void, Drawable>
    {
        private String s_url;

        //accepts array of urls to down load
        @Override
        protected Drawable doInBackground(String... params) {
            //checks the cancelled variable to determine whether to continue AsyncTask
           if (cancelled()){
               cancel(cancelled);
            //checks urls for drawable types
           }else {
               s_url = params[0];
               InputStream inStream;
               Drawable picture = null;
               try {
                   Log.v(debugTag, "Fetching: " + s_url);


                   URL url = new URL(s_url);
                   inStream = url.openStream();
                   picture = Drawable.createFromStream(inStream, "src");

               } catch (MalformedURLException e) {
                   Log.v(debugTag, "Malformed: " + e.getMessage());
               } catch (IOException e) {
                   Log.d(debugTag, "I/O : " + e.getMessage());

               }
               return picture;
           }
            return null;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            super.onPostExecute(result);
            synchronized (this) {
                //adds resulting drawable to memory
                imageCache.put(s_url, result);


            }
            //updates adapter view
            adapter.notifyDataSetChanged();
        }

    }
}
