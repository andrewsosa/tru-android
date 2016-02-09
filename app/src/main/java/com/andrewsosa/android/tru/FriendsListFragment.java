package com.andrewsosa.android.tru;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsListFragment extends Fragment {


    protected Query queryRef;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter mAdapter;


    public FriendsListFragment() {
        // Required empty public constructor
    }


    public static FriendsListFragment newInstance(Query query) {
        FriendsListFragment fragment = new FriendsListFragment();
        fragment.setQueryRef(query);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_friends_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        

        // Adapt our things
        mAdapter = new FriendFirebaseAdapter(MessageModel.class,
                R.layout.tile_message_author,
                MessageVH.class,
                queryRef);


        // Make recyclerview stuff work
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(getContext())
                .colorResId(R.color.dividerColor)
                .build());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void setQueryRef(Query queryRef) {
        this.queryRef = queryRef;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class MessageVH extends RecyclerView.ViewHolder {

        TextView username;
        TextView react;

        public MessageVH(View itemView) {
            super(itemView);
            this.username = (TextView) itemView.findViewById(R.id.tv_message);
            this.react = (TextView) itemView.findViewById(R.id.tv_response);
        }
    }

    public class FriendFirebaseAdapter extends FirebaseRecyclerAdapter<MessageModel, MessageVH> {

        public FriendFirebaseAdapter(Class<MessageModel> modelClass, int modelLayout, Class<MessageVH> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(MessageVH viewHolder, MessageModel model, int position) {
            viewHolder.username.setText(model.getContents());
            String x = "" + model.getValue() + " tru points";
            viewHolder.react.setText(x);
        }
    }
}
