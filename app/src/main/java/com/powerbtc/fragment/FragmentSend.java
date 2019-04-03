package com.powerbtc.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
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
import com.powerbtc.MoneyValueFilter;
import com.powerbtc.R;
import com.powerbtc.activity.BarCodeScanActivity;
import com.powerbtc.activity.SignInActivity;
import com.powerbtc.model.CommonStatusResponce;
import com.powerbtc.webservice.RestClient;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class FragmentSend extends Fragment {
    private LinearLayout lvPaste, lvQrCode;
    private TextView tvBalance, tvTotalAmount, tvSubmit,tvFees, edFees;
    private EditText edAddress, edAmount, edTrans;
    private double mainFee = 0, total = 0;
    float i;
    Dialog dialogChangeTransaction;
    public FragmentSend() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.mainTitle.setText("Send DTCH");
        init(view);
        setClickEvent();
    }

    private void init(View view) {
        lvPaste = (LinearLayout) view.findViewById(R.id.linear_Send_TRC_Paste);
        lvQrCode = (LinearLayout) view.findViewById(R.id.linear_Send_TRC_QrCode);

        tvBalance = (TextView) view.findViewById(R.id.tv_Send_TRC_Balance);
        tvTotalAmount = (TextView) view.findViewById(R.id.tv_Send_TRC_TotalAmount);
        tvFees = (TextView) view.findViewById(R.id.tv_Send_Fees);
        tvSubmit = (TextView) view.findViewById(R.id.tv_Send_TRC_Submit);
        edFees = (TextView) view.findViewById(R.id.tv_Send_TRC_Fees);

        edAddress = (EditText) view.findViewById(R.id.ed_Send_TRC_Address);
        edAmount = (EditText) view.findViewById(R.id.ed_Send_TRC_Amount);
        edTrans = (EditText) view.findViewById(R.id.ed_Send_TRC_TransPassword);

        if (!AppGlobal.getStringPreference(getActivity(), WsConstant.SP_WALLET_GUC_FEES).equalsIgnoreCase("")) {
            mainFee = Double.parseDouble(AppGlobal.getStringPreference(getActivity(), WsConstant.SP_WALLET_GUC_FEES));
        }

        tvBalance.setText("Balance : " + AppGlobal.getStringPreference(getActivity(), WsConstant.SP_WALLET_GUC_BALANCE) + " DTCH");
        tvFees.setText("Fees : " + AppGlobal.getStringPreference(getActivity(), WsConstant.SP_WALLET_GUC_FEES) + " %");
    }

    private void setClickEvent() {
        edAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edAmount.getText().toString().trim().equalsIgnoreCase("") ||
                        edAmount.getText().toString().trim().equalsIgnoreCase("0") ||
                        edAmount.getText().toString().equalsIgnoreCase(".")) {
                    tvTotalAmount.setText("0");
                    edFees.setText("0");
                } else {
                    double mainAmount = Double.parseDouble(edAmount.getText().toString());
                    double fees = (mainAmount * mainFee) / 100;

                    total = mainAmount - fees;

                    edFees.setText(String.format("%.8f", fees));
                    tvTotalAmount.setText(String.format("%.8f", total));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int cursorPosition = edAmount.getSelectionEnd();
                String originalStr = edAmount.getText().toString();

                //To restrict only two digits after decimal place
                edAmount.setFilters(new InputFilter[]{new MoneyValueFilter(Integer.parseInt("8"))});

                try {
                    edAmount.removeTextChangedListener(this);
                    String value = edAmount.getText().toString();

                    if (value != null && !value.equals("")) {
                        if (value.startsWith(".")) {
                            edAmount.setText("0.");
                        }
                        String str = edAmount.getText().toString().replaceAll(",", "");
                        if (!value.equals("")){
                            edAmount.setText(getDecimalFormattedString(str));
                        }

                        int diff = edAmount.getText().toString().length() - originalStr.length();
                        edAmount.setSelection(cursorPosition + diff);
                    }
                    edAmount.addTextChangedListener(this);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    edAmount.addTextChangedListener(this);
                }
            }
        });


        lvPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edAddress.setText(AppGlobal.pasteData(getActivity()));
                edAddress.setSelection(edAddress.getText().length());
            }
        });

        lvQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), BarCodeScanActivity.class);
                startActivityForResult(i, 101);
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(edAmount.getText().toString().trim().equalsIgnoreCase(""))){
                    i = Float.parseFloat(edAmount.getText().toString());
                }

                if (edAddress.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter DTCH address", Toast.LENGTH_SHORT).show();
                } else if (edAmount.getText().toString().trim().equalsIgnoreCase("") || edAmount.getText().toString().trim().equalsIgnoreCase("0")) {
                    Toast.makeText(getActivity(), "Please enter valid amount", Toast.LENGTH_SHORT).show();
                } else if (i<=0) {
                    Toast.makeText(getActivity(), "Please enter valid amount", Toast.LENGTH_SHORT).show();
                } else if (edTrans.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter Transaction password", Toast.LENGTH_SHORT).show();
                } else if (edTrans.getText().toString().length()<6) {
                    Toast.makeText(getActivity(), "Minimum 6 digit password", Toast.LENGTH_SHORT).show();
                } else {
                    doTRC();
                }
            }
        });

    }

    public static String getDecimalFormattedString(String value) {
        if (value != null && !value.equalsIgnoreCase("")) {
            StringTokenizer lst = new StringTokenizer(value, ".");
            String str1 = value;
            String str2 = "";
            if (lst.countTokens() > 1) {
                str1 = lst.nextToken();
                str2 = lst.nextToken();
            }
            String str3 = "";
            int i = 0;
            int j = -1 + str1.length();
            if (str1.charAt(-1 + str1.length()) == '.') {
                j--;
                str3 = ".";
            }
            for (int k = j; ; k--) {
                if (k < 0) {
                    if (str2.length() > 0)
                        str3 = str3 + "." + str2;
                    return str3;
                }
                str3 = str1.charAt(k) + str3;
                i++;
            }
        }
        return "";
    }

    public void doTRC() {
        if (AppGlobal.isNetwork(getActivity())) {

            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", AppGlobal.getStringPreference(getActivity(), WsConstant.SP_LOGIN_REGID));
            optioMap.put("ToAddress", edAddress.getText().toString().trim());
            optioMap.put("OtherAmount", edAmount.getText().toString().trim());
            optioMap.put("LoginPassword", edTrans.getText().toString().trim());
            optioMap.put("ValidData", AppGlobal.createAPIString());

            new RestClient(getActivity()).getInstance().get().soSend(optioMap).enqueue(new Callback<CommonStatusResponce>() {
                @Override
                public void onResponse(Call<CommonStatusResponce> call, Response<CommonStatusResponce> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {
                            Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            String i = response.body().getInfo().toString();
                            String j = i.replace(".0","");
                            AppGlobal.setStringPreference(getActivity(), String.valueOf(j),WsConstant.SP_OTP);
                            doOTP();
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

    public void doTRCWITHOTP(final String otp) {
        if (AppGlobal.isNetwork(getActivity())) {

            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", AppGlobal.getStringPreference(getActivity(), WsConstant.SP_LOGIN_REGID));
            optioMap.put("ToAddress", edAddress.getText().toString().trim());
            optioMap.put("OtherAmount", edAmount.getText().toString().trim());
            optioMap.put("LoginPassword", edTrans.getText().toString().trim());
            optioMap.put("ValidData", AppGlobal.createAPIString());
            optioMap.put("OTPCode", otp);

            new RestClient(getActivity()).getInstance().get().soSendwithotp(optioMap).enqueue(new Callback<CommonStatusResponce>() {
                @Override
                public void onResponse(Call<CommonStatusResponce> call, Response<CommonStatusResponce> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {
                            Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(),MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
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



    private void doOTP() {
        dialogChangeTransaction = new Dialog(getActivity());
        dialogChangeTransaction.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogChangeTransaction.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogChangeTransaction.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogChangeTransaction.setCancelable(true);

        View vi = getActivity().getLayoutInflater().inflate(R.layout.dialog_otp_send, null, false);
        dialogChangeTransaction.setContentView(vi);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogChangeTransaction.getWindow();

        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setAttributes(lp);

        String pass = AppGlobal.getStringPreference(getActivity(),WsConstant.SP_TRANSACTION_PASSSWORD);

        final EditText edOldPassword = (EditText) dialogChangeTransaction.findViewById(R.id.ed_OTP_Old);


        ImageView imClose = (ImageView) dialogChangeTransaction.findViewById(R.id.tv_DialogChangePassword_Close);
        imClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChangeTransaction.dismiss();
            }
        });

        TextView tvSave = (TextView) dialogChangeTransaction.findViewById(R.id.tv_DialogOTP);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String i = AppGlobal.getStringPreference(getActivity(),WsConstant.SP_OTP);
                String j = edOldPassword.getText().toString();
                if (i.equalsIgnoreCase(j)){
                    doTRCWITHOTP(j);
                } else {
                    Toast.makeText(getActivity(), "Invalid OTP!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialogChangeTransaction.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == 101) {
            if (data != null) {
                Log.e("Ritzpay", "Scanned: " + data.getStringExtra("Barcode"));
                String contents = data.getStringExtra("Barcode");

                if (contents.contains("bitcoins:")) {
                    contents = contents.replace("bitcoins:", "");
                }
                if (contents.contains("?")) {
                    //   contents=contents.split("\\?")[0];
                    edAddress.setText(contents.split("\\?")[0]);
                } else {
                    edAddress.setText(contents);

                }
                edAddress.setSelection(edAddress.getText().length());

            }
        }
    }
}