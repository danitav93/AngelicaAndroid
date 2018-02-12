package com.example.danieletavernelli.angelica.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.danieletavernelli.angelica.R;
import com.example.danieletavernelli.angelica.entity.ViewUtente;
import com.example.danieletavernelli.angelica.utility.AppMethods;
import com.example.tavernelli.daniele.libreriadidanieletavernelli.Methods.IntentMethods;
import com.example.tavernelli.daniele.libreriadidanieletavernelli.Methods.ToastMethods;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewUtente viewUtente;

    private LinearLayout resultlayout;

    private ListView resultListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //handle intent for search view
        handleIntent(getIntent());

        //setData
        setData();

        //setSearchView
        setSearchView();

        //setVisibilities
        setVisibilities();





    }



    private void setVisibilities() {
        resultlayout.setVisibility(View.INVISIBLE);
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
            ToastMethods.showShortToast(this,"Selezionare una collocazione dalla lista.");

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {

            // Handle a suggestions click (because the suggestions all use ACTION_VIEW)
            Uri data = intent.getData();

            showResult(data);

        }

    }

    /**
     * show result after selection di una collocazione
     */
    private void showResult(Uri data) {

        resultlayout.setVisibility(View.VISIBLE);

        ((ArrayAdapter)resultListView.getAdapter()).clear();
        ((ArrayAdapter<String>)resultListView.getAdapter()).addAll(AppMethods.getCollocazioneResultFromUriIntent(data.toString()));
        ((ArrayAdapter) resultListView.getAdapter()).notifyDataSetChanged();

    }

    /**
     * Set menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        menu.findItem(R.id.activity_main_menu_item_username).setTitle(viewUtente.getUsername());
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
        IntentMethods.startActivityFinishActual(this, LoginActivity.class);
    }

    /**
     * set Data fields
     */
    private void setData() {
        Intent intent = getIntent();
        if (intent.getAction()!=null && intent.getAction().equals(LoginActivity.ACTION_CHANGE_ACTIVITY)) {
            viewUtente = (ViewUtente) intent.getSerializableExtra(LoginActivity.VIEW_UTENTE_EXTRA_TAG);
        }

        resultListView = findViewById(R.id.activity_main_result_list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.collocazione_result_item_text_view, new ArrayList<String>());
        resultListView.setAdapter(adapter);

        resultlayout = findViewById(R.id.activity_main_result_rel_layout);

    }



    /**
     * make search view features
     */
    private void setSearchView() {

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =  findViewById(R.id.activity_main_search_view);
        searchView.setSearchableInfo(searchManager != null ? searchManager.getSearchableInfo(getComponentName()) : null);
        searchView.setIconifiedByDefault(false);


    }
}
