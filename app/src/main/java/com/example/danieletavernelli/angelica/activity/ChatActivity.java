package com.example.danieletavernelli.angelica.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.danieletavernelli.angelica.R;
import com.example.danieletavernelli.angelica.Singleton.UserSingleton;
import com.example.danieletavernelli.angelica.entity.Messaggio;
import com.example.danieletavernelli.angelica.entity.ViewUtente;
import com.example.danieletavernelli.angelica.model.ChatMessaggioModel;
import com.example.danieletavernelli.angelica.model.ChatUserModel;
import com.example.danieletavernelli.angelica.rest.service.GitHubService;
import com.example.danieletavernelli.angelica.utility.AppMethods;
import com.example.danieletavernelli.angelica.utility.Constants;
import com.example.tavernelli.daniele.libreriadidanieletavernelli.Methods.DialogMethods;
import com.example.tavernelli.daniele.libreriadidanieletavernelli.Methods.ToastMethods;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * activity oh chat between users
 */
public class ChatActivity extends AppCompatActivity {

    public static String TAG = "Chat activity";
    public static String VIEW_UTENTE_INTERLOCUTORE_EXTRA_TAG = "viwUtenteInterLocutore";

    private Context context = this;



    //DATA
    ViewUtente viewUtenteLoggato = UserSingleton.getInstance().getViewUtente();
    ChatUserModel chatUserModelUtenteLoggato = new ChatUserModel(viewUtenteLoggato);
    ViewUtente viewUtenteInterlocutore;
    ChatUserModel chatUserModelUtenteInterlocutore;

    List<Messaggio> messaggi;

    MessagesListAdapter<ChatMessaggioModel> adapter;

    //layout
    MessagesList messagesList;
    private MessageInput messageInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        viewUtenteInterlocutore = (ViewUtente) getIntent().getSerializableExtra(VIEW_UTENTE_INTERLOCUTORE_EXTRA_TAG);

        chatUserModelUtenteInterlocutore = new ChatUserModel(viewUtenteInterlocutore);

        setMessageList();

        setInputChatMessage();

        setChat();

        setBar();

