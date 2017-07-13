package com.example.adity.invoicemaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ClientDetails extends AppCompatActivity {

    EditText name,phone,email,addline,addline2,addline3;
    String Name,Phone,Email,Address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);


        name=(EditText)findViewById(R.id.clientname);
        phone=(EditText)findViewById(R.id.clientphone);
        email=(EditText)findViewById(R.id.clientemail);
        addline=(EditText)findViewById(R.id.Address1);
        addline2=(EditText)findViewById(R.id.Address2);
        addline3=(EditText)findViewById(R.id.Address3);



        Button save=(Button)findViewById(R.id.buttonclient) ;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Name=name.getText().toString();
                Phone=phone.getText().toString();
                Email=email.getText().toString();
                Address=addline.getText().toString()+"\n"+addline2.getText().toString()+"\n"+addline3.getText().toString();

                Intent i=new Intent();
                i.putExtra("name",Name);
                i.putExtra("phone",Phone);
                i.putExtra("email",Email);
                i.putExtra("address",Address);
                setResult(3,i);


                finish();


            }
        });

    }
}
