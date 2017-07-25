package com.dorren.eventhub.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dorren.eventhub.R;
import com.dorren.eventhub.model.User;
import com.dorren.eventhub.ui.event.MainActivity;
import com.dorren.eventhub.ui.user.LoginActivity;
import com.dorren.eventhub.util.AppUtil;
import com.dorren.eventhub.util.PreferenceUtil;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new AuthenticateAsyncTask(this).execute();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AppUtil.LOGIN_REQUEST){
            if(resultCode == AppUtil.LOGIN_SUCCESS){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * check for current logged in user. if user found, goto main page. If not found, go to
     * login page.
     */
    class AuthenticateAsyncTask extends AsyncTask<Context, Void, User> {
        private Context mContext;
        private User user;

        public AuthenticateAsyncTask(Context context){
            mContext = context;
        }

        @Override
        protected User doInBackground(Context... params) {
            user = PreferenceUtil.getCurrentUser(mContext);
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if(user == null){
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivityForResult(intent, AppUtil.LOGIN_REQUEST);
            }else{
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
