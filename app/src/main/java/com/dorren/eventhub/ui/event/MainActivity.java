package com.dorren.eventhub.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dorren.eventhub.R;
import com.dorren.eventhub.model.Event;
import com.dorren.eventhub.ui.user.LoginActivity;
import com.dorren.eventhub.ui.user.ProfileFragment;
import com.dorren.eventhub.util.AppUtil;
import com.dorren.eventhub.util.PreferenceUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity implements
        EventListFragment.EventListFragmentListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView mBtmNav;

    private FragmentManager mFragmentMgr;
    private EventListFragment mEventListFragment;
    private ProfileFragment mProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEventListFragment = new EventListFragment();
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
                            replaceFragment(mEventListFragment);
                            break;
                        case R.id.action_schedule:
                            break;
                        case R.id.action_me:
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
