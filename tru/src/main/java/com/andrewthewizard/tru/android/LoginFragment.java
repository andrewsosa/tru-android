package com.andrewthewizard.tru.android;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class LoginFragment extends Fragment {


    private AuthFragment.AuthInteractionListener mListener;

    EditText email;
    EditText password;
    Button submit;

    // Required empty public constructor
    public LoginFragment() {}

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_login, container, false);

        email = (EditText) v.findViewById(R.id.et_email);
        password = (EditText) v.findViewById(R.id.et_password);
        submit = (Button) v.findViewById(R.id.btn_submit);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.login(email.getText().toString(), password.getText().toString(), null);
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AuthFragment.AuthInteractionListener) {
            mListener = (AuthFragment.AuthInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AuthInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
