package com.dorren.eventhub.ui.newevent;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dorren.eventhub.R;
import com.dorren.eventhub.data.ApiService;
import com.dorren.eventhub.data.model.Event;
import com.dorren.eventhub.util.PreferenceUtil;
import com.dorren.eventhub.data.util.TimeUtil;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;

import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{
    private static final String TAG = NewEventActivity.class.getSimpleName();

    private TextView mTitle, mDetail, mLocation, mTimeFromTxt, mmTimeToTxt;
    private View mTimeSrc; // either time_from_btn or time_to_btn
    private OffsetDateTime mTimeFrom, mTimeTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_new_event);
        AndroidThreeTen.init(this); // fetch default time zone

        mTitle = (TextView) findViewById(R.id.title);
        mDetail = (TextView) findViewById(R.id.detail);
        mLocation = (TextView) findViewById(R.id.location);
        //mTimeFromBtn = (Button) findViewById(R.id.time_from_btn);
        mTimeFromTxt = (TextView) findViewById(R.id.time_from);
        mmTimeToTxt = (TextView) findViewById(R.id.time_to);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openDatePicker(View view){
        mTimeSrc = view;
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                NewEventActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void openTimePicker(View view){
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                NewEventActivity.this, 9, 0, true);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        LocalDateTime ldt = LocalDateTime.of(year, monthOfYear + 1, dayOfMonth, 0, 0);

        if(mTimeSrc.getId() == R.id.time_from_btn) {
            mTimeFrom = OffsetDateTime.of(ldt, TimeUtil.defaultOffset());
        }else{
            mTimeTo = OffsetDateTime.of(ldt, TimeUtil.defaultOffset());
        }
        openTimePicker(mTimeSrc);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        if(mTimeSrc.getId() == R.id.time_from_btn) {
            LocalDateTime ldt = LocalDateTime.of(
                    mTimeFrom.getYear(),
                    mTimeFrom.getMonth(),
                    mTimeFrom.getDayOfMonth(),
                    hourOfDay,
                    minute);
            mTimeFrom = OffsetDateTime.of(ldt, TimeUtil.defaultOffset());
            mTimeFromTxt.setText(TimeUtil.dateToString(mTimeFrom));
        }else{
            LocalDateTime ldt = LocalDateTime.of(
                    mTimeTo.getYear(),
                    mTimeTo.getMonth(),
                    mTimeTo.getDayOfMonth(),
                    hourOfDay,
                    minute);
            mTimeTo = OffsetDateTime.of(ldt, TimeUtil.defaultOffset());
            mmTimeToTxt.setText(TimeUtil.dateToString(mTimeTo));
        }
    }

    public void postEvent(View view){
        String title = mTitle.getText().toString();
        String detail = mDetail.getText().toString();
        String location = mLocation.getText().toString();
        String timeFrom = mTimeFrom.toString();
        String timeTo = mTimeTo.toString();
        String organizer_id = PreferenceUtil.getCurrentUser(this).id;

        // TODO hardcode for now, implement upload later.
        String imageUrl = "https://s3.amazonaws.com/eventhubapp/events.png";

        JSONObject json = new JSONObject();
        try {
            json.put(Event.COL_TITLE, title);
            json.put(Event.COL_DETAIL, detail);
            json.put(Event.COL_TIME_FROM, timeFrom);
            json.put(Event.COL_TIME_TO, timeTo);
            json.put(Event.COL_IMAGE_URL, imageUrl);
            json.put(Event.COL_LOCATION, location);
            json.put(Event.COL_ORGANIZER_ID, organizer_id);
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        CreateEventTask task = new CreateEventTask(this);
        task.execute(json);
    }

    public void cancel(View view) {
        finish();
    }

    /**
     * save event to api.
     */
    public class CreateEventTask extends AsyncTask<JSONObject, Void, Event> {
        private Context mContext;
        private String mErrorMsg;
        private ApiService api;

        public CreateEventTask(Context context){
            mContext = context;
            api  = new ApiService();
        }

        @Override
        protected Event doInBackground(JSONObject... params) {
            JSONObject json = params[0];
            Event result = null;

            try {
                result = api.createEvent(json);
            } catch (Exception e) {
                e.printStackTrace();
                mErrorMsg = e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Event event) {
            Toast.makeText(mContext, "Event saved", Toast.LENGTH_LONG).show();
        }
    }
}
