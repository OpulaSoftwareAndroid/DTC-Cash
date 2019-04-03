package com.powerbtc.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.powerbtc.Constants.AppGlobal;
import com.powerbtc.Constants.WsConstant;
import com.powerbtc.MainActivity;
import com.powerbtc.R;
import com.powerbtc.activity.SetupPinActivity;
import com.powerbtc.model.CommonStatusResponce;
import com.powerbtc.webservice.RestClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class FragmentSetting extends Fragment {
    public FragmentSetting() {
    }

    LinearLayout linearSettingChangePin, lvChangePassword, lvChangeTransaction;
    RecyclerView rvLoginHistory;
    TextView tv_tra_pass;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        MainActivity.mainTitle.setText("Security");

        init(v);
        setClickEvent();
        String pass = AppGlobal.getStringPreference(getActivity(),WsConstant.SP_TRANSACTION_PASSSWORD);
        if (pass == null){
            tv_tra_pass.setText("Create Transaction Password");
        } else {
            tv_tra_pass.setText("Change Transaction Password");
        }
//        doGetLoginHistory();
        return v;
    }

    private void init(View v) {

        rvLoginHistory = (RecyclerView) v.findViewById(R.id.recyclerView_LoginHistory);
        rvLoginHistory.setLayoutManager(new LinearLayoutManager(getActivity()));

        linearSettingChangePin = (LinearLayout) v.findViewById(R.id.linear_setting_change_pin);
        lvChangePassword = (LinearLayout) v.findViewById(R.id.linear_setting_change_Password);
        lvChangeTransaction = (LinearLayout) v.findViewById(R.id.linear_setting_change_Transaction);
        tv_tra_pass = (TextView) v.findViewById(R.id.tv_tra_pass);
    }

    private void setClickEvent() {

        linearSettingChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SetupPinActivity.class);
                intent.putExtra("From", "change_pin");
                getActivity().startActivity(intent);
            }
        });

        lvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogForChangePassword();
            }
        });

        lvChangeTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogForChangeTransaction();
            }
        });
    }


    public void doGetLoginHistory() {
        /*if (AppGlobal.isNetwork(getActivity())) {
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", AppGlobal.getStringPreference(getActivity(), WsConstant.SP_LOGIN_REGID));

            AppGlobal.showProgressDialog(getActivity());

            new RestClient(getActivity()).getInstance().get().getLoginHistory(optioMap).enqueue(new Callback<LoginShieldResponse>() {
                @Override
                public void onResponse(Call<LoginShieldResponse> call, Response<LoginShieldResponse> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {
                            if (response.body().getInfo().size() > 0) {
                                LoginShieldAdapter adapter = new LoginShieldAdapter(getActivity(), response.body().getInfo());
                                rvLoginHistory.setAdapter(adapter);
                            } else {
                                Toast.makeText(getActivity(), "No data available", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginShieldResponse> call, Throwable t) {
                    AppGlobal.hideProgressDialog(getActivity());
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                }
            });
        }*/
    }

    Dialog dialogChangeTransaction;

    private void openDialogForChangeTransaction() {
        dialogChangeTransaction = new Dialog(getActivity());
        dialogChangeTransaction.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogChangeTransaction.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogChangeTransaction.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogChangeTransaction.setCancelable(true);

        View vi = getActivity().getLayoutInflater().inflate(R.layout.dialog_change_transaction_password, null, false);
        dialogChangeTransaction.setContentView(vi);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogChangeTransaction.getWindow();

        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setAttributes(lp);

        String pass = AppGlobal.getStringPreference(getActivity(),WsConstant.SP_TRANSACTION_PASSSWORD);

        final TextView txt_old = (TextView) dialogChangeTransaction.findViewById(R.id.txt_old);
        final TextView txt_form = (TextView) dialogChangeTransaction.findViewById(R.id.txt_form);
        final EditText edOldPassword = (EditText) dialogChangeTransaction.findViewById(R.id.ed_DialogChangePassword_Old);
        final EditText edNewPassword = (EditText) dialogChangeTransaction.findViewById(R.id.ed_DialogChangePassword_New);
        final EditText edConfirmPassword = (EditText) dialogChangeTransaction.findViewById(R.id.ed_DialogChangePassword_Confirm);

        if (pass == null){
            txt_old.setVisibility(View.GONE);
            edOldPassword.setVisibility(View.GONE);
            txt_form.setText("Create Transaction Password");
            tv_tra_pass.setText("Create Transaction Password");
        } else {
            txt_old.setVisibility(View.VISIBLE);
            edOldPassword.setVisibility(View.VISIBLE);
            txt_form.setText("Change Transaction Password");
            tv_tra_pass.setText("Change Transaction Password");
        }

        ImageView imClose = (ImageView) dialogChangeTransaction.findViewById(R.id.tv_DialogChangePassword_Close);
        imClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChangeTransaction.dismiss();
            }
        });

        TextView tvSave = (TextView) dialogChangeTransaction.findViewById(R.id.tv_DialogChangePassword_Save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edNewPassword.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter New Password", Toast.LENGTH_SHORT).show();
                } else if (edNewPassword.getText().toString().length()<6) {
                    Toast.makeText(getActivity(), "Minimum 6 digit Password", Toast.LENGTH_SHORT).show();
                } else if (edConfirmPassword.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter Confirm Password", Toast.LENGTH_SHORT).show();
                } else if (!edNewPassword.getText().toString().trim().equalsIgnoreCase(edConfirmPassword.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "Password and confirm password does not match", Toast.LENGTH_SHORT).show();
                } else {
                    doChangeTransactionPassword(edOldPassword.getText().toString().trim(), edNewPassword.getText().toString().trim());
                }
            }
        });

        dialogChangeTransaction.show();
    }

    public void doChangeTransactionPassword(String oldPassword, String newPassword) {
        if (AppGlobal.isNetwork(getActivity())) {

            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", AppGlobal.getStringPreference(getActivity(), WsConstant.SP_LOGIN_REGID));
            optioMap.put("OldPassword", oldPassword);
            optioMap.put("NewPassword", newPassword);
            optioMap.put("ValidData", AppGlobal.createAPIString());

            new RestClient(getActivity()).getInstance().get().changeTransactionPassword(optioMap).enqueue(new Callback<CommonStatusResponce>() {
                @Override
                public void onResponse(Call<CommonStatusResponce> call, Response<CommonStatusResponce> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {

                        Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();

                        if (response.body().getSuccess() == 1) {
                            dialogChangeTransaction.dismiss();
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

    Dialog dialogChangePassword;

    private void openDialogForChangePassword() {
        dialogChangePassword = new Dialog(getActivity());
        dialogChangePassword.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogChangePassword.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogChangePassword.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogChangePassword.setCancelable(true);

        View vi = getActivity().getLayoutInflater().inflate(R.layout.dialog_change_password, null, false);
        dialogChangePassword.setContentView(vi);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogChangePassword.getWindow();

        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setAttributes(lp);

        final EditText edOldPassword = (EditText) dialogChangePassword.findViewById(R.id.ed_DialogChangePassword_Old);
        final EditText edNewPassword = (EditText) dialogChangePassword.findViewById(R.id.ed_DialogChangePassword_New);
        final EditText edConfirmPassword = (EditText) dialogChangePassword.findViewById(R.id.ed_DialogChangePassword_Confirm);

        ImageView imClose = (ImageView) dialogChangePassword.findViewById(R.id.tv_DialogChangePassword_Close);
        imClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChangePassword.dismiss();
            }
        });

        TextView tvSave = (TextView) dialogChangePassword.findViewById(R.id.tv_DialogChangePassword_Save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edOldPassword.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter Old Password", Toast.LENGTH_SHORT).show();
                } else if (edOldPassword.getText().toString().length()<6) {
                    Toast.makeText(getActivity(), "Minimum 6 digit Password", Toast.LENGTH_SHORT).show();
                } else if (edNewPassword.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter New Password", Toast.LENGTH_SHORT).show();
                } else if (edNewPassword.getText().toString().length()<6) {
                    Toast.makeText(getActivity(), "Minimum 6 digit Password", Toast.LENGTH_SHORT).show();
                } else if (edConfirmPassword.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter Confirm Password", Toast.LENGTH_SHORT).show();
                } else if (!edNewPassword.getText().toString().trim().equalsIgnoreCase(edConfirmPassword.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "Password and confirm password does not match", Toast.LENGTH_SHORT).show();
                } else {
                    doChangePassword(edOldPassword.getText().toString().trim(), edNewPassword.getText().toString().trim());
                }
            }
        });

        dialogChangePassword.show();
    }

    public void doChangePassword(String oldPassword, String newPassword) {
        if (AppGlobal.isNetwork(getActivity())) {

            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", AppGlobal.getStringPreference(getActivity(), WsConstant.SP_LOGIN_REGID));
            optioMap.put("OldPassword", oldPassword);
            optioMap.put("NewPassword", newPassword);
            optioMap.put("ValidData", AppGlobal.createAPIString());

            new RestClient(getActivity()).getInstance().get().changePassword(optioMap).enqueue(new Callback<CommonStatusResponce>() {
                @Override
                public void onResponse(Call<CommonStatusResponce> call, Response<CommonStatusResponce> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {

                        Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();

                        if (response.body().getSuccess() == 1) {
                            dialogChangePassword.dismiss();
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
