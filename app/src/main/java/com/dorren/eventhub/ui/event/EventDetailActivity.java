package com.dorren.eventhub.ui.event;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dorren.eventhub.R;
import com.dorren.eventhub.model.Event;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class EventDetailActivity extends AppCompatActivity {
    private static final String TAG = "EventDetail";
    private ImageView mMainImageView;
    private TextView mTitle, mTime, mDetail, mLocation;
    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        mMainImageView = (ImageView) findViewById(R.id.event_image);
        mTitle = (TextView) findViewById(R.id.event_title);
        mTime = (TextView) findViewById(R.id.event_time);
        mDetail = (TextView) findViewById(R.id.event_detail);

        mLocation = (TextView) findViewById(R.id.event_location);
        mLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String location = (String)((TextView) v).getText();
                openLocationInMap(location);
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)){
                String json = intent.getStringExtra(Intent.EXTRA_TEXT);
                mEvent = Event.fromJson(json);
                render();
            }
        }
    }

    protected void render() {
        Picasso.with(this).load(mEvent.image_url).into(mMainImageView);
        mTitle.setText(mEvent.title);
        mTime.setText(mEvent.dateStringFromTo());
        mDetail.setText(mEvent.detail);

        mLocation.setText(mEvent.location);
        Pattern pattern = Pattern.compile(".*", Pattern.DOTALL);
        Linkify.addLinks(mLocation, pattern, "geo:0,0?q=");
    }

    private void openLocationInMap(String location) {
        Uri geoLocation = Uri.parse("geo:0,0?q=" + location);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call " + geoLocation.toString()
                    + ", no receiving apps installed!");
        }
    }
}
