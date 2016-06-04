package com.andrewthewizard.tru.android;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.annotations.Nullable;

import java.util.Map;
import java.util.regex.Pattern;

public class AuthActivity extends AppCompatActivity implements AuthFragment.AuthInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        CustomViewPager mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tablayout);

        mViewPager.setAdapter(new AuthActivity.CustomPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View view = getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(AuthActivity.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }

    public boolean validate(String s) {
        return s.trim().length() > 0;
    }

    public boolean validateUsername(String s) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        boolean hasSpecialChar = p.matcher(s).find();
        return validate(s) && !hasSpecialChar;
    }


    public void login(final String email, final String pass, @Nullable final String user) {
        final Firebase ref = new Firebase(Tru.URL);

        if(!validate(email) || !validate(pass)) {
            return;
        }

        ref.authWithPassword(email, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {

                if(user != null) {
                    ref.child("users").child(authData.getUid())
                            .setValue(new UserModel(authData.getUid(), email, user));
                }

                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Toast.makeText(AuthActivity.this, "Not tru, yo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signup(final String email, final String pass, final String user) {

        Firebase ref = new Firebase(Tru.URL);

        if(!validate(email) || !validate(pass) || !validateUsername(user)) {
            return;
        }

        ref.createUser(email, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                login(email, pass, user);
            }

            @Override
            public void onError(FirebaseError firebaseError) {

                if(firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                    login(email, pass, null);
                } else {
                    Toast.makeText(AuthActivity.this, "Not tru, yo", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static Fragment prepareFragment(int position) {
        return (position == 0 ? LoginFragment.newInstance() : SignUpFragment.newInstance());
    }

    public static class CustomPagerAdapter extends FragmentPagerAdapter {
        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            return AuthActivity.prepareFragment(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "Log In" : "Sign Up";
        }
    }


}
