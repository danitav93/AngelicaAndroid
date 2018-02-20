package com.example.danieletavernelli.angelica.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.danieletavernelli.angelica.activity.MainActivity;
import com.example.danieletavernelli.angelica.fragment.ChatFragment;
import com.example.danieletavernelli.angelica.fragment.CollocazioneFragment;

/**
 * Adapter for swipe fragments in main activity
 * Created by Daniele Tavernelli on 2/13/2018.
 */

public class MainActivityFragmentAdapter extends FragmentPagerAdapter {

    public static final int NUMBER_OF_MAIN_ACTIVITY_TABS = 2;

    public static final int COLLOCAZIONE_FRAGMENT_POSITION = 0;
    public static final int CHAT_FRAGMENT_POSITION = 1;


    public String COLLOCAZIONE_FRAGMENT_TAG;
    public String CHAT_FRAGMENT_TAG;

    public MainActivityFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case COLLOCAZIONE_FRAGMENT_POSITION:
                return CollocazioneFragment.newInstance();
            case CHAT_FRAGMENT_POSITION:
                return ChatFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUMBER_OF_MAIN_ACTIVITY_TABS;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // get the tags set by FragmentPagerAdapter
        switch (position) {
            case COLLOCAZIONE_FRAGMENT_POSITION:
                COLLOCAZIONE_FRAGMENT_TAG = createdFragment.getTag();
                break;
            case CHAT_FRAGMENT_POSITION:
                CHAT_FRAGMENT_TAG = createdFragment.getTag();
                break;
        }
        // ... save the tags somewhere so you can reference them later
        return createdFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case COLLOCAZIONE_FRAGMENT_POSITION:
                return CollocazioneFragment.NAME;


            case CHAT_FRAGMENT_POSITION:
                return ChatFragment.NAME;
        }
        return "";
    }
}
