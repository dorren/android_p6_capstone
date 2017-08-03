package com.dorren.eventhub.ui.event;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dorren.eventhub.MapActivity;
import com.dorren.eventhub.R;
import com.dorren.eventhub.data.ApiService;
import com.dorren.eventhub.data.model.Event;
import com.dorren.eventhub.data.model.User;
import com.dorren.eventhub.data.model.UserEvent;
import com.dorren.eventhub.util.PreferenceUtil;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finishAfterTransition();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void render() {
        Log.d(TAG, "render()");

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mEvent.title);

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

    public void bookmarkEvent(View view){
        User user = PreferenceUtil.getCurrentUser(this);
        UserEvent ue = new UserEvent(mEvent.id, user.id);
        ue.user_action = UserEvent.ACTION_BOOKMARK;

        UserEventTask task = new UserEventTask(this);
        task.execute(ue);
    }

    public void confirmEvent(View view){
        User user = PreferenceUtil.getCurrentUser(this);
        UserEvent ue = new UserEvent(mEvent.id, user.id);
        ue.user_action = UserEvent.ACTION_CONFIRM;

        UserEventTask task = new UserEventTask(this);
        task.execute(ue);
    }

    public class UserEventTask extends AsyncTask<UserEvent, Void, UserEvent> {
        private Context mContext;
        private String mErrorMsg;
        private ApiService api;

        public UserEventTask(Context context){
            mContext = context;
            api  = new ApiService();
        }

        @Override
        protected UserEvent doInBackground(UserEvent... params) {
            UserEvent ue = params[0];
            UserEvent result = null;

            try {
                result = api.bookmark(ue);
            } catch (Exception e) {
                e.printStackTrace();
                mErrorMsg = e.getMessage();
            }
            return ue;
        }

        @Override
        protected void onPostExecute(UserEvent userEvent) {
            String str = String.format(getString(R.string.event_actioned), userEvent.user_action);

            Toast.makeText(mContext, str, Toast.LENGTH_LONG).show();
        }
    }
}
