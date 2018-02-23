package com.example.danieletavernelli.angelica.model;

import com.example.danieletavernelli.angelica.entity.Messaggio;
import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.Date;

/**
 * Created by Daniele Tavernelli on 2/20/2018.
 */

public class ChatMessaggioModel implements IMessage {

    private ChatUserModel chatUserModel;
    private Messaggio messaggio;

    public ChatMessaggioModel(ChatUserModel chatUserModel,Messaggio messaggio) {
        this.chatUserModel = chatUserModel;
        this.messaggio=messaggio;
    }

    @Override
    public String getId() {
        return messaggio.getIdMessaggio().toString();
    }

    @Override
    public String getText() {
        return messaggio.getBody();
    }

    @Override
    public ChatUserModel getUser() {
        return chatUserModel;
    }

    @Override
    public Date getCreatedAt() {
        return messaggio.getData();
    }


}
