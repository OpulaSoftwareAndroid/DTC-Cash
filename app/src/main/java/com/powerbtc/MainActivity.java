package com.powerbtc;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.powerbtc.adapter.TransactionHistoryAdapter;
import com.powerbtc.fragment.FragmentHome;
import com.powerbtc.fragment.FragmentMyAddressALC;
import com.powerbtc.fragment.FragmentMyAddressBTC;
import com.powerbtc.fragment.FragmentSend;
import com.powerbtc.fragment.FragmentSetting;
import com.powerbtc.fragment.FragmentStackDeposit;
import com.powerbtc.fragment.FragmentStackDistribution;
import com.powerbtc.fragment.FragmentTicket;
import com.powerbtc.fragment.FragmentUpdateProfile;
import com.powerbtc.Constants.AppGlobal;
import com.powerbtc.Constants.WsConstant;
import com.powerbtc.activity.BaseActivity;
import com.powerbtc.activity.SignInActivity;
import com.powerbtc.model.CommonStatusResponce;
import com.powerbtc.model.TransactionResponse;
import com.powerbtc.services.DepositBTCService;
import com.powerbtc.services.DepositService;
import com.powerbtc.services.WalletAmountService;
import com.powerbtc.webservice.RestClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class MainActivity extends BaseActivity {
    private DrawerLayout drawerLayout;
    public static TextView mainTitle;
    public static ImageView drawerImg;
    public static ImageView userImage;
    private LinearLayout lvSupport, lvMyAddressALC, lvMyAddressBTC, lvSetting,
            lvHome, lvLogOut, lvSend,linearLayoutStackDeposit,linearLayoutStackDistribution;
    private ImageView imEditProfile;
    EditText editTextAmount;
    public static TextView drawerName;
    public static TextView drawerEmail;

    Toolbar toolbar;
    long lastBackPressTime = 0;

    public static MainActivity mainActivity;
    String mainActivityComeFrom = "";
    public static ProgressBar pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = MainActivity.this;

        startService(new Intent(MainActivity.this, WalletAmountService.class));
        startService(new Intent(MainActivity.this, DepositService.class));



        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            mainActivityComeFrom = bundle.getString("forVerifyAccount");
        }

        init();
        setClickEvent();


        callFragment(new FragmentHome(), getString(R.string.app_name));
    }

    private void init() {

        mainTitle = (TextView) findViewById(R.id.titleActionbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        pDialog = (ProgressBar) findViewById(R.id.progressBar_Main);

        // toolbar.setNavigationIcon(R.drawable.dr_icon);
        drawerName = (TextView) findViewById(R.id.activity_main_drawer_name);
        drawerEmail = (TextView) findViewById(R.id.activity_main_drawer_email);

        drawerImg = (ImageView) findViewById(R.id.drawerImg);
        userImage = (ImageView) findViewById(R.id.user_image);

        imEditProfile = (ImageView) findViewById(R.id.image_Home_EditProfile);

        lvHome = (LinearLayout) findViewById(R.id.linear_Home_Home);
        lvSupport = (LinearLayout) findViewById(R.id.linear_Home_Support);
        lvMyAddressALC = (LinearLayout) findViewById(R.id.linear_Home_MyAddress_ALC);
        lvMyAddressBTC = (LinearLayout) findViewById(R.id.linear_Home_MyAddress_BTC);
        lvSetting = (LinearLayout) findViewById(R.id.linear_Home_Setting);
        lvLogOut = (LinearLayout) findViewById(R.id.linear_Home_Logout);
        lvSend = (LinearLayout) findViewById(R.id.linear_Home_Send);
        linearLayoutStackDeposit = (LinearLayout) findViewById(R.id.linearLayoutStakeDeposit);
        linearLayoutStackDistribution = (LinearLayout) findViewById(R.id.linearLayoutStakeDistribution);


        setUserData();
        setClickEvent();
    }

    public static void setUserData() {
        drawerName.setText("" + AppGlobal.getStringPreference(mainActivity, WsConstant.SP_LOGIN_NAME));
        drawerEmail.setText("" + AppGlobal.getStringPreference(mainActivity, WsConstant.SP_LOGIN_EMAIL));

        if (!AppGlobal.getStringPreference(mainActivity, WsConstant.SP_LOGIN_PROFILE_PIC).equalsIgnoreCase("")) {
            Glide.with(mainActivity)
                    .load(AppGlobal.getStringPreference(mainActivity, WsConstant.SP_LOGIN_PROFILE_PIC))
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            pDialog.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            pDialog.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(userImage);
        }
    }


    private void setClickEvent() {

        lvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new FragmentHome(), getString(R.string.app_name));
            }
        });

        lvSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new FragmentTicket(), "Support");
            }
        });

        lvMyAddressALC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new FragmentMyAddressALC(), "Receive DTC");
            }
        });

        lvMyAddressBTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new FragmentMyAddressBTC(), "Buy DTC");
            }
        });

        imEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new FragmentUpdateProfile(), "User Profile");
            }
        });

        lvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new FragmentSetting(), "Security");
            }
        });

        lvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogForLogOut();
            }
        });

        lvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new FragmentSend(), "Send DTC");
            }
        });

        drawerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (getFragmentIntanceForClick()) {
                drawerLayout.openDrawer(Gravity.START);
               /* } else {
                    onBackPressed();
                }*/
            }
        });

        linearLayoutStackDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new FragmentStackDeposit(), "Stack Deposit History");
            }
        });


        linearLayoutStackDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new FragmentStackDeposit(), "Stack Deposit History");
            }
        });

        linearLayoutStackDistribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new FragmentStackDistribution(), "Stack Distribution History");
            }
        });

    }

    public void callFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.relative_container, fragment)
                .addToBackStack(null)
                .commit();
        if (drawerLayout.isDrawerOpen(Gravity.START))
            drawerLayout.closeDrawers(); // Cerrar drawer

        mainTitle.setText(title);
