package com.example.adity.invoicemaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BankDetails extends AppCompatActivity {

    EditText bankname,ifsc,accholdername,accnumber;
    String bank,ifsccode,accholder,accno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);


        bankname=(EditText)findViewById(R.id.bankname);
        ifsc=(EditText)findViewById(R.id.IfSc);
        accholdername=(EditText)findViewById(R.id.accholder);
        accnumber=(EditText)findViewById(R.id.accno);




        Button save=(Button)findViewById(R.id.savebank) ;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                bank=bankname.getText().toString();
                ifsccode=ifsc.getText().toString();
                accholder=accholdername.getText().toString();
                accno=accnumber.getText().toString();

                Intent i=new Intent();
                i.putExtra("bank_name",bank);
                i.putExtra("ifsc_code",ifsccode);
                i.putExtra("account_holder",accholder);
                i.putExtra("account_number",accno);

                Bundle extras = getIntent().getExtras();
                 String act = extras.getString("Type");

// do something here if from activity a
                if(act.equals("VENDOR")) {
                    setResult(1,i);

                } else {
                    setResult(5,i);
                    // / if from activity a
                }

                finish();


            }
        });


    }
}
