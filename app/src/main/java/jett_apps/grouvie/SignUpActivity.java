package jett_apps.grouvie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static jett_apps.grouvie.MainActivity.USER_PHONE_NO;

public class SignUpActivity extends AppCompatActivity {


    private String userPhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userPhoneNo = getIntent().getStringExtra(USER_PHONE_NO);

        //TODO: Check if phoneNo is registered already. If so, skip signup
//        if (phoneNo is registered) {
//            Get name, phone, postcode from web server
//            updateProfileGotoLanding(name, phone, postcode);
//        }

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


        //TODO: Send new user profile data to web server

        //Save user profile in shared pref and goto LandingPage
        updateProfileGotoLanding(firstName + ' ' + lastName, userPhoneNo, postCode);

    }

    private void updateProfileGotoLanding(String name, String phone, String postcode) {

        ProfileManager.updateAll(SignUpActivity.this, name, phone, postcode);

        Intent intent = new Intent(SignUpActivity.this, LandingPage.class);
        startActivity(intent);

    }

}
