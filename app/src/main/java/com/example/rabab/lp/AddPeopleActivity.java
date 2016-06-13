package com.example.rabab.lp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddPeopleActivity extends AppCompatActivity {
    private final String LOG_TAG = AddPeopleActivity.class.getSimpleName();
    EditText nameEditText = null;
    EditText emailEditText = null;
    String name;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void sendNewPeople(View view) {
        nameEditText = (EditText) findViewById(R.id.EnterNameEditView);
        name = String.valueOf(nameEditText.getText());
        emailEditText = (EditText) findViewById(R.id.EnterEmailEditView);
        email = String.valueOf(emailEditText.getText());

        Log.v(LOG_TAG, name + "----" + email);

        InsertPeople insertPeople = new InsertPeople();
        insertPeople.execute(name, email);
    }

    public class InsertPeople extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection myConnection = null;
            try {

                final String BASE_URL = "http://rabab-magiccoder.rhcloud.com/signup.php";
                final String USER_NAME_PARAM = "name";
                final String USER_EMAIL_PARAM = "email";


                URL myUrl = new URL(BASE_URL);

                myConnection = (HttpURLConnection) myUrl.openConnection();
                myConnection.setRequestMethod("POST");
                myConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(myConnection.getOutputStream());
                wr.writeBytes(USER_NAME_PARAM + "=" + params[0] + "&" + USER_EMAIL_PARAM + "=" + params[1]);

                wr.flush();

                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
//                    Toast.makeText(AddPeopleActivity.this, "this is my Toast message!!! =)",
//                            Toast.LENGTH_LONG).show();
                }
                wr.close();
                reader.close();
            } catch (Exception e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return result;
            } finally {
                if (myConnection != null) {
                    myConnection.disconnect();
                }

            }

            return result;
        }
    }


}
