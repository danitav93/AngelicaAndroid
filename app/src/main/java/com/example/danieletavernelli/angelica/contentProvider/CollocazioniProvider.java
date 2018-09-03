package com.example.danieletavernelli.angelica.contentProvider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.danieletavernelli.angelica.entity.Collocazione;
import com.example.danieletavernelli.angelica.rest.service.GitHubService;
import com.example.danieletavernelli.angelica.utility.Constants;
import com.example.tavernelli.daniele.libreriadidanieletavernelli.Methods.ToastMethods;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Provider for collocazioni
 * Created by Daniele Tavernelli on 2/9/2018.
 */

public class CollocazioniProvider extends ContentProvider {

    public static String AUTHORITY = "com.example.danieletavernelli.angelica.CollocazioneProvider";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        String query = uri.getLastPathSegment().toLowerCase();

        List<Collocazione> collocazioni = getCollocazioniSuggestions(query);

        MatrixCursor cursor = new MatrixCursor(new String[] {BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1,"piano","stanza","denominazione","note",SearchManager.SUGGEST_COLUMN_TEXT_2,SearchManager.SUGGEST_COLUMN_INTENT_DATA,SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID});

        assert collocazioni != null;
        for (Collocazione collocazione: collocazioni) {
            cursor.newRow().add(BaseColumns._ID,collocazione.getIdCollocazione())
                    .add(SearchManager.SUGGEST_COLUMN_TEXT_1,collocazione.getCollocazione())
                    .add("piano",collocazione.getPiano())
                    .add("stanza",collocazione.getStanza())
                    .add("denominazione",collocazione.getDenominazione())
                    .add("note",collocazione.getNote())
                    .add(SearchManager.SUGGEST_COLUMN_TEXT_2,"Piano: "+collocazione.getPiano()+"    Stanza: "+collocazione.getStanza())
                    .add(SearchManager.SUGGEST_COLUMN_INTENT_DATA,collocazione.toUri());
        }


        return cursor;

    }

    private List<Collocazione> getCollocazioniSuggestions(String query) {

        try {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL )
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            GitHubService service = retrofit.create(GitHubService.class);

            Call<List<Collocazione>> call = service.getPageCollocazione(1,5,query);

            Response<List<Collocazione>> response = call.execute();

            if (response.isSuccessful() && response.code() == HttpURLConnection.HTTP_OK) {

                return response.body();

            } else {

                    return new ArrayList<>();

            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastMethods.showShortToast(getContext(),"Attenzione, problema di comunicazione con il server.");
            return new ArrayList<>();
        }


    }



    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}
