/**
 * @author Kevin Moturi
 * This class represents the main fragment in which the user selects posts to view and filters
 * @version 1.0
 */

package com.google.android.gms.redditviewr.app;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.io.Serializable;
import java.util.ArrayList;
import Adapters.RedditDataAdapter;
import ListData.ListData;
import Listeners.OnSelectionListener;
import Tasks.RedditApiTask;

public class MainFragment extends android.support.v4.app.Fragment {

    private ArrayList<ListData> data;
    private GridView postList;
    private LayoutInflater layoutInflater;
    private Button goButton;
    private Spinner filters;
    private RedditApiTask apiTask;
    private ProgressDialog progDialog;
    private String DEBUG_TAG = "MainFragment";
    private int mCurCheckPosition;


    private Host host;
    private OnSelectionListener selectionListener;
    private static final String SAVED_STATE="save_state";
    private Serializable mObject;
    private Bundle savedState = null;
    private boolean createdStateInDestroyView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedState != null) {
            mObject = savedState.getSerializable(SAVED_STATE);
        }
        //Setting views in layout
        View v = inflater.inflate(R.layout.activity_main, container,
                false);
        this.postList = (GridView) v.findViewById(R.id.mainGrid);
        this.filters = (Spinner) v.findViewById(R.id.filter);
        this.goButton = (Button) v.findViewById(R.id.go_button);
        this.host = new Host();

        //Creates load more button
//        Button loadMore = new Button(getActivity());
//        loadMore.setText("Load More");
//        postList.add(loadMore);

        //Passes spinner selection to load different subreddits
        this.goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                apiTask = new RedditApiTask(MainFragment.this);
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



                Fragment newFragment = new DetailsView();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                selectionListener.OnSelectionListener(largeImg, permalink);


            }
        });
        //Loads more reddit posts on click
//        loadMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TextView textView = (TextView) filters.getSelectedView();
//                String filter = textView.getText().toString();
//
//            }
//        });





        return v;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

    }

    public void alert(String msg) {
        Toast.makeText(this.getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            selectionListener = (OnSelectionListener)getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSelectionListener");
        }
    }

    //view holder sets view resource data from API task
    public static class MyViewHolder {
        public TextView listName, authorName, redditScore, postTime;
        public Button goButton;
        public NetworkImageView thumbnail;
        public ListData data;
    }
//sets all the data in the layout view
    public void setTopics(ArrayList<ListData> data) {
        this.data = data;
        this.postList.setAdapter(new RedditDataAdapter(this, this.layoutInflater, this.data));

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:
                apiTask = new RedditApiTask(MainFragment.this);
                try {
                    TextView textView = (TextView) filters.getSelectedView();
                    String filter = textView.getText().toString();
                    apiTask.execute(filter);

                } catch (Exception e) {
                    e.printStackTrace();
                    apiTask.cancel(true);
                    alert(getResources().getString(R.string.looking_for_topics));
                }

                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(getActivity(), "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }

        return true;
    }
    private Bundle saveState(){
        Bundle state = new Bundle();
        
        state.putSerializable(SAVED_STATE, mObject);
        return state;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (mObject == null) {
            outState.putBundle(SAVED_STATE, savedState);
        } else {
            outState.putBundle(SAVED_STATE, createdStateInDestroyView ? savedState : saveState());
        }
        createdStateInDestroyView = false;
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        savedState = saveState();
        createdStateInDestroyView = true;
        mObject = null;
    }
    @Override
    public void onPause() {
        super.onPause();

    }
}
