package com.dorren.eventhub.ui.user;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dorren.eventhub.R;
import com.dorren.eventhub.data.ApiService;
import com.dorren.eventhub.data.model.User;
import com.dorren.eventhub.ui.event.MainActivity;
import com.dorren.eventhub.util.AppUtil;
import com.dorren.eventhub.data.util.NetworkUtil;
import com.dorren.eventhub.util.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = SignupActivity.class.getSimpleName();

    private SignupTask mSignupTask = null;
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPasswd;
    private TextView mErrorMsg;
    private String mErrorTxt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mName = (EditText)findViewById(R.id.signup_name);
        mEmail = (EditText)findViewById(R.id.signup_email);
        mPassword = (EditText)findViewById(R.id.signup_password);
        mConfirmPasswd = (EditText)findViewById(R.id.signup_confirm_password);
        mErrorMsg = (TextView)findViewById(R.id.error_msg);
    }

    public void signUp(View view) {
        boolean valid = true;
        mErrorTxt = "";

        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String passwd = mPassword.getText().toString();
        String passwd_confirm = mConfirmPasswd.getText().toString();


        if(AppUtil.isEmpty(name)){
            valid = false;
            mErrorTxt += "name is required.\n";
            mName.requestFocus();
        }

        if(AppUtil.isEmpty(email)){
            valid = false;
            mErrorTxt += "email is required.\n";
            mEmail.requestFocus();
        }

        if(AppUtil.isEmpty(passwd)){
            valid = false;
            mErrorTxt += "password is required.\n";
            mPassword.requestFocus();
        }

        if(!AppUtil.isEmpty(passwd) && !passwd.equals(passwd_confirm)){
            valid = false;
            mErrorTxt += "password doesn't matching.\n";
            mPassword.requestFocus();
        }

        if(!AppUtil.isEmpty(mErrorTxt)){
            mErrorMsg.setText(mErrorTxt + "\n");
            mErrorMsg.setVisibility(View.VISIBLE);
        }
        if (valid) {
            mSignupTask = new SignupTask(this);
            mSignupTask.execute(name, email, passwd);
        }
    }


    public void cancel(View view) {
        finish();
    }

    private void showProgress(final boolean show) {

    }

    public void setErrorMsg(String str){
        Log.d(TAG, "setErrorMsg " + str);
        mErrorMsg.setText(str);
        mErrorMsg.setVisibility(View.VISIBLE);
    }

    public class SignupTask extends AsyncTask<String, Void, User> {
        private SignupActivity mContext;
        private String mErrorMsg;


        public SignupTask(SignupActivity context){
            mContext = context;
        }

        @Override
        protected User doInBackground(String... params) {
            String name = params[0];
            String email = params[1];
            String password = params[2];

            try {
                User user = attemptSignup(name, email, password);
                return user;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            mSignupTask = null;
            showProgress(false);

            if (user != null) {
                PreferenceUtil.saveCurrentUser(mContext, user);
                setResult(AppUtil.SIGNUP_SUCCESS);
                finish();
            } else {
                mContext.setErrorMsg(mErrorMsg);
            }
        }

        private User attemptSignup(String name, String email, String password)
                throws IOException, JSONException {

            Uri uri = Uri.parse(NetworkUtil.getApiUrl()).buildUpon().
                    appendPath("users").build();
            URL url = new URL(uri.toString());

            JSONObject data = new JSONObject();
            data.put("name", name);
            data.put("email", email);
            data.put("password", password);

            String response = NetworkUtil.query(url, "POST", data);
            Log.d(TAG, "attemptSignup() " + response);
            JSONObject json = new JSONObject(response);

            if(json.has(ApiService.errKey)){
                mErrorMsg = json.getString(ApiService.errKey);
                Log.d(TAG, "attemptSignup() " + mErrorMsg);
            }else{
                User user = User.fromJson(response);
                return user;
            }

            return null;
        }
    }
}
