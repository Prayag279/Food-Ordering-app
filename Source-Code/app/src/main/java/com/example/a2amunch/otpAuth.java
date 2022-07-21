package com.example.a2amunch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class otpAuth extends AppCompatActivity {
    String key,phnumber;
    EditText ph_num,otp_code;
    String typed_otp;
    Button check_otp,send_otp;
    FirebaseAuth auth;
    ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_auth);


        loadingbar = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        ph_num = findViewById(R.id.ph_num);
        otp_code = findViewById(R.id.otp_code);
        check_otp= findViewById(R.id.check_otp);
        send_otp= findViewById(R.id.send_otp);
        ph_num.setVisibility(View.VISIBLE);
        otp_code.setVisibility(View.GONE);
        check_otp.setVisibility(View.GONE);
        ph_num.setEnabled(true);

        if(CurrentRequest.forget_pass)
        {
            ph_num.setText(CurrentRequest.current_number);
        }
        else if(CurrentRequest.chnage_pass_frm_profile)
        {
            CurrentRequest.chnage_pass_frm_profile = false;
            ph_num.setText(CurrentRequest.current_number);
            ph_num.setEnabled(false);
        }
        else
        {
            CurrentRequest.current_number = "";
        }






    }

    //signin user
    private void signin(PhoneAuthCredential cred) {

            auth.signInWithCredential(cred).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {

                    //check otp request came from froget password or not
                    if(CurrentRequest.forget_pass)
                    {
                        CurrentRequest.forget_pass = false;
                        Intent i = new Intent(this,ForgetPassword.class);
                        startActivity(i);
                        auth.signOut();
                        FirebaseAuth.getInstance().signOut();

                        finish();
                    }
                    else
                    {
                        Intent i = new Intent(this, createAccount.class);
                        startActivity(i);
                        auth.signOut();
                        FirebaseAuth.getInstance().signOut();

                        finish();
                    }

                }
                else
                {
                    Toast.makeText(otpAuth.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    FirebaseAuth.getInstance().signOut();

                    finish();

                }



            });



    }

    public void sendOTP(View view)
    {


        if(ph_num.getText().toString().equals(""))
        {
            Toast.makeText(this, "Enter Number", Toast.LENGTH_SHORT).show();
        }
        else if(ph_num.getText().toString().length() > 10 || ph_num.getText().toString().length() < 10 )
        {
            Toast.makeText(this, "Enter legit Number", Toast.LENGTH_SHORT).show();
        }
        else
        {
            CurrentRequest.current_number = ph_num.getText().toString();
            phnumber = ph_num.getText().toString();
            loadingbar.setTitle("Checking Details");
            loadingbar.setMessage("Wait for a while");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            if(CurrentRequest.forget_pass)
            {
                //check otp request came from froget password then numbber supposd to be exist
                sendingotp();
            }
            else
            {
                DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
                rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("users").child(phnumber).exists()) {
                            //check otp request came from froget password then numbber supposd to be exist
                            Toast.makeText(otpAuth.this, "Number exist already!!", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();

                        } else {
                            sendingotp();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError e) {
                        Toast.makeText(otpAuth.this, ""+e, Toast.LENGTH_SHORT).show();

                    }
                });
            }
       }
    }

    private void sendingotp() {
        {

        otp_code.setVisibility(View.VISIBLE);
        check_otp.setVisibility(View.VISIBLE);
        send_otp.setText("Resend OTP");


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91" + phnumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(otpAuth.this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                loadingbar.dismiss();
                                signin(phoneAuthCredential);
                                // jo automatic thyu hse to jate nai thai ekj VAR VARIFY THSE
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                loadingbar.dismiss();

                                if (e instanceof FirebaseTooManyRequestsException) {
                                    Toast.makeText(otpAuth.this, "Too Mnay Request \n Try Again Later", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(otpAuth.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                                }
                                if(CurrentRequest.forget_pass)
                                {
                                    CurrentRequest.forget_pass = false;
                                    Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                                    startActivity(i);
                                    auth.signOut();
                                    FirebaseAuth.getInstance().signOut();
                                    Looper.loop();//bad tokken ni exception mate
                                    Looper.myLooper().quit();

                                }

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                key = s;
                                loadingbar.dismiss();
                                Toast.makeText(otpAuth.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    }


    //check otp is coreect
    public void checkOTP(View view) {
        if(otp_code.getText().toString().equals(""))
        {
            Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show();
        }
        else
        {
            try
            {
                typed_otp = otp_code.getText().toString();
                Toast.makeText(this, ""+typed_otp, Toast.LENGTH_SHORT).show();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(key, typed_otp);
                signin(credential);
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void go_back(View view) {
        onBackPressed();
    }
}