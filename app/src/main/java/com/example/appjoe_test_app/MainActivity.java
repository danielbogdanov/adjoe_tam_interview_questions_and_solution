package com.example.appjoe_test_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import io.adjoe.sdk.Playtime;
import io.adjoe.sdk.PlaytimeException;
import io.adjoe.sdk.PlaytimeInitialisationListener;
import io.adjoe.sdk.PlaytimeNotInitializedException;
import io.adjoe.sdk.PlaytimeOptions;
import io.adjoe.sdk.PlaytimeParams;
import io.adjoe.sdk.PlaytimeUserProfile;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserProfileUtils userProfileUtils = new UserProfileUtils();
        PlaytimeUserProfile userProfile = userProfileUtils.playtimeUserProfile();

        //Set params
        PlaytimeParams playtimeParams = new PlaytimeParams.Builder()
                .setUaNetwork("LinkedIn")
                .setUaChannel("Email")
                .setUaSubPublisherCleartext("com.domain.appName")
                .setUaSubPublisherEncrypted("SubPublisherEncrypted")
                .setPlacement("Interstitial")
                .build();
        Playtime.setUAParams(this, playtimeParams);

        // Create PlaytimeOptions with userId, params, and other configurations
        PlaytimeOptions options = new PlaytimeOptions()
                .setUserId("DanyBoy")
                .setParams(playtimeParams)
                .setUserProfile(userProfile);

        // Initialize Playtime SDK with options
        Playtime.init(this, "1ba0c6f1ac7ac4f09b245838ec23e366", options, new PlaytimeInitialisationListener() {

            @Override
            public void onInitialisationFinished() {
                // the Playtime SDK was initialized successfully
                Log.d("Playtime", "Initialization finished successfully");

                // Now that initialization is complete, send the teaser event
                addTeaser(playtimeParams);
            }

            @Override
            public void onInitialisationError(Exception exception) {
                // an error occurred while initializing the Playtime SDK.
                Log.d("Playtime", "OOOPS error: " + (exception != null ? exception.getMessage() : "OTHER error"));
            }
        });

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set the greeting by getting the user ID
        TextView greeting = findViewById(R.id.textView);
        greeting.setText("Hi " + Playtime.getUserId(this));
        Log.d("Playtime", "User ID: " + Playtime.getUserId(this));

        // Show the version of the Playtime SDK
        TextView version = findViewById(R.id.textView2);
        version.setText("Playtime SDK Version: " + Playtime.getVersionName());

        // Add a button to open the catalog
        Button button = findViewById(R.id.button);
        button.setText("Open Catalog");
        button.setOnClickListener(v -> {
            // Handle button click
            Log.d("Dany", "Button clicked, opening catalog");
            openCatalog(playtimeParams);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void openCatalog(PlaytimeParams playtimeParams) {

        Log.d("Playtime", "Open Catalog method called, but handled in button click listener");
        try {
            Intent playtimeCatalogIntent = Playtime.getCatalogIntent(this, playtimeParams);
            this.startActivity(playtimeCatalogIntent);
        }
        catch (PlaytimeNotInitializedException notInitializedException) {
            // you have not initialized the Playtime SDK
        }
        catch (PlaytimeException exception) {
            // the catalog cannot be displayed for some other reason
        }
    }
    private void addTeaser(PlaytimeParams playtimeParams) {
        if (Playtime.isInitialized()) {
            try {
                Playtime.sendUserEvent(this, Playtime.EVENT_TEASER_SHOWN, null, playtimeParams);
                Log.d("Playtime", "Teaser event sent successfully");
            } catch (PlaytimeNotInitializedException e) {
                Log.d("Playtime", "Playtime SDK is not initialized, cannot send user event: " + e.getMessage());
            }
        } else {
            // Use Handler to retry after a delay
            Log.d("Playtime", "Playtime not initialized yet, retrying in 2 seconds...");
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                addTeaser(playtimeParams);
            }, 2000); // Retry after 2 seconds
        }
    }
}