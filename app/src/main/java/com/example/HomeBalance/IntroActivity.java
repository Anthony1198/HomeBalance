package com.example.HomeBalance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro2 {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        checkFirstOpen();

        super.onCreate(savedInstanceState);




        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        addSlide(AppIntroFragment.newInstance(getString(R.string.introTitle1), getString(R.string.introDescr1), R.drawable.intro1, Color.BLACK));
        addSlide(AppIntroFragment.newInstance(getString(R.string.introTitle2), getString(R.string.introDescr2), R.drawable.intro2, Color.BLACK));

    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
            Intent intent = new Intent(getApplicationContext(), DatenEingabeActivity.class);
            startActivity(intent);
    }

    private void checkFirstOpen(){
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (!isFirstRun) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun",
                false).apply();
    }
}


