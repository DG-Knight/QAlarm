package com.ict.dg_knight.qalarm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
/**
 * Created by DG-Knight on 24/10/2559.
 */

public class MyIntro extends AppIntro {
    //  DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

        //adding the slides for introduction app
        addSlide(AppIntroSlider.newInstance(R.layout.app_intro1));
//        addSlide(AppIntroSlider.newInstance(R.layout.app_intro2));
        addSlide(AppIntroSlider.newInstance(R.layout.app_intro3));
        addSlide(AppIntroSlider.newInstance(R.layout.app_intro4));

        // Show and Hide Skip and Done buttons
        showStatusBar(false);
        showSkipButton(false);

        // Turn vibration on and set intensity
        // will need to add VIBRATE permission in Manifest file
        setVibrate(true);
        setVibrateIntensity(30);

        //Add animation to the intro slider
//        setDepthAnimation();
        setFadeAnimation();
    }

    @Override
    public void onSkipPressed() {
        // Do something here when users click or tap on Skip button.
        Toast.makeText(getApplicationContext(),
                getString(R.string.app_intro_skip), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onNextPressed() {
        // Do something here when users click or tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do something here when users click or tap tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something here when slide is changed
    }
}
