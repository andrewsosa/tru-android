package com.andrewsosa.android.tru;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.w3c.dom.Text;



public class MessageListFragment extends Fragment {


    protected Query queryRef;

    private BaseFragment.OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter mAdapter;
    private ImageView mEmptyView;


    public MessageListFragment() {
        // Required empty public constructor
    }


    public static MessageListFragment newInstance(Query query) {
        MessageListFragment fragment = new MessageListFragment();
        fragment.setQueryRef(query);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_message_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        mEmptyView = (ImageView) v.findViewById(R.id.emptyView);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Adapt our things
        mAdapter = new MessageFirebaseAdapter(MessageModel.class,
                R.layout.tile_message,
                MessageVH.class,
                queryRef);


        // Make recyclerview stuff work
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(getContext())
                .colorResId(R.color.dividerColor)
                .showLastDivider()
                .build());

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mEmptyView.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                mAdapter.getRef(viewHolder.getAdapterPosition()).removeValue();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Get RecyclerView item from the ViewHolder
                    View itemView = viewHolder.itemView;

                    Paint p = new Paint();
                    p.setColor(getResources().getColor(R.color.red_400));
                    if (dX > 0) {
                        /* Set your color for positive displacement */

                        // Draw Rect with varying right side, equal to displacement dX
                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                (float) itemView.getBottom(), p);
                    } else {
                        /* Set your color for negative displacement */

                        // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), p);
                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseFragment.OnFragmentInteractionListener) {
            mListener = (BaseFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void setQueryRef(Query queryRef) {
        this.queryRef = queryRef;
    }



    public static class MessageVH extends RecyclerView.ViewHolder {

        View tile;
        TextView message;
        TextView reveal;
        View overtile;

        public MessageVH(View itemView) {
            super(itemView);
            this.tile = itemView;
            this.message = (TextView) itemView.findViewById(R.id.tv_message);
            this.reveal = (TextView) itemView.findViewById(R.id.tv_tru);
        }
    }

    public class MessageFirebaseAdapter extends FirebaseRecyclerAdapter<MessageModel, MessageVH> {

        public MessageFirebaseAdapter(Class<MessageModel> modelClass, int modelLayout, Class<MessageVH> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(final MessageVH viewHolder, MessageModel model, int position) {
            viewHolder.message.setText(model.getContents());

            viewHolder.reveal.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "ProductSans.ttf"));
            viewHolder.reveal.setLetterSpacing((float)0.3);

            viewHolder.tile.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    View myView = viewHolder.reveal;

                    // get the center for the clipping circle
                    int cx = myView.getWidth() / 2;
                    int cy = myView.getHeight() / 2;

                    // get the final radius for the clipping circle
                    float finalRadius = (float) Math.hypot(cx, cy);

                    // create the animator for this view (the start radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

                    // make the view visible and start the animation
                    myView.setVisibility(View.VISIBLE);
                    anim.start();
                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {}

                        @Override
                        public void onAnimationEnd(Animator animation) {

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    final Firebase ref = mAdapter.getRef(viewHolder.getAdapterPosition());

                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            MessageModel mm = dataSnapshot.getValue(MessageModel.class);

                                            Firebase target = new Firebase(Tru.URL)
                                                    .child("users")
                                                    .child(mm.getAuthorKey())
                                                    .child("sent")
                                                    .child(ref.getKey());

                                            target.setValue(mm.incPoints());

                                            ref.removeValue();
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });



                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            viewHolder.reveal.setVisibility(View.INVISIBLE);
                                        }
                                    },500);

                                }
                            },2000);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {}
                        @Override
                        public void onAnimationRepeat(Animator animation) {}
                    });


                    return true;
                }
            });
        }
    }
}


