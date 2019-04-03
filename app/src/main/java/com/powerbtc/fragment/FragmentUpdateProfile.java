package com.powerbtc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.powerbtc.MainActivity;
import com.powerbtc.R;


@SuppressWarnings("ALL")
public class FragmentUpdateProfile extends Fragment {
    public FragmentUpdateProfile() {}

    private LinearLayout lvPersonal, lvContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);
        MainActivity.mainTitle.setText("User Profile");
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setClickEvent();
    }

    private void init(View v)
    {
        lvPersonal = (LinearLayout) v.findViewById(R.id.linear_Profile_Personal);
        lvContact = (LinearLayout) v.findViewById(R.id.linear_Profile_Contact);
    }

    private void setClickEvent()
    {
        lvPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).callFragment(new FragmentPersonalProfile());
            }
        });

        lvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).callFragment(new FragmentContactDetails());
            }
        });
    }
}