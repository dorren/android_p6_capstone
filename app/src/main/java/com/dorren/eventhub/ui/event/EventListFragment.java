package com.dorren.eventhub.ui.event;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dorren.eventhub.R;
import com.dorren.eventhub.data.EventContentProvider;
import com.dorren.eventhub.model.Event;
import com.dorren.eventhub.ui.newevent.NewEventActivity;
import com.dorren.eventhub.util.AppUtil;
import com.dorren.eventhub.util.PreferenceUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link EventListFragmentListener}
 * interface.
 */
public class EventListFragment extends Fragment implements
        EventListAdapter.EventListAdapterListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = EventListFragment.class.getSimpleName();

    public static final String EVENT_TYPE = "event_type";
    private String mEventType;
    private EventListFragmentListener mListener;
    private RecyclerView mRecyclerView;
    private EventListAdapter mAdapter;
    private AdView mAdView;
    private FloatingActionButton mFab;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    private EventListFragment() {
    }

    /**
     * return this instance with filtered events.
     *
     * @param event_type all, bookmarked, confirmed, or organized
     * @return EventListFragment
     */
    public static EventListFragment newInstance(String event_type){
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putString(EVENT_TYPE, event_type);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate " + mEventType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView " + mEventType);
        View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);

        mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().
                addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_list_recycle_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new EventListAdapter(null, this);
        mRecyclerView.setAdapter(mAdapter);

        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab_new_event);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNewEvent(view);
            }
        });
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getArguments() != null) {
            mEventType = getArguments().getString(EVENT_TYPE);
        }else{
            mEventType = Event.TYPE_ALL;
        }

        if (context instanceof EventListFragmentListener) {
            mListener = (EventListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        Log.d(TAG, "onAttach " + mEventType);

        getLoaderManager().initLoader(AppUtil.EVENTS_CURSOR_LOADER, null, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(Event event) {
        mListener.onClick(event);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader " + mEventType);

        try {
            if (mEventType == Event.TYPE_ALL) {
                return new CursorLoader(getActivity(),
                        EventContentProvider.EVENT_URI,
                        null, null, null, null);
            } else if (mEventType == Event.TYPE_BOOKMARKED) {
                String userId = PreferenceUtil.getCurrentUser(getActivity()).id;
                JSONObject options = new JSONObject();
                options.put("user_id", userId);
                options.put("bookmarked", true);
                return new CursorLoader(getActivity(),
                        EventContentProvider.MY_EVENT_URI,
                        null, options.toString(), null, null);
            } else if (mEventType == Event.TYPE_CONFIRMED) {
                String userId = PreferenceUtil.getCurrentUser(getActivity()).id;
                JSONObject options = new JSONObject();
                options.put("user_id", userId);
                options.put("confirmed", true);
                return new CursorLoader(getActivity(),
                        EventContentProvider.MY_EVENT_URI,
                        null, options.toString(), null, null);
            } else {
                return new CursorLoader(getActivity(),
                        EventContentProvider.EVENT_URI,
                        null, null, null, null);
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void swapCursor(Cursor cursor){
        mAdapter.swapCursor(cursor);
    }

    public void toNewEvent(View view) {
        Intent intent = new Intent(getActivity(), NewEventActivity.class);
        startActivity(intent);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface EventListFragmentListener {
        void onClick(Event event);
    }
}
