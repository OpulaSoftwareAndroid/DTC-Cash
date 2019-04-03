package com.powerbtc.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.powerbtc.Constants.AppGlobal;
import com.powerbtc.R;
import com.powerbtc.model.CommonStatusResponce;
import com.powerbtc.model.CountryResponse;
import com.powerbtc.model.Signup;
import com.powerbtc.webservice.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class SignUpActivity extends BaseActivity {

    ImageView imBack;
    Spinner spCountry;
    ArrayList<CountryResponse.Info> listCityMain;
    ArrayList<String> listCity;
    private EditText edName, edtUserName;
    private EditText edtEmail;
    private EditText edtContactNumber;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private TextView txtSignIn;
    private TextView txtSignUp;
    String regidd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
        doGetCountry();
    }

    private void init() {
        edName = (EditText) findViewById(R.id.ed_Register_Name);
        edtUserName = (EditText) findViewById(R.id.ed_Register_UserName);
        edtEmail = (EditText) findViewById(R.id.ed_Register_Email);
        edtContactNumber = (EditText) findViewById(R.id.ed_Register_Number);
        edtPassword = (EditText) findViewById(R.id.ed_Register_Password);
        edtConfirmPassword = (EditText) findViewById(R.id.ed_Register_ConfirmPassword);

        spCountry = (Spinner) findViewById(R.id.spinner_Country);

        imBack = (ImageView) findViewById(R.id.image_SignUp_Back);

        txtSignUp = (TextView) findViewById(R.id.signup_signup);
        txtSignIn = (TextView) findViewById(R.id.signup_signin);

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edName.getText().toString().trim().equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please Enter fullname", Toast.LENGTH_SHORT).show();
                } else if (edtUserName.getText().toString().trim().equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please Enter UserName", Toast.LENGTH_SHORT).show();
                } else if (edtContactNumber.getText().toString().trim().equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please Enter ContactNumber", Toast.LENGTH_SHORT).show();
                } else if (edtEmail.getText().toString().trim().equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                } else if (!(AppGlobal.checkEmail(edtEmail.getText().toString()))) {
                    Toast.makeText(SignUpActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (edtPassword.getText().toString().trim().equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else if (edtPassword.getText().toString().length()<6) {
                    Toast.makeText(SignUpActivity.this, "Minimum 6 digit password required", Toast.LENGTH_SHORT).show();
                } else if (edtConfirmPassword.getText().toString().trim().equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                } else if (!edtPassword.getText().toString().trim().equals(edtConfirmPassword.getText().toString().trim())) {
                    Toast.makeText(SignUpActivity.this, "Password mismatch", Toast.LENGTH_SHORT).show();
                } else {
                    doRegistartion(edName.getText().toString(),
                            edtUserName.getText().toString(),
                            edtEmail.getText().toString(),
                            edtPassword.getText().toString(),
                            edtContactNumber.getText().toString());
                }
            }
        });
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity.this.finish();
            }
        });
    }

    public void doRegistartion(String fullName, String UserName, String Email, String Password, String contactNumber) {
        if (AppGlobal.isNetwork(SignUpActivity.this)) {

            AppGlobal.showProgressDialog(SignUpActivity.this);
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("FullName", fullName);
            optioMap.put("UserName", UserName);
            optioMap.put("EmailId", Email);
            optioMap.put("Password", Password);
            optioMap.put("MobileNo", contactNumber);
            optioMap.put("ValidData", AppGlobal.createAPIString());
            optioMap.put("CountryName", spCountry.getSelectedItem().toString());
            optioMap.put("MobileCode", "+" + listCityMain.get(spCountry.getSelectedItemPosition()).getCountryCode());
            optioMap.put("FCM", FirebaseInstanceId.getInstance().getToken());

            new RestClient(SignUpActivity.this).getInstance().get().UserRegisterNew(optioMap).enqueue(new Callback<Signup>() {
                @Override
                public void onResponse(Call<Signup> call, Response<Signup> response) {
                    AppGlobal.hideProgressDialog(SignUpActivity.this);
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            regidd = String.valueOf(response.body().getInfo());
                            Toast.makeText(SignUpActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            openDialogForResendLink("" + response.body().getInfo());
                        } else {
                            Toast.makeText(SignUpActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Signup> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(SignUpActivity.this);
                        Toast.makeText(SignUpActivity.this, getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
            });
        } else {
            Toast.makeText(SignUpActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void openDialogForResendLink(final String regId) {
        final Dialog dialog = new Dialog(SignUpActivity.this);
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

        final TextView txtForgotPassword = (TextView) dialog.findViewById(R.id.tv_DialogResendLink_Resend);

        final ImageView close = (ImageView) dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                finish();
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doResendLink(regidd);
            }
        });

        dialog.show();


    }

    public void doResendLink(String regId) {
        if (AppGlobal.isNetwork(SignUpActivity.this)) {

            AppGlobal.showProgressDialog(SignUpActivity.this);
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", regId);
            optioMap.put("ValidData", AppGlobal.createAPIString());

            new RestClient(SignUpActivity.this).getInstance().get().resendLink(optioMap).enqueue(new Callback<CommonStatusResponce>() {
                @Override
                public void onResponse(Call<CommonStatusResponce> call, Response<CommonStatusResponce> response) {
                    AppGlobal.hideProgressDialog(SignUpActivity.this);
                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {
                            Toast.makeText(SignUpActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonStatusResponce> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(SignUpActivity.this);
                        Toast.makeText(SignUpActivity.this, getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
            });
        } else {
            Toast.makeText(SignUpActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void doGetCountry() {
        if (AppGlobal.isNetwork(SignUpActivity.this)) {

            AppGlobal.showProgressDialog(SignUpActivity.this);

            new RestClient(SignUpActivity.this).getInstance().get().getCountry().enqueue(new Callback<CountryResponse>() {
                @Override
                public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                    AppGlobal.hideProgressDialog(SignUpActivity.this);
                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {

                            listCityMain = new ArrayList<CountryResponse.Info>();
                            listCity = new ArrayList<String>();

                            listCityMain = response.body().getInfo();
                            for (int i = 0; i < listCityMain.size(); i++) {
                                listCity.add(listCityMain.get(i).getCountryName());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpActivity.this, R.layout.spinner_text, listCity);
                            adapter.setDropDownViewResource(R.layout.spinner_text_drop_down);
                            spCountry.setAdapter(adapter);

//                            edCountryCode.setText("+" + listCityMain.get(0).getCountryCode());
                        } else {
                            Toast.makeText(SignUpActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CountryResponse> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(SignUpActivity.this);
                        Toast.makeText(SignUpActivity.this, getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
            });
        } else {
            Toast.makeText(SignUpActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

}
