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


        addSlide(AppIntroFragment.newInstance("Willkommen bei HomeBalance!",
                "HomeBalance trägt zur Balance deiner Arbeits- Lebens- Schlaf- und Aktivitätszeiten bei",
                R.drawable.intro1, Color.BLACK));

        addSlide(AppIntroFragment.newInstance(
                "Daten eingeben und Unterstützung erhalten",
                "Durch unser intelligentes System erhälst du täglich an dich angepasste Vorschläge, um deinen Alltag zu strukturieren, auszugleichen und vieles Mehr",
                R.drawable.intro2, Color.BLACK
        ));

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


