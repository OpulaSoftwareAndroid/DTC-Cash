package com.powerbtc.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.powerbtc.Constants.AppGlobal;
import com.powerbtc.Constants.WsConstant;
import com.powerbtc.MainActivity;
import com.powerbtc.fragment.FragmentHome;
import com.powerbtc.model.WalletAmountResponse;
import com.powerbtc.webservice.RestClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressWarnings("ALL")
public class WalletAmountService extends Service {

    public static Runnable runnable = null;
    public static Handler handler = new Handler();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doCallBuySellRate();
        return super.onStartCommand(intent, flags, startId);
    }


    private void callEvent() {
        runnable = new Runnable() {
            @Override
            public void run() {
                doCallBuySellRate();
            }
        };
        handler.postDelayed(runnable, 300 * 1000);
    }

    public static void removeCallback() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeCallback();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void doCallBuySellRate() {
        if (AppGlobal.isNetwork(this)) {

            Map<String, String> options = new HashMap<>();
            options.put("RegisterId", AppGlobal.getStringPreference(this, WsConstant.SP_LOGIN_REGID));
            options.put("ValidData", AppGlobal.createAPIString());

            new RestClient(this).getInstance().get().WalletLiveAmount(options).enqueue(new Callback<WalletAmountResponse>() {
                @Override
                public void onResponse(Call<WalletAmountResponse> call, Response<WalletAmountResponse> response) {

                    Log.e("TAG", "Result : " + response.body());

                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {
                            try {

                                Intent broadcastIntentDashBoard = new Intent();
                                if (MainActivity.mainActivity != null) {
                                    broadcastIntentDashBoard.setAction(FragmentHome.MyWalletReceiver.PROCESS_RESPONSE);
                                }

//                                AppGlobal.setStringPreference(WalletAmountService.this, response.body().getInfo().getBitcoinAddress(), WsConstant.SP_WALLET_BTC_ADDRESS);
                                AppGlobal.setStringPreference(WalletAmountService.this, response.body().getInfo().getCoinAddress(), WsConstant.SP_WALLET_GUC_ADDRESS);
                                AppGlobal.setStringPreference(WalletAmountService.this, response.body().getInfo().getAmount(), WsConstant.SP_WALLET_GUC_BALANCE);
                                AppGlobal.setStringPreference(WalletAmountService.this, response.body().getInfo().getFees(), WsConstant.SP_WALLET_GUC_FEES);


                                broadcastIntentDashBoard.addCategory(Intent.CATEGORY_DEFAULT);
                                sendBroadcast(broadcastIntentDashBoard);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    callEvent();
                }

                @Override
                public void onFailure(Call<WalletAmountResponse> call, Throwable t) {
                    callEvent();
                }
            });
        } else {
            callEvent();
        }
    }
}