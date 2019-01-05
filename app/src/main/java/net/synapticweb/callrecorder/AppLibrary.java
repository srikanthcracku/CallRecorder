package net.synapticweb.callrecorder;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;

public class AppLibrary {
    public static final int SQLITE_TRUE = 1;
    public static final int SQLITE_FALSE = 0;
    public static final int UNKNOWN_TYPE_PHONE_CODE = -1;

    //https://stackoverflow.com/questions/2760995/arraylist-initialization-equivalent-to-array-initialization
    public static final List<PhoneTypeContainer> PHONE_TYPES = new ArrayList<>(Arrays.asList(
            new PhoneTypeContainer(1, "Home"),
            new PhoneTypeContainer(2, "Mobile"),
            new PhoneTypeContainer(3, "Work"),
            new PhoneTypeContainer(-1, "Unknown"),
            new PhoneTypeContainer(7, "Other")
    ));

    //http://tools.medialab.sciences-po.fr/iwanthue/
    public static final List<Integer> colorList = new ArrayList<>(Arrays.asList(
            0xFFa18c00,
            0xFF5300b2,
            0xFF37ca00,
            0xFFc70089,
            0xFF01da57,
            0xFFf81a6e,
            0xFF549b00,
            0xFFde61e1,
            0xFF73b100,
            0xFF311d41,
            0xFFef9515,
            0xFF0298d2,
            0xFF952900,
            0xFF00a5a8,
            0xFF382200,
            0xFF00735e
    ));

    //https://stackoverflow.com/questions/3659809/where-am-i-get-country
    //De văzut și https://stackoverflow.com/questions/26971806/unexpected-telephonymanager-getsimcountryiso-behaviour
    @Nullable
    public static String getUserCountry(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(tm != null) {
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toUpperCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toUpperCase(Locale.US);
                }
            }
        }
        return null;
    }

    //https://stackoverflow.com/questions/625433/how-to-convert-milliseconds-to-x-mins-x-seconds-in-java
    public static String getDurationHuman(long millis, boolean spokenStyle) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        if(spokenStyle) {
            String duration = "";
            if(hours > 0)
                duration += (hours + " hour" + (hours > 1 ? "s" : ""));
            if(minutes > 0)
                duration += ((hours > 0 ? ", " : "") + minutes + " minute" + (minutes > 1 ? "s" : "") );
            if(seconds > 0)
                duration += ((minutes > 0 || hours > 0 ? ", " : "") + seconds + " second" + (seconds > 1 ? "s" : "") );
            return duration;
        }
        else {
            if (hours > 0)
                return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
            else
                return String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
    }

    public static String getFileSizeHuman(long size) {
        int numUnits = (int) size / 1024;
        String unit = "KB";
        if(numUnits > 1000) {
            numUnits = (int) size / 1048576;
            unit = "MB";
            if(numUnits > 1000) {
                numUnits = (int) (size / 1099511627776L);
                unit = "GB";
            }
        }
        return numUnits + unit;
    }

    //https://stackoverflow.com/questions/4605527/converting-pixels-to-dp
    public static int pxFromDp(final Context context, final int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

}
