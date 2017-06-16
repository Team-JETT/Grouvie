package jett_apps.grouvie.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import jett_apps.grouvie.Activities.SelectGroup;
import jett_apps.grouvie.HelperObjects.Friend;
import jett_apps.grouvie.HelperObjects.FriendView;
import jett_apps.grouvie.R;

public class CustomFriendAdapter extends ArrayAdapter<Friend> {
    private LayoutInflater inflater;

    public CustomFriendAdapter(Context context, List<Friend> friendList) {
        super(context, R.layout.simplerow, R.id.rowTextView, friendList);
        inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Friend friend = this.getItem(position);

        CheckBox checkBox;
        TextView textView;
        // Creates a new row view.
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.simplerow, null);

            textView = (TextView) convertView.findViewById(R.id.rowTextView);
            checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);
            // Tagging the row with the child view means that it's easier
            // to access later on.
            convertView.setTag(new FriendView(textView, checkBox));

            checkBox.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Friend friend = (Friend) cb.getTag();
                    friend.setChecked(cb.isChecked());
                }
            });
        } else {
            FriendView viewHolder = (FriendView) convertView.getTag();
            checkBox = viewHolder.getCheckBox();
            textView = viewHolder.getTextView();
        }

        checkBox.setTag(friend);

        checkBox.setChecked(friend.isChecked());
        textView.setText(friend.getName());

        return convertView;
    }
}
