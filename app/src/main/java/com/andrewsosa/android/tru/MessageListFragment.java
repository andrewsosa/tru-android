package com.andrewsosa.android.tru;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.HashMap;
import java.util.Map;


public class MessageListFragment extends Fragment {


    protected Query queryRef;

    private BaseFragment.OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter mAdapter;
    private ImageView mEmptyView;

    // View types
    public static final int DEFAULT = 0;
    public static final int AUTHOR = 1;
    public static final int HIDDEN = 2;
    public static final int AGREED = 3;


    public MessageListFragment() {
        // Required empty public constructor
    }


    public static MessageListFragment newInstance(Query query) {
        MessageListFragment fragment = new MessageListFragment();
        fragment.setQueryRef(query);
        return fragment;
    }

    public void setQueryRef(Query queryRef) {
        this.queryRef = queryRef;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
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
        /*mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(getContext())
                .colorResId(R.color.dividerColor)
                .showLastDivider()
                .build());
        */

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mEmptyView.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });


        ItemTouchHelper helper = new ItemTouchHelper(new MessageTouchCallbacks(0, ItemTouchHelper.RIGHT));
        helper.attachToRecyclerView(mRecyclerView);
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


    public static class MessageVH extends RecyclerView.ViewHolder {

        View tile;
        TextView messageText;
        TextView revealText;
        TextView reactionText;
        TextView authorText;
        ImageView icon;

        public MessageVH(View itemView) {
            super(itemView);
            this.tile = itemView;
            this.messageText = (TextView) itemView.findViewById(R.id.tv_message);
            this.revealText = (TextView) itemView.findViewById(R.id.tv_tru);
            this.reactionText = (TextView) itemView.findViewById(R.id.tv_response);
            this.authorText = (TextView) itemView.findViewById(R.id.tv_author);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }

    public class MessageFirebaseAdapter extends FirebaseRecyclerAdapter<MessageModel, MessageVH> {

        public MessageFirebaseAdapter(Class<MessageModel> modelClass, int modelLayout, Class<MessageVH> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected MessageModel parseSnapshot(DataSnapshot snapshot) {
          return ModelTools.fullMessageParse(snapshot);
        }

        @Override
        public int getItemViewType(int position) {
            String userID = new Firebase(Tru.URL).getAuth().getUid();
            MessageModel mm = this.getItem(position);

            if(mm.getAuthorID().equals(userID)) return AUTHOR;
            else if(mm.ignoredBy(userID)) return HIDDEN;
            else if(mm.agreedBy(userID)) return AGREED;
            else return DEFAULT;

        }

        @Override
        public MessageVH onCreateViewHolder(ViewGroup parent, int viewType) {

            // create a new view
            int layout = (viewType == HIDDEN ? R.layout.tile_message_hidden : R.layout.tile_message);

            View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

            // optionally set the view's size, margins, paddings and layout parameters
            return new MessageVH(v);
        }


        @Override
        protected void populateViewHolder(final MessageVH viewHolder, final MessageModel model, int position) {

            final Firebase ref = mAdapter.getRef(viewHolder.getAdapterPosition());

            viewHolder.messageText.setText(model.getContents());
            viewHolder.messageText.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "ProductSans.ttf"));
            viewHolder.revealText.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "ProductSans.ttf"));

            int[] colors = getContext().getResources().getIntArray(R.array.tile_colors);
            viewHolder.messageText.setBackgroundColor(colors[(Math.abs((int)model.getTimestamp()))%4]);

            String x = "" + model.getValue() + " tru";



            //if (this.getItemViewType(position) == HIDDEN) {
                //viewHolder.tile.setVisibility(View.GONE);
                //viewHolder.messageText.setText(model.getContents());
            //}

            if (this.getItemViewType(position) == AUTHOR) {
                //viewHolder.reactionText.setVisibility(View.VISIBLE);
                viewHolder.reactionText.setText(x);
                viewHolder.icon.setImageResource(R.drawable.ic_subdirectory_arrow_right_black_24dp);
                viewHolder.authorText.setText("you posted this");
                //viewHolder.messageText.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));


            }

            if (this.getItemViewType(position) == AGREED) {
                viewHolder.reactionText.setText(x);
                //viewHolder.icon.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                //viewHolder.messageText.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            }

            if (this.getItemViewType(position) == DEFAULT) {
                viewHolder.reactionText.setText("tru?");


                viewHolder.icon.setImageResource(R.drawable.ic_chevron_right_black_32dp);

                viewHolder.tile.setClickable(true);
                viewHolder.tile.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        View myView = viewHolder.revealText;

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
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                try {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Firebase target = new Firebase(Tru.URL)
                                                    .child("feed")
                                                    .child(ref.getKey());

                                            String authorID = target.getAuth().getUid();

                                            // TODO make this less gross
                                            //target.setValue(model);
                                            target.child("value").setValue(++model.value);
                                            target.child("agreed").child(authorID).setValue(true);

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    viewHolder.revealText.setVisibility(View.INVISIBLE);
                                                }
                                            }, 500);

                                        }
                                    }, 2000);
                                } catch (Exception e) {
                                    Log.e("Tru", "Someone did the tj thing");
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });


                        return true;
                    }
                });
            }
        }
    }

    public class MessageTouchCallbacks extends ItemTouchHelper.SimpleCallback {

        public MessageTouchCallbacks(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            //Remove swiped item from list and notify the RecyclerView
            try {
                //mAdapter.getRef(viewHolder.getAdapterPosition()).removeValue();
                mAdapter.getRef(viewHolder.getAdapterPosition()).child("hidden").child(new Firebase(Tru.URL).getAuth().getUid()).setValue(true);
            } catch (Exception e) {
                Log.e("Tru", "Someone did the TJ thing");
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Get RecyclerView item from the ViewHolder
                View itemView = viewHolder.itemView;

                int padding = dpToPx(16);

                Paint p = new Paint();
                p.setColor(getResources().getColor(R.color.red_300));
                if (dX > 0) {

                    // Draw Rect with varying right side, equal to displacement dX
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                            ((float) itemView.getBottom()-padding+1), p);
                } else {

                    // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom()-padding, p);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if(mAdapter.getItemViewType(viewHolder.getAdapterPosition()) == DEFAULT)
                return super.getSwipeDirs(recyclerView, viewHolder);
            else return 0;

        }
    }
}


