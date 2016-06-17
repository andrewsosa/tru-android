package com.andrewthewizard.tru.android.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by andrewsosa on 2/6/16.
 */
public class BaseFragment extends Fragment {

    public final static int REQUEST_SEND = 0;
    public final static int REQUEST_FRIEND = 1;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int requestCode);
    }
}
