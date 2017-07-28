package com.dorren.eventhub.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dorren.eventhub.MapActivity;
import com.dorren.eventhub.R;
import com.dorren.eventhub.model.Event;
import com.squareup.picasso.Picasso;

public class EventDetailActivity extends AppCompatActivity {
    private static final String TAG = "EventDetail";
    private ImageView mMainImageView;
    private TextView mTitle, mTime, mDetail, mAddress;
    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        mMainImageView = (ImageView) findViewById(R.id.event_image);
        mTitle = (TextView) findViewById(R.id.event_title);
        mTime = (TextView) findViewById(R.id.event_time);
        mDetail = (TextView) findViewById(R.id.event_detail);

        mAddress = (TextView) findViewById(R.id.event_location);
        mAddress.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String address = (String)((TextView) v).getText();
                Log.d(TAG, "onClick address " + address);
                openMapActivity(address);
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
        Log.d(TAG, "render()");
        Picasso.with(this).load(mEvent.image_url).into(mMainImageView);
        mTitle.setText(mEvent.title);
        mTime.setText(mEvent.dateStringFromTo());
        mDetail.setText(mEvent.detail);

        mAddress.setText(mEvent.location);
    }

    private void openMapActivity(String address){
        Log.d(TAG, "Open map activity");
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, address);
        startActivity(intent);
    }
}
