package com.dorren.eventhub.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dorren.eventhub.R;
import com.dorren.eventhub.data.model.Event;
import com.dorren.eventhub.ui.myevent.MyEventFragment;
import com.dorren.eventhub.ui.user.LoginActivity;
import com.dorren.eventhub.ui.user.ProfileFragment;
import com.dorren.eventhub.util.AppUtil;
import com.dorren.eventhub.util.PreferenceUtil;

public class MainActivity extends AppCompatActivity implements
        EventListFragment.EventListFragmentListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RadioGroup mEventTypeNav;
    private BottomNavigationView mBtmNav;

    private FragmentManager mFragmentMgr;
    private EventListFragment mEventListFragment;
    private MyEventFragment mMyEventFragment;
    private ProfileFragment mProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEventTypeNav = (RadioGroup) findViewById(R.id.event_type_nav);
        setupEventTypeNav();

        mEventListFragment = EventListFragment.newInstance(Event.TYPE_ALL);
        mMyEventFragment = new MyEventFragment();
        mProfileFragment = new ProfileFragment();

        mFragmentMgr = getSupportFragmentManager();
        mFragmentMgr.beginTransaction()
                .add(R.id.main_fragment_holder, mEventListFragment)
                .commit();

        mBtmNav = (BottomNavigationView) findViewById(R.id.bottom_nav);
        mBtmNav.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_home:
                            mEventTypeNav.setVisibility(View.VISIBLE);
                            replaceFragment(mEventListFragment);
                            break;
                        case R.id.action_schedule:
                            mEventTypeNav.setVisibility(View.GONE);
                            replaceFragment(mMyEventFragment);
                            break;
                        case R.id.action_me:
                            mEventTypeNav.setVisibility(View.GONE);
                            replaceFragment(mProfileFragment);
                            break;
                    }
                    return true;
                }
            });
    }

    private void replaceFragment(Fragment fragment){
        mFragmentMgr.beginTransaction()
                .replace(R.id.main_fragment_holder, fragment)
                .commit();
    }

    private void setupEventTypeNav() {
        for(int index=0; index< mEventTypeNav.getChildCount(); index++) {
            RadioButton btn = (RadioButton) mEventTypeNav.getChildAt(index);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId() == R.id.event_type_all){
                        mEventListFragment = EventListFragment.newInstance(Event.TYPE_ALL);
                        replaceFragment(mEventListFragment);
                    }else if(v.getId() == R.id.event_type_bookmarked){
                        mEventListFragment = EventListFragment.newInstance(Event.TYPE_BOOKMARKED);
                        replaceFragment(mEventListFragment);
                    }else if(v.getId() == R.id.event_type_confirmed){
                        mEventListFragment = EventListFragment.newInstance(Event.TYPE_CONFIRMED);
                        replaceFragment(mEventListFragment);
                    }else if(v.getId() == R.id.event_type_organized){
                        mEventListFragment = EventListFragment.newInstance(Event.TYPE_ORGANIZED);
                        replaceFragment(mEventListFragment);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_logout) {
            PreferenceUtil.logout(this);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, AppUtil.LOGIN_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Event event) {
        //openDetailPage(event);
    }

    private void openDetailPage(Event event) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        String json = event.toString();
        intent.putExtra(Intent.EXTRA_TEXT, json);
        startActivity(intent);
    }
}