//        setDrawerIcon();
    }

    public void callFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.relative_container, fragment)
                .addToBackStack(null)
                .commit();
    }

   /* public void setDrawerIcon() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.relative_container);
        if (fragment instanceof FragmentHome) {
            drawerImg.setImageResource(R.drawable.dr_icon);
        } else {
            drawerImg.setImageResource(R.drawable.left_arrow);
        }
    }*/

    public boolean getFragmentIntanceForClick() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.relative_container);
        if (fragment instanceof FragmentHome) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.relative_container);

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else if (frag instanceof FragmentHome) {
            openDialogForCloseApp();
        } else {
            stopService(new Intent(MainActivity.this, WalletAmountService.class));
            stopService(new Intent(MainActivity.this, DepositService.class));
//            callFragment(new FragmentHome(), getString(R.string.app_name));
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(MainActivity.this, WalletAmountService.class));
        stopService(new Intent(MainActivity.this, DepositService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.relative_container);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openDialogForCloseApp() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        View vi = getLayoutInflater().inflate(R.layout.dialog_close_app, null, false);
        dialog.setContentView(vi);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();

        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setAttributes(lp);

        final TextView dialogText = (TextView) dialog.findViewById(R.id.dialog_text);
        dialogText.setText("Are you sure you want to exit?");
        final TextView closeAppNo = (TextView) dialog.findViewById(R.id.close_app_no);
        closeAppNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        final TextView closeAppYes = (TextView) dialog.findViewById(R.id.close_app_yes);
        closeAppYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finish();
            }
        });

        dialog.show();
    }

    private void openDialogForLogOut() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        View vi = getLayoutInflater().inflate(R.layout.dialog_close_app, null, false);
        dialog.setContentView(vi);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();

        dialog.show();

        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setAttributes(lp);

        final TextView dialogText = (TextView) dialog.findViewById(R.id.dialog_text);
        dialogText.setText("Are you sure you want to Logout?");

        final TextView closeAppNo = (TextView) dialog.findViewById(R.id.close_app_no);
        closeAppNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        final TextView closeAppYes = (TextView) dialog.findViewById(R.id.close_app_yes);
        closeAppYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutApplication();
            }
        });

    }

    public void logoutApplication() {
        if (AppGlobal.isNetwork(MainActivity.this)) {
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", AppGlobal.getStringPreference(MainActivity.this, WsConstant.SP_LOGIN_REGID));
            optioMap.put("ValidData", AppGlobal.createAPIString());

            AppGlobal.showProgressDialog(MainActivity.this);

            new RestClient(MainActivity.this).getInstance().get().logoutApp(optioMap).enqueue(new Callback<CommonStatusResponce>() {
                @Override
                public void onResponse(Call<CommonStatusResponce> call, Response<CommonStatusResponce> response) {
                    AppGlobal.hideProgressDialog(MainActivity.this);
                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {
                            AppGlobal.logoutApplication(MainActivity.this);
                            stopService(new Intent(MainActivity.this, WalletAmountService.class));
                            stopService(new Intent(MainActivity.this, DepositService.class));
                        } else {
                            Toast.makeText(MainActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonStatusResponce> call, Throwable t) {
                    AppGlobal.hideProgressDialog(MainActivity.this);
                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}