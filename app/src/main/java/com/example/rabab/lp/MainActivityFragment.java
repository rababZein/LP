package com.example.rabab.lp;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final  String LOG_TAG = FetchPeople.class.getSimpleName();
    public ArrayList<People> peoples;


    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        peoples = new ArrayList<>();


        InsertPeople insertPeople = new InsertPeople();
        insertPeople.execute("mahmoud","mahmoud@yahoo.com");

        FetchPeople fetchPeople = new FetchPeople();
        fetchPeople.execute("7"); // user ID
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public class InsertPeople extends  AsyncTask <String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            Integer result=0;
            HttpURLConnection urlConnection = null;
            try {

                final String BASE_URL = "http://rabab-magiccoder.rhcloud.com/signup.php";
                final String USER_NAME_PARAM = "name";
                final String USER_EMAIL_PARAM = "email";


                URL myUrl = new URL(BASE_URL);

                HttpURLConnection myConnection = (HttpURLConnection) myUrl.openConnection();
                myConnection.setRequestMethod("POST");
                myConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(myConnection.getOutputStream());
                wr.writeBytes(USER_NAME_PARAM + "=" + params[0] + "&" + USER_EMAIL_PARAM + "=" + params[1]);

                wr.flush();

                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                wr.close();
                reader.close();
            }catch (Exception e){

                Log.e("PlaceholderFragment", "Error ", e);

                return result;

            }finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }

            return result;
        }
    }

    public class FetchPeople  extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            int result = 0;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String peopleJsonStr = null;
            try {

                final String BASE_URL = "http://rabab-magiccoder.rhcloud.com/info.php?uid=1";
                final String USER_PARAM = "uid";


                Uri builduri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(USER_PARAM, params[0])
                        .build();

                URL url = new URL(builduri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.v(LOG_TAG, "Build URL " + url.toString());
                urlConnection.setRequestMethod("GET");
               // urlConnection.addRequestProperty();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return result;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return result;
                }

                peopleJsonStr = buffer.toString();
                Log.v(LOG_TAG, "people json string " + peopleJsonStr);


            }catch (Exception e){

                Log.e("PlaceholderFragment", "Error ", e);

                return result;

            }finally{
                if (urlConnection != null) {
                     urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }



            try {


                getPeopleDataFromJson(peopleJsonStr);
                result=1;
                return result;
            } catch (JSONException e) {


                Log.e(LOG_TAG, e.getMessage(), e);
                e.getStackTrace();
            }
            return result;

        }
    }

    public void  getPeopleDataFromJson(String peopleJsonStr) throws JSONException {
        final String STATUS = "status";
        final String INFO = "info";
        final String NAME = "name";
        final String EMAIL = "email";
        JSONObject peopleJson = new JSONObject(peopleJsonStr);
        JSONArray resultsArray = peopleJson.getJSONArray(INFO);

        People people;


        for(int i = 0; i < resultsArray.length(); i++) {

            String name;
            String email;

            people = new People();
            JSONObject user = resultsArray.getJSONObject(i);
            name = user.getString(NAME);
            people.setName(name);
            email = user.getString(EMAIL);
            people.setEmail(email);

            peoples.add(people);
        }

        for (People p : peoples) {
            Log.v(LOG_TAG, "User Name entry: " + p.getName());
            Log.v(LOG_TAG, "User Email entry: " + p.getEmail());
        }


    }
}
