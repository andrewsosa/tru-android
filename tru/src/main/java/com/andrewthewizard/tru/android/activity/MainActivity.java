package com.andrewthewizard.tru.android.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.andrewthewizard.tru.android.R;
import com.andrewthewizard.tru.android.fragment.BaseFragment;
//import com.firebase.client.AuthData;
//import com.firebase.client.Firebase;
//import com.firebase.client.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {


    Toolbar mToolbar;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    TextView mPoints;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    private static int NUM_PAGES = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        // Auth
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new AuthListener();

        // UI
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mPoints = (TextView) findViewById(R.id.tv_points);

        mViewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setPageMargin(dpToPx(32));

        TextView tru = (TextView) findViewById(R.id.tru);
        if (tru != null) {
            tru.setTypeface(Typeface.createFromAsset(getAssets(), "MontserratBold.ttf"));
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, SendingActivity.class));
                }
            });

            // Unauth
            fab.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    return true;
                }
            });
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(fab != null) {
                    if(position == 0) fab.show();
                    else fab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}

        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    public static Fragment prepareFragment(int position) {

//        Firebase ref = new Firebase(Tru.FIREBASE_URL);
//        Query q;
//
//        if(position == 0) {
//            //q = ref.child("inbox");
//            q = ref.child("feed").orderByChild("order").limitToFirst(50);
//            return FeedListFragment.newInstance(q);
//        } else {
//            //q = ref.child("users").child(ref.getAuth().getUid()).child("sent");
//            q = ref.child("feed").orderByChild("authorID").equalTo(ref.getAuth().getUid());
//            return FriendsListFragment.newInstance(q);
//            //return FeedListFragment.newInstance(q);
//        }

        return new Fragment();

    }

    public static class CustomPagerAdapter extends FragmentPagerAdapter {
        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            return MainActivity.prepareFragment(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "what's Fire" : "the fam";
        }
    }

    public class AuthListener implements FirebaseAuth.AuthStateListener {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d("Tru", "User is logged in");
            } else {
                // User is signed out
                Log.d("Tru", "User is logged out");
                startActivity(new Intent(MainActivity.this, TruActivity.class));
                finish();
            }
        }
    }

    @Override
    public void onFragmentInteraction(int requestCode) {
        if(requestCode == BaseFragment.REQUEST_SEND)  startActivity(new Intent(MainActivity.this, SendingActivity.class));
    }
}
