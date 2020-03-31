package com.postaplus.postascannerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.List;

import webservice.WebService;

//activity used to save the signature
public class OtpActivity extends Activity {
    private static int TIME_IN_MILLISECONDS = 61000;

    Button verifyOtpButton;
    Button resendOtpButton;
    EditText otpEditText;
    TextView counterTextView;
    int timer = 59;
    private List<String> waybill;
    private String driverCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_otp);
        initViews();
        if(getIntent()!=null && getIntent().getExtras()!=null){
            waybill = (List<String>) getIntent().getExtras().getSerializable("wayBillList");
            driverCode = getIntent().getExtras().getString("drivercode");
        }

        verifyOtpButton.setOnClickListener(v -> {
            if (otpEditText.getText().toString().length() > 0) {
                verifyAction(otpEditText.getText().toString());
            } else {
                Toast.makeText(OtpActivity.this, "Please enter the otp value", Toast.LENGTH_LONG).show();
            }
        });
        resendOtpButton.setOnClickListener(v -> {
            resendAction();
        });


    }

    private void initViews() {
        verifyOtpButton = findViewById(R.id.verifyOtp_button);
        resendOtpButton = findViewById(R.id.resendOtp_button);
        otpEditText = findViewById(R.id.otp_edittext);
        counterTextView = findViewById(R.id.counter_textView);
    }

    private void resendAction() {
        resendOtp();
    }

    private void startDownCounter() {
        counterDownFinished(false, View.VISIBLE);
        timer = 59;
        new CountDownTimer(TIME_IN_MILLISECONDS, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                counterTextView.setText("00:" + checkDigit(timer--));
            }

            @Override
            public void onFinish() {
                counterDownFinished(true, View.INVISIBLE);

            }
        }.start();
    }

    private void verifyAction(String otp) {
        try {
            String result = WebService.otpVerification(driverCode,otp,waybill);
            if(result.contains("TRUE"))
                verifiedSucceed();
            else
                verifiedFailed(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void resendOtp(){
        try {
            String result = WebService.resendOtp(driverCode,waybill);
            if(result.contains("TRUE"))
                startDownCounter();
            else
                verifiedFailed(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
    private void verifiedFailed(String result) {
        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
        counterDownFinished(true, View.INVISIBLE);

    }
    private void counterDownFinished(boolean isResendButtonEnabled, int invisible) {
        resendOtpButton.setEnabled(isResendButtonEnabled);
        counterTextView.setVisibility(invisible);
    }
    public void verifiedSucceed(){
           Intent intent = new Intent();
           setResult(12, intent);
           finish();
    }
}
