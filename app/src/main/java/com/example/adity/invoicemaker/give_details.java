package com.example.adity.invoicemaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class give_details extends AppCompatActivity {

    EditText l1,l2,l3,gst,pan,number,name;
    Button signup;
    Map<String,String> mp=new HashMap<>();

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_details);

        l1=(EditText)findViewById(R.id.line1);
        l2=(EditText)findViewById(R.id.line2);
        l3=(EditText)findViewById(R.id.line3);
        gst=(EditText)findViewById(R.id.gst);
        pan=(EditText)findViewById(R.id.pan);
        number=(EditText)findViewById(R.id.num);
        name=(EditText)findViewById(R.id.name);

        signup=(Button)findViewById(R.id.sup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*pd.setMessage("Saving Details");
                pd.show();


                String address=l1.getText().toString()+"\n"+l2.getText().toString()+"\n"+l3.getText().toString();
                String gstin=gst.getText().toString();
                String panno=pan.getText().toString();
                String no=number.getText().toString();
                String nam=name.getText().toString();

                mp.put("name", nam);
                mp.put("address", address);
                mp.put("gst", gstin);
                mp.put("Mobile num", no);
                mp.put("pan", panno);

                DatabaseReference db= FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
                db.child("Registration Details").push().setValue(mp, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        pd.hide();
                    }
                });
                */
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(give_details.this,MainActivity.class));





            }
        });






    }
}
