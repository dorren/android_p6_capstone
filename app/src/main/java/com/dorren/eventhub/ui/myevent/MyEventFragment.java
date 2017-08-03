package com.dorren.eventhub.ui.myevent;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dorren.eventhub.R;
import com.dorren.eventhub.data.ApiException;
import com.dorren.eventhub.data.ApiService;
import com.dorren.eventhub.data.model.Event;
import com.dorren.eventhub.data.util.TimeUtil;
import com.dorren.eventhub.ui.event.EventDetailActivity;
import com.dorren.eventhub.ui.event.MainActivity;
import com.dorren.eventhub.util.PreferenceUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Fragment to show user's bookmarked, confirmed, and organized events.
 */
public class MyEventFragment extends Fragment {
    private static final String TAG = MyEventFragment.class.getSimpleName();
    private LinearLayout mLayout;
    private EventsLoader mLoader;

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
        Log.d(TAG, "onCreate() ");
        super.onCreate(savedInstanceState);
        mLoader = new EventsLoader(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() ");

        View rootView =  inflater.inflate(R.layout.fragment_my_event, container, false);
        mLayout = (LinearLayout) rootView.findViewById(R.id.my_events_layout);


        String userId = PreferenceUtil.getCurrentUser(getActivity()).id;
        mLoader.execute(userId);
        return rootView;
    }

    public void renderEvents(Event[] events){
        OffsetDateTime now = OffsetDateTime.now();
        int latest_year = now.getYear();
        int latest_month = -1;

        LayoutInflater inflater = getActivity().getLayoutInflater();

        for(final Event event : events){
            OffsetDateTime dt = event.time_from;
            int year = dt.getYear();
            int month = dt.getMonthValue();

            // render year header
            if (year != latest_year){
                View monthHeader = inflater.inflate(R.layout.fragment_my_event_year, mLayout, false);
                TextView txtTV = (TextView)monthHeader.findViewById(R.id.year_text);
                String txt = dt.format(DateTimeFormatter.ofPattern("YYYY"));
                txtTV.setText(txt);
                mLayout.addView(monthHeader);

                latest_year = year;
            }

            // render month header
            if (!(month == latest_month)){
                View monthHeader = inflater.inflate(R.layout.fragment_my_event_month_header, mLayout, false);
                TextView monthTV = (TextView)monthHeader.findViewById(R.id.month_text);
                String txt = TimeUtil.getMonthName(dt);
                monthTV.setText(txt);
                mLayout.addView(monthHeader);

                latest_month = month;
            }

            // add an event
            View eventView = inflater.inflate(R.layout.fragment_event, mLayout, false);
            ImageView imgView = (ImageView)eventView.findViewById(R.id.event_image);
            TextView dateView = (TextView)eventView.findViewById(R.id.event_date);
            TextView titleView = (TextView)eventView.findViewById(R.id.event_title);
            Picasso.with(getActivity()).load(event.image_url).into(imgView);
            dateView.setText(event.dateStringShort(event.time_from));
            titleView.setText(event.title);

            eventView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDetailActivity(v, event);
                }
            });
            mLayout.addView(eventView);
        }
    }

    private void openDetailActivity(View view, Event event){
        ImageView img = (ImageView)view.findViewById(R.id.event_image);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                img, img.getTransitionName()
        ).toBundle();

        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        String json = event.toString();
        intent.putExtra(Intent.EXTRA_TEXT, json);

        getActivity().startActivity(intent, bundle);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach() ");
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }





    public class EventsLoader extends AsyncTask<String, Void, Event[]> {
        private final String TAG = EventsLoader.class.getSimpleName();
        private ApiService api;
        private MyEventFragment mContext;
        private String userId;
        private Event[] mEvents;

        public EventsLoader(MyEventFragment context){
            mContext = context;
            api = new ApiService();
        }

        @Override
        protected Event[] doInBackground(String... params) {
            String userId = params[0];

            JSONObject options = new JSONObject();
            try {
                options.put("user_id", userId);
                options.put("confirmed", true);

                mEvents = api.getMyEvents(options.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (ApiException e) {
                e.printStackTrace();
            }

            return mEvents;
        }

        @Override
        protected void onPostExecute(Event[] events) {
            Log.d(TAG, "onPostExecute() " + events.length);
            mContext.renderEvents(events);
        }

    }
}
