package com.dorren.eventhub.ui.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dorren.eventhub.R;
import com.dorren.eventhub.data.model.User;
import com.dorren.eventhub.util.PreferenceUtil;

/**
 * User profile that shows user name, email.
 *
 * Activities that contain this fragment must implement the
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private User mUser;
    private TextView mName, mEmail;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mName = (TextView) rootView.findViewById(R.id.profile_name);
        mEmail = (TextView) rootView.findViewById(R.id.profile_email);

        mUser = PreferenceUtil.getCurrentUser(getActivity());
        mName.setText(mUser.name);
        mEmail.setText(mUser.email);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
