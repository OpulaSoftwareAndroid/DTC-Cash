package com.powerbtc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.powerbtc.Constants.AppGlobal;
import com.powerbtc.Constants.WsConstant;
import com.powerbtc.MainActivity;
import com.powerbtc.R;
import com.powerbtc.model.CommonStatusResponce;
import com.powerbtc.webservice.RestClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressWarnings("ALL")
public class FragmentContactDetails extends Fragment {
    private EditText edAddress, edState, edCity, edPinCode, edCountry;
    private TextView tvSave;

    public FragmentContactDetails() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.mainTitle.setText("Contact Info");
        init(view);
        setClickEvent();
    }

    private void init(View view) {
        edAddress = (EditText) view.findViewById(R.id.ed_ContactInfo_Address);
        edState = (EditText) view.findViewById(R.id.ed_ContactInfo_State);
        edCity = (EditText) view.findViewById(R.id.ed_ContactInfo_City);
        edPinCode = (EditText) view.findViewById(R.id.ed_ContactInfo_PinCode);
        edCountry = (EditText) view.findViewById(R.id.ed_ContactInfo_Country);

        edAddress.setText(AppGlobal.getStringPreference(getActivity(), WsConstant.SP_ADDRESS_RES));
        edState.setText(AppGlobal.getStringPreference(getActivity(), WsConstant.SP_STATE));
        edCity.setText(AppGlobal.getStringPreference(getActivity(), WsConstant.SP_CITY));
        edPinCode.setText(AppGlobal.getStringPreference(getActivity(), WsConstant.SP_PICODE));
        edCountry.setText(AppGlobal.getStringPreference(getActivity(), WsConstant.SP_COUNTRY));

        tvSave = (TextView) view.findViewById(R.id.tv_ContactInfo_Save);
    }

    private void setClickEvent() {
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edAddress.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter address", Toast.LENGTH_SHORT).show();
                } else if (edState.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter State", Toast.LENGTH_SHORT).show();
                } else if (edCity.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter city", Toast.LENGTH_SHORT).show();
                } else if (edPinCode.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter Pincode", Toast.LENGTH_SHORT).show();
                } else {
                    doContactUpdate(edAddress.getText().toString().trim(), edState.getText().toString().trim(),
                            edCity.getText().toString().trim(), edPinCode.getText().toString().trim());
                }
            }
        });
    }

    public void doContactUpdate(final String address, final String state, final String city, final String pincode) {
        if (AppGlobal.isNetwork(getActivity())) {

            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", AppGlobal.getStringPreference(getActivity(), WsConstant.SP_LOGIN_REGID));
            optioMap.put("Address", address);
            optioMap.put("State", state);
            optioMap.put("City", city);
            optioMap.put("PinCode", pincode);
            optioMap.put("ValidData", AppGlobal.createAPIString());

            new RestClient(getActivity()).getInstance().get().updateContactInfo(optioMap).enqueue(new Callback<CommonStatusResponce>() {
                @Override
                public void onResponse(Call<CommonStatusResponce> call, Response<CommonStatusResponce> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {

                            AppGlobal.setStringPreference(getActivity(), address, WsConstant.SP_ADDRESS_RES);
                            AppGlobal.setStringPreference(getActivity(), state, WsConstant.SP_STATE);
                            AppGlobal.setStringPreference(getActivity(), city, WsConstant.SP_CITY);
                            AppGlobal.setStringPreference(getActivity(), pincode, WsConstant.SP_PICODE);

                            Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommonStatusResponce> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(getActivity());
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }
}