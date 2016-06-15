package com.example.rabab.lp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class AddPeopleActivity extends AppCompatActivity {
    private final String LOG_TAG = AddPeopleActivity.class.getSimpleName();
    EditText nameEditText = null;
    EditText emailEditText = null;
    String name;
    String email;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    String imageName;
    String imageContent;
    String imagepath;

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

        Log.v(LOG_TAG, name + "----" + email + "----"+imagepath);

        try {
            imageContent=fileToBase64(imagepath);
            Log.v("imageContent length", String.valueOf(imageContent.length()));
            InsertPeople insertPeople = new InsertPeople();
            insertPeople.execute(name, email,imageContent);
        } catch (IOException e) {
            Log.v("yarb",e.toString());
            e.printStackTrace();
        }


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
                final String USER_PWD_PARAM = "pwd";

                URL myUrl = new URL(BASE_URL);

                myConnection = (HttpURLConnection) myUrl.openConnection();
                myConnection.setRequestMethod("POST");

                myConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(myConnection.getOutputStream());
                wr.writeBytes(USER_NAME_PARAM + "=" + params[0] + "&" + USER_EMAIL_PARAM + "=" + params[1] + "&" + USER_PWD_PARAM + "=" + params[2] );

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

public void loadImagefromGallery(View view){
    // Create intent to Open Image applications like Gallery, Google Photos
    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
//                // Get the Image from data
//
               Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                       filePathColumn, null, null, null);
//                // Move to first row
               cursor.moveToFirst();

                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                imagepath= cursor.getString(idx);
//
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                System.out.print(imgDecodableString);
               cursor.close();
               ImageView imgView = (ImageView) findViewById(R.id.imgView);
//                // Set the Image in ImageView after decoding the String
           imgView.setImageBitmap(BitmapFactory
                   .decodeFile(imgDecodableString));

//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                //ImageView imgView = (ImageView) findViewById(R.id.imgView);
//                //imgView.setImageBitmap(photo);
//
//
//                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//                Uri tempUri = getImageUri(getApplicationContext(), photo);
//
//                // CALL THIS METHOD TO GET THE ACTUAL PATH
//              //  File finalFile = new File(getRealPathFromURI(tempUri));


//                Cursor cursor2 = getContentResolver().query(selectedImage, null, null, null, null);
//                cursor2.moveToFirst();
//                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                imagepath= cursor.getString(idx);

               // imagepath = getRealPathFromURI(tempUri);

                //System.out.println(mImageCaptureUri);

                Log.e(LOG_TAG ,imagepath);

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("img", e.toString());
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static byte[] fileToByteArray(String path) throws IOException {
        File imagefile = new File(path);
        byte[] data = new byte[(int) imagefile.length()];
        FileInputStream fis = new FileInputStream(imagefile);
        fis.read(data);
        fis.close();
        return data;
    }

    public static String fileToBase64(String path) throws IOException {
        byte[] bytes = fileToByteArray(path);
        Log.w("imgeBytes",bytes.toString());
        String input = Base64.encodeToString(bytes, Base64.URL_SAFE);
        Log.w("afterimageString encode",input);
        return input;
    }
}
