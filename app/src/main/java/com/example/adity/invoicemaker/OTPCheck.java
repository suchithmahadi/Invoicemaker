package com.example.adity.invoicemaker;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class OTPCheck extends AppCompatActivity   {
   // private static final int GITHUB_SEARCH_LOADER = 22;

String sms="";
    EditText editText;
    TextView t;
    String result=" ";
    URL url,urlverify;
    String phoneNumber="9582738120";
    String test=" ",Details=" ",verifyResult,otp=" ",test1=" ",Details1=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpcheck);
        editText=(EditText)findViewById(R.id.OTP);
        url= NetworkUtils.buildUrl(phoneNumber);
        //getSupportLoaderManager().initLoader(GITHUB_SEARCH_LOADER, null, this);
        new SendOTP().execute();
        Button submit=(Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               try {
                    urlverify=new URL("http://2factor.in/API/V1/5229354a-66c9-11e7-94da-0200cd936042/SMS/VERIFY/" + Details + "/" + editText.getText().toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                 if(test.equals("Success")) {
                  new GetAnswersThread().execute();



              }


            }
        });
        }




    public class GetAnswersThread extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {

            try {
                String githubSearchResults = NetworkUtils.getResponseFromHttpUrl(urlverify);
                return githubSearchResults;
             } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // do something with result

            if (result== null) {
                Toast.makeText(OTPCheck.this, "ERROR", Toast.LENGTH_SHORT).show();
            }  else {
                try {
                    JSONObject json = new JSONObject(result);
                    test1 = json.getString("Status");
                    Details1 = json.getString("Details");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (Details1.equals("OTP Matched")) {
                    Toast.makeText(getApplicationContext(), "VERIFIED", Toast.LENGTH_SHORT).show();
                      startActivity(new Intent(OTPCheck.this,NavigationDrawer.class));
                } else {
                    Toast.makeText(OTPCheck.this, "Not Matched", Toast.LENGTH_SHORT).show();
                }
            }
    }


}
    public class SendOTP extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {

            try {
                String githubSearchResults = NetworkUtils.getResponseFromHttpUrl(url);
                return githubSearchResults;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // do something with result

            if (result== null) {
                Toast.makeText(OTPCheck.this, "ERROR", Toast.LENGTH_SHORT).show();
            }  else {
                try {
                    JSONObject json = new JSONObject(result);
                    test = json.getString("Status");
                    Details = json.getString("Details");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(test.equals("Success"))
                {
                    Toast.makeText(OTPCheck.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                }


            }
        }


    }

}



