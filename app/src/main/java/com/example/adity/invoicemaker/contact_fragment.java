package com.example.adity.invoicemaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by adity on 7/10/2017.
 */

public class contact_fragment extends Fragment {

    EditText name,email,msg;
     String Subject="";
     String Message="";
    public contact_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




    return (inflater.inflate(R.layout.fragment_contact,container,false));
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Contact Us");

        name=(EditText) getActivity().findViewById(R.id.yurname);
        email=(EditText) getActivity().findViewById(R.id.contactemail);
        msg=(EditText) getActivity().findViewById(R.id.msg);


        Subject=email.getText().toString();
        Message=msg.getText().toString();



        Button send=(Button) getActivity().findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SendMail().execute("");


            }
        });





    }

    private class SendMail extends AsyncTask<String, Integer, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "Please wait","", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        protected Void doInBackground(String... params) {
            Mail m = new Mail("aditya01tache@gmail.com", "tache123");

            String[] toArr = {"adityasaxena1997@gmail.com"};
            m.setTo(toArr);
            m.setFrom("sssss");
            m.setSubject("Query ");
            m.setBody("sss");

            try {
                if(m.send()) {
                    Toast.makeText(getActivity(), "Email was sent successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Email was not sent.", Toast.LENGTH_LONG).show();
                }
            } catch(Exception e) {
                Toast.makeText(getActivity(), "Email was not sent."+e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }




}
