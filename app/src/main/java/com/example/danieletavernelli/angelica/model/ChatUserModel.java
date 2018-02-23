package com.example.danieletavernelli.angelica.model;

import com.example.danieletavernelli.angelica.entity.ViewUtente;
import com.stfalcon.chatkit.commons.models.IUser;

/**
  Created by Daniele Tavernelli on 2/20/2018.
 */

public class ChatUserModel implements IUser {

    private ViewUtente viewUtente;

    public ChatUserModel(ViewUtente viewUtente) {
        this.viewUtente = viewUtente;
    }

    @Override
    public String getId() {
        return viewUtente.getIdUtente().toString();
    }

    @Override
    public String getName() {
        return viewUtente.getUsername();
    }

    @Override
    public String getAvatar() {
        return null;
    }

}
