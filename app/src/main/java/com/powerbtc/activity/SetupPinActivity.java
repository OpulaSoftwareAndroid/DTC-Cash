package com.powerbtc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.powerbtc.Constants.AppGlobal;
import com.powerbtc.Constants.WsConstant;
import com.powerbtc.MainActivity;
import com.powerbtc.R;


@SuppressWarnings("ALL")
public class SetupPinActivity extends BaseActivity {
    private TextView tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven, tvEight, tvNine, tvZero;
    private LinearLayout lvDelete, lvReset;
    private ImageView imgCodeOne, imgCodeTwo, imgCodeThree, imgCodeFour;
    private String stCode1 = "", stCode2 = "", stCode3 = "", stCode4 = "";
    private String stPin = "", stConfirmPin = "", stFrom = "", stDoublePin = "";
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_pin);

        Intent intent = getIntent();
        if (intent != null) {
            stFrom = intent.getStringExtra("From");
        }

        init();
        setClickEvent();
    }

    private void init() {
        imgCodeOne = (ImageView) findViewById(R.id.img_PIN_Code_One);
        imgCodeTwo = (ImageView) findViewById(R.id.img_PIN_Code_Two);
        imgCodeThree = (ImageView) findViewById(R.id.img_PIN_Code_Three);
        imgCodeFour = (ImageView) findViewById(R.id.img_PIN_Code_Four);

        tvOne = (TextView) findViewById(R.id.tv_PIN_One);
        tvTwo = (TextView) findViewById(R.id.tv_PIN_Two);
        tvThree = (TextView) findViewById(R.id.tv_PIN_Three);
        tvFour = (TextView) findViewById(R.id.tv_PIN_Four);
        tvFive = (TextView) findViewById(R.id.tv_PIN_Five);
        tvSix = (TextView) findViewById(R.id.tv_PIN_Six);
        tvSeven = (TextView) findViewById(R.id.tv_PIN_Seven);
        tvEight = (TextView) findViewById(R.id.tv_PIN_Eight);
        tvNine = (TextView) findViewById(R.id.tv_PIN_Nine);
        tvZero = (TextView) findViewById(R.id.tv_PIN_Zero);

        tvTitle = (TextView) findViewById(R.id.tv_Title);

        lvDelete = (LinearLayout) findViewById(R.id.linear_PIN_Delete);
        lvReset = (LinearLayout) findViewById(R.id.linear_Pin_Reset);
    }

    private void setClickEvent() {
        tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(tvOne.getText().toString());
            }
        });

        tvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(tvTwo.getText().toString());
            }
        });

        tvThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(tvThree.getText().toString());
            }
        });

        tvFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(tvFour.getText().toString());
            }
        });

        tvFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(tvFive.getText().toString());
            }
        });

        tvSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(tvSix.getText().toString());
            }
        });

        tvSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(tvSeven.getText().toString());
            }
        });

        tvEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(tvEight.getText().toString());
            }
        });

        tvNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(tvNine.getText().toString());
            }
        });

        tvZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click(tvZero.getText().toString());
            }
        });

        lvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });

        lvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
    }

    private void reset() {
        stCode1 = "";
        stCode2 = "";
        stCode3 = "";
        stCode4 = "";

        stPin = "";
        stConfirmPin = "";

        tvTitle.setText("Please enter 4 digit pin");

        imgCodeFour.setImageResource(R.drawable.image_white_dot);
        imgCodeThree.setImageResource(R.drawable.image_white_dot);
        imgCodeTwo.setImageResource(R.drawable.image_white_dot);
        imgCodeOne.setImageResource(R.drawable.image_white_dot);
    }

    private void clear() {
        if (!stCode4.equalsIgnoreCase("")) {
            stCode4 = "";
            imgCodeFour.setImageResource(R.drawable.image_white_dot);
        } else if (!stCode3.equalsIgnoreCase("")) {
            stCode3 = "";
            imgCodeThree.setImageResource(R.drawable.image_white_dot);
        } else if (!stCode2.equalsIgnoreCase("")) {
            stCode2 = "";
            imgCodeTwo.setImageResource(R.drawable.image_white_dot);
        } else if (!stCode1.equalsIgnoreCase("")) {
            stCode1 = "";
            imgCodeOne.setImageResource(R.drawable.image_white_dot);
        }
    }

    int wrongPinCounter = 0;

    private void click(String code) {
        if (stCode1.equalsIgnoreCase("")) {
            stCode1 = code;
            imgCodeOne.setImageResource(R.drawable.image_black_dot);
        } else if (stCode2.equalsIgnoreCase("")) {
            stCode2 = code;
            imgCodeTwo.setImageResource(R.drawable.image_black_dot);
        } else if (stCode3.equalsIgnoreCase("")) {
            stCode3 = code;
            imgCodeThree.setImageResource(R.drawable.image_black_dot);
        } else if (stCode4.equalsIgnoreCase("")) {
            stCode4 = code;
            imgCodeFour.setImageResource(R.drawable.image_black_dot);

            String stPIN = stCode1 + stCode2 + stCode3 + stCode4;

            if (stFrom.equalsIgnoreCase("set_pin")) {
                if (stPin.equals("")) {

                    stPin = stPIN;
                    tvTitle.setText("Confirm a 4 digit pin");

                    stCode1 = "";
                    stCode2 = "";
                    stCode3 = "";
                    stCode4 = "";

                    imgCodeFour.setImageResource(R.drawable.image_white_dot);
                    imgCodeThree.setImageResource(R.drawable.image_white_dot);
                    imgCodeTwo.setImageResource(R.drawable.image_white_dot);
                    imgCodeOne.setImageResource(R.drawable.image_white_dot);
                } else {
                    stConfirmPin = stPIN;
                    if (!stPin.equals(stConfirmPin)) {
                        reset();

                        Toast.makeText(SetupPinActivity.this, "Pin Confirm failed, Please try again", Toast.LENGTH_SHORT).show();
                    } else {
                        //Call SetupPin API
                        AppGlobal.setStringPreference(SetupPinActivity.this, stConfirmPin, WsConstant.SP_LOGIN_SET_PIN);

                        Toast.makeText(SetupPinActivity.this, "Pin Confirmed !!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SetupPinActivity.this, MainActivity.class));
                        SetupPinActivity.this.finish();
                    }
                }
            } else if (stFrom.equalsIgnoreCase("check_pin")) {
                if (stPIN.equalsIgnoreCase(AppGlobal.getStringPreference(SetupPinActivity.this, WsConstant.SP_LOGIN_SET_PIN))) {
                    Intent intent = new Intent(SetupPinActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    reset();
                    Toast.makeText(SetupPinActivity.this, "Wrong PIN, Please try again", Toast.LENGTH_SHORT).show();
                }
            } else if (stFrom.equalsIgnoreCase("set_result")) {
                if (stPIN.equalsIgnoreCase(AppGlobal.getStringPreference(SetupPinActivity.this, WsConstant.SP_LOGIN_SET_PIN))) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    reset();
                    Toast.makeText(SetupPinActivity.this, "Wrong PIN, Please try again", Toast.LENGTH_SHORT).show();
                }
            } else if (stFrom.equalsIgnoreCase("change_pin")) {
                if (stPin.equals("")) {

                    if (!AppGlobal.getStringPreference(SetupPinActivity.this, WsConstant.SP_LOGIN_SET_PIN).equalsIgnoreCase(stPIN)) {
                        if (wrongPinCounter == 4) {

                            SetupPinActivity.this.finish();
                        } else {
                            reset();

                            Toast.makeText(SetupPinActivity.this, "Wrong PIN, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        stPin = stPIN;
                        tvTitle.setText("enter new 4 digit pin");

                        stCode1 = "";
                        stCode2 = "";
                        stCode3 = "";
                        stCode4 = "";
                        imgCodeFour.setImageResource(R.drawable.image_white_dot);
                        imgCodeThree.setImageResource(R.drawable.image_white_dot);
                        imgCodeTwo.setImageResource(R.drawable.image_white_dot);
                        imgCodeOne.setImageResource(R.drawable.image_white_dot);
                    }
                } else if (stDoublePin.equals("")) {


                    stDoublePin = stPIN;
                    tvTitle.setText("Confirm a 4 digit pin");

                    stCode1 = "";
                    stCode2 = "";
                    stCode3 = "";
                    stCode4 = "";
                    imgCodeFour.setImageResource(R.drawable.image_white_dot);
                    imgCodeThree.setImageResource(R.drawable.image_white_dot);
                    imgCodeTwo.setImageResource(R.drawable.image_white_dot);
                    imgCodeOne.setImageResource(R.drawable.image_white_dot);

                } else {
                    stConfirmPin = stPIN;
                    if (!stDoublePin.equals(stConfirmPin)) {
                        reset();

                        stDoublePin = "";

                        Toast.makeText(SetupPinActivity.this, "Pin Confirm failed, Please try again", Toast.LENGTH_SHORT).show();
                    } else {

                        //Call SetupPin API
                        AppGlobal.setStringPreference(SetupPinActivity.this, stConfirmPin, WsConstant.SP_LOGIN_SET_PIN);

                        SetupPinActivity.this.finish();
                        Toast.makeText(SetupPinActivity.this, "Pin Changed !!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
