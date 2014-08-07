package com.google.android.gms.redditviewr.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;

import Listeners.OnSelectionListener;
import Tasks.RedditApiTask;

public class Host extends FragmentActivity implements OnSelectionListener {
    private RedditApiTask apiTask;
    private MainFragment mainFragment;
    private DetailsView  detailsFragment;
    private FragmentManager mFragmentManager;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        this.mainFragment = new MainFragment();

        if (!isInTwoPaneMode()) {
            mainFragment = new MainFragment();
            // add the MainFragment to the fragment_container
            mFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, mainFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else {
            //Save a reference to the details fragment
            detailsFragment = (DetailsView)getSupportFragmentManager().findFragmentById(R.id.detials_frag);
        }
        ViewServer.get(this).addWindow(this);

    }

    private boolean isInTwoPaneMode() {

        return findViewById(R.id.fragment_container) == null;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }
    //attempts to restart Image download when activity is resumed
    @Override
    protected void onResume() {
        ViewServer.get(this).setFocusedWindow(this);
        super.onResume();

    }

    @Override
    public void OnSelectionListener(String img, String comments) {
        Fragment detailsView = new DetailsView();
        Bundle args = new Bundle();
        args.putString("img",img);
        args.putString("comments",comments);
        detailsView.setArguments(args);

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, detailsView, TAG_FRAGMENT);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }

    @Override
    public void onBackPressed() {
           super.onBackPressed();
        mFragmentManager = getSupportFragmentManager();
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            mFragmentManager.popBackStack();
        } else {
            finish();
            Log.i("MainActivity", "nothing on backstack, calling super");
        }


    }
    @Override
    public void onDestroy() {
         super.onDestroy();
         ViewServer.get(this).removeWindow(this);
             }
}
