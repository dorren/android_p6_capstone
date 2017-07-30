package com.dorren.eventhub.ui.newevent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dorren.eventhub.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{
    private static final String TAG = NewEventActivity.class.getSimpleName();

    private TextView mTimeFromTxt, mmTimeToTxt;
    private View mTimeSrc; // either time_from_btn or time_to_btn
    private LocalDateTime mTimeFrom, mTimeTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        //mTimeFromBtn = (Button) findViewById(R.id.time_from_btn);
        mTimeFromTxt = (TextView) findViewById(R.id.time_from);
        mmTimeToTxt = (TextView) findViewById(R.id.time_to);
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
        if(mTimeSrc.getId() == R.id.time_from_btn) {
            mTimeFrom = LocalDateTime.of(year, monthOfYear, dayOfMonth, 0, 0);
        }else{
            mTimeTo = LocalDateTime.of(year, monthOfYear, dayOfMonth, 0, 0);
        }
        openTimePicker(mTimeSrc);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        if(mTimeSrc.getId() == R.id.time_from_btn) {
            mTimeFrom = LocalDateTime.of(
                    mTimeFrom.getYear(),
                    mTimeFrom.getMonth(),
                    mTimeFrom.getDayOfMonth(),
                    hourOfDay,
                    minute);

            mTimeFromTxt.setText(mTimeFrom.toString());
        }else{
            mTimeTo = LocalDateTime.of(
                    mTimeTo.getYear(),
                    mTimeTo.getMonth(),
                    mTimeTo.getDayOfMonth(),
                    hourOfDay,
                    minute);
            mmTimeToTxt.setText(mTimeTo.toString());
        }
    }

    public void postEvent(View view){

    }
}
