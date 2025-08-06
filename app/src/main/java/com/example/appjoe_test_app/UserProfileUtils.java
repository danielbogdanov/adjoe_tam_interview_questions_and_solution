package com.example.appjoe_test_app;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import io.adjoe.sdk.PlaytimeGender;
import io.adjoe.sdk.PlaytimeUserProfile;

public class UserProfileUtils {

    public static PlaytimeUserProfile playtimeUserProfile() {
        PlaytimeGender gender = getPlaytimeGender(getUserGender());
        Date birthday = getBirthday();
        Log.d("Dany",getBirthday().toString());
        Log.d("Dany",gender.toString());
        return new PlaytimeUserProfile(gender, birthday);
    }

    private static PlaytimeGender getPlaytimeGender(String userGender) {
        switch (userGender) {
            case "male":
                return PlaytimeGender.MALE;
            case "female":
                return PlaytimeGender.FEMALE;
            default:
                return PlaytimeGender.UNKNOWN;
        }
    }

    private static String getUserGender() {
        // Implement this method to get the user's gender
        return "male"; // Example placeholder
    }

    private static Date getBirthday() {
        if (isExactBirthdayKnown()) {
            return getUserBirthday();
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, getYearOfBirth());
            calendar.set(Calendar.MONTH, Calendar.APRIL);
            calendar.set(Calendar.DAY_OF_MONTH, 10);
            return calendar.getTime();
        }
    }

    private static Date getUserBirthday() {
        // Implement this method to get the user's birthday
        return new Date(); // Example placeholder
    }

    private static boolean isExactBirthdayKnown() {
        // Implement this method to determine if the exact birthday is known
        return false; // Example placeholder
    }

    private static int getYearOfBirth() {
        // Implement this method to get the user's year of birth
        return 1990; // Example placeholder
    }
}