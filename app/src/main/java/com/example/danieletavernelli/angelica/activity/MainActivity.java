package com.example.danieletavernelli.angelica.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.danieletavernelli.angelica.R;
import com.example.danieletavernelli.angelica.Singleton.UserSingleton;
import com.example.danieletavernelli.angelica.adapter.MainActivityFragmentAdapter;
import com.example.danieletavernelli.angelica.fragment.CollocazioneFragment;
import com.example.tavernelli.daniele.libreriadidanieletavernelli.Methods.IntentMethods;
import com.example.tavernelli.daniele.libreriadidanieletavernelli.Methods.KeyboardMethods;
import com.example.tavernelli.daniele.libreriadidanieletavernelli.Methods.ToastMethods;

//fragment for search collcoazioni
public class MainActivity extends AppCompatActivity {

    Context context =this;

    private MainActivityFragmentAdapter mainActivityFragmentAdapter;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setToolbar();

        setData();




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

        viewPager = findViewById(R.id.activity_main_view_pager);

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

        TabLayout tabLayout= findViewById(R.id.activity_main_tab_layout);

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

}


