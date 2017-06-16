package jett_apps.grouvie.HelperObjects;

import android.widget.CheckBox;
import android.widget.TextView;

public class FriendView {

    private CheckBox checkBox;
    private TextView textView;

    public FriendView(TextView textView, CheckBox checkBox) {
        this.checkBox = checkBox;
        this.textView = textView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}
