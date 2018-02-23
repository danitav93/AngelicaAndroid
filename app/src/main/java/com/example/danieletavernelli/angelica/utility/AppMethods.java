package com.example.danieletavernelli.angelica.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danieletavernelli.angelica.R;
import com.example.danieletavernelli.angelica.entity.ViewUtente;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Daniele Tavernelli on 2/12/2018.
 */

public class AppMethods {

    public static ArrayList<String> getCollocazioneResultFromUriIntent(String uri) {

        ArrayList<String> toReturn = new ArrayList<>();

        String[] rows = uri.split("#");

        toReturn.addAll(Arrays.asList(rows));

        return toReturn;

    }

    public static void setCursorColor(EditText view, @ColorInt int color) {
        try {
            // Get the cursor resource id
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(view);

            // Get the editor
            field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(view);

            // Get the drawable and set a color filter
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableResId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {drawable, drawable};

            // Set the drawables
            field = editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(editor, drawables);
        } catch (Exception ignored) {
        }
    }

    //get Icon based on user role
    public static Drawable getUserIconeBasedOnRole(Context context, ViewUtente utente) {

        Integer iconId = null;

        switch ((int) (long) utente.getIdRuolo()) {

            case Constants.SUPERVISORE:
                iconId = R.drawable.supervisor_icon;
                break;
            case Constants.UTENTE_ESTERNO:
                iconId = R.drawable.utente_esterno_icon;
                break;
            case Constants.UTENTE_INTERNO:
                iconId = R.drawable.utente_interno_icon;
                break;
        }

        return context.getResources().getDrawable(iconId);

    }


    //Check if google play services are available
    public static boolean isGooglePlayServicesAvailable(Context context){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }

    //allow users to download Google Play services from the Play Store.
    public static void makeGooglePlayServicesAvailable(Activity context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        googleApiAvailability.makeGooglePlayServicesAvailable(context);
    }
}
