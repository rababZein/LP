package com.example.rabab.lp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
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
    public GridView mGridView;
    public ProgressBar mProgressBar;
    public GridViewAdapter mGridAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        peoples = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, peoples);
        mGridView.setAdapter(mGridAdapter);
        mProgressBar.setVisibility(View.VISIBLE);

        FetchAllPeople fetchAllPeople = new FetchAllPeople();
        fetchAllPeople.execute();

//        InsertPeople insertPeople = new InsertPeople();
//        insertPeople.execute("mahmoud","mahmoud@yahoo.com");
//
//        FetchPeople fetchPeople = new FetchPeople();
//        fetchPeople.execute("1"); // user ID
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                People item = (People) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), InfoActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("name", item.getName());
                intent.putExtra("email", item.getEmail());
                startActivity(intent);



            }
        });

        return rootView;
    }

    public void getPeopleDataFromJson(String peopleJsonStr) throws JSONException {
        final String STATUS = "status";
        final String INFO = "info";
        final String NAME = "name";
        final String EMAIL = "email";
        final String PSW = "password";
        JSONObject peopleJson = new JSONObject(peopleJsonStr);
        JSONArray resultsArray = peopleJson.getJSONArray(INFO);

        People people;


        for (int i = 0; i < resultsArray.length(); i++) {

            String name;
            String email;
            String password;
            people = new People();
            JSONObject user = resultsArray.getJSONObject(i);
            name = user.getString(NAME);
            people.setName(name);
            email = user.getString(EMAIL);
            people.setEmail(email);
            password = user.getString(PSW);
            people.setPassword(password);
            peoples.add(people);
        }

        for (People p : peoples) {
            Log.v(LOG_TAG, "User Name entry: " + p.getName());
            Log.v(LOG_TAG, "User Email entry: " + p.getEmail());
        }


    }

    public class FetchPeople  extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == 1) {
                mGridAdapter.setGridData(peoples);
            } else {
                Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            int result = 0;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String peopleJsonStr = null;
            try {

                final String BASE_URL = "http://rabab-magiccoder.rhcloud.com/info.php?";
                final String USER_PARAM = "uid";


                Uri builduri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(USER_PARAM, params[0])
                        .build();

                URL url = new URL(builduri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.v(LOG_TAG, "Build URL " + url.toString());
                urlConnection.setRequestMethod("GET");
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

    public class FetchAllPeople  extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == 1) {
                mGridAdapter.setGridData(peoples);
            } else {
                Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);


        }

        @Override
        protected Integer doInBackground(String... params) {

            int result = 0;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String peopleJsonStr = null;
            try {


                final String BASE_URL = "http://rabab-magiccoder.rhcloud.com/all.php";


                Uri builduri = Uri.parse(BASE_URL).buildUpon()
                        .build();

                URL url = new URL(builduri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.v(LOG_TAG, "Build URL " + url.toString());
                urlConnection.setRequestMethod("GET");
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


}
