package com.example.danieletavernelli.angelica.rest.service;

import com.example.danieletavernelli.angelica.entity.Collocazione;
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

    @POST(Constants.LOGIN_PATH)
    Call<ViewUtente> loginUser(@Body ViewUtente viewUtente);

    @GET(Constants.COLLOCAZIONE_PATH)
    Call<List<Collocazione>> getPageCollocazione(@Query("page") int page,
                                                @Query("size") int size,
                                                @Query("collocazione") String collocazione );

}
