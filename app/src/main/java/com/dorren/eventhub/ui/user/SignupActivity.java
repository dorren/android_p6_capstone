package com.dorren.eventhub.ui.user;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.dorren.eventhub.R;
import com.dorren.eventhub.data.ApiService;
import com.dorren.eventhub.data.model.User;
import com.dorren.eventhub.data.util.AppUtil;
import com.dorren.eventhub.data.util.NetworkUtil;
import com.dorren.eventhub.data.util.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class SignupActivity extends AppCompatActivity {
    private SignupTask mSignupTask = null;
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mName = (EditText)findViewById(R.id.signup_name);
        mEmail = (EditText)findViewById(R.id.signup_email);
        mPassword = (EditText)findViewById(R.id.signup_password);
        mConfirmPasswd = (EditText)findViewById(R.id.signup_confirm_password);
    }

    public void signUp(View view) {
        mSignupTask = new SignupTask(this);
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String passwd = mPassword.getText().toString();

        mSignupTask.execute(name, email, passwd);
    }

    public void cancel(View view) {
        finish();
    }

    private void showProgress(final boolean show) {

    }

    public class SignupTask extends AsyncTask<String, Void, User> {
        private Context mContext;
        private String mErrorMsg;


        public SignupTask(Context context){
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
            }
        }

        private User attemptSignup(String name, String email, String password)
                throws IOException, JSONException {

            Uri uri = Uri.parse(NetworkUtil.API_BASE_URL).buildUpon().
                    appendPath("users").build();
            URL url = new URL(uri.toString());

            JSONObject data = new JSONObject();
            data.put("name", name);
            data.put("email", email);
            data.put("password", password);

            String response = NetworkUtil.query(url, "POST", data);
            JSONObject json = new JSONObject(response);

            if(json.has(ApiService.errKey)){
                mErrorMsg = json.getString(ApiService.errKey);
            }else{
                User user = User.fromJson(response);
                return user;
            }

            return null;
        }
    }
}
