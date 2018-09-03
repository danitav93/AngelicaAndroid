package com.example.danieletavernelli.angelica.utility;

/**
 * Created by Daniele Tavernelli on 2/5/2018.
 * CONSTANTS
 */

public class Constants {

    //FB MESSAGE FORM IS id_messaggio
    //chat
    public static final String KEY_ID_MESSAGGIO = "idMessaggio";



    //rest
    public static final String BASE_URL="http://192.168.0.170:8080/";
    //public static final String BASE_URL="http://angelica.eu-west-3.elasticbeanstalk.com/:8080/";
    public static final String LOGIN_PATH = "utente/login/";
    public static final String REFRESH_TOKEN ="utente/refreshToken/" ;
    public static final String COLLOCAZIONE_PATH ="collocazione";
    public static final String USER_LIST_FOR_MESSAGE_PATH ="utente/listForMessage/";
    public static final String CHAT_PATH ="messaggio/chat/";
    public static final String SAVE_MESSAGGIO_PATH ="messaggio/save/";
    public static final String GET_MESSAGGIO_PATH = "messaggio/get/";
    public static final String SET_MESSAGGIO_LETTO_PATH = "messaggio/setMessaggioLetto/";

    //RUOLI
    public static final int SUPERVISORE = 1;
    public static final int UTENTE_ESTERNO = 2;
    public static final int UTENTE_INTERNO = 3;



}
