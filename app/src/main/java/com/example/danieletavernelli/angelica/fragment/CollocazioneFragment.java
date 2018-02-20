package com.example.danieletavernelli.angelica.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.danieletavernelli.angelica.R;
import com.example.danieletavernelli.angelica.utility.AppMethods;

import java.util.ArrayList;

public class CollocazioneFragment extends Fragment {

    public static final String NAME = "Collocazioni";

    private View entireLayout;

    private LinearLayout resultlayout;

    private ListView resultListView;

    public static CollocazioneFragment newInstance() {

        return new CollocazioneFragment();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        entireLayout = inflater.inflate(R.layout.fragment_collocazione, container, false);

        //setData
        setData();

        //setSearchView
        setSearchView();

        //setVisibilities
        setVisibilities();

        return entireLayout;


    }



    private void setVisibilities() {
        resultlayout.setVisibility(View.INVISIBLE);
    }



    /**
     * show result after selection di una collocazione
     */
    public void showResult(Uri data) {

        resultlayout.setVisibility(View.VISIBLE);

        ((ArrayAdapter)resultListView.getAdapter()).clear();
        ((ArrayAdapter<String>)resultListView.getAdapter()).addAll(AppMethods.getCollocazioneResultFromUriIntent(data.toString()));
        ((ArrayAdapter) resultListView.getAdapter()).notifyDataSetChanged();

    }



    /**
     * set Data fields
     */
    private void setData() {


        resultListView = entireLayout.findViewById(R.id.fragment_collocazione__result_list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.collocazione_result_item_text_view, new ArrayList<String>());

        resultListView.setAdapter(adapter);

        resultlayout = entireLayout.findViewById(R.id.fragment_collocazione__result_rel_layout);

    }



    /**
     * make search view features
     */
    private void setSearchView() {

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =  entireLayout.findViewById(R.id.fragment_collocazione_search_view);
        searchView.setSearchableInfo(searchManager != null ? searchManager.getSearchableInfo(getActivity().getComponentName()) : null);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        //set layout of search view
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        if (searchPlate!=null) {
            searchPlate.setBackground(getResources().getDrawable(R.drawable.red_line_bottom));
            int searchSrc = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            EditText mSearchEditText =searchPlate.findViewById(searchSrc);
            if(mSearchEditText != null){
                AppMethods.setCursorColor(mSearchEditText,getResources().getColor(R.color.red_light));
            }

        }
        int submitAreaId = searchView.getContext().getResources().getIdentifier("android:id/submit_area", null, null);
        View submitArea = searchView.findViewById(submitAreaId);
        if (submitArea!=null) {
            submitArea.setBackground(getResources().getDrawable(R.drawable.red_line_bottom));
            /*int voiceId = searchView.getContext().getResources().getIdentifier("android:id/search_voice_btn",null,null);
            ImageView voiceImgViw = searchPlate.findViewById(voiceId);
            if (voiceImgViw!=null) {
                voiceImgViw.setBackground(getResources().getDrawable(R.drawable.red_line_bottom));
            }*/
        }
        //
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (! (newText.length() > 0)) {

                    resultlayout.setVisibility(View.INVISIBLE);
                }
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }
        });
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                resultlayout.setVisibility(View.VISIBLE);
                return false;
            }
        });

    }
}
