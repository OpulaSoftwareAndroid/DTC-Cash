package com.powerbtc.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.powerbtc.Constants.AppGlobal;
import com.powerbtc.Constants.WsConstant;
import com.powerbtc.R;
import com.powerbtc.model.CommonStatusResponce;
import com.powerbtc.webservice.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class SplashActivity extends BaseActivity {

    PackageInfo pInfo = null;
    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            ProviderInstaller.installIfNeeded(SplashActivity.this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            Log.e("PowerBTC", "Splash first : " + e.toString());
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            Log.e("PowerBTC", "Splash Second : " + e.toString());
        }

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        getPermission();
    }

    private void init() {

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {*/
        if (!AppGlobal.getStringPreference(SplashActivity.this, WsConstant.SP_LOGIN_REGID).equals("")) {
            if (AppGlobal.getStringPreference(SplashActivity.this, WsConstant.SP_LOGIN_SET_PIN).equalsIgnoreCase("")) {
                Intent intent = new Intent(SplashActivity.this, SetupPinActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("From", "set_pin");
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, SetupPinActivity.class);
                intent.putExtra("From", "check_pin");
                startActivity(intent);
                SplashActivity.this.finish();
            }
        } else {
            Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
           /* }
        }, 2000);*/

    }

    public static int REQUEST_CAMERA = 0;
    boolean showRationale;

    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA);
            }

        } else {
            doVersion();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try {
            if (requestCode == REQUEST_CAMERA) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    doVersion();
                } else {
                    getPermission();
                }
            }
        } catch (Exception e) {
            getPermission();
        }
    }

    public void doVersion() {
        if (AppGlobal.isNetwork(SplashActivity.this)) {

            new RestClient(SplashActivity.this).getInstance().get().getVersion().enqueue(new Callback<CommonStatusResponce>() {
                @Override
                public void onResponse(Call<CommonStatusResponce> call, Response<CommonStatusResponce> response) {
                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {
                            if (response.body().getInfo().toString().equalsIgnoreCase(version)) {
                                init();
                            } else {
                                Toast.makeText(SplashActivity.this, "Please update application for use our latest app features", Toast.LENGTH_SHORT).show();
                                try {
                                    String url = "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + "&hl=en";
                                    Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));

                                    startActivity(viewIntent);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Unable to Connect Try Again...", Toast.LENGTH_LONG).show();
                                    SplashActivity.this.finish();
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        init();
                    }
                }

                @Override
                public void onFailure(Call<CommonStatusResponce> call, Throwable t) {
                    Toast.makeText(SplashActivity.this, SplashActivity.this.getResources().getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
