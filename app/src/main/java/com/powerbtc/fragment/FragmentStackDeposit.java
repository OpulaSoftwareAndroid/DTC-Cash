package com.powerbtc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.powerbtc.Constants.AppGlobal;
import com.powerbtc.Constants.WsConstant;
import com.powerbtc.MainActivity;
import com.powerbtc.R;
import com.powerbtc.adapter.StackDepositAdapter;
import com.powerbtc.adapter.TicketAdapter;
import com.powerbtc.adapter.TransactionHistoryAdapter;
import com.powerbtc.model.CommonStatusResponce;
import com.powerbtc.model.StackDeposit.StackDeposit;
import com.powerbtc.model.TicketHistoryResponse;
import com.powerbtc.webservice.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressWarnings("ALL")
public class FragmentStackDeposit extends Fragment {
    private EditText edAddress, edState, edCity, edPinCode, edCountry;
    private TextView tvSave;
    RecyclerView recyclerViewStackDeposit;
    StackDepositAdapter stackDepositAdapter;
    String TAG="FragmentStackDeposit";
    public FragmentStackDeposit() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stack_deposit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.mainTitle.setText("Stake Deposit History");
        init(view);
        setClickEvent();
        getStackDepositTransactionHistory();
    }

    private void init(View view) {
       recyclerViewStackDeposit=view.findViewById(R.id.recyclerViewStackDeposit);
        recyclerViewStackDeposit.setLayoutManager(new LinearLayoutManager(getActivity()));

        stackDepositAdapter=new StackDepositAdapter(getContext());

    }

    private void setClickEvent() {
    }
    public void getStackDepositTransactionHistory() {
        if (AppGlobal.isNetwork(getActivity())) {
            AppGlobal.showProgressDialog(getActivity());

            Map<String, String> optioMap = new HashMap<>();
          //  optioMap.put("RegisterId", AppGlobal.getStringPreference(getActivity(), WsConstant.SP_LOGIN_REGID));
            optioMap.put("RegisterId", "67127100");

            optioMap.put("ValidData", AppGlobal.createAPIString());

            new RestClient(getActivity()).getInstance().get().getStakeDepositHistory(optioMap).enqueue(new Callback<StackDeposit>() {
                @Override
                public void onResponse(Call<StackDeposit> call, Response<StackDeposit> response) {
                    AppGlobal.hideProgressDialog(getActivity());

                    if (response.body() != null) {
                        Log.d(TAG,"jigar the deposit history have is "+new Gson().toJson(response));
                        if (response.body().getSuccess() == 1) {
//                            listTicket = new ArrayList<>();
                            StackDepositAdapter adapter = new StackDepositAdapter(getActivity(), response.body().getInfo());
                            recyclerViewStackDeposit.setAdapter(adapter);

                            if (response.body().getInfo().size() > 0) {

                            } else {
                                Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {


                    }
                }

                @Override
                public void onFailure(Call<StackDeposit> call, Throwable t) {
                    try {
                        Log.e(TAG,"jigar the deposit history have is "+t.getMessage());

                        AppGlobal.hideProgressDialog(getActivity());
                        Log.e("Taurus", "Error while call API : " + t.toString());
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