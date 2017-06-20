package jett_apps.grouvie.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jett_apps.grouvie.HelperObjects.Friend;
import jett_apps.grouvie.R;
import jett_apps.grouvie.Views.GroupView;

public class CustomGroupAdapter extends ArrayAdapter<Friend> {

    public CustomGroupAdapter(@NonNull Context context, ArrayList<Friend> friends) {
        super(context, R.layout.custom_group_layout, friends);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Friend friend = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.custom_group_layout, parent, false);

        TextView text = (TextView) view.findViewById(R.id.friendName);
        text.setText(friend.getName());

        if(friend.hasAccepted()) {
            text.setTextColor(Color.GREEN);
        } else {
            text.setTextColor(Color.RED);
        }
        return view;
    }
}
