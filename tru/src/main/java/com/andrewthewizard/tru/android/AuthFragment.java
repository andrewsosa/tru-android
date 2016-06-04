package com.andrewthewizard.tru.android;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by andrewsosa on 2/11/16.
 */
abstract public class AuthFragment extends Fragment {

    public interface AuthInteractionListener {
        void login(String email, String pwd, @Nullable String user);
        void signup(String email, String pwd, String user);
    }

}
