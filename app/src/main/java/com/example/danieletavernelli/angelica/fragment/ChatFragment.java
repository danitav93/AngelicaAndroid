package com.example.danieletavernelli.angelica.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.danieletavernelli.angelica.R;
import com.example.danieletavernelli.angelica.Singleton.UserSingleton;
import com.example.danieletavernelli.angelica.adapter.ListViewChatAdapter;
import com.example.danieletavernelli.angelica.entity.ViewUtente;
import com.example.danieletavernelli.angelica.rest.service.GitHubService;
import com.example.danieletavernelli.angelica.utility.Constants;
import com.example.tavernelli.daniele.libreriadidanieletavernelli.Methods.ToastMethods;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Fragment for chat
 * Created by Daniele Tavernelli on 2/13/2018.
 */

public class ChatFragment  extends Fragment {

    public static final String NAME = "Messaggi";

    Context context ;

    //layout
    private View entireLayout;
    private ListView listView;

    //data
    private List<ViewUtente> users = new ArrayList<>();


    public static ChatFragment newInstance() {

        return  new ChatFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();

        entireLayout = inflater.inflate(R.layout.fragment_chat, container, false);

        setData();

        setListViewOfUser();

        return entireLayout;

    }

    @SuppressLint("StaticFieldLeak")
    private void setData() {

        new AsyncTask<Void,Void,Boolean>() {

            Response<List<ViewUtente>> response;

            @Override
            protected Boolean doInBackground(Void... voids) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.SVILUPPO_BASE_URL_1 )
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build();

                GitHubService service = retrofit.create(GitHubService.class);

                Call<List<ViewUtente>> call = service.getUserListForMessage(UserSingleton.getInstance().getViewUtente().getIdUtente());

                try {

                    response = call.execute();

                    if (response.isSuccessful() && response.code() == HttpURLConnection.HTTP_OK) {
                        users.addAll(response.body());
                        return true;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                if (success) {
                    ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
                } else if (response!=null){
                        ToastMethods.showShortToast(context, "Errore " + response.code());
                } else {
                    ToastMethods.showShortToast(context, "Errore nella comunicazione con il server");
                }

            }
        }.execute();


    }

    private void setListViewOfUser() {



        listView = entireLayout.findViewById(R.id.fragment_chat_list_view);

        ListViewChatAdapter listViewChatAdapter = new ListViewChatAdapter(getContext(),R.layout.chat_list_view_row,users);

        listView.setAdapter(listViewChatAdapter);




    }


}
