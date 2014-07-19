package Tasks;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 7/8/2014.
 */
public class RedditCommentHelper {
    private static final String REDDIT_URL =  "http://www.reddit.com/";
    private static final int HTTP_STATUS_OK = 200;
    private static byte[] buff = new byte[1024];
    private static final String LOGTAG = "RedditCommentHelper";
    private String commentLink;

    public static class ApiException extends Exception {
        private static final long serialVersionUID = 2L;

        public ApiException (String msg)
        {
            super (msg);
        }

        public ApiException (String msg, Throwable thr)
        {
            super (msg, thr);
        }
    }

    public String getCommentLink() {
        return commentLink;
    }

    public void setCommentLink(String commentLink) {
        this.commentLink = commentLink;
    }

    /**
     * download comments
     * @return Array of json strings returned by the API.
     * @throws ApiException
     */
    protected static synchronized String downloadFromServer (String... params)
            throws ApiException
    {
        String retvalue = null;
        String permalink = params[0];
        Log.v(LOGTAG,"past this point");




        String url = REDDIT_URL +permalink+".json?sort=new&count=25";

        Log.v(LOGTAG, "Fetching " + url);

        // create an http client and a request object.
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        try {

            // execute the request
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HTTP_STATUS_OK) {
                // handle error here
                throw new ApiException("Invalid response from reddit.com" +
                        status.toString());
            }

            // process the content.
            HttpEntity entity = response.getEntity();
            InputStream inStream = entity.getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();

            int readCount;
            while ((readCount = inStream.read(buff)) != -1) {
                content.write(buff, 0, readCount);
            }
            retvalue = new String (content.toByteArray());

        } catch (Exception e) {
            throw new ApiException("Problem connecting to the server " +
                    e.getMessage(), e);
        }

        return retvalue;
    }
}
