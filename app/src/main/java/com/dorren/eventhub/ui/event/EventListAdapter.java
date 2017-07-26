package com.dorren.eventhub.ui.event;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dorren.eventhub.R;
import com.dorren.eventhub.model.Event;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Event} and makes a call to the
 * specified {@link EventListAdapterListener}.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private Cursor mCursor;
    private final EventListAdapterListener mListener;

    public EventListAdapter(Cursor cursor, EventListAdapterListener listener) {
        mCursor = cursor;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        Event event = new Event(id, title, detail, timeFrom, timeTo);
        holder.mEvent = event;

        holder.mIdView.setText(id);
        holder.mContentView.setText(title);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onClick(holder.mEvent);
                }
            }
        });
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
        public final TextView mIdView;
        public final TextView mContentView;
        public Event mEvent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public interface EventListAdapterListener {
        void onClick(Event event);
    }
}
