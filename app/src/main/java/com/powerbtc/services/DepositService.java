package com.powerbtc.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.powerbtc.Constants.AppGlobal;
import com.powerbtc.Constants.WsConstant;
import com.powerbtc.model.CommonStatusResponce;
import com.powerbtc.webservice.RestClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressWarnings("ALL")
public class DepositService extends Service {

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

            new RestClient(this).getInstance().get().depositData(options).enqueue(new Callback<CommonStatusResponce>() {
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
        }
    }
}