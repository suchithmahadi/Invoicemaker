package com.example.adity.invoicemaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ClientDetails extends AppCompatActivity {

    EditText name,phone,email,addline,addline2,addline3,GSTIN,PAN_NO;
    String Name,Phone,Email,Address,pan_no,gstin;
    Map<String,String> mp;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);

        pd=new ProgressDialog(ClientDetails.this);

        name=(EditText)findViewById(R.id.clientname);
        phone=(EditText)findViewById(R.id.clientphone);
        email=(EditText)findViewById(R.id.clientemail);
        addline=(EditText)findViewById(R.id.Address1);
        addline2=(EditText)findViewById(R.id.Address2);
        addline3=(EditText)findViewById(R.id.Address3);
        GSTIN=(EditText)findViewById(R.id.gst);
        PAN_NO=(EditText)findViewById(R.id.pan);

        Button save=(Button)findViewById(R.id.buttonclient) ;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mp=new HashMap<>();
                pd.setMessage("Please Wait...");
                pd.show();


                Name=name.getText().toString();
                Phone=phone.getText().toString();
                Email=email.getText().toString();
                Address=addline.getText().toString()+"\n"+addline2.getText().toString()+"\n"+addline3.getText().toString();
                pan_no=PAN_NO.getText().toString();
                gstin=GSTIN.getText().toString();



                mp.put("Phone",Phone);
                mp.put("Email",Email);
                mp.put("Address",Address);
                mp.put("Pan no",pan_no);
                mp.put("Gstin",gstin);



                if(getIntent().getExtras().getString("Type").equals("Vendor")) {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Company");
                    db.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Name).setValue(mp, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        pd.hide();
                        }
                    });
                }
                else if(getIntent().getExtras().getString("Type").equals(""))
                {

                }



                Intent i=new Intent();
                i.putExtra("name",Name);
                i.putExtra("phone",Phone);
                i.putExtra("email",Email);
                i.putExtra("address",Address);
                i.putExtra("gstin",gstin);
                i.putExtra("pan",pan_no);
                setResult(3,i);

                finish();


            }
        });

    }
}