        MainActivity.broadcaster.get().registerReceiver(mMessageReceiver, new IntentFilter(MainActivity.ACTION_CHAT_MESSAGE_DISPATCHED));



    }

    @Override
    protected void onDestroy() {
        MainActivity.broadcaster.get().unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void setInputChatMessage() {

        messageInput = findViewById(R.id.activity_chat_input);

        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                Messaggio messaggio = new Messaggio();
                messaggio.setBody(input.toString());
                messaggio.setIdDestinatario(viewUtenteInterlocutore.getIdUtente());
                messaggio.setIdMittente(viewUtenteLoggato.getIdUtente());
                messaggio.setData(Calendar.getInstance().getTime());
                messaggio.setLetto(0);
                adapter.addToStart(new ChatMessaggioModel(chatUserModelUtenteLoggato, messaggio), true);

                new saveMessageTask(messaggio, context).execute();

                return true;
            }
        });
    }

    private void setBar() {

        Toolbar toolbarLayout = findViewById(R.id.activity_chat_toolbar);
        setSupportActionBar(toolbarLayout);
        toolbarLayout.setNavigationIcon(R.drawable.back_arrow);
        toolbarLayout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Drawable icon = AppMethods.getUserIconeBasedOnRole(this, viewUtenteInterlocutore);
        icon.setColorFilter(new
                PorterDuffColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP));
        getSupportActionBar().setTitle(viewUtenteInterlocutore.getUsername());
        getSupportActionBar().setSubtitle(viewUtenteInterlocutore.getDescFunc());
        getSupportActionBar().setIcon(icon);

    }

    private void setChat() {

        messagesList = findViewById(R.id.activity_chat_messagesList);

        /*ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String urlString) {
                try {
                    URL url = new URL(urlString);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    imageView.setImageBitmap(bmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };*/

        adapter = new MessagesListAdapter<>(viewUtenteLoggato.getIdUtente().toString(), null);

        /*adapter.setLoadMoreListener(new MessagesListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                int a=1;
            }
        });*/

        messagesList.setAdapter(adapter);

    }

    private void setMessageList() {

        GetMessaggiTask getMessaggiTask = new GetMessaggiTask(this);
        getMessaggiTask.execute();

    }

    private static class GetMessaggiTask extends AsyncTask<Void, Void, Boolean> {

        Response<List<Messaggio>> response;

        private WeakReference<ChatActivity> chatActivity;

        private GetMessaggiTask(ChatActivity chatActivity) {
            this.chatActivity = new WeakReference<>(chatActivity);

        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.SVILUPPO_BASE_URL_1)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            GitHubService service = retrofit.create(GitHubService.class);

            Call<List<Messaggio>> call = service.getChat(1, 20, chatActivity.get().viewUtenteLoggato.getIdUtente(), chatActivity.get().viewUtenteInterlocutore.getIdUtente());

            try {

                response = call.execute();

                if (response.isSuccessful() && response.code() == HttpURLConnection.HTTP_OK) {
                    chatActivity.get().messaggi = response.body();
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
                List<ChatMessaggioModel> listToAdd = new ArrayList<>();
                List<Integer> msgIds = new ArrayList<>();
                for (Messaggio messaggio : chatActivity.get().messaggi) {
                    ChatMessaggioModel chatMessaggioModel;
                    if (messaggio.getIdMittente().equals(chatActivity.get().viewUtenteLoggato.getIdUtente())) {
                        chatMessaggioModel = new ChatMessaggioModel(chatActivity.get().chatUserModelUtenteLoggato, messaggio);
                    } else {
                        chatMessaggioModel = new ChatMessaggioModel(chatActivity.get().chatUserModelUtenteInterlocutore, messaggio);
                    }
                    listToAdd.add(chatMessaggioModel);
                    if (messaggio.getLetto()==0) {
                        msgIds.add(messaggio.getIdMessaggio());
                    }
                }
                if (!msgIds.isEmpty()) {
                    new SetMessaggiLettoTask(msgIds).execute();
                }
                ((MessagesListAdapter<ChatMessaggioModel>) chatActivity.get().messagesList.getAdapter()).addToEnd(listToAdd, false);

            } else if (response != null) {
                ToastMethods.showShortToast(chatActivity.get(), "Errore " + response.code());
            } else {
                ToastMethods.showShortToast(chatActivity.get(), "Errore nella comunicazione con il server");
            }

        }

    }

    public static class saveMessageTask extends AsyncTask<Void, Void, Boolean> {

        private Response<Boolean> response;
        private Exception e;
        private Messaggio messaggio;
        private WeakReference<Context> context;

        saveMessageTask(Messaggio messaggio, Context context) {
            this.messaggio = messaggio;
            this.context = new WeakReference<>(context);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.SVILUPPO_BASE_URL_1)
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build();

                GitHubService service = retrofit.create(GitHubService.class);

                Call<Boolean> call = service.saveMessaggio(messaggio);

                response = call.execute();

                return (response.isSuccessful() && response.code() == HttpURLConnection.HTTP_OK);

            } catch (Exception e) {
                this.e = e;
                e.printStackTrace();
                return false;
            }


        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (!success) {

                if (response != null) {
                    Log.e(TAG, "errore durante la login. Response code: " + response.code());
                    DialogMethods.createMessageAlertDialog(context.get(), "Errore " + response.code(), null);
                } else {
                    String errorToShow = "Errore durante la richiesta ";
                    if (e instanceof SocketTimeoutException) {
                        errorToShow = "Problema di comunicazione con il server";
                    }
                    DialogMethods.createMessageAlertDialog(context.get(), errorToShow, null);
                }

            }
        }

    }

    //Receiver for when arrive message
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(MainActivity.ACTION_CHAT_MESSAGE_DISPATCHED)) {
                Messaggio messaggio = (Messaggio) intent.getSerializableExtra(MainActivity.EXTRA_MESSAGGIO_CHAT_RICEVUTO);
                if (messaggio.getIdMittente().equals(viewUtenteInterlocutore.getIdUtente())) {
                    ChatMessaggioModel chatMessaggioModel = new ChatMessaggioModel(chatUserModelUtenteInterlocutore,messaggio);
                    adapter.addToStart(chatMessaggioModel,true);
                    List<Integer> msgids = new ArrayList<>();
                    msgids.add(messaggio.getIdMessaggio());
                    new SetMessaggiLettoTask(msgids).execute();
                }
            }

        }


    };

    private static class SetMessaggiLettoTask extends AsyncTask<Void,Void,Boolean>{

        private  List<Integer> idsmsg;

        private Response<Boolean> response;

        SetMessaggiLettoTask(List<Integer> idsmsg) {
            this.idsmsg=idsmsg;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.SVILUPPO_BASE_URL_1)
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build();

                GitHubService service = retrofit.create(GitHubService.class);

                Call<Boolean> call = service.setMessaggioLetto(idsmsg);

                response = call.execute();

                return (response.isSuccessful() && response.code() == HttpURLConnection.HTTP_OK);

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (!success) {

                //Todo: handle success and failure of set messaggio letto

            }
        }
    }
}