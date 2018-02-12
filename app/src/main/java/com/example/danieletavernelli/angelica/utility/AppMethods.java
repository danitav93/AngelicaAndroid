package com.example.danieletavernelli.angelica.utility;

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

}
