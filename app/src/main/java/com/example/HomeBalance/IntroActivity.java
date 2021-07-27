package com.example.HomeBalance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;


/**
 * Controller für die Anzeige des App-Intros mit Nutzung einer Library
 */
public class IntroActivity extends AppIntro {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        checkFirstOpen();
        super.onCreate(savedInstanceState);

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        addSlide(AppIntroFragment.newInstance(getString(R.string.introTitle1), getString(R.string.introDescr1), R.drawable.intro1, Color.BLACK));
        addSlide(AppIntroFragment.newInstance(getString(R.string.introTitle2), getString(R.string.introDescr2), R.drawable.intro2, Color.BLACK));
    }


    /**
     * Bei Klick auf den Skip-Button wird die DatenEingabeActivity Klasse aufgerufen
     */
    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(),DatenEingabeActivity.class);
        startActivity(intent);
    }

    /**
     * Bei Klick auf den Fertig-Button wird die DatenEingabeActivity Klasse aufgerufen
     */
    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
            Intent intent = new Intent(getApplicationContext(), DatenEingabeActivity.class);
            startActivity(intent);
    }

    /**
     * Die Methode checkt, ob die App schon einmal geöffnet wurde, um das Intro ggfls. nicht mehr anzeigen zu müssen
     */
    private void checkFirstOpen(){
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
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


