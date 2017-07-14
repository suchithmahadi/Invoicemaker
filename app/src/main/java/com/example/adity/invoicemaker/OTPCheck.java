package com.example.adity.invoicemaker;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class OTPCheck extends AppCompatActivity   {
   // private static final int GITHUB_SEARCH_LOADER = 22;

String sms="";
    EditText editText;
    TextView t;
    String result=" ";
    URL url,urlverify;
    String phoneNumber="9582738120";
    String test=" ",Details=" ",verifyResult,otp=" ",test1=" ",Details1=" ";
    ProgressDialog pd;
    DatabaseReference db;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    Map<String,String> mp=new HashMap<>();

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

                    pd=new ProgressDialog(OTPCheck.this);
                    pd.setMessage("Registering User");
                    pd.show();

                    Bundle b=getIntent().getExtras();
                   final String em= b.getString("Email");
                   final String pwd=b.getString("password");

                    final String name = b.getString("company_name");
                    mp.put("Company", b.getString("company_name"));
                    mp.put("Email", b.getString("Email"));
                    mp.put("Mobile number", b.getString("number"));
                    mp.put("contact person",b.getString("Contact_person"));


                    mAuth.createUserWithEmailAndPassword(em, pwd).addOnCompleteListener(OTPCheck.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

                                mAuth.getCurrentUser().updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                }
                                            }
                                        });
                                mAuth.signInWithEmailAndPassword(em,pwd).addOnCompleteListener(OTPCheck.this,new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            db = FirebaseDatabase.getInstance().getReference("Users");

                                            db.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mp, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    /*if (databaseError == null) {
                                                        Toast.makeText(signup.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(signup.this, "database error" + databaseError.toString(), Toast.LENGTH_SHORT).show();
                                                    }*/
                                                }
                                            });
                                            pd.setMessage("Successfully Registered");
                                            pd.hide();
                                            startActivity(new Intent(OTPCheck.this,NavigationDrawer.class));
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(OTPCheck.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });

                            }
                            else
                            {
                                pd.hide();
                                Toast.makeText(OTPCheck.this, "Could not Register " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

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



