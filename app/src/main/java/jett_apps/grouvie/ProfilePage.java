package jett_apps.grouvie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ProfilePage extends AppCompatActivity {

    private EditText postcodeText;
    private String name;
    private String phone;
    private String postcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        name = ProfileManager.getName(ProfilePage.this);
        phone = ProfileManager.getPhone(ProfilePage.this);
        postcode = ProfileManager.getPostcode(ProfilePage.this);

        TextView nameText = (TextView) findViewById(R.id.nameText);
        nameText.setText(name);

        TextView phoneText = (TextView) findViewById(R.id.phoneText);
        phoneText.setText(phone);

        postcodeText = (EditText) findViewById(R.id.postcodeText);
        postcodeText.setHint(postcode);

//        TextView postcodeText = (TextView) findViewById(R.id.postcodeText);
//        postcodeText.setText(postcode);

    }


    public void updateProfile(View view) {

        //Send updated profile to web server if settings are changed
        if (!postcodeText.getText().toString().isEmpty()) {
           //TODO: Send new postcode to web server. Can use phoneNumber field if required
            new ServerContact().execute("update_postcode", postcode);
        }

        Intent backIntent = new Intent(ProfilePage.this, LandingPage.class);
        startActivity(backIntent);

    }

}
