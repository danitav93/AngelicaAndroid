package com.example.danieletavernelli.angelica.Singleton;

import com.example.danieletavernelli.angelica.entity.ViewUtente;

/**
 * Created by Daniele Tavernelli on 2/20/2018.
 */

public class UserSingleton {

    private static UserSingleton ourInstance = null;

    ViewUtente viewUtente;

    public static UserSingleton getInstance() {
        if (ourInstance==null) {
            ourInstance = new UserSingleton();
        }
        return ourInstance;
    }

    public void clear() {
        viewUtente = null;
    }

    public void setViewUtente(ViewUtente viewUtente) {
        this.viewUtente = viewUtente;
    }

    public ViewUtente getViewUtente() {
        return viewUtente;
    }


}
