package ImageLoader;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.redditviewr.app.DetailsView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 7/10/2014.
 */
public class LargeIconTask {
    private static final String debugTag = "LargeImageWorker";
    private DetailsView activity;
    private static Drawable LARGE_IMG = null;


    public LargeIconTask(DetailsView activity) {
        this.activity = activity;

    }
//executes download of large image passing in the url from the details view
    public Drawable execImage (String url){
        new ImageTask().execute(url);
        Log.v(debugTag, "We atleast got here " );

        return LARGE_IMG;
    }

    public class ImageTask extends AsyncTask<String, Void, Drawable>
    {
        private String s_url;

        @Override
            protected Drawable doInBackground(String... params) {
            //accepts array of urls to down load
            s_url = params[0];
            InputStream inStream;
            Drawable picture=null;

            //checks urls for drawable types
            try {
                Log.v(debugTag, "Fetching: " + s_url);


                URL url = new URL(s_url);
                inStream = url.openStream();
                picture= Drawable.createFromStream(inStream, "src");

            } catch (MalformedURLException e) {
                Log.v(debugTag, "Malformed: " + e.getMessage());
            } catch (IOException e)
            {
                Log.d(debugTag, "I/O : " + e.getMessage());

            }
            return picture;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            super.onPostExecute(result);

              activity.setImage(result);
        }


    }
}

