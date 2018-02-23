package com.example.danieletavernelli.angelica.rest.service;

import com.example.danieletavernelli.angelica.entity.Collocazione;
import com.example.danieletavernelli.angelica.entity.Messaggio;
import com.example.danieletavernelli.angelica.entity.ViewUtente;
import com.example.danieletavernelli.angelica.utility.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by Daniele Tavernelli on 2/5/2018.
 * retrofit interface
 */

public interface GitHubService {


    //POST

    @POST(Constants.LOGIN_PATH)
    Call<ViewUtente> loginUser(@Body ViewUtente viewUtente);

    @POST(Constants.SAVE_MESSAGGIO_PATH)
    Call<Boolean> saveMessaggio(@Body Messaggio messaggio);

    @POST(Constants.REFRESH_TOKEN)
    Call<Boolean> refreshToken(@Query("refreshToken") String refreshToken, @Query("idUtente") long idUtente);


    //GET

    @GET(Constants.COLLOCAZIONE_PATH)
    Call<List<Collocazione>> getPageCollocazione(@Query("page") int page,
                                                @Query("size") int size,
                                                @Query("collocazione") String collocazione );

    @GET(Constants.USER_LIST_FOR_MESSAGE_PATH)
    Call<List<ViewUtente>> getUserListForMessage(@Query("id_utente") long id_utente);

    @GET(Constants.CHAT_PATH)
    Call<List<Messaggio>> getChat(@Query("page") int page,
                                  @Query("size") int size,
                                  @Query("id_mittente") long id_mittente,
                                  @Query("id_destinatario") long id_destinatario);

    @GET(Constants.GET_MESSAGGIO_PATH)
    Call<Messaggio> getMessaggio(@Query("id_messaggio") long idMessaggio);
}
