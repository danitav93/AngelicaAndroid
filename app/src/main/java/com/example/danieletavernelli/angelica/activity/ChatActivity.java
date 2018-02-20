package com.example.danieletavernelli.angelica.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.example.tavernelli.daniele.libreriadidanieletavernelli.Methods.ToastMethods;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.io.IOException;
import java.net.HttpURLConnection;
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


    public static String VIEW_UTENTE_INTERLOCUTORE_EXTRA_TAG = "viwUtenteInterLocutore";

    private Context context = this;

    //DATA
    private ViewUtente viewUtenteLoggato = UserSingleton.getInstance().getViewUtente();
    private ChatUserModel chatUserModelUtenteLoggato = new ChatUserModel(viewUtenteLoggato);
    private ViewUtente viewUtenteInterlocutore ;
    private ChatUserModel chatUserModelUtenteInterlocutore;

    private List<Messaggio> messaggi;

    MessagesListAdapter<ChatMessaggioModel> adapter;

    //layout
    private MessagesList messagesList;
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
    }

    private void setInputChatMessage() {

        messageInput = findViewById(R.id.activity_chat_input);

        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                Messaggio messaggio = new Messaggio();
                messaggio.setBody(input.toString());
                messaggio.setId_destinatario(viewUtenteInterlocutore.getId_utente());
                messaggio.setId_mittente(viewUtenteLoggato.getId_utente());
                messaggio.setData(Calendar.getInstance().getTime());

                adapter.addToStart(new ChatMessaggioModel(chatUserModelUtenteLoggato,messaggio), true);
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
        Drawable icon =AppMethods.getUserIconeBasedOnRole(this,viewUtenteInterlocutore);
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

        adapter = new MessagesListAdapter<>(viewUtenteLoggato.getId_utente().toString(), null);

        /*adapter.setLoadMoreListener(new MessagesListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                int a=1;
            }
        });*/

        messagesList.setAdapter(adapter);

    }

    @SuppressLint("StaticFieldLeak")
    private void setMessageList() {

        new AsyncTask<Void,Void,Boolean>() {

            Response<List<Messaggio>> response;

            @Override
            protected Boolean doInBackground(Void... voids) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.SVILUPPO_BASE_URL_1 )
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build();

                GitHubService service = retrofit.create(GitHubService.class);

                Call<List<Messaggio>> call = service.getChat(1,50,viewUtenteLoggato.getId_utente(),viewUtenteInterlocutore.getId_utente());

                try {

                    response = call.execute();

                    if (response.isSuccessful() && response.code() == HttpURLConnection.HTTP_OK) {
                        messaggi=response.body();
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
                    for (Messaggio messaggio:messaggi) {
                        ChatMessaggioModel chatMessaggioModel;
                        if (messaggio.getId_mittente().equals(viewUtenteLoggato.getId_utente())) {
                            chatMessaggioModel = new ChatMessaggioModel(chatUserModelUtenteLoggato,messaggio);
                        } else {
                            chatMessaggioModel = new ChatMessaggioModel(chatUserModelUtenteInterlocutore,messaggio);
                        }
                        ((MessagesListAdapter<ChatMessaggioModel>)messagesList.getAdapter()).addToStart(chatMessaggioModel,true);
                    }
                } else if (response!=null){
                    ToastMethods.showShortToast(context, "Errore " + response.code());
                } else {
                    ToastMethods.showShortToast(context, "Errore nella comunicazione con il server");
                }

            }

        }.execute();
    }
}
