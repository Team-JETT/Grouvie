package jett_apps.grouvie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static jett_apps.grouvie.MainActivity.USER_PHONE_NO;

public class SignUpActivity extends AppCompatActivity {


    private String userPhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userPhoneNo = getIntent().getStringExtra(USER_PHONE_NO);
        assert userPhoneNo != null;

        // Check if phoneNo is registered already. If so, skip signup
        int success_code = 0;
        try {
            String result = new ServerContact().execute("verify_user", userPhoneNo).get();
            success_code = Integer.parseInt(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //If already registered
        if (success_code == 1) {
            JSONObject json_data = null;

            String name = null;
            String postcode = null;

            try {
                // Get name, phone, postcode from web server
                String data = new ServerContact().execute("get_user", userPhoneNo).get();
                json_data = new JSONObject(data);
                name = json_data.getString("name");
                postcode = json_data.getString("postcode");
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }

            updateProfileGotoLanding(name, userPhoneNo, postcode);
        }

    }


    public void signupClick(View view) {
        String firstName = ((EditText) findViewById(R.id.firstNameBox)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.lastNameBox)).getText().toString();
        String postCode = ((EditText) findViewById(R.id.postCodeBox)).getText().toString();

        if (firstName.isEmpty() || lastName.isEmpty()
                || postCode.isEmpty() || userPhoneNo.isEmpty()) {

            Toast.makeText(SignUpActivity.this, "Please fill in all required information",
                    Toast.LENGTH_LONG).show();

            return;
        }
        String name = firstName + ' ' + lastName;
        JSONObject json_data = new JSONObject();
        try {
            json_data.accumulate("phone_number", userPhoneNo);
            json_data.accumulate("name", name);
            json_data.accumulate("postcode", postCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Send new user profile data to web server
        new ServerContact().execute("new_user", json_data.toString());

        //Save user profile in shared pref and goto LandingPage
        updateProfileGotoLanding(name, userPhoneNo, postCode);

    }

    // Update the profile with postcode data.
    private void updateProfileGotoLanding(String name, String phone, String postcode) {
        ProfileManager.updateAll(SignUpActivity.this, name, phone, postcode);

        Intent intent = new Intent(SignUpActivity.this, LandingPage.class);
        startActivity(intent);

    }

    // Update the profile with latitude and longitude data.
    private void updateProfileGotoLanding(String name, String phone, String latitude,
                                          String longitude) {

        ProfileManager.updateAll(SignUpActivity.this, name, phone, latitude, longitude);

        Intent intent = new Intent(SignUpActivity.this, LandingPage.class);
        startActivity(intent);

    }

}
