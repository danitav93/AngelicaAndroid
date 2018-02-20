package com.example.danieletavernelli.angelica.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danieletavernelli.angelica.R;
import com.example.danieletavernelli.angelica.activity.ChatActivity;
import com.example.danieletavernelli.angelica.entity.ViewUtente;
import com.example.danieletavernelli.angelica.utility.AppMethods;
import com.example.tavernelli.daniele.libreriadidanieletavernelli.Methods.IntentMethods;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Daniele Tavernelli on 2/15/2018.
 * adapter of the list view of contacts in chat fragment
 */

public class ListViewChatAdapter extends ArrayAdapter<ViewUtente> {

    private int layoutResourceId;


    public ListViewChatAdapter(@NonNull Context context, int resource, @NonNull List<ViewUtente> objects) {
        super(context, resource, objects);
        layoutResourceId = resource;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();

            convertView = inflater.inflate(layoutResourceId, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.roleIcon = convertView.findViewById(R.id.chat_list_view_row_profile_image_viw);

            viewHolder.username = convertView.findViewById(R.id.chat_list_view_row_name_user_txt_viw);

            viewHolder.descRoleFunc = convertView.findViewById(R.id.chat_list_view_row_desc_role_user_txt_viw);

            viewHolder.statoImageView = convertView.findViewById(R.id.chat_list_view_row_stato_img_viw);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.username.setText(getItem(position).getUsername());

        viewHolder.descRoleFunc.setText(getItem(position).getDescFunc());

        viewHolder.roleIcon.setImageDrawable(AppMethods.getUserIconeBasedOnRole(getContext(),getItem(position)));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] extraTags = new String[]{ChatActivity.VIEW_UTENTE_INTERLOCUTORE_EXTRA_TAG};
                Serializable[] extras = new Serializable[]{getItem(position)};
                IntentMethods.startActivityWithExtras((Activity)getContext(),ChatActivity.class,extraTags,extras);
            }
        });

        return convertView;

    }


    static class ViewHolder {

        ImageView roleIcon;
        TextView username;
        TextView descRoleFunc;
        ImageView statoImageView;

    }


}
