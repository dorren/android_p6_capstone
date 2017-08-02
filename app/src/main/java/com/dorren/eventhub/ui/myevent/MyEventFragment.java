package com.dorren.eventhub.ui.myevent;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
import com.dorren.eventhub.R;
import com.dorren.eventhub.data.ApiService;
import com.dorren.eventhub.data.model.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Fragment to show user's bookmarked, confirmed, and organized events.
 */
public class MyEventFragment extends Fragment implements MonthLoader.MonthChangeListener{
    private WeekView mWeekView;
    private EventWeekLoader mLoader;

    public MyEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyEventFragment newInstance(String param1, String param2) {
        MyEventFragment fragment = new MyEventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoader = new EventWeekLoader(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_my_event, container, false);
        mWeekView = (WeekView)  rootView.findViewById(R.id.weekView);
        mWeekView.setWeekViewLoader(mLoader);
        mWeekView.setMonthChangeListener(mLoader);

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return null;
    }



    public class EventWeekLoader extends AsyncTask<String, Void, Event[]>
            implements WeekViewLoader, MonthLoader.MonthChangeListener{

        private final String TAG = EventWeekLoader.class.getSimpleName();
        private Context mContext;
        private Event[] mEvents;

        public EventWeekLoader(Context context){
            mContext = context;
        }

        @Override
        protected Event[] doInBackground(String... params) {

            return new Event[0];
        }

        @Override
        public double toWeekViewPeriodIndex(Calendar instance) {
            Log.d(TAG, "toWeekViewPeriodIndex() " + instance.toString());
            return 0;
        }

        @Override
        public List<? extends WeekViewEvent> onLoad(int periodIndex) {
            Log.d(TAG, "onLoad() " + periodIndex);
            return toList(mEvents);
        }

        @Override
        public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            Log.d(TAG, "onMonthChange() " + newYear + ", " + newMonth);
            this.execute("");
            return toList(mEvents);
        }

        private List<WeekViewEvent> toList(Event[] events){
            if(mEvents == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>();
        }
    }
}
