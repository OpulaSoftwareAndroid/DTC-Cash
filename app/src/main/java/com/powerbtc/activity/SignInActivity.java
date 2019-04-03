package com.powerbtc.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.powerbtc.Constants.AppGlobal;
import com.powerbtc.Constants.WsConstant;
import com.powerbtc.R;
import com.powerbtc.model.CommonStatusResponce;
import com.powerbtc.model.LoginResponse;
import com.powerbtc.webservice.RestClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class SignInActivity extends BaseActivity {

    private EditText edtUserName;
    private EditText edtPassword;
    private TextView txtSignIn;
    private TextView txtForgotPassword;
    private TextView txtSignUp;
    ImageView imBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        edtUserName = (EditText) findViewById(R.id.signin_username);
        edtPassword = (EditText) findViewById(R.id.signin_password);

        imBack = (ImageView) findViewById(R.id.image_Login_Back);

        txtForgotPassword = (TextView) findViewById(R.id.signin_ForgotPassword);
        txtSignUp = (TextView) findViewById(R.id.signin_singup);
        txtSignIn = (TextView) findViewById(R.id.signin_signin);

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogForForgotPassword();
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtUserName.getText().toString().trim().equals("")) {
                    Toast.makeText(SignInActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                } else if (edtPassword.getText().toString().trim().equals("")) {
                    Toast.makeText(SignInActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else if (edtPassword.getText().toString().length()<6) {
                    Toast.makeText(SignInActivity.this, "Minimum 6 digit password", Toast.LENGTH_SHORT).show();
                } else {
                    doSignIn(edtUserName.getText().toString(), edtPassword.getText().toString());
                }
            }
        });

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInActivity.this.finish();
            }
        });
    }

    public void doSignIn(String UserName, String Password) {
        if (AppGlobal.isNetwork(SignInActivity.this)) {
            AppGlobal.showProgressDialog(SignInActivity.this);

            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("UserName", UserName);
            optioMap.put("Password", Password);
            optioMap.put("ValidData", AppGlobal.createAPIString());
            optioMap.put("FCM", FirebaseInstanceId.getInstance().getToken());

            new RestClient(SignInActivity.this).getInstance().get().UserLoginNew(optioMap).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    AppGlobal.hideProgressDialog(SignInActivity.this);
                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {

                            if (response.body().getInfo().getEmailVerificationStatus().equalsIgnoreCase("1")) {

                                AppGlobal.setStringPreference(getApplicationContext(), response.body().getInfo().getRegisterId(), WsConstant.SP_LOGIN_REGID);
                                AppGlobal.setStringPreference(getApplicationContext(), response.body().getInfo().getName(), WsConstant.SP_LOGIN_NAME);
                                AppGlobal.setStringPreference(getApplicationContext(), response.body().getInfo().getUsername(), WsConstant.SP_LOGIN_USERNAME);
                                AppGlobal.setStringPreference(getApplicationContext(), response.body().getInfo().getEmailId(), WsConstant.SP_LOGIN_EMAIL);
                                AppGlobal.setStringPreference(getApplicationContext(), edtPassword.getText().toString().trim(), WsConstant.SP_LOGIN_PASSSWORD);
                                AppGlobal.setStringPreference(getApplicationContext(), response.body().getInfo().getMobileNo(), WsConstant.SP_LOGIN_MOBILE);
                                AppGlobal.setStringPreference(getApplicationContext(), response.body().getInfo().getProfile(), WsConstant.SP_LOGIN_PROFILE_PIC);
                                AppGlobal.setStringPreference(getApplicationContext(), response.body().getInfo().getLoginStatus(), WsConstant.SP_LOGIN_STATUS);
                                AppGlobal.setStringPreference(getApplicationContext(), response.body().getInfo().getEmailVerificationStatus(), WsConstant.SP_LOGIN_EMAIL_VERIFY);

                                AppGlobal.setStringPreference(getApplicationContext(), response.body().getInfo().getResidentAddress(), WsConstant.SP_ADDRESS_RES);
                                AppGlobal.setStringPreference(getApplicationContext(), response.body().getInfo().getCity(), WsConstant.SP_CITY);
                                AppGlobal.setStringPreference(getApplicationContext(), response.body().getInfo().getState(), WsConstant.SP_STATE);
                                AppGlobal.setStringPreference(getApplicationContext(), response.body().getInfo().getPincode(), WsConstant.SP_PICODE);
                                AppGlobal.setStringPreference(getApplicationContext(), response.body().getInfo().getCountryName(), WsConstant.SP_COUNTRY);
                                AppGlobal.setStringPreference(getApplicationContext(), response.body().getInfo().getCountryCode(), WsConstant.SP_COUNTRY_CODE);

                                if (AppGlobal.getStringPreference(SignInActivity.this, WsConstant.SP_LOGIN_SET_PIN).equalsIgnoreCase("")) {
                                    Intent intent = new Intent(SignInActivity.this, SetupPinActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("From", "set_pin");
                                    startActivity(intent);
                                    SignInActivity.this.finish();
                                } else {
                                    Intent intent = new Intent(SignInActivity.this, SetupPinActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("From", "check_pin");
                                    startActivity(intent);
                                    SignInActivity.this.finish();
                                }
                            } else {
                                openDialogForResendLink(response.body().getInfo().getRegisterId());
                            }
                        } else {
                            Toast.makeText(SignInActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(SignInActivity.this);
                        Toast.makeText(SignInActivity.this, getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
            });
        } else {
            Toast.makeText(SignInActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void openDialogForForgotPassword() {
        final Dialog dialog = new Dialog(SignInActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        View vi = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null, false);
        dialog.setContentView(vi);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();

        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setAttributes(lp);

        final ImageView imgClose = (ImageView) dialog.findViewById(R.id.forgot_password_close);
        final TextView txtForgotPassword = (TextView) dialog.findViewById(R.id.forgot_password_fp);
        final EditText edtEmailAddress = (EditText) dialog.findViewById(R.id.forgot_password_email);

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtEmailAddress.getText().toString().trim().equals("")) {
                    edtEmailAddress.setError("Please Enter Email Address");
                } else if (!(AppGlobal.checkEmail(edtEmailAddress.getText().toString()))){
                    edtEmailAddress.setError("Invalid Email Address");
                } else {
                    doForgotPassword(edtEmailAddress.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void openDialogForResendLink(final String regId) {
        final Dialog dialog = new Dialog(SignInActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        View vi = getLayoutInflater().inflate(R.layout.dialog_resend_link, null, false);
        dialog.setContentView(vi);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();

        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setAttributes(lp);

        final ImageView close = (ImageView) dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        TextView tvMsg = (TextView) dialog.findViewById(R.id.tv_ResendLink_Msg);
        tvMsg.setText("Your email id is not verify, Please verify your email id. click on Resend button to get verification mail.");

        final TextView txtForgotPassword = (TextView) dialog.findViewById(R.id.tv_DialogResendLink_Resend);

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String regidd = AppGlobal.getStringPreference(getApplicationContext(), WsConstant.SP_LOGIN_REGID);
                doResendLink(regidd);
            }
        });

        dialog.show();
    }

    public void doResendLink(String regId) {
        if (AppGlobal.isNetwork(SignInActivity.this)) {

            AppGlobal.showProgressDialog(SignInActivity.this);
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", regId);
            optioMap.put("ValidData", AppGlobal.createAPIString());

            new RestClient(SignInActivity.this).getInstance().get().resendLink(optioMap).enqueue(new Callback<CommonStatusResponce>() {
                @Override
                public void onResponse(Call<CommonStatusResponce> call, Response<CommonStatusResponce> response) {
                    AppGlobal.hideProgressDialog(SignInActivity.this);
                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {
                            Toast.makeText(SignInActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignInActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonStatusResponce> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(SignInActivity.this);
                        Toast.makeText(SignInActivity.this, getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
            });
        } else {
            Toast.makeText(SignInActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void doForgotPassword(String Email) {
        if (AppGlobal.isNetwork(SignInActivity.this)) {
            AppGlobal.showProgressDialog(SignInActivity.this);

            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("EmailId", Email);
            optioMap.put("ValidData", AppGlobal.createAPIString());

            new RestClient(SignInActivity.this).getInstance().get().ResetPassword(optioMap).enqueue(new Callback<CommonStatusResponce>() {
                @Override
                public void onResponse(Call<CommonStatusResponce> call, Response<CommonStatusResponce> response) {
                    AppGlobal.hideProgressDialog(SignInActivity.this);
                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {
                            Toast.makeText(SignInActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignInActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
//                        Toast.makeText(SignInActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommonStatusResponce> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(SignInActivity.this);
                        Toast.makeText(SignInActivity.this, getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
            });
        } else {
            Toast.makeText(SignInActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }
}
