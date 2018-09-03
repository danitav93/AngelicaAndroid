package com.example.danieletavernelli.angelica.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.danieletavernelli.angelica.R;
import com.example.danieletavernelli.angelica.Singleton.UserSingleton;
import com.example.danieletavernelli.angelica.adapter.MainActivityFragmentAdapter;
import com.example.danieletavernelli.angelica.entity.Messaggio;
import com.example.danieletavernelli.angelica.firebase.MessageService;
import com.example.danieletavernelli.angelica.fragment.CollocazioneFragment;
import com.example.danieletavernelli.angelica.rest.service.GitHubService;
import com.example.danieletavernelli.angelica.utility.AppMethods;
import com.example.danieletavernelli.angelica.utility.Constants;
import com.example.tavernelli.daniele.libreriadidanieletavernelli.Methods.IntentMethods;
import com.example.tavernelli.daniele.libreriadidanieletavernelli.Methods.KeyboardMethods;
import com.example.tavernelli.daniele.libreriadidanieletavernelli.Methods.ToastMethods;
import com.google.firebase.iid.FirebaseInstanceId;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.example.danieletavernelli.angelica.firebase.MessageService.ACTION_CHAT_MESSAGE_RECEIVED_FROM_SERVER;

//fragment for search collcoazioni
public class MainActivity extends AppCompatActivity {


    public static final String TAG = "MainActivity";

    public static final String ACTION_CHAT_MESSAGE_DISPATCHED = "acmd";

    public static final String EXTRA_MESSAGGIO_CHAT_RICEVUTO = "acmd";

    Context context =this;

    private MainActivityFragmentAdapter mainActivityFragmentAdapter;

    private ViewPager viewPager;

    //Receiver for when arrive message
    public static MainActivityReceiver mMessageReceiver ;

    public static WeakReference<LocalBroadcastManager> broadcaster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setToolbar();

        setData();

        checkGooglePlayServices();

        Log.d(TAG, FirebaseInstanceId.getInstance().getToken());

        broadcaster = new WeakReference<>(LocalBroadcastManager.getInstance(context));



    }

    public static BroadcastReceiver getReceiver() {

        if (mMessageReceiver==null) {
            mMessageReceiver= new  MainActivity.MainActivityReceiver();
        }
        return mMessageReceiver;
    }

    @Override
    protected void onDestroy() {
        if (MessageService.broadcaster!=null) {
            MessageService.broadcaster.get().unregisterReceiver(mMessageReceiver);
        }
        super.onDestroy();
    }


    //google play services for chat
    private void checkGooglePlayServices() {
        if (!AppMethods.isGooglePlayServicesAvailable(this)) {
            AppMethods.makeGooglePlayServicesAvailable(this);
        }
    }

    //setToolbar
    private void setToolbar() {

        setSupportActionBar((Toolbar) findViewById(R.id.activity_main_toolbar));

    }



    /**
     * Set menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        menu.findItem(R.id.activity_main_menu_item_username).setTitle(UserSingleton.getInstance().getViewUtente().getUsername());
        return true;
    }

    /**
     * Handle item selection
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_main_menu_item_log_out:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        UserSingleton.getInstance().clear();
        IntentMethods.startActivityFinishActual(this, LoginActivity.class);
    }

    /**
     * set useful data
     */
    private void setData() {

        mainActivityFragmentAdapter = new MainActivityFragmentAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.activity_main_view_pager);

        viewPager.setAdapter(mainActivityFragmentAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                KeyboardMethods.hideKeyboard((Activity) context);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout= (TabLayout) findViewById(R.id.activity_main_tab_layout);

        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Because this activity has set launchMode="singleTop", the system calls this method
        // to deliver the intent if this activity is currently the foreground activity when
        // invoked again (when the user executes a search from this activity, we don't create
        // a new instance of this activity, so the system delivers the search intent here)
        handleIntent(intent);
    }

    /**
     * handle intent for search view
     */
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // Handle the normal search query case
            ToastMethods.showShortToast(this, "Selezionare una collocazione dalla lista.");

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {

            // Handle a suggestions click (because the suggestions all use ACTION_VIEW)
            Uri data = intent.getData();

            CollocazioneFragment collocazioneFragment = (CollocazioneFragment) getSupportFragmentManager().findFragmentByTag(mainActivityFragmentAdapter.COLLOCAZIONE_FRAGMENT_TAG);

            if (collocazioneFragment != null && viewPager.getCurrentItem() == MainActivityFragmentAdapter.COLLOCAZIONE_FRAGMENT_POSITION) {
                collocazioneFragment.showResult(data);
            }

        }

    }

    public static class GetMessaggioTask extends AsyncTask<Void, Void, Boolean> {


        private int idMessaggio;
        private Response<Messaggio> response;
        private WeakReference<Context> context;
        private Messaggio messaggio;


        GetMessaggioTask(Context context, int idMessaggio) {
            this.context = new WeakReference<>(context);
            this.idMessaggio = idMessaggio;

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build();

                GitHubService service = retrofit.create(GitHubService.class);

                Call<Messaggio> call = service.getMessaggio(idMessaggio);

                response = call.execute();

                if (response.isSuccessful() && response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                    messaggio = response.body();
                    return true;
                }

                return false;


            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (!success) {

                ToastMethods.showShortToast(context.get(),"Problemi di comunicazione con il server, non è possibile ricevere messaggi");

            } else {

                Intent intent = new Intent();
                intent.setAction(ACTION_CHAT_MESSAGE_DISPATCHED);
                intent.putExtra(EXTRA_MESSAGGIO_CHAT_RICEVUTO,messaggio);
                broadcaster.get().sendBroadcast(intent);

                //addNotificationArrivedMessage();

            }
        }

    }


    public static  class MainActivityReceiver extends BroadcastReceiver{

        private List<Integer> messageHandled = new ArrayList<>();

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_CHAT_MESSAGE_RECEIVED_FROM_SERVER)) {
                int id_messaggio = intent.getIntExtra(MessageService.EXTRA_ID_MESSAGGIO, 0);
                if (!messageHandled.contains(id_messaggio)) {
                    new GetMessaggioTask(context, id_messaggio).execute();
                    messageHandled.add(id_messaggio);
                }
            }
        }

    }


}


