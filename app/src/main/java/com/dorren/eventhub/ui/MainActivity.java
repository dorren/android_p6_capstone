package com.dorren.eventhub.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dorren.eventhub.R;
import com.dorren.eventhub.user.LoginActivity;
import com.dorren.eventhub.util.AppUtil;
import com.dorren.eventhub.util.PreferenceUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        if(itemId == R.id.menu_logout){
            PreferenceUtil.logout(this);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, AppUtil.LOGIN_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }
}
