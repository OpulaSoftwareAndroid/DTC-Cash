package com.powerbtc.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.powerbtc.Constants.AppGlobal;
import com.powerbtc.Constants.WsConstant;
import com.powerbtc.MainActivity;
import com.powerbtc.R;
import com.powerbtc.adapter.TicketAdapter;
import com.powerbtc.model.CommonResponce;
import com.powerbtc.model.CommonStatusResponce;
import com.powerbtc.model.TicketHistoryResponse;
import com.powerbtc.webservice.RestClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.kbeanie.imagechooser.api.ChooserType.REQUEST_CAPTURE_PICTURE;

@SuppressWarnings("ALL")
public class FragmentTicket extends Fragment implements TicketAdapter.OnClickTicket, ImageChooserListener {
    public FragmentTicket() {}

    private RecyclerView rvTickets;
    private TextView tvCreateTicket;
    private Dialog dialogTicket;
    private TicketAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ticket, container, false);
        MainActivity.mainTitle.setText("Ticket History");
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void init(View v)
    {
        mSwipeRefreshLayout = v.findViewById(R.id.swipeToRefresh_ticket);
        rvTickets = (RecyclerView) v.findViewById(R.id.recyclerView_Support);
        rvTickets.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*adapter = new TicketAdapter(getActivity());
        rvTickets.setAdapter(adapter);*/

        tvCreateTicket = (TextView) v.findViewById(R.id.tv_Support_CreateTicket);
        tvCreateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogForTicket();
            }
        });

        getData();
    }

    EditText edFileName;
    private void openDialogForTicket() {
        dialogTicket = new Dialog(getActivity());
        dialogTicket.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogTicket.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogTicket.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogTicket.setCancelable(true);

        View vi = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_ticket, null, false);
        dialogTicket.setContentView(vi);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogTicket.getWindow();

        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setAttributes(lp);

        EditText edName = (EditText) dialogTicket.findViewById(R.id.ed_DialogCreateTicket_UserName);
        final EditText edSubject = (EditText) dialogTicket.findViewById(R.id.ed_DialogCreateTicket_Subject);
        final EditText edMessage = (EditText) dialogTicket.findViewById(R.id.ed_DialogCreateTicket_Msg);
        edFileName = (EditText) dialogTicket.findViewById(R.id.tv_DialogTicketDetails_FileName);

        ImageView imAttchemnt = (ImageView) dialogTicket.findViewById(R.id.image_DialogTicketDetails_Attachment);
        imAttchemnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogForChosseImage();
            }
        });

        edName.setText(AppGlobal.getStringPreference(getActivity(), WsConstant.SP_LOGIN_NAME));

        ImageView imClose = (ImageView) dialogTicket.findViewById(R.id.image_DialogCreateTicket_Close);
        imClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTicket.dismiss();
            }
        });

        TextView tvSave = (TextView) dialogTicket.findViewById(R.id.tv_DialogCreateTicket_Submit);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edMessage.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter message.", Toast.LENGTH_SHORT).show();
                } else if (edSubject.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter subject.", Toast.LENGTH_SHORT).show();
                } else {
                    stMessage = edMessage.getText().toString();
                    stSubject = "" + edSubject.getText().toString().trim();

                    doCreateTicket(AppGlobal.getStringPreference(getActivity(), WsConstant.SP_LOGIN_REGID), stSubject, stMessage);
                }
            }
        });

        dialogTicket.show();
    }

    private ArrayList<String> getSubjects()
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("General inquiry");
        list.add("Account hacked");
        list.add("Account Suspended");
        list.add("Become Taurus Partner");
        list.add("Taurus Coin Mining");
        list.add("Taurus Coin Staking");
        list.add("BTC/TRC deposit problem");
        list.add("BTC/TRC transaction confirmation");
        list.add("BTC/TRC withdrawal problem");
        list.add("Email change");
        list.add("Error");
        list.add("Incorrect bonus paid");
        list.add("Incorrect interest paid");
        list.add("Login shield verification");
        list.add("2FA/Google authenticator");

        return list;
    }

    String stMessage, stSubject;

    private MultipartBody.Part body;
    public void doCreateTicket(String regId, String subject, String msg) {
        if (AppGlobal.isNetwork(getActivity())) {

            AppGlobal.showProgressDialog(getActivity());
            String fileName = "";

            if (!filePath.equalsIgnoreCase("")) {
                File file = new File(filePath);

                if (!file.exists())
                {
                    Toast.makeText(getActivity(), "File Not Found", Toast.LENGTH_SHORT).show();
                    return;
                }

                fileName = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('/')+1, file.getAbsolutePath().length());
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
            }

            RequestBody fileName1 = RequestBody.create(MediaType.parse("text/plain"), fileName);
            RequestBody regId1 = RequestBody.create(MediaType.parse("text/plain"), regId);
            RequestBody subject1 = RequestBody.create(MediaType.parse("text/plain"), subject);
            RequestBody message1 = RequestBody.create(MediaType.parse("text/plain"), msg);
            RequestBody token = RequestBody.create(MediaType.parse("text/plain"), AppGlobal.createAPIString());

            new RestClient(getActivity()).getInstance().get()
                    .createTicket(body, fileName1, regId1, subject1, message1, token).enqueue(new Callback<CommonStatusResponce>() {
                @Override
                public void onResponse(Call<CommonStatusResponce> call, Response<CommonStatusResponce> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            dialogTicket.dismiss();
                            getData();
                        } else {
                            Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Data Null", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommonStatusResponce> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(getActivity());
                        Toast.makeText(getActivity(), "Network not available", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    ArrayList<TicketHistoryResponse.Info> listTicket = new ArrayList<>();
    public void getData() {
        if (AppGlobal.isNetwork(getActivity())) {
            AppGlobal.showProgressDialog(getActivity());

            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", AppGlobal.getStringPreference(getActivity(), WsConstant.SP_LOGIN_REGID));
            optioMap.put("ValidData", AppGlobal.createAPIString());

            new RestClient(getActivity()).getInstance().get().getTicketHistory(optioMap).enqueue(new Callback<TicketHistoryResponse>() {
                @Override
                public void onResponse(Call<TicketHistoryResponse> call, Response<TicketHistoryResponse> response) {
                    AppGlobal.hideProgressDialog(getActivity());

                    if (response.body() != null) {
                        if (response.body().getSuccess() == 1) {
                            listTicket = new ArrayList<>();
                            if (response.body().getInfo().size() > 0) {

                                listTicket = response.body().getInfo();
                                adapter = new TicketAdapter(getActivity(), response.body().getInfo(), FragmentTicket.this);
                                rvTickets.setAdapter(adapter);

//                                tvNoData.setVisibility(View.GONE);
//                                rvTickets.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
//                                tvNoData.setVisibility(View.VISIBLE);
//                                rvTickets.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
//                            tvNoData.setVisibility(View.VISIBLE);
//                            rvTickets.setVisibility(View.GONE);
                        }
                    } else {
//                        tvNoData.setVisibility(View.VISIBLE);
//                        rvTickets.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<TicketHistoryResponse> call, Throwable t) {
                    try {
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

    public static TicketHistoryResponse.Info stTicketNumber;
    @Override
    public void setOnClickTicket(int position) {
            stTicketNumber = listTicket.get(position);
            ((MainActivity) getActivity()).callFragment(new FragmentTicketDetails());

    }

    private void openDialogForChosseImage() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        View vi = getActivity().getLayoutInflater().inflate(R.layout.dialog_open_image, null, false);
        dialog.setContentView(vi);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();

        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setAttributes(lp);


        final TextView closeAppNo = (TextView) dialog.findViewById(R.id.image_camera);
        closeAppNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
                dialog.dismiss();
            }
        });
        final TextView closeAppYes = (TextView) dialog.findViewById(R.id.image_gallary);
        closeAppYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.clearOldFiles();
        try {
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        chooserType = REQUEST_CAPTURE_PICTURE;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAPTURE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "File Path : " + filePath + "\nChooser Type: " + chooserType);

        if (resultCode == RESULT_OK && (requestCode == ChooserType.REQUEST_PICK_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CAPTURE_PICTURE) {
            try {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;

                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
                filePath = destination.getAbsolutePath();

                edFileName.setText(filePath);
                edFileName.setSelection(edFileName.getText().length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String filePath = "";
    private ImageChooserManager imageChooserManager;
    private int chooserType;

    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(getActivity(), chooserType, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                filePath = image.getFilePathOriginal();

                edFileName.setText(filePath);
                edFileName.setSelection(edFileName.getText().length());
            }
        });
    }

    @Override
    public void onError(String s) {
        Log.e("Error", ":" + s);
    }

    @Override
    public void onImagesChosen(final ChosenImages images) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "On Images Chosen: " + images.size());
                onImageChosen(images.getImage(0));
            }
        });
    }
}