package com.powerbtc.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.powerbtc.Constants.AppGlobal;
import com.powerbtc.Constants.WsConstant;
import com.powerbtc.MainActivity;
import com.powerbtc.R;
import com.powerbtc.services.DepositBTCService;


@SuppressWarnings("ALL")
public class FragmentMyAddressBTC extends Fragment {
    public FragmentMyAddressBTC() {
    }

    TextView myAddressTxt;
    ImageView myAddressCopy;
    ImageView myAddressShare;
    ImageView myImgQrCode;

    String myZydexAdress = "notGenerated";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_address_btc, container, false);
        MainActivity.mainTitle.setText("Buy DTCH");

        getActivity().startService(new Intent(getActivity(), DepositBTCService.class));

        initView(v);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().stopService(new Intent(getActivity(), DepositBTCService.class));
    }

    private void initView(View v) {

        myAddressCopy = (ImageView) v.findViewById(R.id.my_address_copy_ALC);
        myAddressShare = (ImageView) v.findViewById(R.id.my_address_share_ALC);
        myImgQrCode = (ImageView) v.findViewById(R.id.img_qr_code_ALC);
        myAddressTxt = (TextView) v.findViewById(R.id.my_address_ALC);

        myZydexAdress = AppGlobal.getStringPreference(getActivity(), WsConstant.SP_WALLET_BTC_ADDRESS);
        myAddressTxt.setText(myZydexAdress);
        myImgQrCode.setImageBitmap(encodeAsBitmap(myZydexAdress));

        setClickEvent();
    }

    private void setClickEvent() {
        myAddressCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!myZydexAdress.equalsIgnoreCase("notGenerated")) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("address", myAddressTxt.getText().toString());
//                Toast.makeText(getActivity(), "Address has been copied to the clipboard", Toast.LENGTH_SHORT).show();
                    clipboard.setPrimaryClip(clip);
                    Snackbar snackbar = Snackbar.make(view, "Address has been copied to the clipboard !!", Snackbar.LENGTH_SHORT);
                    View view1 = snackbar.getView();
                    view1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    snackbar.setActionTextColor(getResources().getColor(R.color.white));
                    TextView txt = (TextView) view1.findViewById(R.id.snackbar_text);
                    txt.setTextColor(getResources().getColor(R.color.white));
                    snackbar.show();
                } else {
                    Toast.makeText(getActivity(), "Try after sometime", Toast.LENGTH_SHORT).show();
                }
            }
        });
        myAddressShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!myZydexAdress.equalsIgnoreCase("notGenerated")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, myZydexAdress);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                } else {
                    Toast.makeText(getActivity(), "Try after sometime", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Bitmap encodeAsBitmap(String str) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            Bitmap bmp = Bitmap.createBitmap(256, 256, Bitmap.Config.RGB_565);
            ;
            try {
                BitMatrix bitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, 256, 256);
                int width = 256;
                int height = 256;
                bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        if (bitMatrix.get(x, y))
                            bmp.setPixel(x, y, Color.BLACK);
                        else
                            bmp.setPixel(x, y, Color.WHITE);
                    }
                }

            } catch (WriterException e) {
                //Log.e("QR ERROR", ""+e);

            }
            return bmp;
        } catch (Exception e) {
            return null;
        }
    }


}
