package com.andrewsosa.android.tru;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {


    Toolbar mToolbar;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    TextView mPoints;

    private static int NUM_PAGES = 2;
    private int truPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(mToolbar);
        //setTitle("Tru");

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mPoints = (TextView) findViewById(R.id.tv_points);


        mViewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);


        final Firebase ref = new Firebase(Tru.URL);

        TextView tru = (TextView) findViewById(R.id.tru);
        tru.setTypeface(Typeface.createFromAsset(getAssets(), "ProductSans.ttf"));
        tru.setLetterSpacing((float)0.1);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SendingActivity.class));
            }
        });



        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) fab.show();
                else fab.hide();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if(authData == null) {
                    startActivity(new Intent(MainActivity.this, TruActivity.class));
                    finish();
                }
            }
        });


        ref.child("feed").orderByChild("authorID").equalTo(ref.getAuth().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageModel mm = dataSnapshot.getValue(MessageModel.class);
                incrementPointsDisplay((int)mm.value);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                incrementPointsDisplay(1);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void incrementPointsDisplay(int val) {
        truPoints = truPoints + val;
        String x = ""+truPoints;
        mPoints.setText(x);
    }


    public static Fragment prepareFragment(int position) {

        Firebase ref = new Firebase(Tru.URL);

        Query q;

        if(position == 0) {
            //q = ref.child("inbox");
            q = ref.child("feed").orderByChild("order").limitToFirst(50);
            return MessageListFragment.newInstance(q);
        } else {
            //q = ref.child("users").child(ref.getAuth().getUid()).child("sent");
            q = ref.child("feed").orderByChild("authorID").equalTo(ref.getAuth().getUid());
            return FriendsListFragment.newInstance(q);
        }

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
            return position == 0 ? "what's Fire" : "you said";
        }
    }

    @Override
    public void onFragmentInteraction(int requestCode) {
        if(requestCode == BaseFragment.REQUEST_SEND)  startActivity(new Intent(MainActivity.this, SendingActivity.class));
        //if(requestCode == BaseFragment.REQUEST_FRIEND)
    }
}
