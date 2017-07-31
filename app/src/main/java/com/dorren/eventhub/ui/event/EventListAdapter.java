package com.dorren.eventhub.ui.event;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dorren.eventhub.R;
import com.dorren.eventhub.data.model.Event;
import com.squareup.picasso.Picasso;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Event} and makes a call to the
 * specified {@link EventListAdapterListener}.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private Cursor mCursor;
    private final EventListAdapterListener mListener;
    private Context mContext;

    public EventListAdapter(Cursor cursor, EventListAdapterListener listener) {
        mCursor = cursor;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String id = mCursor.getString(mCursor.getColumnIndex(Event.COL_ID));
        String title = mCursor.getString(mCursor.getColumnIndex(Event.COL_TITLE));
        String detail = mCursor.getString(mCursor.getColumnIndex(Event.COL_DETAIL));
        String timeFrom = mCursor.getString(mCursor.getColumnIndex(Event.COL_TIME_FROM));
        String timeTo = mCursor.getString(mCursor.getColumnIndex(Event.COL_TIME_TO));
        String imageUrl = mCursor.getString(mCursor.getColumnIndex(Event.COL_IMAGE_URL));
        String location = mCursor.getString(mCursor.getColumnIndex(Event.COL_LOCATION));

        Event event = new Event(id, title, detail, timeFrom, timeTo, imageUrl, location);
        holder.mEvent = event;

        Picasso.with(mContext).load(imageUrl).into(holder.mImageView);
        holder.mDateView.setText(event.dateStringShort(event.time_from));
        holder.mTitleView.setText(title);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailActivity(v, holder.mEvent);

                if (null != mListener) {
                    mListener.onClick(holder.mEvent);
                }
            }
        });
    }

    private void openDetailActivity(View view, Event event){
        ImageView img = (ImageView)view.findViewById(R.id.event_image);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((MainActivity)mContext,
                img, img.getTransitionName()
        ).toBundle();

        Intent intent = new Intent(mContext, EventDetailActivity.class);
        String json = event.toString();
        intent.putExtra(Intent.EXTRA_TEXT, json);

        mContext.startActivity(intent, bundle);
    }

    @Override
    public int getItemCount() {
        if(mCursor != null) {
            return mCursor.getCount();
        }else{
            return 0;
        }
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (mCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mDateView;
        public final TextView mTitleView;
        public Event mEvent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.event_image);
            mDateView = (TextView) view.findViewById(R.id.event_date);
            mTitleView = (TextView) view.findViewById(R.id.event_title);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

    public interface EventListAdapterListener {
        void onClick(Event event);
    }
}
