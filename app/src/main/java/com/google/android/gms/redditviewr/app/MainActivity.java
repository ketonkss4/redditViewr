package com.google.android.gms.redditviewr.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import Adapters.RedditDataAdapter;
import ListData.ListData;
import Tasks.RedditApiTask;
import ImageLoader.RedditIconTask;



public class MainActivity extends Activity {

    private ArrayList<ListData> data;
    private ListView postList;
    private LayoutInflater layoutInflater;
    private Button goButton;
    private Spinner filters;
    private InputMethodManager inMangr;
    private RedditIconTask getImg;
    private RedditApiTask apiTask;
    private ProgressDialog progDialog;
    private String DEBUG_TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting views in layout
        this.inMangr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        this.postList = (ListView) findViewById(R.id.title_list);
        this.getImg = new RedditIconTask(this);
        this.layoutInflater = LayoutInflater.from(this);
        this.filters = (Spinner) this.findViewById(R.id.filter);
        this.goButton = (Button) this.findViewById(R.id.go_button);

        //Creates load more button
        Button loadMore = new Button(this);
        loadMore.setText("Load More");
        postList.addFooterView(loadMore);

        //Passes spinner selection to load different subreddits
        this.goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inMangr.hideSoftInputFromInputMethod(goButton.getWindowToken(), 0);
                apiTask = new RedditApiTask(MainActivity.this);
                try {
                    TextView textView = (TextView) filters.getSelectedView();
                    String filter = textView.getText().toString();
                    apiTask.execute(filter);

                } catch (Exception e) {
                    e.printStackTrace();
                    apiTask.cancel(true);
                    alert(getResources().getString(R.string.looking_for_topics));
                }
            }
        });

        //sends intent with image link and comment link data as an extra
        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ListData link = data.get(position);
                String permalink = link.getComments();
                String largeImg = link.getImageUrl();
                Intent intent = new Intent(MainActivity.this, DetailsView.class);
                intent.putExtra("img", largeImg);
                intent.putExtra("link", permalink);
                startActivity(intent);


            }
        });
        //Loads more reddit posts on click
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) filters.getSelectedView();
                String filter = textView.getText().toString();

            }
        });


//            restore fetched data on orientation change.
        @SuppressWarnings("deprecation")
        final Object[] data = (Object[]) getLastNonConfigurationInstance();
        if (data != null) {
            this.data = (ArrayList<ListData>) data[0];
            this.getImg = (RedditIconTask) data[1];
            postList.setAdapter(new RedditDataAdapter(this, this.getImg, this.layoutInflater, this.data));

        }

    }

    public void alert(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
//view holder sets view resource data from API task
    public static class MyViewHolder {
        public TextView listName, authorName, redditScore, postTime;
        public Button goButton;
        public ImageView thumbnail;
        public ListData data;
    }
//sets all the data in the layout view
    public void setTopics(ArrayList<ListData> data) {
        this.data = data;
        this.postList.setAdapter(new RedditDataAdapter(this, this.getImg, this.layoutInflater, this.data));

    }
//attempts to restart Image download when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        getImg.stopImage(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }
// stops asynchronous background tasks from slowing down the second view
    @Override
    protected void onPause() {
        super.onPause();
        apiTask.cancel(true);
        getImg.stopImage(true);

    }


}
