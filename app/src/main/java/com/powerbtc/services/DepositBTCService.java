package com.powerbtc.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;


@SuppressWarnings("ALL")
public class DepositBTCService extends Service {

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
        /*if (AppGlobal.isNetwork(this)) {
            Map<String, String> options = new HashMap<>();
            options.put("RegisterId", AppGlobal.getStringPreference(this, WsConstant.SP_LOGIN_REGID));
            options.put("Address", AppGlobal.getStringPreference(this, WsConstant.SP_WALLET_BTC_ADDRESS));

            new RestClient(this).getInstance().get().depositBTCData(options).enqueue(new Callback<CommonStatusResponce>() {
                @Override
                public void onResponse(Call<CommonStatusResponce> call, Response<CommonStatusResponce> response) {
                    callEvent();
                }

                @Override
                public void onFailure(Call<CommonStatusResponce> call, Throwable t) {
                    callEvent();
                }
            });
        } else {
            callEvent();
        }*/
    }
}