package com.example.a2amunch;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import io.paperdb.Paper;

public class IntroActivity extends AppCompatActivity {

    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Paper.init(this);

        lottieAnimationView=findViewById(R.id.lottie);

        lottieAnimationView.animate().translationY(1600).setDuration(1000).setStartDelay(4000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(TextUtils.isEmpty(Paper.book().read("password")) && TextUtils.isEmpty(Paper.book().read("phonenumber")) )
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    CurrentRequest.current_number = Paper.book().read("phonenumber").toString();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}