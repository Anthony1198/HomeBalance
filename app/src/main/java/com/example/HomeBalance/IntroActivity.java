package com.example.HomeBalance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro2 {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addSlide(AppIntroFragment.newInstance(getString(R.string.introTitle1), getString(R.string.introDescr1), R.drawable.intro1, Color.BLACK));

        addSlide(AppIntroFragment.newInstance(getString(R.string.introTitle2), getString(R.string.introDescr2), R.drawable.intro2, Color.BLACK));

        setImmersiveMode();
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(),DatenEingabe.class);
        startActivity(intent);
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}


