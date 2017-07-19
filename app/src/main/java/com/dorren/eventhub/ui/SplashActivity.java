package com.dorren.eventhub.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dorren.eventhub.R;
import com.dorren.eventhub.data.User;
import com.dorren.eventhub.util.PreferenceUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new AuthenticateAsyncTask(this).execute();
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
                startActivity(intent);
            }else{
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}